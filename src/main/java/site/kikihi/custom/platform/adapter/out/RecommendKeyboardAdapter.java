package site.kikihi.custom.platform.adapter.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardOptions;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.out.bookmark.BookmarkPort;
import site.kikihi.custom.platform.application.out.recommend.RecommendKeyboardPort;
import site.kikihi.custom.platform.domain.bookmark.Bookmark;
import site.kikihi.custom.platform.domain.product.Product;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendKeyboardAdapter implements RecommendKeyboardPort {

    private final MongoTemplate mongoTemplate;
    private final ProductDocumentRepository repository;
    private final BookmarkPort bookmarkPort; // 여기에 port를 써도 되는가??
    private static final ObjectMapper mapper = new ObjectMapper();


    // 키보드 추천 로직
    @Override
    public List<Product> filterAndRecommendKeyboards(
            UUID userId,
            KeyboardOptions.Size size,
            KeyboardOptions.KeyPressure keyPressure,
            KeyboardOptions.KeycapProfile keycapProfile,
            KeyboardOptions.SwitchType switchType,
            KeyboardOptions.SoundDampener soundDampener,
            KeyboardOptions.RGB rgb,
            String brand,
            KeyboardOptions.KeycapMaterial keycapMaterial,
            int minPrice,
            int maxPrice
    ) {
        Query query = new Query();

        // OR 조건들을 모아둘 리스트
        List<Criteria> orCriterias = new ArrayList<>();

        // -----------------------
        // description 관련 조건
        // -----------------------
        if (size != null) {
            orCriterias.add(Criteria.where("description").regex(size.getValue(), "i"));
        }
        if (keycapProfile != null) {
            orCriterias.add(Criteria.where("description").regex(keycapProfile.getValue(), "i"));
        }
        if (keyPressure != null) {
            // spec_table.기능 > 키압 문자열에서 숫자만 추출해서 비교
            String keyPressureField = "spec_table.기능 > 키압";

            if (keyPressure == KeyboardOptions.KeyPressure.LIGHT) {
                query.addCriteria(Criteria.where(keyPressureField).regex("^([0-4]?[0-9])g$"));
            } else if (keyPressure == KeyboardOptions.KeyPressure.NORMAL) {
                query.addCriteria(Criteria.where(keyPressureField).regex("^([5-9][0-9]|[1-9][0-9]{2,})g$"));
            }
        }


// -----------------------
// Switch Type
// -----------------------
        if (switchType != null) {
            orCriterias.add(new Criteria().orOperator(
                    Criteria.where("options.option_name").regex(switchType.getValue(), "i"),
                    Criteria.where("spec_table.기능 > 키 스위치").regex(switchType.getValue(), "i"),
                    Criteria.where("name").regex(switchType.getValue(), "i")
            ));
        }

// -----------------------
// SoundDampener
// -----------------------
        if (soundDampener != null) {
            if (soundDampener == KeyboardOptions.SoundDampener.YES) {
                orCriterias.add(new Criteria().orOperator(
                        Criteria.where("spec_table.키보드구조 > 흡음재").is("○"),
                        Criteria.where("description").regex("흡음재")
                ));
            } else {
                query.addCriteria(new Criteria().andOperator(
                        Criteria.where("spec_table.키보드구조 > 흡음재").ne("○"),
                        Criteria.where("description").not().regex("흡음재")
                ));
            }
        }

// -----------------------
// RGB
// -----------------------
        if (rgb != null) {
            if (rgb == KeyboardOptions.RGB.YES) {
                orCriterias.add(new Criteria().orOperator(
                        Criteria.where("spec_table.키보드구조 > RGB 백라이트").is("○"),
                        Criteria.where("description").regex("RGB", "i")
                ));
            } else {
                query.addCriteria(new Criteria().andOperator(
                        Criteria.where("spec_table.키보드구조 > RGB 백라이트").ne("○"),
                        Criteria.where("description").not().regex("RGB", "i")
                ));
            }
        }

// -----------------------
// Brand
// -----------------------
        if (brand != null) {
            query.addCriteria(Criteria.where("manufacturer").regex("^" + brand + "$", "i"));
        }

// -----------------------
// KeycapMaterial
// -----------------------
        if (keycapMaterial != null) {
            orCriterias.add(new Criteria().orOperator(
                    Criteria.where("spec_table.키캡 > 키캡 재질").regex(keycapMaterial.getValue(), "i"),
                    Criteria.where("description").regex(keycapMaterial.getValue(), "i")
            ));
        }


// -----------------------
// Price (옵션 단위 필터링)
// -----------------------
        if (minPrice > 0 || maxPrice > 0) {
            // options 배열 안의 main_price가 범위 안에 있는지 확인
            query.addCriteria(Criteria.where("options").elemMatch(
                    Criteria.where("main_price").gte(minPrice).lte(maxPrice)
            ));
        }

// -----------------------
// OR 조건 최종 적용
// -----------------------
        if (!orCriterias.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(orCriterias.toArray(new Criteria[0])));
        }

        log.info("MongoDB Query: {}", query);

        var results = mongoTemplate.find(query, ProductDocument.class);
        log.info("검색 결과 건수: {}", results.size());
        return results.stream()
                .map(ProductDocument::toDomain)
                .toList();

    }


    // 유사 상품 추천
    @Override
    public List<Product> getSimilarProducts(UUID userId, String productId, Product baseProduct) {
        log.info("getSimilarProducts 호출: userId={}, productId={}, baseProduct={}", userId, productId, baseProduct);

        // 1단계: 후보군 넓게 추출 (카테고리 동일, 가격 ±25%)
        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(baseProduct.getCategory()));
        query.addCriteria(Criteria.where("_id").ne(baseProduct.getId()));

        double basePrice = baseProduct.getPrice();
        query.addCriteria(Criteria.where("options").elemMatch(
                Criteria.where("main_price").gte((int) (basePrice * 0.75)).lte((int) (basePrice * 1.25))
        ));

        List<ProductDocument> candidates = mongoTemplate.find(query, ProductDocument.class);
        log.info("후보군 추출: {}개", candidates.size());
        if (candidates.isEmpty()) return Collections.emptyList();

        // 2단계: 유저 선호 브랜드 및 스위치 추출
        List<Bookmark> bookmarkList = bookmarkPort.getBookmarksByUserIdAndCategoryId(userId, "keyboard");
        if (bookmarkList == null) bookmarkList = Collections.emptyList();

        Set<String> bookmarkedProductIds = bookmarkList.stream()
                .map(Bookmark::getProductId)
                .collect(Collectors.toSet());

        Query bookmarkProductsQuery = new Query(Criteria.where("_id").in(bookmarkedProductIds));
        List<ProductDocument> bookmarkedProducts = mongoTemplate.find(bookmarkProductsQuery, ProductDocument.class);

        Set<String> likedBrands = new HashSet<>();
        Set<String> likedSwitches = new HashSet<>();
        for (ProductDocument doc : bookmarkedProducts) {
            Product p = doc.toDomain();
            likedBrands.add(p.getManufacturer());

            Map<String, Object> specTable = p.getSpecTable();
            if (specTable == null) specTable = Collections.emptyMap();

            Object rawValue = specTable.get(normalizeKey("기능 > 키 스위치"));
            String keySwitch = (rawValue instanceof String) ? normalizeSwitch((String) rawValue) : null;
            if (keySwitch != null) likedSwitches.add(keySwitch);
        }

        log.info("유저 선호 브랜드={}, 선호 스위치={}", likedBrands, likedSwitches);

        // 3단계: 후보군 점수 계산 및 정렬
        List<ScoredProduct> scoredList = new ArrayList<>();
        for (ProductDocument candidateDoc : candidates) {
            Product candidate = candidateDoc.toDomain();
            SimilarityResult result = calculateSimilarityScore(baseProduct, candidate, likedBrands, likedSwitches);
            if (result.getScore() > 0) {
                scoredList.add(new ScoredProduct(candidate, result.getScore(), result.getReasons()));
            }

        }

        scoredList.sort(Comparator.comparingDouble(ScoredProduct::getScore).reversed());

        List<Product> recommended = scoredList.stream()
                .limit(6)
                .map(ScoredProduct::getProduct)
                .toList();


// 🔹 10개 추천 상품 상세 로그
        log.info("===== 최종 추천 10개 상품 상세 =====");
        for (int i = 0; i < Math.min(6, scoredList.size()); i++) {
            ScoredProduct sp = scoredList.get(i);
            Product p = sp.getProduct();
            double score = sp.getScore();
            log.info("순위 {}: 상품ID={}, 점수={}", i + 1, p.getId(), score);

            // calculateSimilarityScore 안에서 already logged한 reasons를 다시 받아오고 싶으면
            // calculateSimilarityScore를 점수와 reasons를 반환하는 객체로 바꾸거나,
            // 혹은 현재 방식처럼 로그를 찍는 것으로 충분
        }
        log.info("===== 추천 리스트 종료 =====");
        return recommended;
    }


    // 유사도 점수 계산
//    private SimilarityResult calculateSimilarityScore(
//            Product base, Product candidate,
//            Set<String> likedBrands, Set<String> likedSwitches
//    ) {
//        double score = 0.0;
//        List<String> reasons = new ArrayList<>();
//
//        Map<String, Object> baseSpec = Optional.ofNullable(base.getSpecTable())
//                .orElse(Collections.emptyMap());
//        System.out.println("baseSpec = " + baseSpec);
//        Map<String, Object> candSpec = Optional.ofNullable(candidate.getSpecTable())
//                .orElse(Collections.emptyMap());
//        System.out.println("candSpec = " + candSpec);
//
//        List<Map<String, Object>> baseOptions = Optional.ofNullable(base.getOptions()).orElse(Collections.emptyList());
//        System.out.println("baseOptions = " + baseOptions);
//
//        List<Map<String, Object>> candOptions = Optional.ofNullable(candidate.getOptions()).orElse(Collections.emptyList());
//        System.out.println("candOptions = " + candOptions);
//
//        // description List 출력
//        List<String> baseDescriptions = Optional.ofNullable(base.getDescription()).orElse(Collections.emptyList());
//        System.out.println("baseDescriptions = " + baseDescriptions);
//
//        List<String> candDescriptions = Optional.ofNullable(candidate.getDescription()).orElse(Collections.emptyList());
//        System.out.println("candDescriptions = " + candDescriptions);
//
//
//        // 1) 사이즈 필터링 (무조건 같아야 추천 대상)
//        List<String> sizeKeywords = Arrays.asList("풀배열", "미니", "텐키리스");
//
//        String baseSize = null;
//        for (String size : sizeKeywords) {
//            for (String desc : baseDescriptions) {
//                if (desc.trim().equalsIgnoreCase(size)) {
//                    baseSize = size;
//                    break;
//                }
//            }
//            if (baseSize != null) break;
//        }
//
//        String candSize = null;
//        for (String size : sizeKeywords) {
//            for (String desc : candDescriptions) {
//                if (desc.trim().equalsIgnoreCase(size)) {
//                    candSize = size;
//                    break;
//                }
//            }
//            if (candSize != null) break;
//        }
//
//        if (baseSize == null || candSize == null || !baseSize.equalsIgnoreCase(candSize)) {
//            System.out.println("사이즈 불일치: base=" + baseSize + ", candidate=" + candSize);
//            reasons.add("사이즈 불일치");
//        } else {
//            System.out.println("사이즈 일치: " + baseSize);
//            // 유사도 계산 계속 진행
//        }
//
//        // 2) 키압 유사도
//        Integer basePressure = parsePressure(baseSpec.get("기능 > 키압"));  // 공백 포함 정확 키 사용
//        System.out.println("basePressure = " + basePressure);
//        Integer candPressure = parsePressure(candSpec.get("기능 > 키압"));
//        System.out.println("candPressure = " + candPressure);
//        if (basePressure != null && candPressure != null) {
//            double diff = Math.abs(basePressure - candPressure);
//            if (diff > 10) {
//                reasons.add("키압 차이 " + diff + "g (허용 10g) - 점수 감소");
//            } else {
//                double similarity = 1.0 - diff / 10.0;
//                double s = similarity * 0.2;
//                score += s;
//                reasons.add("키압 차이=" + diff + "g, 점수=+" + String.format("%.2f", s));
//            }
//        } else {
//            reasons.add("키압 정보 부족 - 점수 산정 제외");
//        }
//
//        // 3) 스위치 비교
//
//// baseOptions 내 모든 option_name을 정규화하여 Set에 저장
//        Set<String> baseSwitches = new HashSet<>();
//        for (Map<String, Object> opt : baseOptions) {
//            Object optionName = opt.get("option_name");
//            if (optionName instanceof String) {
//                String normalized = normalizeSwitch((String) optionName);
//                if (normalized != null && !normalized.isEmpty()) {
//                    baseSwitches.add(normalized);
//                }
//            }
//        }
//
//// candOptions 내 모든 option_name을 정규화하여 리스트로 수집
//        List<String> candSwitches = new ArrayList<>();
//        for (Map<String, Object> opt : candOptions) {
//            Object optionName = opt.get("option_name");
//            if (optionName instanceof String) {
//                String normalized = normalizeSwitch((String) optionName);
//                if (normalized != null && !normalized.isEmpty()) {
//                    candSwitches.add(normalized);
//                }
//            }
//        }
//
//// 스위치 비교
//        boolean switchMatched = false;
//        boolean switchFamilyMatched = false;
//        String matchedSwitch = null;
//        outer:
//        for (String cSwitch : candSwitches) {
//            if (baseSwitches.contains(cSwitch)) {
//                switchMatched = true;
//                matchedSwitch = cSwitch;
//                break outer;
//            }
//            for (String bSwitch : baseSwitches) {
//                if (sameSwitchFamily(bSwitch, cSwitch)) {
//                    switchFamilyMatched = true;
//                    matchedSwitch = cSwitch;
//                    break outer;
//                }
//            }
//        }
//
//
//// 점수 및 이유 부여
//        if (baseSwitches.isEmpty() || candSwitches.isEmpty()) {
//            reasons.add("스위치 정보 부족 - 점수 산정 제외");
//        } else if (switchMatched) {
//            score += 0.45;
//            reasons.add("스위치 동일 (" + matchedSwitch + ") (+0.5)");
//        } else if (switchFamilyMatched) {
//            score += 0.25;
//            reasons.add("스위치 유사 (" + matchedSwitch + ") (+0.25)");
//        } else {
//            reasons.add("스위치 불일치 - 점수 감소");
//        }
//
//        // 6) description 내 공통 키워드 개수로 점수부여
////        List<String> baseDesc = Optional.ofNullable(base.getDescription()).orElse(Collections.emptyList());
////        List<String> candDesc = Optional.ofNullable(candidate.getDescription()).orElse(Collections.emptyList());
////        Set<String> baseDescSet = new HashSet<>();
////        for (String d : baseDesc) baseDescSet.add(d.trim().toLowerCase());
////        int commonDescCount = 0;
////        for (String d : candDesc) {
////            if (baseDescSet.contains(d.trim().toLowerCase())) commonDescCount++;
////        }
////        double descScore = Math.min(commonDescCount / 5.0 * 0.1, 0.1);
////        if (descScore > 0) {
////            score += descScore;
////            reasons.add("description 공통 키워드 " + commonDescCount + "개 (+"
////                    + String.format("%.2f", descScore) + ")");
////        } else {
////            reasons.add("description 공통 키워드 부족");
////        }
//
//        // 7) options 내 동일 option_name 개수로 점수부여
//        Set<String> baseOptionNames = new HashSet<>();
//        for (Map<String, Object> opt : baseOptions) {
//            Object on = opt.get("option_name");
//            if (on instanceof String) baseOptionNames.add(((String) on).trim().toLowerCase());
//        }
//        int commonOptionCount = 0;
//        for (Map<String, Object> opt : candOptions) {
//            Object on = opt.get("option_name");
//            if (on instanceof String && baseOptionNames.contains(((String) on).trim().toLowerCase())) {
//                commonOptionCount++;
//            }
//        }
//        double optionScore = Math.min(commonOptionCount / 5.0 * 0.1, 0.1);
//        if (optionScore > 0) {
//            score += optionScore;
//            reasons.add("options 공통 항목 " + commonOptionCount + "개 (+"
//                    + String.format("%.2f", optionScore) + ")");
//        } else {
//            reasons.add("options 공통 항목 부족");
//        }
//
//
//        double finalScore = Math.min(score, 1.0);
//        reasons.add("최종 점수: " + String.format("%.2f", finalScore));
//
//        return new SimilarityResult(finalScore, reasons);
//    }

    // 유사도 점수 계산 (모듈화 버전)
    private SimilarityResult calculateSimilarityScore(
            Product base, Product candidate,
            Set<String> likedBrands, Set<String> likedSwitches
    ) {
        double score = 0.0;
        List<String> reasons = new ArrayList<>();

        Map<String, Object> baseSpec = Optional.ofNullable(base.getSpecTable())
                .orElse(Collections.emptyMap());
        Map<String, Object> candSpec = Optional.ofNullable(candidate.getSpecTable())
                .orElse(Collections.emptyMap());
        List<Map<String, Object>> baseOptions = Optional.ofNullable(base.getOptions())
                .orElse(Collections.emptyList());
        List<Map<String, Object>> candOptions = Optional.ofNullable(candidate.getOptions())
                .orElse(Collections.emptyList());
        List<String> baseDescriptions = Optional.ofNullable(base.getDescription())
                .orElse(Collections.emptyList());
        List<String> candDescriptions = Optional.ofNullable(candidate.getDescription())
                .orElse(Collections.emptyList());

        log.info("baseSpec={}, candSpec={}", baseSpec, candSpec);
        log.info("baseOptions={}, candOptions={}", baseOptions, candOptions);
        log.info("baseDescriptions={}, candDescriptions={}", baseDescriptions, candDescriptions);

        // 1) 사이즈 평가
        score += evaluateSize(baseDescriptions, candDescriptions, reasons);

        // 2) 키압 평가
        score += evaluatePressure(baseSpec, candSpec, reasons);

        // 3) 스위치 평가
        score += evaluateSwitches(baseOptions, candOptions, reasons);

        // 4) 옵션 공통 항목 평가
        score += evaluateOptionOverlap(baseOptions, candOptions, reasons);

        // TODO 필요시 description 키워드 유사도 평가 추가

        double finalScore = Math.min(score, 1.0);
        reasons.add("최종 점수: " + String.format("%.2f", finalScore));

        return new SimilarityResult(finalScore, reasons);
    }

// ----------------- 개별 모듈 메서드 -----------------

    // (1) 사이즈 평가
    private double evaluateSize(List<String> baseDescriptions, List<String> candDescriptions, List<String> reasons) {
        List<String> sizeKeywords = Arrays.asList("풀배열", "미니", "텐키리스");

        String baseSize = findSizeKeyword(baseDescriptions, sizeKeywords);
        String candSize = findSizeKeyword(candDescriptions, sizeKeywords);

        if (baseSize == null || candSize == null || !baseSize.equalsIgnoreCase(candSize)) {
            log.info("사이즈 불일치: base={}, candidate={}", baseSize, candSize);
            reasons.add("사이즈 불일치");
            return 0.0; // 불일치는 점수 없음
        } else {
            log.info("사이즈 일치: {}", baseSize);
            reasons.add("사이즈 일치");
            return 0.0; // 필터 조건, 점수는 부여하지 않음
        }
    }

    private String findSizeKeyword(List<String> descriptions, List<String> keywords) {
        for (String size : keywords) {
            for (String desc : descriptions) {
                if (desc.trim().equalsIgnoreCase(size)) {
                    return size;
                }
            }
        }
        return null;
    }

    // (2) 키압 평가
    private double evaluatePressure(Map<String, Object> baseSpec, Map<String, Object> candSpec, List<String> reasons) {
        Integer basePressure = parsePressure(baseSpec.get("기능 > 키압"));
        Integer candPressure = parsePressure(candSpec.get("기능 > 키압"));
        log.info("basePressure={}, candPressure={}", basePressure, candPressure);

        if (basePressure == null || candPressure == null) {
            reasons.add("키압 정보 부족 - 점수 산정 제외");
            return 0.0;
        }

        double diff = Math.abs(basePressure - candPressure);
        if (diff > 10) {
            reasons.add("키압 차이 " + diff + "g (허용 10g) - 점수 감소");
            return 0.0;
        } else {
            double similarity = 1.0 - diff / 10.0; // diff=0 → similarity=1
            double s = similarity * 0.2;
            reasons.add("키압 차이=" + diff + "g, 점수=+" + String.format("%.2f", s));
            return s;
        }
    }

    // (3) 스위치 평가
    private double evaluateSwitches(List<Map<String, Object>> baseOptions,
                                    List<Map<String, Object>> candOptions,
                                    List<String> reasons) {
        Set<String> baseSwitches = extractSwitches(baseOptions);
        List<String> candSwitches = new ArrayList<>(extractSwitches(candOptions));

        if (baseSwitches.isEmpty() || candSwitches.isEmpty()) {
            reasons.add("스위치 정보 부족 - 점수 산정 제외");
            return 0.0;
        }

        for (String cSwitch : candSwitches) {
            if (baseSwitches.contains(cSwitch)) {
                reasons.add("스위치 동일 (" + cSwitch + ") (+0.45)");
                return 0.45;
            }
            for (String bSwitch : baseSwitches) {
                if (sameSwitchFamily(bSwitch, cSwitch)) {
                    reasons.add("스위치 유사 (" + cSwitch + ") (+0.25)");
                    return 0.25;
                }
            }
        }
        reasons.add("스위치 불일치 - 점수 감소");
        return 0.0;
    }

    private Set<String> extractSwitches(List<Map<String, Object>> options) {
        Set<String> switches = new HashSet<>();
        for (Map<String, Object> opt : options) {
            Object optionName = opt.get("option_name");
            if (optionName instanceof String) {
                String normalized = normalizeSwitch((String) optionName);
                if (normalized != null && !normalized.isEmpty()) {
                    switches.add(normalized);
                }
            }
        }
        return switches;
    }

    // (4) 옵션 공통 항목 평가
    private double evaluateOptionOverlap(List<Map<String, Object>> baseOptions,
                                         List<Map<String, Object>> candOptions,
                                         List<String> reasons) {
        Set<String> baseOptionNames = new HashSet<>();
        for (Map<String, Object> opt : baseOptions) {
            Object on = opt.get("option_name");
            if (on instanceof String) baseOptionNames.add(((String) on).trim().toLowerCase());
        }

        int commonOptionCount = 0;
        for (Map<String, Object> opt : candOptions) {
            Object on = opt.get("option_name");
            if (on instanceof String && baseOptionNames.contains(((String) on).trim().toLowerCase())) {
                commonOptionCount++;
            }
        }

        double optionScore = Math.min(commonOptionCount / 5.0 * 0.1, 0.1);
        if (optionScore > 0) {
            reasons.add("options 공통 항목 " + commonOptionCount + "개 (+" + String.format("%.2f", optionScore) + ")");
        } else {
            reasons.add("options 공통 항목 부족");
        }
        return optionScore;
    }



// ---- 유틸 ----

    // 키 정규화 (공백 제거), 로그 포함
    private String normalizeKey(String key) {
        return key == null ? null : key.trim();
    }

    // 키압 파싱, 로그 포함
    private Integer parsePressure(Object pressureObj) {
        if (pressureObj == null) {
            log.debug("parsePressure 호출: 입력 null");
            return null;
        }
        String str = pressureObj.toString().replaceAll("[^0-9]", "");
        if (str.isEmpty()) {
            log.debug("parsePressure 호출: 숫자 추출 실패 (입력값={})", pressureObj);
            return null;
        }
        Integer value = Integer.parseInt(str);
        log.debug("parsePressure 변환: 원본='{}' -> 변환={}", pressureObj, value);
        return value;
    }

    // 스위치명 정규화, 로그 포함
    private String normalizeSwitch(String sw) {
        if (sw == null) {
            log.debug("normalizeSwitch 호출: 입력 null");
            return null;
        }
        String normalized = sw.trim().replace("축", "").replace(" ", "").toLowerCase();
        log.debug("normalizeSwitch 변환: 원본='{}' -> 변환='{}'", sw, normalized);
        return normalized;
    }

    // 스위치 계열 비교, 로그 포함
    private boolean sameSwitchFamily(String s1, String s2) {
        if (s1 == null || s2 == null) {
            log.debug("sameSwitchFamily 호출: 입력 null (s1={}, s2={})", s1, s2);
            return false;
        }
        boolean result = s1.charAt(0) == s2.charAt(0);
        log.debug("sameSwitchFamily 비교: s1='{}', s2='{}' -> 결과={}", s1, s2, result);
        return result;
    }

    // ScoredProduct 내부 상태 로그
    private void logScoredProduct(ScoredProduct sp) {
        log.info("추천 상품: id={}, 점수={}", sp.getProduct().getId(), sp.getScore());
    }

    // ScoredProduct 클래스
    private static class ScoredProduct {
        private final Product product;
        private final double score;
        private final List<String> reasons;

        public ScoredProduct(Product product, double score, List<String> reasons) {
            this.product = product;
            this.score = score;
            this.reasons = reasons;
            log.info("ScoredProduct 생성: 상품ID={}, 점수={}, 이유={}", product.getId(), score, String.join(", ", reasons));
        }

        public Product getProduct() {
            return product;
        }

        public double getScore() {
            return score;
        }

        public List<String> getReasons() {
            return reasons;
        }
    }

    public static class SimilarityResult {
        private final double score;
        private final List<String> reasons;

        public SimilarityResult(double score, List<String> reasons) {
            this.score = score;
            this.reasons = reasons;
        }

        public double getScore() {
            return score;
        }

        public List<String> getReasons() {
            return reasons;
        }
    }

}

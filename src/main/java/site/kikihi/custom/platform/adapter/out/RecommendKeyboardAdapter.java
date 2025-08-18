package site.kikihi.custom.platform.adapter.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import site.kikihi.custom.platform.adapter.in.web.dto.request.product.KeyboardOptions;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocument;
import site.kikihi.custom.platform.adapter.out.mongo.product.ProductDocumentRepository;
import site.kikihi.custom.platform.application.out.recommend.RecommendKeyboardPort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendKeyboardAdapter implements RecommendKeyboardPort {

    private final MongoTemplate mongoTemplate;
    private final ProductDocumentRepository repository;

    @Override
    public List<ProductDocument> filterAndRecommendKeyboards(
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
            query.addCriteria(new Criteria().orOperator(orCriterias.toArray(new Criteria[0])));
        }

        log.info("MongoDB Query: {}", query);

        var results = mongoTemplate.find(query, ProductDocument.class);
        log.info("검색 결과 건수: {}", results.size());
        return results;

    }
}

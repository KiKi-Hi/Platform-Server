package site.kikihi.custom.platform.application.service;

import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.in.web.dto.request.custom.CustomKeyboardRequest;
import site.kikihi.custom.platform.application.in.custom.CustomKeyboardUseCase;
import site.kikihi.custom.platform.application.out.custom.CustomKeyboardPort;
import site.kikihi.custom.platform.application.out.product.ProductPort;
import site.kikihi.custom.platform.application.out.user.UserPort;
import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardLayout;
import site.kikihi.custom.platform.domain.custom.CustomKeyboardWithName;
import site.kikihi.custom.platform.domain.product.Product;
import site.kikihi.custom.platform.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 키보드 커스텀 서비스
 */

@Service
@Transactional
@RequiredArgsConstructor
public class CustomKeyboardService implements CustomKeyboardUseCase {

    /// DB 포트
    private final CustomKeyboardPort port;

    /// 외부 의존성 처리
    private final UserPort userPort;
    private final ProductPort productPort;

    // =================
    //  저장 함수
    // =================

    /**
     * 커스텀 키보드를 저장하는 로직
     *
     * @param request 키보드 저장 DTO
     */
    @Override
    public CustomKeyboard saveCustomKeyboard(CustomKeyboardRequest request, UUID userId) {

        /// 상품 예외 처리
        var frameProduct = getProduct(request.getHousingId());
        var switchProduct = getProduct(request.getSwitchId());
        var keyCapProduct = getProduct(request.getKeyCapId());

        /// 객체 생성
        var customKeyboard = CustomKeyboard.of(userId, request.getLayout(), frameProduct.getId(), switchProduct.getId(), keyCapProduct.getId(), request.getName(), "thumbnail");

        /// 저장 후 리턴
        return port.saveCustomKeyboard(customKeyboard);
    }

    // =================
    //  조회 함수
    // =================

    // 배열 조회

    /**
     * 키보드 레이아웃을 조회
     */
    @Override
    public List<CustomKeyboardLayout> getKeyboardLayouts() {
        return CustomKeyboardLayout.getKeyboardLayouts();
    }

    @Override
    public Slice<CustomKeyboardWithName> getCustomKeyboards(UUID userId) {

        /// 유저 예외 처리
        User user = getUser(userId);

        /// 커스텀 키보드 목록 조회
        Slice<CustomKeyboard> keyboards = port.loadCustomKeyboardsByUserID(user.getId());

        /// 비었다면 빈 값 출력
        if (keyboards == null) {
            keyboards = new SliceImpl<>(Collections.emptyList());
        }

        /// 필요한 모든 productId를 한 번에 수집
        Set<String> allProductIds = new HashSet<>();
        keyboards.forEach(k -> {
            allProductIds.add(k.getFrameId());
            allProductIds.add(k.getSwitchId());
            allProductIds.add(k.getKeyCapId());
        });

        /// productId로 한 번에 조회 (N+1 방지 핵심)
        Map<String, Product> productMap = getProductsByIds(allProductIds);

        /// 결과 변환
        List<CustomKeyboardWithName> customKeyboards = keyboards.stream()
                .map(keyboard -> {
                    var frameProduct = productMap.get(keyboard.getFrameId());
                    var switchProduct = productMap.get(keyboard.getSwitchId());
                    var keyCapProduct = productMap.get(keyboard.getKeyCapId());

                    return CustomKeyboardWithName.of(keyboard, frameProduct, switchProduct, keyCapProduct);
                })
                .toList();

        return new SliceImpl<>(customKeyboards, keyboards.getPageable(), keyboards.hasNext());
    }



    /**
     * 커스텀 키보드 상세 조회하기
     * @param customKeyboardId  조회할 커스텀 키보드 ID
     */
    @Override
    public CustomKeyboardWithName getCustomKeyboard(Long customKeyboardId) {

        /// 예외 처리 후 응답,
        CustomKeyboard keyBoard = getKeyboard(customKeyboardId);

        /// 개별 상품 조회
        // TODO! 한번에 조회하도록 쿼리문 수정
        var frameProduct = getProduct(keyBoard.getFrameId());
        var switchProduct = getProduct(keyBoard.getSwitchId());
        var keyCapProduct = getProduct(keyBoard.getKeyCapId());

        /// 응답
        return CustomKeyboardWithName.of(keyBoard, frameProduct, switchProduct, keyCapProduct);

    }

    // =================
    //  삭제 함수
    // =================

    /**
     * 커스텀 키보드 삭제
     * @param customKeyboardId 삭제할 커스텀 ID
     */
    @Override
    public void deleteCustomKeyboard(Long customKeyboardId, UUID userId) {

        /// 해당 유저의 커스텀 키보드인지 체크
        boolean checked = port.existCustomKeyboardByUserIdAndId(userId, customKeyboardId);

        /// 키보드가 요청자의 것이 아니라면 에러 발생
        if (!checked){
            throw new IllegalStateException(ErrorCode.UNAUTHORIZED_DELETE_CUSTOM.getMessage());
        }

        /// 권한이 정상이기에 삭제
        port.deleteCustomKeyboard(customKeyboardId);

    }


    // =================
    //  공통 함수
    // =================

    /**
     * 상품 조회 함수
     * @param productId 상품 ID
     */
    private Product getProduct(String productId) {
        return productPort.getProduct(productId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    /**
     * 상품 여러개 조회 함수
     * @param productIds   상품 IDs
     */
    private Map<String, Product> getProductsByIds(Set<String> productIds) {
        return productPort.getProductsByIds(new ArrayList<>(productIds));
    }



    /**
     * 유저 조회 함수
     * @param userId  유저 ID
     */
    private User getUser(UUID userId) {
        return userPort.loadUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    /**
     * 커스텀 키보드 조회 함수
     * @param customKeyboardId  커스텀 키보드 ID
     */
    private CustomKeyboard getKeyboard(Long customKeyboardId) {
        return port.loadCustomKeyboard(customKeyboardId)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.CUSTOM_NOT_FOUND.getMessage()));
    }

}

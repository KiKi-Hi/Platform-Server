package site.kikihi.custom.platform.domain.custom;

import site.kikihi.custom.platform.domain.product.Product;
import lombok.Builder;

import java.util.UUID;

/**
 * 여러 도메인 엔티티 정보를 조합한 커스텀 키보드 DTO 클래스입니다.
 *
 * <p>이 클래스는 프레임, 스위치, 키캡 등의 이름과 ID, 총 가격, 이미지를 포함합니다.
 * 서비스 계층에서 도메인 모델을 조합해 생성 후, 컨트롤러나 어댑터를 통해 외부로 전달됩니다.</p>
 *
 * @param id         커스텀 키보드 식별자
 * @param userId     사용자 UUID
 * @param layout     레이아웃
 * @param frameId    프레임 ID
 * @param frameName  프레임 이름
 * @param switchId   스위치 ID
 * @param switchName 스위치 이름
 * @param keyCapId   키캡 ID
 * @param keyCapName 키캡 이름
 * @param name       커스텀 키보드 이름
 * @param totalPrice 총 가격
 * @param imageUrl   키보드 이미지 URL
 */
@Builder
public record CustomKeyboardWithName(
        Long id,
        UUID userId,
        String layout,
        String frameId,
        String frameUrl,
        String frameName,
        String switchId,
        String switchName,
        String keyCapId,
        String keyCapUrl,
        String keyCapName,
        String accessoryId,
        String accessoryName,
        String name,
        double totalPrice,
        String imageUrl
) {

    public static CustomKeyboardWithName of(
            CustomKeyboard keyboard,
            Product housingProduct,
            Product switchProduct,
            Product keyCapProduct,
            Product accessoryProduct) {

        // 총 가격 계산 (null 안전)
        double totalPrice = 0.0;
        if (housingProduct   != null) totalPrice += housingProduct.getPrice();
        if (switchProduct    != null) totalPrice += switchProduct.getPrice();
        if (keyCapProduct    != null) totalPrice += keyCapProduct.getPrice();
        if (accessoryProduct != null) totalPrice += accessoryProduct.getPrice();

        return CustomKeyboardWithName.builder()
                .id(keyboard.getId())
                .userId(keyboard.getUserId())
                .layout(keyboard.getLayout() != null ? keyboard.getLayout().getLayoutName() : null)

                .frameId(housingProduct != null ? housingProduct.getId() : null)
                .frameUrl(housingProduct != null ? housingProduct.getMapping() : null)
                .frameName(housingProduct != null ? housingProduct.getName() : null)

                .switchId(switchProduct != null ? switchProduct.getId() : null)
                .switchName(switchProduct != null ? switchProduct.getName() : null)

                .keyCapId(keyCapProduct != null ? keyCapProduct.getId() : null)
                .keyCapUrl(keyCapProduct != null ? keyCapProduct.getMapping() : null)
                .keyCapName(keyCapProduct != null ? keyCapProduct.getName() : null)

                .accessoryId(accessoryProduct != null ? accessoryProduct.getId() : null)
                .accessoryName(accessoryProduct != null ? accessoryProduct.getName() : null)

                .name(keyboard.getName())
                .totalPrice(totalPrice)
                .imageUrl(keyboard.getImageUrl())
                .build();
    }
}

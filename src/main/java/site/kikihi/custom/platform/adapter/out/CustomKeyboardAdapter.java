package site.kikihi.custom.platform.adapter.out;

import jakarta.transaction.Transactional;
import site.kikihi.custom.global.response.ErrorCode;
import site.kikihi.custom.platform.adapter.out.jpa.custom.CustomKeyboardJpaEntity;
import site.kikihi.custom.platform.adapter.out.jpa.custom.CustomKeyboardJpaRepository;
import site.kikihi.custom.platform.application.out.custom.CustomKeyboardPort;
import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomKeyboardAdapter implements CustomKeyboardPort {

    /// DB 저장
    private final CustomKeyboardJpaRepository repository;

    // =================
    //  DB 저장
    // =================
    /**
     * DB에 저장하는 함수 로직
     * @param customKeyboard    저장할 도메인 객체
     */
    @Override
    public CustomKeyboard saveCustomKeyboard(CustomKeyboard customKeyboard) {
        var entity = CustomKeyboardJpaEntity.from(customKeyboard);

        return repository.save(entity)
                .toDomain();
    }


    // =================
    //  DB 조회
    // =================

    /**
     * 상세 조회
     * @param id    상세 조회할 ID
     */
    @Override
    public Optional<CustomKeyboard> loadCustomKeyboard(Long id) {
        return repository.findById(id)
                .map(CustomKeyboardJpaEntity::toDomain);
    }

    /**
     * 유저에 따른 목록 조회
     * @param userID    유저 ID
     */
    @Override
    public Slice<CustomKeyboard> loadCustomKeyboardsByUserID(UUID userID) {
        return repository.findKeyboardsByUserId(userID)
                .map(CustomKeyboardJpaEntity::toDomain);
    }

    /**
     * 커스텀 키보드를 제작한 사람과 요청자가 동일한 지 체크
     * @param userId    유저
     * @param id        커스텀 키보드 ID
     */
    @Override
    public boolean existCustomKeyboardByUserIdAndId(UUID userId, Long id) {
        return repository.existsByUserIdAndId(userId, id);
    }

    /**
     * 커스텀 키보드를 제작했다면 최신의 것 반영
     * @param userId    유저
     */
    @Override
    public Optional<CustomKeyboard> loadCustomKeyboardByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .findFirst()
                .map(CustomKeyboardJpaEntity::toDomain);
    }

    /**
     * 커스텀 키보드 내부에 해당 상품 ID가 존재하는지 체크
     *
     * @param id        커스텀 ID
     * @param productId 상품 ID
     */
    @Override
    public boolean existProductInsideCustomKeyboard(Long id, String categoryId, String productId) {

        switch (categoryId) {
            case "housing":
                return repository.existsByIdAndFrameId(id, productId);
            case "switch":
                return repository.existsByIdAndSwitchId(id, productId);
            case "keycap":
                return repository.existsByIdAndKeyCapId(id, productId);
            case "accessory":
                return repository.existsByIdAndAccessoryId(id, productId);
            default:
                return false;
        }
    }

    // =================
    //  DB 수정
    // =================

    /**
     * 수정
     * @param customKeyboard    수정할 도메인
     */
    @Override
    public void updateCustomKeyboard(CustomKeyboard customKeyboard) {

    }

    // =================
    //  DB 삭제
    // =================

    /**
     * 삭제할 커스텀 키보드
     * @param id    삭제할 ID
     */
    @Override
    public void deleteCustomKeyboard(Long id) {
        repository.deleteById(id);
    }

    /**
     * 커스텀 키보드 내부 상품 삭제
     * @param id            아이디
     * @param categoryId    카테고리
     * @param productId     상품 ID
     */
    @Override
    @Transactional
    public void deleteProductInsideCustomKeyboard(Long id, String categoryId, String productId) {

        /// 실질적으로는 삭제가 아닌 수정으로 진행
        /// JPA이기에 영속성을 살려서, 더티 체킹으로서 수행

        /// 조회를 통해, 영속성 컨테이너에 넣기
        CustomKeyboardJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        /// 찾은 값을 도메인으로 변환 및 로직 수행
        CustomKeyboard domain = entity.toDomain();
        /// 도메인 로직 수행
        domain.removeProduct(categoryId, productId);

        /// 더티체킹 수행(영속성 컨테이너 있는 값 수정)
        entity.update(domain);

    }

    /**
     * 커스텀 키보드 내부 상품 추기
     * @param id            아이디
     * @param categoryId    카테고리
     * @param productId     상품 ID
     */
    @Override
    public void addProductInsideCustomKeyboard(Long id, String categoryId, String productId) {

        // 실질적으로는 삭제가 아닌 수정으로 진행
        /// JPA이기에 영속성을 살려서, 더티 체킹으로서 수행

        /// 조회를 통해, 영속성 컨테이너에 넣기
        CustomKeyboardJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));

        /// 찾은 값을 도메인으로 변환 및 로직 수행
        CustomKeyboard domain = entity.toDomain();
        /// 도메인 로직 수행
        domain.addProduct(categoryId, productId);

        /// 더티체킹 수행(영속성 컨테이너 있는 값 수정)
        entity.update(domain);

    }
}

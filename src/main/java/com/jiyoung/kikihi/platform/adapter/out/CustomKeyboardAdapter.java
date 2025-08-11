package com.jiyoung.kikihi.platform.adapter.out;

import com.jiyoung.kikihi.platform.adapter.out.jpa.custom.CustomKeyboardJpaEntity;
import com.jiyoung.kikihi.platform.adapter.out.jpa.custom.CustomKeyboardJpaRepository;
import com.jiyoung.kikihi.platform.application.out.custom.CustomKeyboardPort;
import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

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

    @Override
    public Optional<CustomKeyboard> loadCustomKeyboardByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .map(CustomKeyboardJpaEntity::toDomain);
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
}

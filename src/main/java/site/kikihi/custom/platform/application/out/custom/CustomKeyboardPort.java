package site.kikihi.custom.platform.application.out.custom;

import site.kikihi.custom.platform.domain.custom.CustomKeyboard;
import org.springframework.data.domain.Slice;

import java.util.Optional;
import java.util.UUID;

public interface CustomKeyboardPort {

    /// 저장
    CustomKeyboard saveCustomKeyboard(CustomKeyboard customKeyboard);

    /// 조회
    // 상세 조회
    Optional<CustomKeyboard> loadCustomKeyboard(Long id);

    // 목록 조회
    Slice<CustomKeyboard> loadCustomKeyboardsByUserID(UUID userID);

    // 존재 여부 판단
    boolean existCustomKeyboardByUserIdAndId(UUID userId, Long id);

    // 존재 여부 판단
    Optional<CustomKeyboard> loadCustomKeyboardByUserId(UUID userId);

    /// 수정
    void updateCustomKeyboard(CustomKeyboard customKeyboard);


    /// 삭제
    void deleteCustomKeyboard(Long id);

}

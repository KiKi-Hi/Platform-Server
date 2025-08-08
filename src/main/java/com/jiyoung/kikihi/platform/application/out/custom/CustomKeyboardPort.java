package com.jiyoung.kikihi.platform.application.out.custom;

import com.jiyoung.kikihi.platform.domain.custom.CustomKeyboard;
import com.jiyoung.kikihi.platform.domain.product.Product;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CustomKeyboardPort {

    /// 저장
    CustomKeyboard saveCustomKeyBoard(CustomKeyboard customKeyBoard);

    /// 조회
    // 상세 조회
    Optional<CustomKeyboard> loadCustomKeyBoard(Long id);

    // 목록 조회
    Slice<CustomKeyboard> loadCustomKeyBoardsByUserID(UUID userID);

    // 존재 여부 판단
    boolean existCustomKeyBoardByUserIdAndId(UUID userId, Long id);

    /// 수정
    void updateCustomKeyBoard(CustomKeyboard customKeyBoard);


    /// 삭제
    void deleteCustomKeyBoard(Long id);

}

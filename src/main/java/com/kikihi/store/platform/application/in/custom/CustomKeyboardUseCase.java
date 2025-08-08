package com.kikihi.store.platform.application.in.custom;

import com.kikihi.store.platform.adapter.in.web.dto.request.CustomKeyBoardRequest;
import com.kikihi.store.platform.domain.custom.CustomKeyboard;
import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
import com.kikihi.store.platform.domain.custom.CustomKeyboardWithName;
import org.springframework.data.domain.Slice;

import java.util.*;

/**
 * 커스텀 키보드 관련 인터페이스입니다.
 * - 배열 조회
 * - 내 키보드 생성/수정/삭제
 * - 실제 상품 카테고리별 목록 조회
 * - 필터링(가격대)
 * - 좋아요
 * - 이름 저장하기
 * - 부품 추가하기
 * - 내 키보드 목록 조회
 * - 내 키보드 상세 조회
 */
public interface CustomKeyboardUseCase {

    /// 생성
    CustomKeyboard saveCustomKeyBoard(CustomKeyBoardRequest request, UUID userId);

    /// 조회
    // 배열 조회
    List<CustomKeyboardLayout> getKeyboardLayouts();

    // 나의 커스텀 키보드 목록 조회
    Slice<CustomKeyboardWithName> getCustomKeyBoards(UUID userId);

    // 나의 커스텀 키보드 목록 조회
    CustomKeyboardWithName getCustomKeyBoard(Long customKeyBoardId);

    // 부품 추가하기


    /// 삭제
    void deleteCustomKeyBoard(Long customKeyBoardId, UUID userId);


}

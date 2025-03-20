package com.jiyoung.kikihi.platform.application.port.in.category;

public interface CategoryUseCase {


    // 카테고리 생성하기


    // 카테고리 상세 조회하기(하위 카테고리까지 조회)

    // 카테고리 목록 조회하기(루트)


    // 카테고리 제거하기
    void deleteCategory(Long categoryId);

}

package com.jiyoung.kikihi.global.response.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CustomPageRequest {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    // 인기순,,,,
//    private String sortBy;
//    private String direction;
//
//    public Pageable toPageable() {
//        Sort sort = Sort.unsorted();
//
//        if (sortBy != null && direction != null) {
//            try {
//                sort = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
//            } catch (IllegalArgumentException e) {
//                log.warn("Invalid sort direction: {}. Defaulting to ASC.", direction);
//                sort = Sort.by(Sort.Direction.ASC, sortBy);  // 기본값 설정
//            }
//        }

    }

package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.platform.application.port.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductLikeController {

    private final ReactionService reactionService;

    //userID -> 나중에 추가하기로!
    //따로 테이블을 만들어야하나? -
    @PostMapping("/{productId}")
    public ApiResponse<?> toggleLike(@PathVariable Long productId) {
        long userId=1L;
        boolean hasLiked = reactionService.handleLike(productId,userId);
        String message = hasLiked ? "좋아요가 추가되었습니다." : "좋아요가 취소되었습니다.";

        return ApiResponse.ok(message);
    }

}
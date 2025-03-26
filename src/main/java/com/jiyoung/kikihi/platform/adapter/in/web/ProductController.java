package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.pageable.CustomPageRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.KeycapFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.SwitchFilter;
import com.jiyoung.kikihi.platform.application.port.service.CustomService;
import com.jiyoung.kikihi.platform.application.port.service.GetProductService;
import com.jiyoung.kikihi.platform.domain.product.CustomKeyboard;
import com.jiyoung.kikihi.platform.domain.product.frame.Frame;
import com.jiyoung.kikihi.platform.domain.product.keycap.Keycap;
import com.jiyoung.kikihi.platform.domain.product.switches.Switch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/custom")
@RequiredArgsConstructor
public class ProductController {

    private final GetProductService getProductService;
    private final CustomService customService;


    // custom keyboard 저장 (순서,호환 여부 check 가격 계산) 후 저장
    @GetMapping
    public ApiResponse<CustomKeyboard> saveCustomKeyboard(CustomRequest customRequest) {
        CustomKeyboard customKeyboard=customService.create(customRequest);
        return ApiResponse.ok(customKeyboard);
    }


    // 조회 & 필터링 /api/frames?page=0&size=5&sort=price,desc
    @GetMapping("/frames")
    public ApiResponse<Page<Frame>> getFrames(
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String mountType,
            @RequestParam(required = false) String soundDampening,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) String layout,
            CustomPageRequest pageRequest) {
        FrameFilter filter = new FrameFilter(material, mountType, soundDampening, weight, layout);
        Pageable pageable = PageRequest.of(
                Math.max(pageRequest.getPage(), 0),  // 페이지 번호가 0보다 작지 않도록 보정
                pageRequest.getSize()
//                Sort.by(Sort.Direction.fromString(pageRequest.getDirection()), "price")  // <- 변경된 부분
        );
        System.out.println("Filter: " + filter);
        System.out.println("pageable:"+pageable);
        // 필터, 프레임 list를 다 갖고 오기 위해서 뭐가 필요한가?? 전체를 가져오는 건데???
        Page<Frame> frameList = getProductService.getFrameByFilter(filter, pageable);
        System.out.println("Total elements: " + frameList.getTotalElements());
        return ApiResponse.ok(frameList);
    }


//    @GetMapping("/keycaps")
//    public ApiResponse<List<Keycap>> getKeycaps(
//            KeycapFilter filter,
//            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.ASC) Pageable pageable,
//            @RequestParam(required = false) Boolean usePaging) {
//
//        return ApiResponse.ok(getProductsByFilter(filter, pageable, usePaging));
//    }
//
//    @GetMapping("/switches")
//    public ApiResponse<List<Switch>> getSwitches(
//            SwitchFilter filter,
//            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.ASC) Pageable pageable,
//            @RequestParam(required = false) Boolean usePaging) {
//
//        return ApiResponse.ok(getProductsByFilter(filter, pageable, usePaging));
//    }


}

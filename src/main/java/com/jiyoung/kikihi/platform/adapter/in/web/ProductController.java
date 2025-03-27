package com.jiyoung.kikihi.platform.adapter.in.web;

import com.jiyoung.kikihi.global.response.ApiResponse;
import com.jiyoung.kikihi.global.response.pageable.CustomPageRequest;
import com.jiyoung.kikihi.global.response.pageable.PageResponse;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.CustomRequest;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.FrameFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.KeycapFilter;
import com.jiyoung.kikihi.platform.adapter.in.web.dto.request.filter.SwitchFilter;
import com.jiyoung.kikihi.platform.application.port.in.product.CreateCustomUseCase;
import com.jiyoung.kikihi.platform.application.port.in.product.GetProductUseCase;
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

    private final GetProductUseCase getProductService;
    private final CreateCustomUseCase customService;


    // custom keyboard 저장 (순서,호환 여부 check 가격 계산) 후 저장
    @PostMapping
    public ApiResponse<CustomKeyboard> saveCustomKeyboard(@RequestBody CustomRequest customRequest) {
        // 입력받은 JSON 데이터를 출력
        System.out.println("Received DTO: " + customRequest);

        // 개별 필드가 null인지 확인
        System.out.println("frameId: " + customRequest.getFrameId());
        System.out.println("switchId: " + customRequest.getSwitchId());
        System.out.println("keycapId: " + customRequest.getKeycapId());

        CustomKeyboard customKeyboard=customService.create(customRequest);
        return ApiResponse.ok(customKeyboard);
    }


    // 조회 & 필터링 /api/frames?page=0&size=5&sort=price,desc
    @GetMapping("/frames")
    public ApiResponse<PageResponse<Frame>> getFrames(
                @RequestParam(required = false) String material,
                @RequestParam(required = false) String mountType,
                @RequestParam(required = false) String soundDampening,
                @RequestParam(required = false) Double weight,
                @RequestParam(required = false) String layout,
                CustomPageRequest pageRequest) {
        FrameFilter filter = new FrameFilter(material, mountType, soundDampening, weight, layout);
        Pageable pageable = org.springframework.data.domain.PageRequest
                .of(pageRequest.getPage() - 1, pageRequest.getSize(), Sort.by("id")
                        .descending());
        Page<Frame> result=getProductService.getFrame(pageable);
        List<Frame> frames=result.getContent();
        return ApiResponse.ok(new PageResponse<>(frames,pageRequest,result.getTotalElements()));
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

    // 상품 좋아요 기능



}

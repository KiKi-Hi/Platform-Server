package com.kikihi.store.platform.adapter.in.web.dto.request;

import com.kikihi.store.platform.domain.custom.CustomKeyboardLayout;
import lombok.Data;

/**
 * 커스텀 키보드를 만들기 위한 요청 DTO
 */
@Data
public class CustomKeyBoardRequest {

    private CustomKeyboardLayout layout;

    private String housingId;

    private String switchId;

    private String keyCapId;

    private String name;

}

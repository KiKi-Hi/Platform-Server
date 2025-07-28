package com.jiyoung.kikihi.platform.adapter.in.web.dto.request;

import lombok.Data;

/**
 * 커스텀 키보드를 만들기 위한 요청 DTO
 */
@Data
public class CustomKeyBoardRequest {

    private String frameId;

    private String switchId;

    private String keyCapId;

    private String name;

}

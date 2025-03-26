package com.jiyoung.kikihi.global.config;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig {
    // Other configuration beans, if necessary
}

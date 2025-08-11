package com.jiyoung.kikihi.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.TreeMap;

@Configuration
@Profile("prod")
public class SwaggerConfig {

    @Value("${spring.back.prod}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
        );

        Server server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("KIKIHI API 명세서");

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .addServersItem(server);
    }

    private Info apiInfo() {
        return new Info()
                .version("1.0")
                .title("키키하이 API")
                .description("키키하이 개발 서버의 API 입니다");
    }

    @Bean
    public OpenApiCustomizer removeGenericSchemas() {
        return openApi -> {
            openApi.getComponents().getSchemas().keySet().removeIf(name ->
                    name.contains("ApiResponse") || name.contains("SliceResponse")
            );
        };
    }
    /// 스키마 이름 기준 오름차순

    @Bean
    public OpenApiCustomizer sortSchemasAlphabetically() {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
}

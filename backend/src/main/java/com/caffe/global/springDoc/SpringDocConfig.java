package com.caffe.global.springDoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Caffe Menu Service API", version = "beta", description = "BE8 Team12 1차 프로젝트 API 문서입니다."))
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi groupCaffeApi() {
        return GroupedOpenApi.builder()
                .group("caffeApi")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi groupController() {
        return GroupedOpenApi.builder()
                .group("domain")
                .pathsToExclude("/**")
                .build();
    }
}

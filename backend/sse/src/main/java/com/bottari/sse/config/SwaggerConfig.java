package com.bottari.sse.config;

import com.bottari.sse.error.ErrorCodeOperationCustomizer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${bottari.openapi.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${bottari.openapi.server.description:Local}")
    private String serverDescription;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(new Server().url(serverUrl).description(serverDescription)))
                .addSecurityItem(getSecurityRequirement())
                .components(getComponents());
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return new ErrorCodeOperationCustomizer();
    }

    private Info apiInfo() {
        return new Info()
                .title("Bottari API docs")
                .description("Bottari API docs")
                .version("1.0.0");
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement()
                .addList("ssaid");
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("ssaid", getSecurityScheme());
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("ssaid")
                .description("SSAID를 입력해주세요.");
    }
}

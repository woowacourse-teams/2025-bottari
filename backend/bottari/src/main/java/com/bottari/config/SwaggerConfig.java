package com.bottari.config;

import com.bottari.error.ErrorCodeOperationCustomizer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class SwaggerConfig {

    private final OpenApiServerProperties openApiServerProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(new Server()
                        .url(openApiServerProperties.url())
                        .description(openApiServerProperties.description())))
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

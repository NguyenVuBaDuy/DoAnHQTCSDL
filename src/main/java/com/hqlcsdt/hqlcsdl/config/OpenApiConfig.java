package com.hqlcsdt.hqlcsdl.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger configuration.
 *
 * - Swagger UI:  http://localhost:8080/swagger-ui/index.html
 * - API Docs:    http://localhost:8080/v3/api-docs
 *
 * Nút "Authorize" trên Swagger UI cho phép paste accessToken (Bearer JWT).
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HQLCSDL API")
                        .description("API hệ quản lý cơ sở dữ liệu — Đồ án môn học")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập accessToken (không cần prefix \"Bearer \")")
                        ));
    }
}

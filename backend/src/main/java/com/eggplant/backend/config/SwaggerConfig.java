package com.eggplant.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // Define the Security Scheme
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Authorization header using the Bearer scheme. Example: \"Bearer {token}\"");

        // Add Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        // Configure OpenAPI
        return new OpenAPI()
                .info(new Info()
                        .title("JWT Authenticated API")
                        .version("1.0.0")
                        .description("API documentation with JWT-based authentication"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}

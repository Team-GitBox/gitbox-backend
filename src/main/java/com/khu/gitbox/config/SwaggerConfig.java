package com.khu.gitbox.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(title = "깃박스 백엔드 API 명세서",
                description = "깃박스 백엔드 API 명세서 입니다",
                version = "1.0"),
        servers = {
                @Server(url = "/", description = "깃박스 서버 URL")}
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer", securityScheme))
                .security(Collections.singletonList(securityRequirement));
    }
}

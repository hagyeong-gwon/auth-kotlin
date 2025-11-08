package demo.kotlin.auth.infrastructure.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): OpenAPI =
        OpenAPI().apply {
            info =
                Info()
                    .title("Demo Kotlin Auth API")
                    .description("Demo Kotlin Auth API")
                    .version("v1.0.0")

            components = Components()

            addSecurityItem(SecurityRequirement().addList("BearerAuth"))
            val securityScheme =
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .`in`(SecurityScheme.In.HEADER)
                    .name("Authorization")
            schemaRequirement("BearerAuth", securityScheme)
        }

    // TODO : 공통 response 타입 설정 하기
}

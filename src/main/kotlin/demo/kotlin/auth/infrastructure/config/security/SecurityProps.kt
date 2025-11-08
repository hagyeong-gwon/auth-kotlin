package demo.kotlin.auth.infrastructure.config.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security")
data class SecurityProps(
    val cors: Cors,
) {
    data class Cors(
        val allowedOrigins: List<String>,
        val allowedMethods: List<String>,
        val allowCredentials: Boolean,
        val allowedHeaders: List<String>,
    )
}

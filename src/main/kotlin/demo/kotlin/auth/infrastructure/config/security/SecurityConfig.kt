package demo.kotlin.auth.infrastructure.config.security

import demo.kotlin.auth.application.port.TokenPort
import demo.kotlin.auth.infrastructure.config.security.filter.BearerTokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val props: SecurityProps,
    private val tokenProvider: TokenPort,
) {
    // 인증 pass 허용
    val allowedList = listOf("api/auth/register", "api/auth/login")

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .cors(cors())
            .csrf { it.disable() }
            .requestCache { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .anonymous { it.disable() }
            .sessionManagement { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*allowedList.toTypedArray())
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }.addFilterBefore(
                BearerTokenAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter::class.java,
            ).build()

    private fun cors(): Customizer<CorsConfigurer<HttpSecurity>> {
        val config =
            CorsConfiguration()
                .apply {
                    allowedOrigins = props.cors.allowedOrigins
                    allowedMethods = props.cors.allowedMethods
                    allowCredentials = props.cors.allowCredentials
                    allowedHeaders = props.cors.allowedHeaders
                }
        val source = UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", config) }
        return Customizer { it.configurationSource(source) }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

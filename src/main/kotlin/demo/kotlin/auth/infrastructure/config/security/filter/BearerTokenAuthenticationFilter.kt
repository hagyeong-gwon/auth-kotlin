package demo.kotlin.auth.infrastructure.config.security.filter

import demo.kotlin.auth.application.port.TokenPort
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class BearerTokenAuthenticationFilter(
    private val tokenProvider: TokenPort,
) : OncePerRequestFilter() {
    private val logger = KotlinLogging.logger {}

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = extractToken(request)

        if (token != null) {
            try {
                val authentication = tokenProvider.verify(token)
                SecurityContextHolder.getContext().authentication = authentication
                logger.debug { "Successfully authenticated user: ${authentication.principal}" }
            } catch (e: JwtException) {
                logger.warn { "Invalid JWT token: ${e.message}" }
                // Continue without authentication - let @RequireAuth handle authorization
            } catch (e: IllegalArgumentException) {
                logger.warn { "Invalid token format: ${e.message}" }
                // Continue without authentication
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")
        return if (authHeader?.startsWith("Bearer ") == true) {
            authHeader.substring(7)
        } else {
            null
        }
    }
}

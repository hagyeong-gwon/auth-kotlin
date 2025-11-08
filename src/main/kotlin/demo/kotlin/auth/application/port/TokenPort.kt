package demo.kotlin.auth.application.port

import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

interface TokenPort {
    fun generateToken(
        userDetails: UserDetails,
        tokenType: TokenType,
    ): String

    fun verify(token: String): UsernamePasswordAuthenticationToken

    fun invalidate(token: String)

    data class UserDetails(
        val id: Int,
        val username: String,
        val email: String,
        val roles: List<Role>?,
        val userType: UserType,
    )

    enum class TokenType {
        ACCESS,
        REFRESH,
    }
}

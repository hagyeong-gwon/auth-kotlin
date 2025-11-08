package demo.kotlin.auth.application.usecase.service

import demo.kotlin.auth.application.port.RedisCachePort
import demo.kotlin.auth.application.port.TokenPort
import demo.kotlin.auth.application.port.UserPort
import demo.kotlin.auth.domain.entity.User
import demo.kotlin.auth.domain.enumerate.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenPort: TokenPort,
    private val passwordEncoder: PasswordEncoder,
    private val redisCachePort: RedisCachePort,
    private val userPort: UserPort,
) {
    @Value("\${jwt.expire-time.refresh}")
    private val refreshExpirationInMs = 0

    fun login(user: User): Pair<String, String> {
        val userDetails =
            TokenPort.UserDetails(
                id = user.id!!,
                username = user.username,
                email = user.email,
                roles = user.roles?.map { Role.valueOf(it.name) },
                userType = user.userType,
            )
        val accessToken =
            tokenPort.generateToken(
                userDetails = userDetails,
                tokenType = TokenPort.TokenType.ACCESS,
            )
        val refreshToken =
            tokenPort.generateToken(
                userDetails = userDetails,
                tokenType = TokenPort.TokenType.REFRESH,
            )

        redisCachePort.save(user.id.toString(), refreshToken, refreshExpirationInMs)
        return Pair(accessToken, refreshToken)
    }

    fun refresh(user: User) =
        redisCachePort.get(user.id.toString())?.let {
            tokenPort.generateToken(
                userDetails =
                    TokenPort.UserDetails(
                        id = user.id!!,
                        username = user.username,
                        email = user.email,
                        roles = user.roles?.map { Role.valueOf(it.name) },
                        userType = user.userType,
                    ),
                tokenType = TokenPort.TokenType.ACCESS,
            )
        }

    fun isValidatePassword(
        passwordEncoded: String,
        password: String,
    ) = passwordEncoder.matches(password, passwordEncoded)
}

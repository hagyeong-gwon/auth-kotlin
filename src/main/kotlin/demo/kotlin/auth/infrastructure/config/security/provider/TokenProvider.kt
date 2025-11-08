package demo.kotlin.auth.infrastructure.config.security.provider

import demo.kotlin.auth.application.port.TokenPort
import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType
import demo.kotlin.auth.infrastructure.config.security.CustomUserDetails
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.spec.SecretKeySpec

@Service
class TokenProvider(
    private val redisTemplate: RedisTemplate<Any, Any>,
) : TokenPort {
    @Value("\${jwt.secret-key}")
    private val secretKey: String = ""

    @Value("\${jwt.expire-time.access}")
    private val accessExpireTime: Long = 0

    @Value("\${jwt.expire-time.refresh}")
    private val refreshExpireTime: Long = 0

    @Value("\${jwt.issuer}")
    private val issuer: String = ""

    private val logger = KotlinLogging.logger {}

    override fun generateToken(
        userDetails: TokenPort.UserDetails,
        tokenType: TokenPort.TokenType,
    ): String =
        Jwts
            .builder()
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName), SignatureAlgorithm.HS512)
            .setIssuer(issuer)
            .setSubject(userDetails.username)
            .claim("userId", userDetails.id)
            .claim("username", userDetails.username)
            .claim("email", userDetails.email)
            .claim("roles", userDetails.roles?.map { it.name })
            .claim("userType", userDetails.userType.name)
            .setIssuedAt(Date())
            .setExpiration(Date(if (tokenType == TokenPort.TokenType.ACCESS) accessExpireTime else refreshExpireTime))
            .compact()

    override fun verify(token: String): UsernamePasswordAuthenticationToken {
        val claims = parseToken(token)

        if (redisTemplate.opsForValue().get("blacklist:${claims.id}") != null) {
            throw IllegalArgumentException("Invalid token")
        }

        val roles = claims["roles"] as? List<*>
        val authorities =
            roles?.mapNotNull {
                it?.toString()?.let { role -> SimpleGrantedAuthority("ROLE_$role") }
            } ?: emptyList()

        val userId = claims["userId"].toString().toIntOrNull() ?: throw IllegalArgumentException("Invalid userId in token")
        val username = claims["username"]?.toString() ?: claims.subject
        val email = claims["email"]?.toString() ?: ""

        val principal =
            CustomUserDetails(
                id = userId,
                username = username,
                email = email,
                authorities = authorities,
            )

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    override fun invalidate(token: String) {
        val claims = parseToken(token)
        val jti = claims.id
        val expiration = claims.expiration

        redisTemplate.opsForValue().set(
            "blacklist:$jti",
            "true",
            expiration.time - System.currentTimeMillis(),
            TimeUnit.MILLISECONDS,
        ) // 토큰 만료 시간까지 블랙리스트 등록

        val userId = claims["userId"].toString().toIntOrNull() ?: throw IllegalArgumentException("Invalid userId in token")
        redisTemplate.delete("$userId") // refresh token 삭제
    }

    private fun parseToken(token: String) =
        Jwts
            .parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
}

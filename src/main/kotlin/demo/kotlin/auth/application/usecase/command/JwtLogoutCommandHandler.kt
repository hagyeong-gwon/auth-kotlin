package demo.kotlin.auth.application.usecase.command

import demo.kotlin.auth.application.port.TokenPort
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class JwtLogoutCommandHandler(
    private val tokenPort: TokenPort,
) : LogoutHandler {
    override fun logout(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        val token = request?.getHeader("Authorization")?.removePrefix("Bearer ")
        if (token != null) {
            tokenPort.invalidate(token)
        }
        // TODO: redis 값 제거
    }
}

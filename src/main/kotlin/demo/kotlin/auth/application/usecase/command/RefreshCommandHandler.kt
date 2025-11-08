package demo.kotlin.auth.application.usecase.command

import demo.kotlin.auth.application.port.UserPort
import demo.kotlin.auth.application.usecase.service.AuthService
import demo.kotlin.auth.domain.entity.CurrentUser
import demo.kotlin.auth.domain.error.exception.ForbiddenException
import org.springframework.stereotype.Service

@Service
class RefreshCommandHandler(
    private val authService: AuthService,
    private val userPort: UserPort,
) {
    fun handle(user: CurrentUser): String {
        val user = userPort.findById(user.id) ?: throw IllegalStateException("User not found")

        return authService.refresh(user = user) ?: throw ForbiddenException("No Login")
    }
}

package demo.kotlin.auth.application.usecase.command

import demo.kotlin.auth.application.port.UserPort
import demo.kotlin.auth.application.usecase.service.AuthService
import demo.kotlin.auth.domain.error.exception.ForbiddenException
import org.springframework.stereotype.Service

@Service
class LoginCommandHandler(
    private val authService: AuthService,
    private val userPort: UserPort,
) {
    fun handle(command: Command): Result {
        val user = userPort.findByEmail(command.email) ?: throw ForbiddenException("User not found for email: ${command.email}")

        if (!authService.isValidatePassword(passwordEncoded = user.passwordEncoded, password = command.password)) {
            throw ForbiddenException("Invalid password")
        }

        val (accessToken, refreshToken) = authService.login(user)
        return Result(accessToken = accessToken, refreshToken = refreshToken)
    }

    data class Command(
        val email: String,
        val password: String,
    )

    data class Result(
        val accessToken: String,
        val refreshToken: String,
    )
}

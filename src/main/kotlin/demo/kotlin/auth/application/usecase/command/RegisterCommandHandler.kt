package demo.kotlin.auth.application.usecase.command

import demo.kotlin.auth.application.port.UserPort
import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class RegisterCommandHandler(
    private val userPort: UserPort,
) {
    @Transactional
    fun handle(command: Command) {
        // TODO: validation function 만들기
        if (userPort.findByEmail(command.email) != null) {
            throw BadRequestException("User with email ${command.email} already exists")
        }
        userPort.insert(
            registerArg =
                UserPort.RegisterArg(
                    username = command.username,
                    password = command.password,
                    email = command.email,
                    phoneNumber = command.phoneNumber,
                    roles = command.roles,
                    userType = command.userType,
                ),
        )
    }

    data class Command(
        val username: String,
        val password: String,
        val email: String,
        val phoneNumber: String,
        val roles: List<Role>? = null,
        val userType: UserType,
    )
}

package demo.kotlin.auth.adapter.input.api.rest

import demo.kotlin.auth.adapter.input.api.rest.dto.LoginRequestDto
import demo.kotlin.auth.adapter.input.api.rest.dto.RefreshResponseDto
import demo.kotlin.auth.adapter.input.api.rest.dto.RegisterRequestDto
import demo.kotlin.auth.adapter.input.api.rest.dto.VerifyResponseDto
import demo.kotlin.auth.application.usecase.command.LoginCommandHandler
import demo.kotlin.auth.application.usecase.command.RefreshCommandHandler
import demo.kotlin.auth.application.usecase.command.RegisterCommandHandler
import demo.kotlin.auth.domain.annotation.CurrentUser
import demo.kotlin.auth.domain.annotation.RequireAuth
import demo.kotlin.auth.domain.enumerate.UserType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val registerCommandHandler: RegisterCommandHandler,
    private val loginCommandHandler: LoginCommandHandler,
    private val refreshCommandHandler: RefreshCommandHandler,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequestDto,
    ) {
        registerCommandHandler.handle(
            RegisterCommandHandler.Command(
                username = request.username,
                password = request.password,
                email = request.email,
                phoneNumber = request.phoneNumber,
                userType = UserType.USER,
            ),
        )
    }

    @PostMapping("/login")
    fun login(
        @RequestBody
        request: LoginRequestDto,
    ) = loginCommandHandler.handle(
        LoginCommandHandler.Command(
            email = request.email,
            password = request.password,
        ),
    )

    @PostMapping("/refresh")
    @RequireAuth
    fun refresh(
        @CurrentUser me: demo.kotlin.auth.domain.entity.CurrentUser,
    ): RefreshResponseDto {
        val refreshToken =
            refreshCommandHandler.handle(
                user = me,
            )
        return RefreshResponseDto(refreshToken)
    }

    @GetMapping("/verify")
    @RequireAuth
    fun verify(
        @CurrentUser me: demo.kotlin.auth.domain.entity.CurrentUser,
    ): VerifyResponseDto =
        VerifyResponseDto(
            id = me.id,
            username = me.username,
            email = me.email,
            roles = me.roles,
        )

//    @PostMapping("/logout")
//    fun logout() = println("Logout")
}

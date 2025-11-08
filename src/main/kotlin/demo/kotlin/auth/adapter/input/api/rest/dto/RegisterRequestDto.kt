package demo.kotlin.auth.adapter.input.api.rest.dto

data class RegisterRequestDto(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
)

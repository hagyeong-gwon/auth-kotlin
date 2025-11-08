package demo.kotlin.auth.adapter.input.api.rest.dto

import demo.kotlin.auth.domain.enumerate.Role

data class VerifyResponseDto(
    val id: Int,
    val username: String,
    val email: String,
    val roles: List<Role>,
)

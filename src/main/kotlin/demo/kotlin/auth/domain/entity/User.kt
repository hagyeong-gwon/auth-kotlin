package demo.kotlin.auth.domain.entity

import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType

data class User(
    val id: Int?,
    val username: String,
    val passwordEncoded: String,
    val email: String,
    val roles: List<Role>?,
    val userType: UserType,
)

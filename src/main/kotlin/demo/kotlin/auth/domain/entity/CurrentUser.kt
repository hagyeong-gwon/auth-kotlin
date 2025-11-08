package demo.kotlin.auth.domain.entity

import demo.kotlin.auth.domain.enumerate.Role

data class CurrentUser(
    val id: Int,
    val username: String,
    val email: String,
    val roles: List<Role>,
)

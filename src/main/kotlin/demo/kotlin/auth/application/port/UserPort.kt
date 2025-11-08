package demo.kotlin.auth.application.port

import demo.kotlin.auth.domain.entity.User
import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType

interface UserPort {
    fun findByEmail(email: String): User?

    fun findById(id: Int): User?

    fun insert(registerArg: RegisterArg)

    data class RegisterArg(
        val username: String,
        val password: String,
        val email: String,
        val phoneNumber: String,
        val roles: List<Role>?,
        val userType: UserType,
    )
}

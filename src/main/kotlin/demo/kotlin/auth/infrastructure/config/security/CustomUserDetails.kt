package demo.kotlin.auth.infrastructure.config.security

import demo.kotlin.auth.domain.entity.CurrentUser
import demo.kotlin.auth.domain.enumerate.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
    val id: Int,
    val username: String,
    val email: String,
    val authorities: List<GrantedAuthority>,
) : UserDetails {
    override fun getAuthorities(): List<GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun toCurrentUser(): CurrentUser {
        val roles = authorities.mapNotNull { authority ->
            val roleName = authority.authority.removePrefix("ROLE_")
            try {
                Role.valueOf(roleName)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        return CurrentUser(
            id = id,
            username = username,
            email = email,
            roles = roles
        )
    }
}

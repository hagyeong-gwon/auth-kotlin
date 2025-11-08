package demo.kotlin.auth.adapter.output.mysql.entity

import demo.kotlin.auth.domain.enumerate.Role
import demo.kotlin.auth.domain.enumerate.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    var username: String,
    @Column
    var password: String,
    @Column
    var email: String,
    @Column
    var phoneNumber: String,
    @Column
    var userType: UserType,
    @Column
    var roles: List<Role>? = null,
)

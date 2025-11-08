package demo.kotlin.auth.adapter.output.mysql.impls

import demo.kotlin.auth.adapter.output.mysql.entity.UserEntity
import demo.kotlin.auth.adapter.output.mysql.mapper.UserMapper.map
import demo.kotlin.auth.adapter.output.mysql.repository.UserRepository
import demo.kotlin.auth.application.port.UserPort
import demo.kotlin.auth.domain.entity.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserImpls(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserPort {
    override fun findByEmail(email: String): User? = userRepository.findByEmail(email = email)?.let { map(it) }

    override fun findById(id: Int): User? {
        val userEntity = userRepository.findById(id)
        return if (userEntity.isPresent) map(userEntity.get()) else null
    }

    override fun insert(registerArg: UserPort.RegisterArg) {
        val user =
            UserEntity(
                username = registerArg.username,
                password = passwordEncoder.encode(registerArg.password),
                email = registerArg.email,
                phoneNumber = registerArg.phoneNumber,
                roles = registerArg.roles,
                userType = registerArg.userType,
            )
        userRepository.save(user)
    }
}

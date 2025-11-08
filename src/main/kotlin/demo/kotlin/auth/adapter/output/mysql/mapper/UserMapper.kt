package demo.kotlin.auth.adapter.output.mysql.mapper

import demo.kotlin.auth.adapter.output.mysql.entity.UserEntity
import demo.kotlin.auth.domain.entity.User

object UserMapper {
    fun map(userOrmEntity: UserEntity) =
        User(
            username = userOrmEntity.username,
            passwordEncoded = userOrmEntity.password,
            email = userOrmEntity.email,
            roles = userOrmEntity.roles,
            id = userOrmEntity.id?.toInt(),
            userType = userOrmEntity.userType,
        )
}

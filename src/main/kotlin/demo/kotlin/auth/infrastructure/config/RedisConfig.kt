package demo.kotlin.auth.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val host: String = ""

    @Value("\${spring.data.redis.port}")
    private val port = 0

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory? = LettuceConnectionFactory(host, port)

    @Bean
    fun redisTemplate(): RedisTemplate<String?, Any?> {
        val redisTemplate = RedisTemplate<String?, Any?>()

        redisTemplate.connectionFactory = redisConnectionFactory()

        // Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()

        // Hash Key-Value 형태로 직렬화를 수행합니다.
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = StringRedisSerializer()

        // 기본적으로 직렬화를 수행합니다.
        redisTemplate.setDefaultSerializer(StringRedisSerializer())

        return redisTemplate
    }
}

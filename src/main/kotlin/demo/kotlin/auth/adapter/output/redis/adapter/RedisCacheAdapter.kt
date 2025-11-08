package demo.kotlin.auth.adapter.output.redis.adapter

import com.fasterxml.jackson.databind.ObjectMapper
import demo.kotlin.auth.application.port.RedisCachePort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisCacheAdapter(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redisMapper: ObjectMapper,
) : RedisCachePort {
    override fun save(
        key: String,
        value: Any,
        expirationInSeconds: Int,
    ) {
        redisTemplate.opsForValue().set(key, redisMapper.writeValueAsString(value))
    }

    override fun get(key: String): String? = redisTemplate.opsForValue().get(key)
}

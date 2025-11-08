package demo.kotlin.auth.application.port

interface RedisCachePort {
    fun save(
        key: String,
        value: Any,
        expirationInSeconds: Int,
    )

    fun get(key: String): String?
}

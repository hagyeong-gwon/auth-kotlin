package demo.kotlin.auth.domain.error

enum class ErrorType(
    val statusCode: Int,
) {
    BAD_REQUEST(400),
    NOT_FOUND(404),
    UNAUTHORIZED(401),
    INTERNAL_SERVER_ERROR(500),
    NOT_ALLOWED(405),
    FORBIDDEN(403),
    TIME_OUT(504),
    UNAVAILABLE(503),
    UNHEALTHY(502),
    TOO_MANY_REQUEST(429),
    TOKEN_EXPIRED(401),
}

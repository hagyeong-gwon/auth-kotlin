package demo.kotlin.auth.domain.error.exception

import demo.kotlin.auth.domain.error.ErrorType

abstract class CustomException(
    val type: ErrorType,
    message: String?,
    cause: Throwable?,
) : RuntimeException(message, cause)

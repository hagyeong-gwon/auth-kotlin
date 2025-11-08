package demo.kotlin.auth.domain.error.exception

import demo.kotlin.auth.domain.error.ErrorType

class ForbiddenException(
    message: String?,
    cause: Throwable?,
) : CustomException(ErrorType.FORBIDDEN, message, cause) {
    constructor(cause: Throwable) : this(ErrorType.FORBIDDEN.name, null)
    constructor(message: String) : this(message, null)
    constructor() : this(ErrorType.FORBIDDEN.name, null)
}

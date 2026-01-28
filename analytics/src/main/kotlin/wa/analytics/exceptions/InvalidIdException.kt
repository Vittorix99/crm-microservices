package wa.analytics.exceptions

class InvalidIdException(analyticId: String? = null, cause: Throwable? = null): RuntimeException("Invalid Id", cause)
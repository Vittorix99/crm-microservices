package wa.analytics.exceptions

class AnalyticMetricsNotFoundException(analyticId: String? = null, cause: Throwable? = null): RuntimeException("Invalid skill", cause)
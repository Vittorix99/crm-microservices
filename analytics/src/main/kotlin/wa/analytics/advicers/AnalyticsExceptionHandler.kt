package wa.analytics.advicers

import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import wa.analytics.exceptions.AnalyticMetricsNotFoundException
import wa.analytics.exceptions.InvalidIdException

@RestControllerAdvice
class AnalyticsExceptionHandler : ResponseEntityExceptionHandler(){
    private val LOGGER = LogManager.getLogger()

    @ExceptionHandler(AnalyticMetricsNotFoundException::class)
    fun handleAnalyticMetricsNotFoundException(e: AnalyticMetricsNotFoundException): ProblemDetail {
        LOGGER.error(e.message);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!);
    }

    @ExceptionHandler(InvalidIdException::class)
    fun handleInvalidIdException(e: InvalidIdException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!);
    }
}
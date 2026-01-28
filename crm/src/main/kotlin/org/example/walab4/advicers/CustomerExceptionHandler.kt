package org.example.walab4.advicers

import jakarta.validation.ConstraintViolationException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.customer.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CustomerExceptionHandler: ResponseEntityExceptionHandler() {
    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleContactNotFoundException(e: CustomerNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }



    @ExceptionHandler(CustomerNoteNotFoundException::class)
    fun handleCustomerNoteNotFoundException(e: CustomerNoteNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(InvalidCustomerPropsException::class)
    fun handleInvalidCustomerException(e: InvalidCustomerPropsException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleInvalidRequestPropException(e: ConstraintViolationException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }


}
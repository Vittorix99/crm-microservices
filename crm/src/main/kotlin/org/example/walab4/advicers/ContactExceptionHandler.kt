package org.example.walab4.advicers

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.contact.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ContactExceptionHandler : ResponseEntityExceptionHandler(){
    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(ContactNotFoundException::class)
    fun handleContactNotFoundException(e: ContactNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(TelephoneNotFoundException::class)
    fun handleTelephoneNotFoundException(e: TelephoneNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(AddressNotFoundException::class)
    fun handleAddressNotFoundException(e: AddressNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(EmailNotFoundException::class)
    fun handleEmailNotFoundException(e: EmailNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(ContactAlreadyPresentException::class)
    fun handleContactAlreadyPresentException(e: ContactAlreadyPresentException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message !!)
    }

    @ExceptionHandler(EmailNotPresentInContactException::class)
    fun handleEmailNotPresentInContactException(e: EmailNotPresentInContactException) : ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(AddressNotPresentInContactException::class)
    fun handleAddressNotPresentInContactException(e: AddressNotPresentInContactException) : ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(TelephoneNotPresentInContactException::class)
    fun handleTelephonelNotPresentInContactException(e: TelephoneNotPresentInContactException) : ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(InvalidCategoryValueException::class)
    fun handleInvalidCategoryValueException(e: InvalidCategoryValueException) : ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

}
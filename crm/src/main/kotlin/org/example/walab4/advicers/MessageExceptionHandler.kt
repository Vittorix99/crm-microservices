package org.example.walab4.advicers

import org.example.walab4.exceptions.message.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.mail.MailNotSentException
import org.example.walab4.exceptions.message.InvalidPriorityValueException
import org.example.walab4.exceptions.message.InvalidStateValueException
import org.example.walab4.exceptions.message.InvalidTargetStateException
import org.example.walab4.exceptions.message.MessageNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class MessageExceptionHandler: ResponseEntityExceptionHandler() {

    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(MessageNotFoundException::class)
    fun handleMessageNotFoundException(e: MessageNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(InvalidTargetStateException::class)
    fun handleInvalidTargetStateException(e: InvalidTargetStateException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }
    @ExceptionHandler(InvalidContactException::class)
    fun handleInvalidContactException(e: InvalidTargetStateException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }



    @ExceptionHandler(InvalidPriorityValueException::class)
    fun handleInvalidPriorityValueException(e: InvalidPriorityValueException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(InvalidStateValueException::class)
    fun handleInvalidStateValueException(e: InvalidStateValueException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }



    @ExceptionHandler(RuntimeException::class)
    fun handleRunTimeException(e: InvalidStateValueException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(MailNotSentException::class)
    fun handleMailNotSent(e: MailNotSentException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }



}
package org.example.walab4.advicers

import jakarta.validation.ConstraintViolationException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.professional.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
@RestControllerAdvice
class ProfessionalExceptionHandler: ResponseEntityExceptionHandler() {
    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(ProfessionalNotFoundException::class)
    fun handleContactNotFoundException(e: ProfessionalNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(SkillNotFoundException::class)
    fun handleSkillNotFoundException(e: SkillNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(ProfessionalNoteNotFoundException::class)
    fun handleProfessionalNoteNotFoundException(e: ProfessionalNoteNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(InvalidProfessionalPropsException::class)
    fun handleInvalidProfessionalException(e: InvalidProfessionalPropsException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleInvalidRequestPropException(e: ConstraintViolationException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }
    @ExceptionHandler(RuntimeException::class)
    fun handleGeneralException(e: RuntimeException): ProblemDetail {
        LOGGER.error("General Error Occurred: ${e.message}")
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: ${e.message}")
    }
}
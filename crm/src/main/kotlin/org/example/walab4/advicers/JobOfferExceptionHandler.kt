package org.example.walab4.advicers

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.jobOffer.*
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class JobOfferExceptionHandler {

    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(InvalidJobOfferStatusValueException::class)
    fun handleInvalidJobOfferStatusException(e: InvalidJobOfferStatusValueException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(JobOfferNotFoundException::class)
    fun handleJobOfferNotFoundException(e: JobOfferNotFoundException): ProblemDetail{
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!)
    }

    @ExceptionHandler(InvalidTransitionException::class)
    fun handleInvalidTransitionException(e: InvalidTransitionException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(JobOfferNotLinkedToProfessionalException::class)
    fun handleJobOfferNotLinkedToProfessionalException(e: JobOfferNotLinkedToProfessionalException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }

    @ExceptionHandler(InterviewAlreadyPresentException::class)
    fun handleJobOfferAlreadyPresentException(e: InterviewAlreadyPresentException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message !!)
    }

    @ExceptionHandler(ProposalNotFoundException::class)
    fun handleProposalNotFoundException(e: ProposalNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(InvalidProposalTransitionException::class)
    fun handleInvalidProposalTransitionException(e: InvalidProposalTransitionException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(InvalidProposalStatusValueException::class)
    fun handleInvalidProposalStatusValueException(e: InvalidProposalStatusValueException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }
}
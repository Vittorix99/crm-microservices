package org.example.walab4.advicers

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.exceptions.interview.InterviewNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler

class InterviewExceptionHandler {

    private val LOGGER: Logger = LogManager.getLogger()

    @ExceptionHandler(InterviewNotFoundException::class)
    fun handleInterviewNotFoundException(e: InterviewNotFoundException): ProblemDetail {
        LOGGER.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!)
    }
}
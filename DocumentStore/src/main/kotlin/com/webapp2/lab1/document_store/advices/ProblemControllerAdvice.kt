package com.webapp2.lab1.document_store.advices

import com.webapp2.lab1.document_store.exceptions.DocumentAlreadyPresentException
import com.webapp2.lab1.document_store.exceptions.DocumentMetadataNotFoundException
import com.webapp2.lab1.document_store.exceptions.DocumentNotFoundException
import com.webapp2.lab1.document_store.exceptions.InvalidDocumentException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class DocumentExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(DocumentNotFoundException::class)
    fun handleDocumentNotFoundException(e: DocumentNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!);
    }

    @ExceptionHandler(DocumentMetadataNotFoundException::class)
    fun handleDocumentMetadataNotFoundException(e: DocumentMetadataNotFoundException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message !!);
    }

    @ExceptionHandler(DocumentAlreadyPresentException::class)
    fun handleDocumentAlreadyPresentException(e: DocumentAlreadyPresentException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message !!);
    }

    @ExceptionHandler(InvalidDocumentException::class)
    fun handleDocumentAlreadyPresentException(e: InvalidDocumentException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message !!);
    }


}
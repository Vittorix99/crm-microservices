package com.webapp2.lab1.document_store.exceptions

class DocumentAlreadyPresentException(documentId: String? = null, cause: Throwable? = null) : RuntimeException("Document already exists: $documentId", cause)

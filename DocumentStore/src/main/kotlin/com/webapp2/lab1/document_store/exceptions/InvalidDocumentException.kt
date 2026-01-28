package com.webapp2.lab1.document_store.exceptions

class InvalidDocumentException(documentName: String? = null, cause: Throwable? = null) : RuntimeException("Provided document isn't valid: $documentName", cause)

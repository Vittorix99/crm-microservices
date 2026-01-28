package com.webapp2.lab1.document_store.exceptions

class DocumentNotFoundException(documentId: String? = null, cause: Throwable? = null) : RuntimeException("Document not found: $documentId", cause)

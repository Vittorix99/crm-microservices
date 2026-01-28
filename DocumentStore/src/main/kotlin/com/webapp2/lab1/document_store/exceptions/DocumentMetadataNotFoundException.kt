package com.webapp2.lab1.document_store.exceptions

class DocumentMetadataNotFoundException(documentMetadataId: String? = null, cause: Throwable? = null)
    : RuntimeException("Document metadata not found: $documentMetadataId", cause)

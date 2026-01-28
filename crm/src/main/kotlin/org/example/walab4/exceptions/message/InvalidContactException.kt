package org.example.walab4.exceptions.message

class InvalidContactException(messageId: String? = null, cause: Throwable? = null): RuntimeException("Invalid contact", cause)
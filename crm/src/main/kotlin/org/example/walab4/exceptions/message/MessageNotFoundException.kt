package org.example.walab4.exceptions.message

class MessageNotFoundException(messageId: String? = null, cause: Throwable? = null): RuntimeException("Message not found: $messageId", cause)
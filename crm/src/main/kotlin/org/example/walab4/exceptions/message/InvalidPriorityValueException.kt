package org.example.walab4.exceptions.message

class InvalidPriorityValueException(messageId: String? = null, priority: String, cause: Throwable? = null): RuntimeException("Invalid priority value for message $messageId: ${priority}", cause)
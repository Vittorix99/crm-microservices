package org.example.walab4.exceptions.message

class InvalidStateValueException(messageId: String? = null, state: String, cause: Throwable? = null): RuntimeException("Invalid state value for message $messageId: ${state}", cause)
package org.example.walab4.exceptions.message

import org.example.walab4.model.message.MessageStatus

class InvalidTargetStateException(messageId: String? = null, targetState: MessageStatus, cause: Throwable? = null): RuntimeException("Invalid transition to ${targetState.name} for message $messageId", cause)
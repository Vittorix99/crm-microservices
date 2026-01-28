package org.example.walab4.exceptions.mail

import org.example.walab4.dto.message.EmailDto

class MailNotSentException( cause: Throwable? = null): RuntimeException("Error during message send", cause)
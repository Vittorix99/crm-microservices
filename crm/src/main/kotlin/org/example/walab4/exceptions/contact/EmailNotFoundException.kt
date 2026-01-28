package org.example.walab4.exceptions.contact

class EmailNotFoundException(emailId: String? = null, cause: Throwable? = null): RuntimeException("Email Not Present in the Repository", cause)
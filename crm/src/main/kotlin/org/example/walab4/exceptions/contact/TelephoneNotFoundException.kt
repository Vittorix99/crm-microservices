package org.example.walab4.exceptions.contact

class TelephoneNotFoundException(telephoneId: String? = null, cause: Throwable? = null): RuntimeException("Telephone Not Present in the Repository", cause)
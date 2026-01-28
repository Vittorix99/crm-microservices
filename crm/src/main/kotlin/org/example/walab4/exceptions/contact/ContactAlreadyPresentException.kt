package org.example.walab4.exceptions.contact

class ContactAlreadyPresentException (contactId: String? = null, cause: Throwable? = null): RuntimeException("Contact already exists: $contactId", cause)
package org.example.walab4.exceptions.contact

class ContactNotFoundException(contactId: String? = null, cause: Throwable? = null): RuntimeException("Contact Not Present in the Repository", cause )
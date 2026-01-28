package org.example.walab4.exceptions.contact

class EmailNotPresentInContactException (contactId: String? = null, emailId: String? = null, cause: Throwable? = null): RuntimeException("Email $emailId not present in contact $contactId", cause)
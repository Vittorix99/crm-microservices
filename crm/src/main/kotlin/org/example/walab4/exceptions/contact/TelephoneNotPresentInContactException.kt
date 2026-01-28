package org.example.walab4.exceptions.contact

class TelephoneNotPresentInContactException (contactId: String? = null, telephoneId: String? = null, cause: Throwable? = null): RuntimeException("Telephone $telephoneId not present in contact $contactId", cause)
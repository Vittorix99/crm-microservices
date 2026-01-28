package org.example.walab4.exceptions.contact

class InvalidCategoryValueException(contactId: String? = null, category: String, cause: Throwable? = null): RuntimeException("Invalid category value for contact $contactId: ${category}", cause)
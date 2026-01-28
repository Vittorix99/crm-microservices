package org.example.walab4.exceptions.jobOffer

class InvalidJobOfferStatusValueException(value: String? = null, cause: Throwable? = null): RuntimeException("JobOfferStatus value not valid: $value", cause)
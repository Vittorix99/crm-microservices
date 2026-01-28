package org.example.walab4.exceptions.jobOffer

class InvalidProposalStatusValueException(value: String? = null, cause: Throwable? = null): RuntimeException("Proposal value not valid: $value", cause)
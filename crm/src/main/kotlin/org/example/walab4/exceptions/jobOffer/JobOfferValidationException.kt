package org.example.walab4.exceptions.jobOffer

class JobOfferValidationException(detail: String?) :
    RuntimeException("Validation error for job offer: ${detail ?: "No detailed error message provided."}.")

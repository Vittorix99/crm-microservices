package org.example.walab4.exceptions.jobOffer
class JobOfferInvalidException(detail: String?) :
    RuntimeException("Invalid job offer details: ${detail ?: "No further information available."}.")

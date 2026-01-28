package org.example.walab4.exceptions.jobOffer

class JobOfferNotLinkedToProfessionalException(jobOfferId: String? = null, cause: Throwable? = null): RuntimeException("Job Offer with id $jobOfferId not linked to any professional", cause)
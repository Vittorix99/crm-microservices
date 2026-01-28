package org.example.walab4.exceptions.jobOffer

import org.example.walab4.model.jobOffer.JobOfferStatus

class InvalidTransitionException(jobOfferId: String? = null, newState: JobOfferStatus, cause: Throwable? = null): RuntimeException("Invalid transition to ${newState.name} for job offer $jobOfferId", cause)
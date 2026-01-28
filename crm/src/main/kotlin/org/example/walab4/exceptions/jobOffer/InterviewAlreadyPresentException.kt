package org.example.walab4.exceptions.jobOffer

class InterviewAlreadyPresentException(jobOfferId: String? = null, cause: Throwable? = null): RuntimeException("JobOffer with id $jobOfferId has already a Interview", cause)
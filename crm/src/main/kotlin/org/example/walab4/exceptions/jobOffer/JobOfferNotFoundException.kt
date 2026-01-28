package org.example.walab4.exceptions.jobOffer

class JobOfferNotFoundException(jobOfferId: Long?=null) :
    RuntimeException("Failed to find job offer with ID ${jobOfferId}")

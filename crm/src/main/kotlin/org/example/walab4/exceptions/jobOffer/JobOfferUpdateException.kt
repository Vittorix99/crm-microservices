package org.example.walab4.exceptions.jobOffer

class JobOfferUpdateException(jobOfferId: Long?, reason: String?) :
    RuntimeException("Failed to update job offer with ID ${jobOfferId ?: "unknown"} due to ${reason ?: "unspecified reason"}.")

package org.example.walab4.exceptions.jobOffer

class ProposalNotFoundException (proposalId: Long?=null) :
    RuntimeException("Failed to find proposal with ID ${proposalId}")
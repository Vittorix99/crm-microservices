package org.example.walab4.exceptions.jobOffer

import org.example.walab4.model.jobOffer.JobOfferStatus
import org.example.walab4.model.jobOffer.ProposalStatus

class InvalidProposalTransitionException(proposalId: String? = null, newState: ProposalStatus, cause: Throwable? = null): RuntimeException("Invalid transition to ${newState.name} for proposal $proposalId", cause)
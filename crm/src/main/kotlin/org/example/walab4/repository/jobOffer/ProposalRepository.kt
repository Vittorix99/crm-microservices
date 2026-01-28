package org.example.walab4.repository.jobOffer

import org.example.walab4.dto.jobOffer.ProposalDTO
import org.example.walab4.model.jobOffer.Proposal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProposalRepository: JpaRepository<Proposal, Long> {

    fun findProposalById(proposalId: Long): Proposal?

    @Query("SELECT p FROM Proposal p WHERE p.jobOffer.id = :jobOfferId")
    fun findProposalByJobOfferId(jobOfferId: Long): List<Proposal>
}
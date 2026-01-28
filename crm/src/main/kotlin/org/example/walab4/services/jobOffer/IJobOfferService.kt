package org.example.walab4.services.jobOffer

import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.jobOffer.JobOfferDTO
import org.example.walab4.dto.jobOffer.JobOfferNoteDTO
import org.example.walab4.dto.jobOffer.ProposalDTO
import org.example.walab4.dto.professional.SkillDto
import org.example.walab4.model.jobOffer.JobOfferStatus
import org.example.walab4.model.jobOffer.ProposalStatus

interface IJobOfferService {

    fun saveJobOffer(jobOfferDto: JobOfferDTO): JobOfferDTO

    fun getJobOffer(jobOfferId: Long): JobOfferDTO

    fun updateJobOfferStatus(jobOfferId: Long, newStatus: JobOfferStatus): JobOfferDTO

    fun updateJobOfferDescription(jobOfferId: Long, newDescription: String): JobOfferDTO

    fun getJobOfferValue(jobOfferId: Long): Double

    fun addNoteToJobOffer(jobOfferId: Long, note: JobOfferNoteDTO): JobOfferDTO

    fun addInterviewToJobOffer(jobOfferId: Long, interviewDto: InterviewDTO): InterviewDTO

    fun getOpenJobOffersForCustomer(customerId: Long, page: Int, limit: Int): List<JobOfferDTO>

    fun getAcceptedJobOffersForProfessional(professionalId: Long, page: Int, size: Int): List<JobOfferDTO>

    fun getRegisteredJobOffersByParams(customerId: Long?, professionalId: Long?, page: Int, size: Int): List<JobOfferDTO>

    fun getJobOfferNotesForJobOffer(jobOfferId: Long): List<JobOfferNoteDTO>

    fun addRequiredSkillJobOffer(jobOfferId: Long, skill: SkillDto): SkillDto

    fun addMultipleSkillsJobOffer(jobOfferId: Long, skills: List<SkillDto>): List<SkillDto>

    fun getJobOfferSkills(jobOfferId: Long): List<SkillDto>

    fun getJobOfferInterview(jobOfferId: Long): List<InterviewDTO>

    fun addProposalToJobOffer(jobOfferId: Long, proposalDTO: ProposalDTO): ProposalDTO

    fun getProposalsFromJobOfferId(jobOfferId: Long): List<ProposalDTO>

    fun getAllProposals(): List<ProposalDTO>

    fun deleteJobOffer(jobOfferId: Long)

    fun updateProposalStatus(proposalId: Long, newStatus: ProposalStatus): ProposalDTO

    fun deleteProposal(proposalId: Long)

}
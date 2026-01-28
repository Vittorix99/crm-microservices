package org.example.walab4.controller

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.dto.jobOffer.JobOfferDTO
import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.jobOffer.JobOfferNoteDTO
import org.example.walab4.dto.jobOffer.ProposalDTO
import org.example.walab4.dto.professional.SkillDto

import org.example.walab4.exceptions.jobOffer.InvalidJobOfferStatusValueException
import org.example.walab4.exceptions.jobOffer.InvalidProposalStatusValueException
import org.example.walab4.model.jobOffer.JobOfferStatus
import org.example.walab4.model.jobOffer.ProposalStatus
import org.example.walab4.services.jobOffer.IJobOfferService
import org.example.walab4.services.kafka.IKafkaService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/joboffers")
class JobOfferController(
    private val jobOfferService: IJobOfferService,
    private val kafkaService: IKafkaService
    ) {

    private val LOGGER: Logger = LogManager.getLogger()

    @GetMapping("/{joboffersid}")
    @ResponseStatus(HttpStatus.OK)
    fun getJobOfferById(@PathVariable joboffersid: Long): JobOfferDTO {

        val jobOffer = jobOfferService.getJobOffer(joboffersid)

        LOGGER.info("[GET - API/joboffers/$joboffersid] - SUCCESS - Job Offer retrieved correctly")

        return jobOffer
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveJobOffer(
        @RequestBody jobOfferDto: JobOfferDTO
    ): JobOfferDTO {

        val jobOffer = jobOfferService.saveJobOffer(jobOfferDto)
        LOGGER.info("[POST - API/joboffers] - SUCCESS - Job Offer saved successfully")

        return jobOffer
    }

    @PostMapping("/{joboffersId}/note")
    @ResponseStatus(HttpStatus.CREATED)
    fun addNoteToJobOffer(
        @PathVariable joboffersId: Long,
        @RequestBody jobOfferNoteDto: JobOfferNoteDTO
    ): JobOfferDTO {

        val jobOffer = jobOfferService.addNoteToJobOffer(joboffersId, jobOfferNoteDto)

        LOGGER.info("[POST - API/joboffers/${joboffersId}/note] - SUCCESS - Job Offer Note saved successfully")

        return jobOffer
    }

    @PostMapping("/{joboffersId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateJobOfferStatus(
        @PathVariable joboffersId: Long,
        @RequestParam status: String
    )
            : JobOfferDTO {

        val jobOfferNewStatus: JobOfferStatus

        try {
            jobOfferNewStatus = JobOfferStatus.valueOf(status.uppercase())
        } catch (iae: IllegalArgumentException) {
            throw InvalidJobOfferStatusValueException(status)
        }

        val jobOffer = jobOfferService.updateJobOfferStatus(joboffersId, jobOfferNewStatus)

        LOGGER.info("[POST - API/joboffers/${joboffersId}] - SUCCESS - job offer status updated successfully")

        return jobOffer
    }

    @PutMapping("/{joboffersId}/description")
    @ResponseStatus(HttpStatus.OK)
    fun updateJobOfferDescription(
        @PathVariable joboffersId: Long,
        @RequestParam newDescription: String
    )
            : JobOfferDTO {

        val jobOffer = jobOfferService.updateJobOfferDescription(joboffersId, newDescription)

        LOGGER.info("[PUT - API/joboffers/${joboffersId}/description] - SUCCESS - job offer description updated successfully")

        return jobOffer
    }


    @GetMapping("/open/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun getOpenJobOffersForCustomer(
        @PathVariable customerId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") limit: Int
    ): List<JobOfferDTO> {
        val jobOffers = jobOfferService.getOpenJobOffersForCustomer(customerId, page, limit)
        LOGGER.info("[GET - API/joboffers/open/$customerId] - SUCCESS - Open job offers retrieved correctly")


        return jobOffers
    }

    @GetMapping("/accepted/{professionalId}")
    @ResponseStatus(HttpStatus.OK)
    fun getAcceptedJobOffersForProfessional(
        @PathVariable professionalId: Long, @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<JobOfferDTO> {
        val jobOffers = jobOfferService.getAcceptedJobOffersForProfessional(professionalId, page, size)
        LOGGER.info("[GET - API/joboffers/accepted/$professionalId] - SUCCESS - Accepted job offers retrieved correctly")

        return jobOffers
    }

    @GetMapping("/aborted")
    @ResponseStatus(HttpStatus.OK)
    fun getJobOffersByParams(
        @RequestParam(value = "customer", required = false) customerId: Long?,
        @RequestParam(value = "professional", required = false) professionalId: Long?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<JobOfferDTO> {
        val jobOffers = jobOfferService.getRegisteredJobOffersByParams(customerId, professionalId, page, size)
        LOGGER.info("[GET - API/joboffers/aborted] - SUCCESS - Aborted job offers retrieved correctly with filters - CustomerId: $customerId, ProfessionalId: $professionalId")

        return jobOffers
    }

    @GetMapping("/{joboffersId}/value")
    @ResponseStatus(HttpStatus.OK)
    fun getValueController(
        @PathVariable joboffersId: Long
    ): Double {

        val value = jobOfferService.getJobOfferValue(joboffersId)

        LOGGER.info("[GET - API/{$joboffersId}/aborted] - SUCCESS - Value successfully retrieved - JobOffersId: $joboffersId")
        return value
    }

    @PostMapping("/{joboffersId}/interview")
    @ResponseStatus(HttpStatus.CREATED)
    fun addInterviewToJobOffer(
        @PathVariable joboffersId: Long,
        @RequestBody interviewDto: InterviewDTO
    ): InterviewDTO {

        val interview = jobOfferService.addInterviewToJobOffer(joboffersId, interviewDto)

        LOGGER.info("[POST - API/{$joboffersId}/interview] - SUCCESS - Interview successfully added to Job Offer - JobOffersId: $joboffersId")
        return interview
    }

    @GetMapping("/{jobOffersId}/note")
    @ResponseStatus(HttpStatus.OK)
    fun getNotesForJobOffer(
        @PathVariable jobOffersId: Long
    ) : List<JobOfferNoteDTO> {

        val jobOfferNote = jobOfferService.getJobOfferNotesForJobOffer(jobOfferId = jobOffersId)
        LOGGER.info("[GET - API/{$jobOffersId}/interview] - SUCCESS - JobOfferNotes successfully retrieved - JobOffersId: $jobOffersId")

        return jobOfferNote
    }

    @PostMapping("/{jobOffersId}/skills")
    @ResponseStatus(HttpStatus.OK)
    fun addSkillToJobOffer(
        @RequestBody skill: SkillDto,
        @PathVariable jobOffersId: Long
    ): SkillDto {
        val skill = jobOfferService.addRequiredSkillJobOffer(jobOffersId, skill)
        LOGGER.info("[POST - API/jobOffers/$jobOffersId]/skill - SUCCESS - Skill ${skill.skill} created correctly")
        return skill
    }

    @GetMapping("/{jobOffersId}/skills")
    @ResponseStatus(HttpStatus.OK)
    fun getSkillsJobOffer(
        @PathVariable jobOffersId: Long
    ): List<SkillDto> {
        val skills = jobOfferService.getJobOfferSkills(jobOffersId)
        LOGGER.info("[GET - API/jobOffers/$jobOffersId]/skill - SUCCESS - Skills retrieved correctly")
        return skills
    }

    @GetMapping("/{jobOffersId}/interviews")
    @ResponseStatus(HttpStatus.OK)
    fun getInterviewsJobOffer(
        @PathVariable jobOffersId: Long
    ): List<InterviewDTO>{
        val interviews = jobOfferService.getJobOfferInterview(jobOffersId)
        LOGGER.info("[GET - API/jobOffers/$jobOffersId]/interviews - SUCCESS - Interview for jobOffer ${jobOffersId} retrieved correctly")
        return interviews
    }

    @PostMapping("/{jobOffersId}/proposal")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProposalToJobOffer(
        @PathVariable jobOffersId: Long,
        @RequestBody proposalDto: ProposalDTO
    ): ProposalDTO {

        val proposal = jobOfferService.addProposalToJobOffer(jobOffersId, proposalDto)
        LOGGER.info("[POST - API/jobOffers/$jobOffersId]/proposal - SUCCESS - Proposal for jobOffer ${jobOffersId} added correctly")
        return proposal
    }

    @GetMapping("/{jobOffersId}/proposals")
    @ResponseStatus(HttpStatus.OK)
    fun getProposalsByJobOffer(
        @PathVariable jobOffersId: Long
    ): List<ProposalDTO> {

        val proposals = jobOfferService.getProposalsFromJobOfferId(jobOffersId)
        LOGGER.info("[GET - API/jobOffers/$jobOffersId]/proposals - SUCCESS - Proposals for jobOffer ${jobOffersId} correctly retrieved")
        return proposals
    }

    @DeleteMapping("/{jobOffersId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteJobOffer(
        @PathVariable jobOffersId: Long
    ) {
        jobOfferService.deleteJobOffer(jobOffersId)
        LOGGER.info("[DELETE - API/jobOffers/$jobOffersId] - SUCCESS - Job Offer $jobOffersId deleted correctly")
    }

    @DeleteMapping("/{jobOffersId}/proposals/{proposalId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProposal(
        @PathVariable jobOffersId: Long,
        @PathVariable proposalId: Long
    ) {
        jobOfferService.deleteProposal(proposalId)
        LOGGER.info("[DELETE - API/jobOffers/$jobOffersId/proposals/$proposalId] - SUCCESS - Proposal $proposalId deleted correctly")
    }

    @PutMapping("/{jobOffersId}/proposals/{proposalId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateProposalStatus(
        @PathVariable jobOffersId: Long,
        @PathVariable proposalId: Long,
        @RequestParam status: String
    ) : ProposalDTO {

        val proposalNewValue: ProposalStatus

        try {
            proposalNewValue = ProposalStatus.valueOf(status.toUpperCase())
        } catch (iae: IllegalArgumentException) {
            throw InvalidProposalStatusValueException(status)
        }

        val proposal = jobOfferService.updateProposalStatus(proposalId, proposalNewValue)
        LOGGER.info("[PUT - API/jobOffers/$jobOffersId/proposals/$proposalId] - SUCCESS - Proposal $proposalId status updated correctly")
        return proposal
    }
}

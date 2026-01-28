package org.example.walab4.services.jobOffer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.example.walab4.model.jobOffer.Proposal
import org.example.walab4.model.jobOffer.ProposalStatus
import org.example.walab4.configurations.JobOfferTransitionsConf
import org.example.walab4.configurations.ProposalTransitionsConf
import org.example.walab4.dto.jobOffer.*
import org.example.walab4.dto.professional.SkillDto
import org.example.walab4.dto.professional.toDto
import org.example.walab4.exceptions.customer.CustomerNotFoundException
import org.example.walab4.exceptions.jobOffer.*
import org.example.walab4.exceptions.professional.ProfessionalNotFoundException
import org.example.walab4.exceptions.professional.SkillNotFoundException
import org.example.walab4.model.jobOffer.*
import org.example.walab4.model.professional.Skill
import org.example.walab4.repository.customer.CustomerRepository
import org.example.walab4.repository.jobOffer.InterviewRepository
import org.example.walab4.repository.jobOffer.JobOfferNoteRepository
import org.example.walab4.repository.jobOffer.JobOfferRepository
import org.example.walab4.repository.jobOffer.ProposalRepository
import org.example.walab4.repository.professional.ProfessionalRepository
import org.example.walab4.repository.professional.SkillRepository
import org.example.walab4.services.kafka.KafkaService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JobOfferService(
    private val jobOfferRepository: JobOfferRepository,
    private val jobOfferNoteRepository: JobOfferNoteRepository,
    private val transitions: JobOfferTransitionsConf,
    private val customerRepository: CustomerRepository,
    private val professionalRepository: ProfessionalRepository,
    private val skillRepository: SkillRepository,
    private val interviewRepository: InterviewRepository,
    private val proposalRepository: ProposalRepository,
    private val kafkaService: KafkaService,
    private val proposalTransitions: ProposalTransitionsConf
) : IJobOfferService {

    val objectMapper = jacksonObjectMapper()

    override fun saveJobOffer(jobOfferDto: JobOfferDTO): JobOfferDTO {

        /* MANCA RICERCA DEL CUSTOMER */
        val customer = customerRepository.findCustomerById(jobOfferDto.customer)?: throw CustomerNotFoundException()

        val professional = jobOfferDto.professional?.let { professionalRepository.findProfessionalById(jobOfferDto.professional!!)?: throw ProfessionalNotFoundException() }

        val resJobOffer = JobOffer(
            description = jobOfferDto.description,
            status = JobOfferStatus.CREATED,
            duration = jobOfferDto.duration,
            requiredSkills = mutableListOf(),
            value = jobOfferDto.value,
            customer = customer,
            jobOfferNotes = mutableListOf(),
            interviews = mutableListOf(),
            professional = professional
            )

        /* Bisogna aggiungere la job offer anche al customer ed eventualmente anche al professional*/
        customer.addJobOffer(resJobOffer)

        val savedJobOffer = jobOfferRepository.save(resJobOffer)
        return savedJobOffer.toDto()

    }

    override fun getJobOffer(jobOfferId: Long): JobOfferDTO {
        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)

        return jobOffer.toDto()
    }

    override fun updateJobOfferStatus(jobOfferId: Long, newStatus: JobOfferStatus): JobOfferDTO {

        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)

        val allowedTransitions: Array<JobOfferStatus>

        when(jobOffer.status){

            JobOfferStatus.CREATED             -> allowedTransitions = this.transitions.created
            JobOfferStatus.SELECTION_PHASE     -> allowedTransitions = this.transitions.selectionPhase
            JobOfferStatus.CANDIDATE_PROPOSAL  -> allowedTransitions = this.transitions.candidateProposal
            JobOfferStatus.CONSOLIDATED        -> allowedTransitions = this.transitions.consolidated
            JobOfferStatus.DONE                -> allowedTransitions = this.transitions.done
            JobOfferStatus.ABORTED             -> allowedTransitions = this.transitions.aborted
        }

        if(!allowedTransitions.contains(newStatus))
            throw InvalidTransitionException(jobOfferId.toString(), newStatus)

        jobOffer.apply { jobOffer.status = newStatus }

        this.jobOfferRepository.save(jobOffer)

        return jobOffer.toDto()
    }

    override fun updateJobOfferDescription(jobOfferId: Long, newDescription: String): JobOfferDTO {

        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)

        jobOffer.apply {
            description = newDescription
        }

        jobOfferRepository.save(jobOffer)

        return jobOffer.toDto()
    }

    override fun getJobOfferValue(jobOfferId: Long): Double {

        /* Cerco la job Offer */
        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)

        /* Cerco se vi è un professional linakto alla job offer*/
        val professional = jobOffer.professional.let { professionalRepository.findProfessionalById(jobOffer.professional?.id!!) }?: throw JobOfferNotLinkedToProfessionalException()
        if(jobOffer.professional == null) throw JobOfferNotLinkedToProfessionalException()

        /* Perchè Daily rate opzionale?, Il daily rate perchè è un long, non è meglio un double? daily rate e duration è giusto che siano opzionali?*/
        val value = PROFIT_MARGIN*professional.dailyRate*jobOffer.duration

        /* Devo sovrascrivere il valore del value */
        jobOffer.value = value

        jobOfferRepository.save(jobOffer)

        return value
    }

    override fun addNoteToJobOffer(jobOfferId: Long, note: JobOfferNoteDTO): JobOfferDTO {

        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)
        val jobOfferNote = JobOfferNote(id = note.id, description = note.description, jobOffer = jobOffer)

        jobOffer.apply {
            this.jobOfferNotes.add(jobOfferNote)
        }

        jobOfferNoteRepository.save(jobOfferNote)

        val savedJobOffer = jobOfferRepository.save(jobOffer)

        return savedJobOffer.toDto()
    }

    override fun addInterviewToJobOffer(jobOfferId: Long, interviewDto: InterviewDTO): InterviewDTO {

        /* Per prima cosa cerco l'id della job offer*/
        var resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException(jobOfferId)

        var interview = interviewDto.toInterview();
        interviewRepository.save(interview)

        resJobOffer.addInterview(interview)

        jobOfferRepository.save(resJobOffer);
        interviewRepository.save(interview);

        return interview.toDto()
    }

    override fun getOpenJobOffersForCustomer(customerId: Long, page: Int, limit: Int): List<JobOfferDTO> {

            val pageable = PageRequest.of(page, limit)
            val jobOffersPage : Page<JobOffer> = jobOfferRepository.findOpenJobOffersByCustomerId(customerId, pageable)?:throw JobOfferNotFoundException(jobOfferId = null)

            return jobOffersPage.content.map { m -> m.toDto() }
        }

    override fun getAcceptedJobOffersForProfessional(professionalId: Long, page: Int, size: Int): List<JobOfferDTO> {
        val pageable = PageRequest.of(page,size)
        val jobOffersPage : Page<JobOffer> = jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId=professionalId, pageable=pageable)?: throw JobOfferNotFoundException(jobOfferId = null)
        return jobOffersPage.content.map { m->m.toDto() }
    }

    override fun getRegisteredJobOffersByParams(
        customerId: Long?,
        professionalId: Long?,
        page: Int,
        size: Int
    ): List<JobOfferDTO> {
        val pageable = PageRequest.of(page, size)
        val jobOffers = jobOfferRepository.findAllFiltered(customerId, professionalId, pageable)?: throw JobOfferNotFoundException(jobOfferId = null)
        return jobOffers.content.map { it-> it.toDto() }.sortedBy { it.id }

    }

    override fun getJobOfferNotesForJobOffer(jobOfferId: Long): List<JobOfferNoteDTO> {

        val jobOfferNotes = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException()

        return jobOfferNotes.jobOfferNotes.map { it.toDto() }
    }

    override fun addRequiredSkillJobOffer(jobOfferId: Long, dto: SkillDto): SkillDto {

        val resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException()
        val skill = skillRepository.findSkillBySkill(dto.skill) ?: skillRepository.save(Skill(skill = dto.skill))

        resJobOffer.addSkill(skill)
        jobOfferRepository.save(resJobOffer)
        kafkaService.sendMessage("job_offer_skill", skill.skill!!)

        return skill.toDto()
    }

    override fun addMultipleSkillsJobOffer(jobOfferId: Long, skills: List<SkillDto>): List<SkillDto> {
        val resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException()
        val resSkills = skills.map { it -> skillRepository.findSkillBySkill(it.skill) ?: skillRepository.save(Skill(skill= it.skill)) }
        resJobOffer.addSkills(resSkills)
        jobOfferRepository.save(resJobOffer)
        resSkills.forEach{
            kafkaService.sendMessage("job_offer_skill", it.skill!!)
        }

        return resSkills.map { it -> it.toDto() }
    }

    override fun getJobOfferSkills(jobOfferId: Long): List<SkillDto> {
        val resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw  JobOfferNotFoundException()
        val resSkills = skillRepository.findSkillByJobOffers(jobOfferId)?: throw SkillNotFoundException()

        return resSkills.map { it -> it.toDto() }
    }

    override fun getJobOfferInterview(jobOfferId: Long): List<InterviewDTO> {
        val resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException()
        val resInterview = interviewRepository.findInterviewsByJobOfferId(jobOfferId);

        return resInterview.map { it.toDto() }
    }

    override fun addProposalToJobOffer(jobOfferId: Long, proposalDTO: ProposalDTO): ProposalDTO {

        val resJobOffer = jobOfferRepository.findJobOfferById(jobOfferId)?: throw JobOfferNotFoundException()

        val resProfessional = proposalDTO.professional.let { professionalRepository.findProfessionalById(proposalDTO.professional!!)?: throw ProfessionalNotFoundException() } ?: throw ProfessionalNotFoundException()

        val resProposal = Proposal(
            status = ProposalStatus.PENDING,
            description = proposalDTO.description,
            jobOffer = resJobOffer,
            professional = resProfessional
        )
        val propWithId = proposalRepository.save(resProposal)

        resJobOffer.addProposal(propWithId)
        resProfessional.addProposal(propWithId)

        /* Salviamo tutti i dati*/
        jobOfferRepository.save(resJobOffer)
        professionalRepository.save(resProfessional)

        val messageData = mapOf("description" to propWithId.description, "status" to propWithId.status.name, "id" to propWithId.id.toString())
        val jacksonMessage = objectMapper.writeValueAsString(messageData)
        kafkaService.sendMessage("proposals", jacksonMessage)
        return proposalRepository.save(propWithId).toDto()
    }

    override fun getProposalsFromJobOfferId(jobOfferId: Long): List<ProposalDTO> {

        val proposals = proposalRepository.findProposalByJobOfferId(jobOfferId)

        return proposals.map { it.toDto() }
    }

    override fun getAllProposals(): List<ProposalDTO> {
        val proposals = proposalRepository.findAll()

        return proposals.map { it.toDto() }
    }

    override fun deleteJobOffer(jobOfferId: Long) {
        val jobOffer = jobOfferRepository.findJobOfferById(jobOfferId) ?: throw JobOfferNotFoundException();
        val skills = skillRepository.findSkillByJobOffers(jobOfferId);
        val interviews = interviewRepository.findInterviewsByJobOfferId(jobOfferId);
        val proposals = proposalRepository.findProposalByJobOfferId(jobOfferId);

        skills.forEach{
            it.jobOffers.remove(jobOffer)
            jobOffer.requiredSkills.remove(it)

            /*Chiamata ad analytics*/
            kafkaService.sendMessage("delete_job_offer_skill", it.skill !!)
        }

        /* Rimuovi tutte le interview*/
        interviews.forEach {
            it.professional = null;
            professionalRepository.save(it.professional!!)
//            it.professional.forEach{ candidate ->
//                candidate.interviews.remove(it)
//                professionalRepository.save(candidate)
//            }
        }

        /* Rimuovi tutte le proposals */
        proposals.forEach {
            it.professional?.proposals?.remove(it)
            professionalRepository.save(it.professional!!)
        }


        interviews.forEach { interviewRepository.delete(it) }
        proposals.forEach { proposalRepository.delete(it) }
        jobOfferRepository.delete(jobOffer)

    }

    override fun updateProposalStatus(proposalId: Long, newStatus: ProposalStatus): ProposalDTO {

        val proposal = proposalRepository.findProposalById(proposalId) ?: throw ProposalNotFoundException()

        val allowedTransitions: Array<ProposalStatus>

        when(proposal.status){

            ProposalStatus.PENDING -> allowedTransitions = this.proposalTransitions.pending
            ProposalStatus.ACCEPTED -> allowedTransitions = this.proposalTransitions.accepted
            ProposalStatus.ABORTED -> allowedTransitions = this.proposalTransitions.aborted

        }

        if(!allowedTransitions.contains(newStatus))
            throw InvalidProposalTransitionException(proposal.toString(), newStatus)

        proposal.apply { proposal.status = newStatus }

        /* Richiamare le API dell'analytics */
        val messageData = mapOf("id" to proposalId.toString(), "newStatus" to newStatus.name)
        val jacksonMessage = objectMapper.writeValueAsString(messageData)
        kafkaService.sendMessage("update_proposals", jacksonMessage)

       this.proposalRepository.save(proposal)

        return proposal.toDto()

    }

    override fun deleteProposal(proposalId: Long) {
        val proposal = proposalRepository.findProposalById(proposalId) ?: throw ProposalNotFoundException()

        /* Elimino la proposta dal professional assegnato*/
        proposal.professional?.proposals?.remove(proposal);
        professionalRepository.save(proposal.professional);

        /* Elimino la proposta dalla Job Offer a cui è assegnata*/
        proposal.jobOffer.proposals.remove(proposal);
        jobOfferRepository.save(proposal.jobOffer);

        /* Devo mandare un messaggio all'analytics che segna che la proposal è stata eliminata */
        val messageData = mapOf("id" to proposalId.toString())
        val jacksonMessage = objectMapper.writeValueAsString(messageData)
        kafkaService.sendMessage("delete_proposals", jacksonMessage)

        /* Cancello la proposal dal repository*/
        proposalRepository.delete(proposal)
    }
}
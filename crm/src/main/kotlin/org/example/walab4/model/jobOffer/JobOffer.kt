package org.example.walab4.model.jobOffer

import jakarta.persistence.*
import org.example.walab4.exceptions.jobOffer.InvalidJobOfferStatusValueException
import org.example.walab4.model.professional.Skill
import org.example.walab4.model.customer.Customer
import org.example.walab4.model.professional.Professional

enum class JobOfferStatus{
    CREATED, SELECTION_PHASE, CANDIDATE_PROPOSAL, CONSOLIDATED, DONE, ABORTED;

    companion object {

        fun fromStringToStatus(value: String): JobOfferStatus {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw InvalidJobOfferStatusValueException(value)
        }
    }
}

const val PROFIT_MARGIN: Double = 0.2

@Entity
class JobOffer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    var id: Long? = null,
    var description: String? = null,
    var status: JobOfferStatus = JobOfferStatus.CREATED,
    var duration: Int,
    var value: Double? = null,


    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var requiredSkills: MutableList<Skill> = mutableListOf(),

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable = false)
    var customer: Customer,

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var jobOfferNotes: MutableList<JobOfferNote> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "jobOffer", orphanRemoval = true)
    var interviews: MutableList<Interview> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", referencedColumnName = "id")
    var professional: Professional? = null,

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var proposals: MutableList<Proposal> = mutableListOf()
) {


    fun addInterview(interview: Interview) {
        this.interviews.add(interview)
        interview.jobOffer = this
    }

    fun addNote(note: JobOfferNote) {
        this.jobOfferNotes.add(note)
        note.jobOffer = this
    }

    fun addProposal(proposal: Proposal){
        this.proposals.add(proposal)
        proposal.jobOffer = this
    }

    fun addSkills(skills: List<Skill>) {
        this.requiredSkills.addAll(skills)
        skills.map { skill: Skill -> skill.jobOffers.add(this) }
    }

    fun addSkill(skill: Skill) {
        this.requiredSkills.add(skill)
        skill.jobOffers.add(this)
    }

    fun copy(id: Long?=this.id, description: String?=this.description, duration: Int=this.duration, status: JobOfferStatus =this.status, value:Double?=this.value,
             requiredSkills: MutableList<Skill> = this.requiredSkills, customer: Customer = this.customer, jobOfferNotes: MutableList<JobOfferNote> = this.jobOfferNotes,
             interviews: MutableList<Interview> = this.interviews, professional: Professional? = this.professional
    ): JobOffer {
        return JobOffer( id, description, status, duration, value, requiredSkills, customer, jobOfferNotes, interviews, professional)
    }
}
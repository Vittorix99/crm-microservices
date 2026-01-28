package org.example.walab4.model.professional

import jakarta.persistence.*
import org.example.walab4.model.jobOffer.Proposal
import org.example.walab4.dto.professional.ProfessionalDto
import org.example.walab4.model.contact.*
import org.example.walab4.model.jobOffer.Interview
import org.example.walab4.model.jobOffer.JobOffer
import org.example.walab4.model.message.Message

enum class EmploymentState{
    EMPLOYED, UNEMPLOYED, NOT_AVAILABLE
}

@Entity
@DiscriminatorValue("2")
open class Professional (
    id: Long = 0L,
    name: String?,
    surname: String?,
    ssnCode: String?,
    category: ContactCategory,
    messages: MutableList<Message>?,
    emails: MutableList<Email>?,
    addresses: MutableList<Address>?,
    telephoneNumbers: MutableList<Telephone>?,

    // extension
    open var location: String,
    open var dailyRate: Double = 0.5,
    open var state: EmploymentState = EmploymentState.NOT_AVAILABLE,
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    open var ownedSkills: MutableList<Skill> = mutableListOf<Skill>(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open var interviews: MutableList<Interview> = mutableListOf(),

    @OneToOne(mappedBy = "professional", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    open var jobOffer: JobOffer? = null,


    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open var notes: MutableList<ProfessionalNote> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.REMOVE, CascadeType.MERGE], fetch = FetchType.LAZY, orphanRemoval = true)
    open var proposals: MutableList<Proposal> = mutableListOf()

) : Contact(id, name, surname, ssnCode, category, messages, emails, addresses, telephoneNumbers) {
    constructor(dto: ProfessionalDto): this(
        0L, dto.name, dto.surname, dto.ssnCode, ContactCategory.valueOf(dto.category), null, null, null, null, dto.location, dto.dailyRate, EmploymentState.valueOf(dto.state))

    fun addSkills(skills: List<Skill>) {
        this.ownedSkills.addAll(skills)
        skills.map { skill: Skill -> skill.professionals.add(this) }
    }

    fun addSkill(skill: Skill) {
        this.ownedSkills.add(skill)
        skill.professionals.add(this)
    }

    fun addNote(note: ProfessionalNote) {
        this.notes.add(note)
        note.professional = this;
    }

    fun addInterview(interview: Interview){
        this.interviews.add(interview)
        interview.professional =this
    }

    fun removeInterview(interview: Interview){
        this.interviews.remove(interview)
        interview.professional = null
    }

    fun addProposal(proposal: Proposal){
        this.proposals.add(proposal)
        proposal.professional = this
    }

    fun addJobOffer(jobOffer: JobOffer) {
        this.jobOffer = jobOffer;
        jobOffer.professional = this
    }
}
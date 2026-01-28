package org.example.walab4.services.professional

import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.professional.ProfessionalDto
import org.example.walab4.dto.professional.ProfessionalNoteDto
import org.example.walab4.dto.professional.SkillDto
import org.example.walab4.dto.professional.toDto
import org.example.walab4.exceptions.interview.InterviewNotFoundException
import org.example.walab4.exceptions.professional.ProfessionalNotFoundException
import org.example.walab4.exceptions.professional.ProfessionalNoteNotFoundException
import org.example.walab4.exceptions.professional.SkillNotFoundException
import org.example.walab4.model.contact.ContactCategory
import org.example.walab4.model.professional.EmploymentState
import org.example.walab4.model.professional.Professional
import org.example.walab4.model.professional.ProfessionalNote
import org.example.walab4.model.professional.Skill
import org.example.walab4.repository.jobOffer.InterviewRepository
import org.example.walab4.repository.jobOffer.ProposalRepository
import org.example.walab4.repository.professional.ProfessionalNoteRepository
import org.example.walab4.repository.professional.ProfessionalRepository
import org.example.walab4.repository.professional.SkillRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProfessionalService(
    private val professionalRepository: ProfessionalRepository,
    private val skillRepository: SkillRepository,
    private val professionalNoteRepository: ProfessionalNoteRepository,
    private val interviewRepository: InterviewRepository,
    private val proposalRepository: ProposalRepository
): IProfessionalService {
    fun createProfessional(dto: ProfessionalDto): ProfessionalDto {
        val newProfessional = Professional(dto)
        return professionalRepository.save(newProfessional).toDto()
    }

    fun getProfessionalById(professionalId: Long): ProfessionalDto {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        return professional.toDto()
    }

    fun getAllProfessionals(page:Int, limit: Int, skillsStr: List<String>?, employmentState: String?, location: String?): List<ProfessionalDto> {
        val state = employmentState?.let { EmploymentState.valueOf(it) } ?: null
        val paging = PageRequest.of(page, limit)
        val skills: MutableList<Skill> = mutableListOf()

        skillsStr?.map { skillStr ->
            val skill = skillRepository.findSkillBySkill(skillStr)
            if(skill != null)
                skills.add(skill)
        }

        var professionals: List<Professional>;
        if(skills.isEmpty()) {
            professionals = professionalRepository.findAllProfessionalFilteredByStateAndLocation(state, location, paging)
        } else {
            professionals = professionalRepository.findAllProfessionalFilteredBySkillStateLocation(skills, state, location, paging)
        }

        return professionals.map { it.toDto() }
    }

    fun createSkill(dto: SkillDto): SkillDto {
        val newSkill = Skill(dto)
        return skillRepository.save(newSkill).toDto()
    }

    fun addSkillsToProfessional(dtos: List<SkillDto>, professionalId: Long): List<SkillDto> {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val skills : MutableList<Skill> = mutableListOf()

        for (dto in dtos) {
            val skill = skillRepository.findSkillBySkill(dto.skill) ?: skillRepository.save(Skill(skill = dto.skill))

            professional.addSkill(skill)
            professionalRepository.save(professional)
            skills.add(skill)
        }

        return skills.map {
            it.toDto()
        }
    }

    fun getProfessionalSkills(professionalId: Long): List<SkillDto> {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val skills = skillRepository.findSkillsByProfessional(professionalId)

        return skills.map { it.toDto() }
    }

    fun updateProfessional(professionalId: Long, dto: ProfessionalDto): ProfessionalDto {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())

        professional.apply {
            this.name = dto.name
            this.surname = dto.surname
            this.ssnCode = dto.ssnCode
            this.dailyRate = dto.dailyRate
            this.state = EmploymentState.valueOf(dto.state)
            this.category = ContactCategory.valueOf(dto.category)
        }

        return professionalRepository.save(professional).toDto()
    }

    fun deleteProfessional(professionalId: Long) {

        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val skills = skillRepository.findSkillsByProfessional(professionalId)
        skills.forEach {
            it.professionals.remove(professional)
            professional.ownedSkills.remove(it)
        }

        professional.notes.forEach { professionalNoteRepository.delete(it) }

        professional.proposals.forEach {
            it.jobOffer.proposals.remove(it)
            proposalRepository.delete(it)
        }

        professional.interviews.forEach {
            it.professional = null;
            it.jobOffer?.interviews = mutableListOf();
            interviewRepository.delete(it)
        }

        professionalRepository.delete(professional)
    }

    fun deleteSkillFromProfessional(skillId: Long, professionalId: Long) {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val skill = skillRepository.findSkillById(skillId) ?: throw SkillNotFoundException(skillId.toString())

        professional.ownedSkills.remove(skill)
        skill.professionals.remove(professional)

        professionalRepository.save(professional)
    }

    fun addNoteToProfessional(professionalId: Long, professionalNoteDto: ProfessionalNoteDto): ProfessionalNoteDto {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val note = ProfessionalNote(professionalNoteDto)

        professional.addNote(note)

        return professionalNoteRepository.save(note).toDto()
    }

    fun getProfessionalNotes(professionalId: Long): List<ProfessionalNoteDto> {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val notes = professionalNoteRepository.findProfessionalsNoteByProfessionalId(professionalId)

        return notes.map { it.toDto() }
    }

    fun deleteNoteFromProfessional(noteId: Long, professionalId: Long) {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val note = professionalNoteRepository.findProfessionalNoteById(noteId) ?: throw ProfessionalNoteNotFoundException(noteId.toString())

        professional.notes.remove(note)
        professionalRepository.save(professional)

        professionalNoteRepository.delete(note)
    }

    fun updateProfessionalNote(noteId: Long, professionalId: Long, noteDto: ProfessionalNoteDto): ProfessionalNoteDto {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId.toString())
        val note = professionalNoteRepository.findProfessionalNoteById(noteId) ?: throw ProfessionalNotFoundException(noteId.toString())

        note.apply {
            description = noteDto.description
            title = noteDto.title
        }

        return professionalNoteRepository.save(note).toDto()
    }

    fun updateSkill(skillId: Long, dto: SkillDto): SkillDto {
        val skill = skillRepository.findSkillById(skillId) ?: throw SkillNotFoundException(skillId.toString())

        skill.apply {
            this.skill = dto.skill
        }

        return skillRepository.save(skill).toDto()
    }

    fun getAllSkills(): List<SkillDto> {
        val skills = skillRepository.findAll()

        return skills.map { it.toDto() }
    }

    override fun addInterviewToProfessional(professionalId: Long, interviewDTO: InterviewDTO): ProfessionalDto {
        val professional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException(professionalId = professionalId.toString());
        val interview = interviewRepository.findInterviewById(interviewDTO.id!!) ?: throw InterviewNotFoundException()

        professional.addInterview(interview);

        /* Salviamo sia le interview che i professional*/
        professionalRepository.save(professional);
        interviewRepository.save(interview);

        return professional.toDto()
    }


}
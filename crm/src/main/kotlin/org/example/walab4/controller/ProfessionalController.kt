package org.example.walab4.controller

import jakarta.validation.constraints.Min
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.professional.ProfessionalDto
import org.example.walab4.dto.professional.ProfessionalNoteDto
import org.example.walab4.dto.professional.SkillDto
import org.example.walab4.exceptions.professional.InvalidProfessionalPropsException
import org.example.walab4.services.professional.ProfessionalService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/professionals")
class ProfessionalController(val professionalService: ProfessionalService) {
    private val LOGGER: Logger = LogManager.getLogger()

    @GetMapping("/{professionalId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProfessional(
        @PathVariable professionalId: Long
    ): ProfessionalDto {
        val professionalDto = professionalService.getProfessionalById(professionalId)
        LOGGER.info("[GET - API/professionals/$professionalId] - SUCCESS - Professional ${professionalId} retrieved correctly")
        return professionalDto
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun getProfessionals(
        @Min(0)
        @RequestParam page: Int = 0,
        @Min(1)
        @RequestParam limit: Int = 15,
        @RequestParam skills: List<String>?,
        @RequestParam employmentState: String?,
        @RequestParam location: String?
    ): List<ProfessionalDto> {
        var professionalsDto: List<ProfessionalDto>;
        try {
            professionalsDto = professionalService.getAllProfessionals(page, limit, skills, employmentState, location)
        } catch (iae: IllegalArgumentException) {
            throw InvalidProfessionalPropsException()
        }
        LOGGER.info("[GET - API/professionals - SUCCESS - Professionals retrieved correctly")
        return professionalsDto
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessional(
        @RequestBody professionalDto: ProfessionalDto
    ): ProfessionalDto {
        var newProfessionalDto: ProfessionalDto;
        try {
            newProfessionalDto = professionalService.createProfessional(professionalDto)
        } catch (iae: IllegalArgumentException) {
            throw InvalidProfessionalPropsException()
        }
        LOGGER.info("[POST - API/professionals] - SUCCESS - Contact ${newProfessionalDto.id} created correctly")
        return newProfessionalDto
    }

    @PutMapping("/{professionalId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateProfessional(
        @RequestBody professionalDto: ProfessionalDto,
        @PathVariable professionalId: Long
    ): ProfessionalDto {
        var newProfessionalDto: ProfessionalDto;
        try {
            newProfessionalDto = professionalService.updateProfessional(professionalId ,professionalDto)
        } catch (iae: IllegalArgumentException) {
            throw InvalidProfessionalPropsException()
        }
        LOGGER.info("[PUT - API/professionals] - SUCCESS - Contact ${newProfessionalDto.id} created correctly")
        return newProfessionalDto
    }

    @PostMapping("/{professionalId}/skills")
    @ResponseStatus(HttpStatus.CREATED)
    fun addSkillsToContact(
        @RequestBody skill: List<SkillDto>,
        @PathVariable professionalId: Long
    ): List<SkillDto> {
        val skills = professionalService.addSkillsToProfessional(skill, professionalId)
        LOGGER.info("[POST - API/professionals/$professionalId]/skill - SUCCESS - Skills ${skills.map { print(it.skill) }} created correctly")
        return skills
    }

    @GetMapping("/{professionalId}/skills")
    @ResponseStatus(HttpStatus.OK)
    fun getProfessionalSkills(
        @PathVariable professionalId: Long
    ): List<SkillDto> {
        val skills = professionalService.getProfessionalSkills(professionalId)
        LOGGER.info("[GET - API/professionals/$professionalId]/skills - SUCCESS - Contact $professionalId Skills retrieved correctly")
        return skills
    }

    @DeleteMapping("/{professionalId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProfessional(
        @PathVariable professionalId: Long
    ) {
       professionalService.deleteProfessional(professionalId)
       LOGGER.info("[DELETE - API/professionals/$professionalId] - SUCCESS - Professional $professionalId deleted correctly")
    }

    @DeleteMapping("/{professionalId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.OK)
    fun removeSkillFromProfessional(
        @PathVariable professionalId: Long,
        @PathVariable skillId: Long
    ) {
        professionalService.deleteSkillFromProfessional(skillId, professionalId)
        LOGGER.info("[DELETE - API/professionals/$professionalId/skills/$skillId] - SUCCESS - Skill $skillId removed correctly from professional $professionalId")
    }

    @PostMapping("/{professionalId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    fun addNoteToProfessional(
        @PathVariable professionalId: Long,
        @RequestBody note: ProfessionalNoteDto
    ): ProfessionalNoteDto {
        val note = professionalService.addNoteToProfessional(professionalId, note)
        LOGGER.info("[POST - API/professionals/$professionalId]/notes - SUCCESS - Skill ${note.id} created correctly")
        return note
    }

    @GetMapping("/{professionalId}/notes")
    @ResponseStatus(HttpStatus.OK)
    fun getProfessionalNotes(
        @PathVariable professionalId: Long
    ): List<ProfessionalNoteDto> {
        val skills = professionalService.getProfessionalNotes(professionalId)
        LOGGER.info("[GET - API/professionals/$professionalId]/notes - SUCCESS - Contact $professionalId notes retrieved correctly")
        return skills
    }

    @DeleteMapping("/{professionalId}/notes/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    fun removeNoteFromProfessional(
        @PathVariable professionalId: Long,
        @PathVariable noteId: Long
    ) {
        professionalService.deleteNoteFromProfessional(noteId, professionalId)
        LOGGER.info("[DELETE - API/professionals/$professionalId/notes/$noteId] - SUCCESS - Note $noteId removed correctly from professional $professionalId")
    }

    @PutMapping("/{professionalId}/notes/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfessionalNote(
        @PathVariable professionalId: Long,
        @PathVariable noteId: Long,
        @RequestBody noteDto: ProfessionalNoteDto
    ): ProfessionalNoteDto {
        val updatedNote = professionalService.updateProfessionalNote(noteId, professionalId, noteDto)
        LOGGER.info("[PUT - API/professionals/$professionalId/note/$noteId] - SUCCESS - Note $noteId removed correctly from professional $professionalId")
        return updatedNote
    }

    @GetMapping("/skills")
    @ResponseStatus(HttpStatus.OK)
    fun getAllProfessionalSkills(): List<SkillDto> {
        val skills = professionalService.getAllSkills()
        LOGGER.info("[GET - API/professionals/skills - SUCCESS - All Skills retrieved correctly")
        return skills
    }

    @PostMapping("/skills")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSkill(
        @RequestBody skill: SkillDto
    ): SkillDto {
        val skill = professionalService.createSkill(skill)
        LOGGER.info("[POST - API/professionals/skill - SUCCESS - Skill ${skill.skill} created correctly")
        return skill
    }

    @PostMapping("/{professionalId}/interviews")
    @ResponseStatus(HttpStatus.OK)
    fun addInterviewToProfessional(
        @PathVariable professionalId: Long,
        @RequestBody interviewDTO: InterviewDTO
    ): ProfessionalDto {
        val professional = professionalService.addInterviewToProfessional(professionalId, interviewDTO)
        LOGGER.info("[POST - API/professionals/$professionalId/interviews - SUCCESS - Interview Successfully added")
        return professional
    }
}
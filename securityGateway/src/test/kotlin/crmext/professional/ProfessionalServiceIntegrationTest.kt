package crmext.professional

import crmext.CrmExtIntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import wa.lab5.dto.dto.professional.ProfessionalNoteDto
import wa.lab5.dto.professional.ProfessionalDto
import wa.lab5.dto.professional.SkillDto
import wa.lab5.exceptions.professional.ProfessionalNotFoundException
import wa.lab5.services.professional.ProfessionalService

class ProfessionalServiceIntegrationTest: CrmExtIntegrationTest() {

    @Autowired
    private lateinit var professionalService: ProfessionalService

    private val expectedProfessionalDto = ProfessionalDto(id = 1L, name = "Simone", surname = "Geraci", location = "ITALY", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "EMPLOYED")
    private val expectedProfessionalDto2 = ProfessionalDto(id = 1L, name = "Carlo", surname = "Rossi", location = "SPAIN", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "UNEMPLOYED")
    private val expectedProfessionalDto3 = ProfessionalDto(id = 1L, name = "Giovanni", surname = "Bianchi", location = "ITALY", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "UNEMPLOYED")
    private val expectedSkillDto = SkillDto(id = 1L, skill = "PYTHON")
    private val expectedSkillDto2 = SkillDto(id = 1L, skill = "JAVA")
    private val expectedNoteDto = ProfessionalNoteDto(id = 0L, professionalId = 0L, title = "NOTE TITLE", description = "NOTE DESCRIPTION")

    @Test // create professional, add skills, retrieve skills and professional
    fun professionaFlow1() {
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val pythonSkill = professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val javaSkill = professionalService.addSkillToProfessional(expectedSkillDto2, professional.id!!)

        val skills = professionalService.getProfessionalSkills(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(skills.containsAll(listOf(pythonSkill, javaSkill)))
    }

    @Test // create professional, add skills, remove skills, retrieve skills and professional
    fun professionaFlow2() {
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val pythonSkill = professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val javaSkill = professionalService.addSkillToProfessional(expectedSkillDto2, professional.id!!)

        professionalService.deleteSkillFromProfessional(pythonSkill.id!!, professional.id!!)
        val skills = professionalService.getProfessionalSkills(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(skills.contains(javaSkill))
        assert(!skills.contains(pythonSkill))
    }

    @Test // create professional, add note, retrieve note and professional
    fun professionalFlow3() {
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val note = professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)

        val notes = professionalService.getProfessionalNotes(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(note.professionalId == professional.id!!)
        assert(notes.contains(note))
    }

    @Test // create professional, add note, remove note, retrieve note and professional
    fun professionalFlow4() {
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val note = professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)

        professionalService.deleteNoteFromProfessional(note.id!!, professional.id!!)
        val notes = professionalService.getProfessionalNotes(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(!notes.contains(note))
    }

    @Test
    fun professionalFlow5() { // create professional, add note, update note, retrieve note and professional
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val note = professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)

        val updatedNote = ProfessionalNoteDto(id = note.id!!, professionalId = note.professionalId, title = "UPDATED TITLE", description = "UPDATED DESCRIPTION")
        val noteUpdated = professionalService.updateProfessionalNote(note.id!!, professional.id!!, updatedNote)

        val notes = professionalService.getProfessionalNotes(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(noteUpdated.professionalId == professional.id!!)
        assert(notes.contains(noteUpdated))
    }

    @Test
    fun professionalFlow6() { // create professional, add 2 skills, delete one, retrieve skills and professional
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val pythonSkill = professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val javaSkill = professionalService.addSkillToProfessional(expectedSkillDto2, professional.id!!)

        professionalService.deleteSkillFromProfessional(pythonSkill.id!!, professional.id!!)
        val skills = professionalService.getProfessionalSkills(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professional.equals(professionalRetrieved))
        assert(skills.contains(javaSkill))
        assert(!skills.contains(pythonSkill))
    }

    @Test
    fun professionalFlow7() { // create professional, update it, retrieve it, add two skill, add two notes, delete one and retrieve all
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val professionalUpdated = professionalService.updateProfessional(professional.id!!, expectedProfessionalDto2)
        val pythonSkill = professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val javaSkill = professionalService.addSkillToProfessional(expectedSkillDto2, professional.id!!)
        val note = professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)
        val note2 = professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)

        professionalService.deleteNoteFromProfessional(note.id!!, professional.id!!)
        val skills = professionalService.getProfessionalSkills(professional.id!!)
        val notes = professionalService.getProfessionalNotes(professional.id!!)
        val professionalRetrieved = professionalService.getProfessionalById(professional.id!!)

        assert(professionalUpdated.equals(professionalRetrieved))
        assert(skills.containsAll(listOf(pythonSkill, javaSkill)))
        assert(notes.contains(note2))
        assert(!notes.contains(note))
    }

    @Test
    fun professionalFlow8() { // create professional, update it, retrieve it, add two skill, add two notes, assert correct creation, delete professional and retrieve all
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)
        professionalService.addNoteToProfessional( professional.id!!, expectedNoteDto)

        professionalService.deleteProfessional(professional.id!!)

        assertThrows<ProfessionalNotFoundException> {
            val skills = professionalService.getProfessionalSkills(professional.id!!)
            val notes = professionalService.getProfessionalNotes(professional.id!!)
            assert(skills.isEmpty())
            assert(notes.isEmpty())
            professionalService.getProfessionalById(professional.id!!)
        }
    }

    @Test
    fun professionalFlow9() { // create three professionals, and filter them with getAll function
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        val professional2 = professionalService.createProfessional(expectedProfessionalDto2)
        val professional3 = professionalService.createProfessional(expectedProfessionalDto3)

        val professionals = professionalService.getAllProfessionals(0,10, null, "EMPLOYED", null)

        assert(professionals.containsAll(listOf(professional)))
    }

    @Test
    fun professionalFlow10() { // create three professionals, and filter them by skill and state
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val professional2 = professionalService.createProfessional(expectedProfessionalDto2)
        val professional3 = professionalService.createProfessional(expectedProfessionalDto3)

        val professionals = professionalService.getAllProfessionals(0,10, listOf("PYTHON"), "EMPLOYED", null)

        assert(professionals.containsAll(listOf(professional)))
    }

    @Test
    fun professionalFlow11() { // create three professionals, and filter them by skill and location
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)
        val professional2 = professionalService.createProfessional(expectedProfessionalDto2)
        val professional3 = professionalService.createProfessional(expectedProfessionalDto3)

        val professionals = professionalService.getAllProfessionals(0,10, listOf("PYTHON"), null, "ITALY")

        assert(professionals.containsAll(listOf(professional)))

    }

    @Test
    fun professionalFlow12() { // create three professionals, and filter them by skill and location and state
        val professional = professionalService.createProfessional(expectedProfessionalDto)
        professionalService.addSkillToProfessional(expectedSkillDto, professional.id!!)

        val professional2 = professionalService.createProfessional(expectedProfessionalDto2)

        val professional3 = professionalService.createProfessional(expectedProfessionalDto3)
        professionalService.addSkillToProfessional(expectedSkillDto, professional3.id!!)

        val professionals = professionalService.getAllProfessionals(0,10, listOf("PYTHON"), "UNEMPLOYED", "ITALY")

        assert(professionals.containsAll(listOf(professional3)))

    }

}
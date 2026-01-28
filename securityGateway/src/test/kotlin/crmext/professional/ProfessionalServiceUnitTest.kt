package crmext.professional;

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import wa.lab5.dto.dto.professional.ProfessionalNoteDto
import wa.lab5.dto.professional.ProfessionalDto
import wa.lab5.dto.professional.SkillDto
import wa.lab5.exceptions.professional.ProfessionalNotFoundException
import wa.lab5.exceptions.professional.ProfessionalNoteNotFoundException
import wa.lab5.exceptions.professional.SkillNotFoundException
import wa.lab5.model.professional.Professional
import wa.lab5.model.professional.ProfessionalNote
import wa.lab5.model.professional.Skill
import wa.lab5.repository.professional.ProfessionalNoteRepository
import wa.lab5.repository.professional.ProfessionalRepository
import wa.lab5.repository.professional.SkillRepository
import wa.lab5.services.professional.ProfessionalService

class ProfessionalServiceUnitTest {
    private val professionalRepository = mockk<ProfessionalRepository>()
    private val skillRepository = mockk<SkillRepository>()
    private val professionalNoteRepository = mockk<ProfessionalNoteRepository>()

    private val professionalService = ProfessionalService(
        professionalRepository,
        skillRepository,
        professionalNoteRepository
    )

    private val expectedProfessionalDto = ProfessionalDto(
        id = 1L,
        name = "Simone",
        surname = "Geraci",
        location = "ITALY",
        category = "PROFESSIONAL",
        ssnCode = "TESTSSN",
        dailyRate = 1.0,
        state = "EMPLOYED"
    )
    private val expectedProfessional = Professional(expectedProfessionalDto)

    private val expectedProfessionalDto2 = ProfessionalDto(
        id = 1L,
        name = "Carlo",
        surname = "Rossi",
        location = "SPAIN",
        category = "PROFESSIONAL",
        ssnCode = "TESTSSN",
        dailyRate = 1.0,
        state = "UNEMPLOYED"
    )
    private val expectedProfessional2 = Professional(expectedProfessionalDto2)

    private val expectedSkillDto = SkillDto(id = 1L, skill = "PYTHON")
    private val expectedSkill = Skill(expectedSkillDto)
    private val expecteSkillsDto = listOf(SkillDto(id = 1L, skill = "PYTHON"), SkillDto(id = 1L, skill = "JAVA"))
    private val expectedSkills = expecteSkillsDto.map { Skill(it) }
    private val expecteSkillsDtoEmpty = listOf<SkillDto>()
    private val expecteSkillsEmpty = listOf<Skill>()

    private val expectedNoteDto = ProfessionalNoteDto(id = 0L, professionalId = 0L, title = "NOTE TITLE", description = "NOTE DESCRIPTION")
    private val expectedNote = ProfessionalNote(expectedNoteDto)


    @Test
    fun createProfessionalSimple() {
        every { professionalRepository.save( any() ) } returns expectedProfessional

        val professional = professionalService.createProfessional(expectedProfessionalDto)

        Assertions.assertEquals(professional.name, expectedProfessionalDto.name)
        Assertions.assertEquals(professional.surname, expectedProfessionalDto.surname)
        Assertions.assertEquals(professional.ssnCode, expectedProfessionalDto.ssnCode)
        Assertions.assertEquals(professional.category, expectedProfessionalDto.category)
        Assertions.assertEquals(professional.location, expectedProfessionalDto.location)
        Assertions.assertEquals(professional.state, expectedProfessionalDto.state)
        Assertions.assertEquals(professional.dailyRate, expectedProfessionalDto.dailyRate)
    }

    @Test
    fun getProfessionalById() {
        val id = 0L
        every { professionalRepository.findProfessionalById( any() ) } returns expectedProfessional

        val professional = professionalService.getProfessionalById(id)

        Assertions.assertEquals(professional.name, expectedProfessionalDto.name)
        Assertions.assertEquals(professional.surname, expectedProfessionalDto.surname)
        Assertions.assertEquals(professional.ssnCode, expectedProfessionalDto.ssnCode)
        Assertions.assertEquals(professional.category, expectedProfessionalDto.category)
        Assertions.assertEquals(professional.location, expectedProfessionalDto.location)
        Assertions.assertEquals(professional.state, expectedProfessionalDto.state)
        Assertions.assertEquals(professional.dailyRate, expectedProfessionalDto.dailyRate)
    }

    @Test
    fun getProfessionalByIdNotFound() {
        val id = 0L
        every { professionalRepository.findProfessionalById( any() ) } throws ProfessionalNotFoundException(id.toString())

        assertThrows<ProfessionalNotFoundException> {
            professionalService.getProfessionalById(id)
        }
    }

    @Test
    fun createSkill() {
        every { skillRepository.save(any()) } returns expectedSkill

        val skill = professionalService.createSkill(expectedSkillDto)

        Assertions.assertEquals(expectedSkillDto.skill, skill.skill)
    }


    @Test
    fun addSkillToProfessional() {
        val profId = 0L

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { skillRepository.findSkillBySkill(any()) } returns expectedSkill
        every { professionalRepository.save(any()) } returns expectedProfessional

        val skill = professionalService.addSkillToProfessional(expectedSkillDto, profId)

        Assertions.assertEquals(expectedSkillDto.skill, skill.skill)
    }

    @Test
    fun addSkillToProfessionalNotFound() {
        val profId = 0L

        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException(profId.toString())

        assertThrows<ProfessionalNotFoundException> {
            professionalService.addSkillToProfessional(expectedSkillDto, profId)
        }
    }

    @Test
    fun getProfessionalSkills() {
        val profId = 0L

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { skillRepository.findSkillsByProfessional(any()) } returns expectedSkills

        val skillsDto = professionalService.getProfessionalSkills(profId)

        Assertions.assertEquals(skillsDto[0].skill, expecteSkillsDto[0].skill)
        Assertions.assertEquals(skillsDto[1].skill, expecteSkillsDto[1].skill)
    }

    @Test
    fun getProfessionalSkillsEmpty() {
        val profId = 0L

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { skillRepository.findSkillsByProfessional(any()) } returns expecteSkillsEmpty

        val skillsDto = professionalService.getProfessionalSkills(profId)

        assert(skillsDto.isEmpty())
    }

    @Test
    fun updateProfessional() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalRepository.save(any()) } returns expectedProfessional2

        val professionalDto = professionalService.updateProfessional(0L, expectedProfessionalDto2)

        Assertions.assertEquals(professionalDto.name, expectedProfessionalDto2.name)
        Assertions.assertEquals(professionalDto.surname, expectedProfessionalDto2.surname)
        Assertions.assertEquals(professionalDto.ssnCode, expectedProfessionalDto2.ssnCode)
        Assertions.assertEquals(professionalDto.category, expectedProfessionalDto2.category)
        Assertions.assertEquals(professionalDto.location, expectedProfessionalDto2.location)
        Assertions.assertEquals(professionalDto.state, expectedProfessionalDto2.state)
        Assertions.assertEquals(professionalDto.dailyRate, expectedProfessionalDto2.dailyRate)    }

    @Test
    fun updateProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.updateProfessional(0L, expectedProfessionalDto2)
        }
    }

    @Test
    fun deleteProfessional() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalRepository.delete(any()) } returns Unit

        assert(professionalService.deleteProfessional(0L).equals(Unit))
    }

    @Test
    fun deleteProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")
        assertThrows<ProfessionalNotFoundException> {
            professionalService.deleteProfessional(0L)
        }
    }

    @Test
    fun deleteSkillFromProfessional() {
        val skillId = 0L
        val profId = 0L

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { skillRepository.findSkillById(any()) } returns expectedSkill
        every { professionalRepository.save(any()) } returns expectedProfessional

        val unit = professionalService.deleteSkillFromProfessional(skillId, profId)

        Assertions.assertEquals(Unit, unit)
    }

    @Test
    fun deleteSkillFromProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.deleteSkillFromProfessional(0L, 0L)
        }
    }

    @Test
    fun deleteSkillFromProfessionalSkillNotFound() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { skillRepository.findSkillById(any()) } throws SkillNotFoundException("0")

        assertThrows<SkillNotFoundException> {
            professionalService.deleteSkillFromProfessional(0L, 0L)
        }
    }

    @Test
    fun addNoteToProfessional() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalNoteRepository.save(any()) } returns expectedNote

        val note = professionalService.addNoteToProfessional(0L, expectedNoteDto)

        Assertions.assertEquals(expectedNoteDto.description, note.description)
        Assertions.assertEquals(expectedNoteDto.title, note.title)
    }

    @Test
    fun addNoteToProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.addNoteToProfessional(0L, expectedNoteDto)
        }
    }

    @Test
    fun getProfessionalNotes() {
        val expectedNotes = listOf(expectedNoteDto)

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalNoteRepository.findProfessionalsNoteByProfessionalId(0L) } returns listOf(expectedNote)

        val professionalNotes = professionalService.getProfessionalNotes(0L)

        Assertions.assertEquals(expectedNotes[0].description, professionalNotes[0].description)
        Assertions.assertEquals(expectedNotes[0].title, professionalNotes[0].title)

    }

    @Test
    fun getProfessionalNotesEmpty() {

        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalNoteRepository.findProfessionalsNoteByProfessionalId(0L) } returns listOf()

        val professionalNotes = professionalService.getProfessionalNotes(0L)

        assert(professionalNotes.isEmpty())
    }

    @Test
    fun getProfessionalNotesNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.getProfessionalNotes(0L)
        }
    }

    @Test
    fun deleteNoteFromProfessional() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalRepository.save(any()) } returns expectedProfessional
        every { professionalNoteRepository.findProfessionalNoteById(any()) } returns expectedNote
        every { professionalNoteRepository.delete(any()) } returns Unit

        val unit = professionalService.deleteNoteFromProfessional(0L, 0L)

        Assertions.assertEquals(Unit, unit)
    }

    @Test
    fun deleteNoteFromProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalNoteRepository.findProfessionalNoteById(any()) } throws ProfessionalNoteNotFoundException("0")

        assertThrows<ProfessionalNoteNotFoundException> {
            professionalService.deleteNoteFromProfessional(0L, 0L)
        }
    }

    @Test
    fun deleteNoteFromProfessionalNoteNodFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.deleteNoteFromProfessional(0L, 0L)
        }
    }


    @Test
    fun updateProfessionalNote() {
        val expectedNoteDto2 = ProfessionalNoteDto(id = 1L, professionalId = 1L, title = "NOTE TITLE2", description = "NOTE DESCRIPTION2")
        val expectedNote2 = ProfessionalNote(expectedNoteDto2)

        every { professionalRepository.findProfessionalById(any()) } returns  expectedProfessional
        every { professionalNoteRepository.findProfessionalNoteById(any()) } returns expectedNote
        every { professionalNoteRepository.save(any()) } returns expectedNote2

        val note2 = professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto2)

        Assertions.assertEquals(expectedNoteDto2.description, note2.description)
        Assertions.assertEquals(expectedNoteDto2.title, note2.title)
    }

    @Test
    fun updateProfessionalNoteProfessionalNotFound() {
        every { professionalRepository.findProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        assertThrows<ProfessionalNotFoundException> {
            professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto)
        }
    }

    @Test
    fun updateProfessionalNoteNotFound() {
        every { professionalRepository.findProfessionalById(any()) } returns expectedProfessional
        every { professionalNoteRepository.findProfessionalNoteById(any()) } throws ProfessionalNoteNotFoundException("0")

        assertThrows<ProfessionalNoteNotFoundException> {
            professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto)
        }
    }

    @Test
    fun getAllProfessionals() {
        every { professionalRepository.findAllProfessionalFilteredByStateAndLocation(any(), any(), any()) } returns listOf(expectedProfessional, expectedProfessional2)

        val professionals = professionalService.getAllProfessionals(0, 10, null, "EMPLOYED", "ITALY")

        Assertions.assertEquals(2, professionals.size)
        Assertions.assertEquals(expectedProfessionalDto.name, professionals[0].name)
        Assertions.assertEquals(expectedProfessionalDto2.name, professionals[1].name)
        Assertions.assertEquals(expectedProfessionalDto.surname, professionals[0].surname)
        Assertions.assertEquals(expectedProfessionalDto2.surname, professionals[1].surname)
        Assertions.assertEquals(expectedProfessionalDto.ssnCode, professionals[0].ssnCode)
        Assertions.assertEquals(expectedProfessionalDto2.ssnCode, professionals[1].ssnCode)
        Assertions.assertEquals(expectedProfessionalDto.category, professionals[0].category)
        Assertions.assertEquals(expectedProfessionalDto2.category, professionals[1].category)
        Assertions.assertEquals(expectedProfessionalDto.location, professionals[0].location)
        Assertions.assertEquals(expectedProfessionalDto2.location, professionals[1].location)
        Assertions.assertEquals(expectedProfessionalDto.state, professionals[0].state)
        Assertions.assertEquals(expectedProfessionalDto2.state, professionals[1].state)
        Assertions.assertEquals(expectedProfessionalDto.dailyRate, professionals[0].dailyRate)
        Assertions.assertEquals(expectedProfessionalDto2.dailyRate, professionals[1].dailyRate)
    }

    @Test
    fun getAllProfessionalsEmpty() {
        every { professionalRepository.findAllProfessionalFilteredByStateAndLocation(any(), any(), any()) } returns listOf()

        val professionals = professionalService.getAllProfessionals(0, 10, null, "EMPLOYED", "ITALY")

        assert(professionals.isEmpty())
    }
}

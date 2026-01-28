package crmext.professional;

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import wa.lab5.Lab5Application
import wa.lab5.controller.ProfessionalController
import wa.lab5.dto.dto.professional.ProfessionalNoteDto
import wa.lab5.dto.professional.ProfessionalDto
import wa.lab5.dto.professional.SkillDto
import wa.lab5.exceptions.professional.ProfessionalNotFoundException
import wa.lab5.model.professional.Professional
import wa.lab5.model.professional.ProfessionalNote
import wa.lab5.model.professional.Skill
import wa.lab5.services.professional.ProfessionalService

@WebMvcTest(controllers = arrayOf(ProfessionalController::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class ProfessionaControllerUnitTest(@Autowired val mockMvc:MockMvc) {

    @MockkBean
    lateinit var professionalService: ProfessionalService


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
    private val expectedNoteDto2 = ProfessionalNoteDto(id = 1L, professionalId = 1L, title = "NOTE TITLE 2", description = "NOTE DESCRIPTION 2")
    private val expectedNote2 = ProfessionalNote(expectedNoteDto2)


    @Test
    fun getProfessional() {
        val profId = 0L

        every { professionalService.getProfessionalById(any()) } returns expectedProfessionalDto

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/$profId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
    }

    @Test
    fun getProfessionalNotFound() {
        val profId = 0L

        every { professionalService.getProfessionalById(any()) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/$profId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun createProfessional() {
        every { professionalService.createProfessional(any()) } returns expectedProfessionalDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/professionals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
    }

    @Test
    fun createProfessionalInvalid() {
        //todo
    }

    @Test
    fun updateProfessional() {
        every { professionalService.updateProfessional(0L, expectedProfessionalDto) } returns expectedProfessionalDto

        mockMvc.perform(MockMvcRequestBuilders.put("/API/professionals/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
    }

    @Test
    fun updateProfessionalNotFound() {
        val profId = 0L

        every { professionalService.updateProfessional(profId, expectedProfessionalDto) } throws ProfessionalNotFoundException(profId.toString())

        mockMvc.perform(MockMvcRequestBuilders.put("/API/professionals/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedProfessionalDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addSkillToContact() {
        every { professionalService.addSkillToProfessional(expectedSkillDto, 0L) } returns expectedSkillDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/professionals/0/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedSkillDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun addSkillToContactNotFound() {
        every { professionalService.addSkillToProfessional(expectedSkillDto, 0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.post("/API/professionals/0/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedSkillDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getProfessionalSkills() {
        every { professionalService.getProfessionalSkills(0L) } returns expecteSkillsDto

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/skills"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expecteSkillsDto)))
    }

    @Test
    fun getProfessionalSkillsProfessionalNotFound() {
        every { professionalService.getProfessionalSkills(0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/skills"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getProfessionalSkillsEmpty() {
        every { professionalService.getProfessionalSkills(0L) } returns expecteSkillsDtoEmpty

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/skills"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expecteSkillsDtoEmpty)))
    }

    @Test
    fun deleteProfessional() {
        every { professionalService.deleteProfessional(0L) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteProfessionalNotFound() {
        every { professionalService.deleteProfessional(0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addNoteToProfessional() {
        every { professionalService.addNoteToProfessional(0L, expectedNoteDto) } returns expectedNoteDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/professionals/0/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun addNoteToProfessionalNotFound() {
        every { professionalService.addNoteToProfessional(0L, expectedNoteDto) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.post("/API/professionals/0/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun removeSkillFromProfessional() {
        every { professionalService.deleteSkillFromProfessional(0L, 0L) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0/skills/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun removeSkillFromProfessionalNotFound() {
        every { professionalService.deleteSkillFromProfessional(0L, 0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0/skills/0"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getProfessionalNotes() {
        every { professionalService.getProfessionalNotes(0L) } returns listOf(expectedNoteDto, expectedNoteDto2)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf(expectedNoteDto, expectedNoteDto2))))
    }

    @Test
    fun getProfessionalNotesNotFound() {
        every { professionalService.getProfessionalNotes(0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getProfessionalNotesEmpty() {
        every { professionalService.getProfessionalNotes(0L) } returns listOf()

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf<ProfessionalNoteDto>())))
    }

    @Test
    fun removeNoteFromProfessional() {
        every { professionalService.deleteNoteFromProfessional(0L, 0L) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0/notes/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun removeNoteFromProfessionalNotFound() {
        every { professionalService.deleteNoteFromProfessional(0L, 0L) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/professionals/0/notes/0"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateProfessionalNote() {
        every { professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto) } returns expectedNoteDto

        mockMvc.perform(MockMvcRequestBuilders.put("/API/professionals/0/notes/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun updateProfessionalNoteProfessionalNotFound() {
        every { professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.put("/API/professionals/0/notes/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateProfessionalNoteNoteNotFound() {
        every { professionalService.updateProfessionalNote(0L, 0L, expectedNoteDto) } throws ProfessionalNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.put("/API/professionals/0/notes/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getAllProfessionals() {
        every { professionalService.getAllProfessionals(any(), any(), any(), any(), any()) } returns listOf(expectedProfessionalDto, expectedProfessionalDto2)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf(expectedProfessionalDto, expectedProfessionalDto2))))
    }

    @Test
    fun getAllProfessionalsEmpty() {
        every { professionalService.getAllProfessionals(any(), any(), any(), any(), any()) } returns listOf()

        mockMvc.perform(MockMvcRequestBuilders.get("/API/professionals"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf<ProfessionalDto>())))
    }

}

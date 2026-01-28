package crmext.professional

import com.fasterxml.jackson.databind.ObjectMapper
import crmext.CrmExtIntegrationTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import wa.lab5.dto.dto.professional.ProfessionalNoteDto
import wa.lab5.dto.professional.ProfessionalDto
import wa.lab5.dto.professional.SkillDto

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ProfessionalControllerIntegrationTest: CrmExtIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val expectedProfessionalDto = ProfessionalDto(id = 1L, name = "Simone", surname = "Geraci", location = "ITALY", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "EMPLOYED")
    private val expectedProfessionalDto2 = ProfessionalDto(id = 1L, name = "Carlo", surname = "Rossi", location = "SPAIN", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "UNEMPLOYED")
    private val expectedProfessionalDto3 = ProfessionalDto(id = 1L, name = "Mario", surname = "Bianchi", location = "FRANCE", category = "PROFESSIONAL", ssnCode = "TESTSSN", dailyRate = 1.0, state = "EMPLOYED")
    private val expectedSkillDto = SkillDto(id = 1L, skill = "PYTHON")
    private val expectedSkillDto2 = SkillDto(id = 1L, skill = "JAVA")
    private val expectedNoteDto = ProfessionalNoteDto(id = 0L, professionalId = 0L, title = "NOTE TITLE", description = "NOTE DESCRIPTION")


    @Test
    fun professionalNotFound() {
        val professionalId = 100L
        mockMvc.get("/API/professionals/$professionalId")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            }
    }

    @BeforeAll
    @Test
    fun createThreeProfessionalsAndRetrieveIt() {
        mockMvc.post("/API/professionals") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedProfessionalDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedProfessionalDto.name) }
                jsonPath("$.surname") { value(expectedProfessionalDto.surname) }
                jsonPath("$.location") { value(expectedProfessionalDto.location) }
                jsonPath("$.category") { value(expectedProfessionalDto.category) }
                jsonPath("$.ssnCode") { value(expectedProfessionalDto.ssnCode) }
                jsonPath("$.dailyRate") { value(expectedProfessionalDto.dailyRate) }
                jsonPath("$.state") { value(expectedProfessionalDto.state) }
            }
        }

        mockMvc.post("/API/professionals") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedProfessionalDto2)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedProfessionalDto2.name) }
                jsonPath("$.surname") { value(expectedProfessionalDto2.surname) }
                jsonPath("$.location") { value(expectedProfessionalDto2.location) }
                jsonPath("$.category") { value(expectedProfessionalDto2.category) }
                jsonPath("$.ssnCode") { value(expectedProfessionalDto2.ssnCode) }
                jsonPath("$.dailyRate") { value(expectedProfessionalDto2.dailyRate) }
                jsonPath("$.state") {value(expectedProfessionalDto2.state) }
            }
        }

        mockMvc.post("/API/professionals") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedProfessionalDto3)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedProfessionalDto3.name) }
                jsonPath("$.surname") { value(expectedProfessionalDto3.surname) }
                jsonPath("$.location") { value(expectedProfessionalDto3.location) }
                jsonPath("$.category") { value(expectedProfessionalDto3.category) }
                jsonPath("$.ssnCode") { value(expectedProfessionalDto3.ssnCode) }
                jsonPath("$.dailyRate") { value(expectedProfessionalDto3.dailyRate) }
                jsonPath("$.state") { value(expectedProfessionalDto3.state) }
            }
        }
    }

    @Test
    fun getProfessionals() {
        mockMvc.get("/API/professionals")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(3) }
                    jsonPath("$[0].name") { value(expectedProfessionalDto.name) }
                    jsonPath("$[0].surname") { value(expectedProfessionalDto.surname ) }
                    jsonPath("$[0].location") { value(expectedProfessionalDto.location) }
                    jsonPath("$[0].category") { value(expectedProfessionalDto.category) }
                    jsonPath("$[0].ssnCode") { value(expectedProfessionalDto.ssnCode) }
                    jsonPath("$[0].dailyRate") { value(expectedProfessionalDto.dailyRate) }
                    jsonPath("$[0].state") { value(expectedProfessionalDto.state) }
                    jsonPath("$[1].name") { value(expectedProfessionalDto2.name) }
                    jsonPath("$[1].surname") { value(expectedProfessionalDto2.surname) }
                    jsonPath("$[1].location") { value(expectedProfessionalDto2.location) }
                    jsonPath("$[1].category") { value(expectedProfessionalDto2.category) }
                    jsonPath("$[1].ssnCode") { value(expectedProfessionalDto2.ssnCode) }
                    jsonPath("$[1].dailyRate") { value(expectedProfessionalDto2.dailyRate) }
                    jsonPath("$[1].state") { value(expectedProfessionalDto2.state) }
                    jsonPath("$[2].name") { value(expectedProfessionalDto3.name) }
                    jsonPath("$[2].surname") { value(expectedProfessionalDto3.surname) }
                    jsonPath("$[2].location") { value(expectedProfessionalDto3.location) }
                    jsonPath("$[2].category") { value(expectedProfessionalDto3.category) }
                    jsonPath("$[2].ssnCode") { value(expectedProfessionalDto3.ssnCode) }
                    jsonPath("$[2].dailyRate") { value(expectedProfessionalDto3.dailyRate) }
                    jsonPath("$[2].state") { value(expectedProfessionalDto3.state) }
                }
            }
    }

    @Test
    fun getAllProfessionalPaginated() {
        mockMvc.get("/API/professionals?page=0&limit=2")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(2) }
                    jsonPath("$[0].name") { value(expectedProfessionalDto.name) }
                    jsonPath("$[0].surname") { value(expectedProfessionalDto.surname) }
                    jsonPath("$[0].location") { value(expectedProfessionalDto.location) }
                    jsonPath("$[0].category") { value(expectedProfessionalDto.category) }
                    jsonPath("$[0].ssnCode") { value(expectedProfessionalDto.ssnCode) }
                    jsonPath("$[0].dailyRate") { value(expectedProfessionalDto.dailyRate) }
                    jsonPath("$[0].state") { value(expectedProfessionalDto.state) }
                    jsonPath("$[1].name") { value(expectedProfessionalDto2.name) }
                    jsonPath("$[1].surname") { value(expectedProfessionalDto2.surname) }
                    jsonPath("$[1].location") { value(expectedProfessionalDto2.location) }
                    jsonPath("$[1].category") { value(expectedProfessionalDto2.category) }
                    jsonPath("$[1].ssnCode") { value(expectedProfessionalDto2.ssnCode) }
                    jsonPath("$[1].dailyRate") { value(expectedProfessionalDto2.dailyRate) }
                    jsonPath("$[1].state") { value(expectedProfessionalDto2.state) }
                }
            }
    }

    @Test
    fun getAllProfessionalPaginatedAndFiltered() {
        mockMvc.get("/API/professionals?page=0&size=2&employmentState=EMPLOYED")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(2) }
                    jsonPath("$[0].name") { value(expectedProfessionalDto.name) }
                    jsonPath("$[0].surname") { value(expectedProfessionalDto.surname) }
                    jsonPath("$[0].location") { value(expectedProfessionalDto.location) }
                    jsonPath("$[0].category") { value(expectedProfessionalDto.category) }
                    jsonPath("$[0].ssnCode") { value(expectedProfessionalDto.ssnCode) }
                    jsonPath("$[0].dailyRate") { value(expectedProfessionalDto.dailyRate) }
                    jsonPath("$[0].state") { value(expectedProfessionalDto.state) }
                    jsonPath("$[1].name") { value(expectedProfessionalDto3.name) }
                    jsonPath("$[1].surname") { value(expectedProfessionalDto3.surname) }
                    jsonPath("$[1].location") { value(expectedProfessionalDto3.location) }
                    jsonPath("$[1].category") { value(expectedProfessionalDto3.category) }
                    jsonPath("$[1].ssnCode") { value(expectedProfessionalDto3.ssnCode) }
                    jsonPath("$[1].dailyRate") { value(expectedProfessionalDto3.dailyRate) }
                    jsonPath("$[1].state") { value(expectedProfessionalDto3.state) }
                }
            }
    }

    @Test
    fun addSkillToProfessionalAndFilterIt() {
        mockMvc.post("/API/professionals/3/skills") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedSkillDto2)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.skill") { value(expectedSkillDto2.skill) }
            }
        }

        mockMvc.get("/API/professionals?page=0&size=2&skills=JAVA")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].name") { value(expectedProfessionalDto3.name) }
                    jsonPath("$[0].surname") { value(expectedProfessionalDto3.surname) }
                    jsonPath("$[0].location") { value(expectedProfessionalDto3.location) }
                    jsonPath("$[0].category") { value(expectedProfessionalDto3.category) }
                    jsonPath("$[0].ssnCode") { value(expectedProfessionalDto3.ssnCode) }
                    jsonPath("$[0].dailyRate") { value(expectedProfessionalDto3.dailyRate) }
                    jsonPath("$[0].state") { value(expectedProfessionalDto3.state) }
                }
            }
    }

    @Test
    fun getAllProfessionalPaginatedAndFilteredInvalidState() {
        mockMvc.get("/API/professionals?page=0&size=2&employmentState=EMPLOYEDINVALID")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            }
    }

    @Test
    fun addSkillToProfessionalAndRetrieve() {
        mockMvc.post("/API/professionals/1/skills") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedSkillDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.skill") { value(expectedSkillDto.skill) }
            }
        }

        mockMvc.get("/API/professionals/1/skills")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].skill") { value(expectedSkillDto.skill) }
                }
            }
    }

    @Test
    fun addSkillToProfessionalDeleteAndRetrieve() {
        mockMvc.post("/API/professionals/1/skills") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedSkillDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.skill") { value(expectedSkillDto.skill) }
            }
        }

        mockMvc.delete("/API/professionals/1/skills/1")
            .andExpect {
                status { isOk() }
            }

        mockMvc.get("/API/professionals/1/skills")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$.length()"){ value(0) }
                }
            }
    }

    @Test
    fun addNoteToProfessionalAndRetrieve() {
        mockMvc.post("/API/professionals/1/notes") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedNoteDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.title") { value(expectedNoteDto.title) }
                jsonPath("$.description") { value(expectedNoteDto.description) }
            }
        }

        mockMvc.get("/API/professionals/1/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].title") { value(expectedNoteDto.title) }
                    jsonPath("$[0].description") { value(expectedNoteDto.description) }
                }
            }
    }

    @Test
    fun updateProfessionalNoteAndRetrieve() {
        val updatedNoteDto = ProfessionalNoteDto(id = 2L, professionalId = 1L, title = "UPDATED TITLE", description = "UPDATED DESCRIPTION")
        mockMvc.put("/API/professionals/1/notes/1") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(updatedNoteDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.title") { value(updatedNoteDto.title) }
                jsonPath("$.description") { value(updatedNoteDto.description) }
            }
        }

        mockMvc.get("/API/professionals/1/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].title") { value(updatedNoteDto.title) }
                    jsonPath("$[0].description") { value(updatedNoteDto.description) }
                }
            }
    }

    @Test
    fun deleteProfessional() {
        var newProfessionalId: Long? = null

        mockMvc.post("/API/professionals") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedProfessionalDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedProfessionalDto.name) }
                jsonPath("$.surname") { value(expectedProfessionalDto.surname) }
                jsonPath("$.location") { value(expectedProfessionalDto.location) }
                jsonPath("$.category") { value(expectedProfessionalDto.category) }
                jsonPath("$.ssnCode") { value(expectedProfessionalDto.ssnCode) }
                jsonPath("$.dailyRate") { value(expectedProfessionalDto.dailyRate) }
                jsonPath("$.state") { value(expectedProfessionalDto.state) }
            }
        }.andReturn().response.contentAsString.let {
            newProfessionalId = ObjectMapper().readTree(it).get("id").asLong()
        }

        mockMvc.delete("/API/professionals/$newProfessionalId")
            .andExpect {
                status { isOk() }
            }
        mockMvc.get("/API/professionals/$newProfessionalId")
            .andExpect {
                status { isNotFound() }
            }
        mockMvc.get("/API/professionals/$newProfessionalId/skills")
            .andExpect {
                status { isNotFound() }
            }
        mockMvc.get("/API/professionals/$newProfessionalId/notes")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun addNoteAndDeleteProfessional() {
        var newNotelId: Long? = null

        mockMvc.post("/API/professionals/2/notes") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedNoteDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.title") { value(expectedNoteDto.title) }
                jsonPath("$.description") { value(expectedNoteDto.description) }
            }
        }.andReturn().response.contentAsString.let {
            newNotelId = ObjectMapper().readTree(it).get("id").asLong()
        }

        mockMvc.delete("/API/professionals/2/notes/$newNotelId")
            .andExpect {
                status { isOk() }
            }
        mockMvc.get("/API/professionals/2/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$.length()") { value(0) }
                }
            }
    }
}
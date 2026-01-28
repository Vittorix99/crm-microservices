package crmext.customer

import crmext.CrmExtIntegrationTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import wa.lab5.dto.customer.CustomerDto
import wa.lab5.dto.customer.CustomerNoteDto


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest : CrmExtIntegrationTest() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val expectedCustomerDto = CustomerDto(
        id = 1L,
        name = "Carlo",
        surname = "Rossi",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedCustomerDto2 = CustomerDto(
        id = 1L,
        name = "Giovanni",
        surname = "Bianchi",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedNoteDto = CustomerNoteDto(id = 0L, customerId = 0L, description = "NOTE DESCRIPTION")


    @Test
    fun customerNotFound() {
        val customerId = 100L
        mockMvc.get("/API/customers/$customerId")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            }
    }

    @BeforeAll
    @Test
    fun createThreeCustomersAndRetrieveIt() {
        mockMvc.post("/API/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedCustomerDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedCustomerDto.name) }
                jsonPath("$.surname") { value(expectedCustomerDto.surname) }
                jsonPath("$.ssnCode") { value(expectedCustomerDto.ssnCode) }
                jsonPath("$.category") { value(expectedCustomerDto.category) }
            }
        }

        mockMvc.post("/API/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedCustomerDto2)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedCustomerDto2.name) }
                jsonPath("$.surname") { value(expectedCustomerDto2.surname) }
                jsonPath("$.ssnCode") { value(expectedCustomerDto2.ssnCode) }
                jsonPath("$.category") { value(expectedCustomerDto2.category) }
            }
        }

        mockMvc.post("/API/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedCustomerDto2)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedCustomerDto2.name) }
                jsonPath("$.surname") { value(expectedCustomerDto2.surname) }
                jsonPath("$.ssnCode") { value(expectedCustomerDto2.ssnCode) }
                jsonPath("$.category") { value(expectedCustomerDto2.category) }
            }
        }
    }

    @Test
    fun getAllCustomerPaginated() {
        mockMvc.get("/API/customers?page=0&limit=2")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(2) }
                    jsonPath("$[0].name") { value(expectedCustomerDto.name) }
                    jsonPath("$[0].surname") { value(expectedCustomerDto.surname) }
                    jsonPath("$[0].ssnCode") { value(expectedCustomerDto.ssnCode) }
                    jsonPath("$[0].category") { value(expectedCustomerDto.category) }
                }
            }
    }

    @Test
    fun addNoteToCustomerAndRetrieve() {
        mockMvc.post("/API/customers/1/notes") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedNoteDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.description") { value(expectedNoteDto.description) }
            }
        }

        mockMvc.get("/API/customers/1/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].description") { value(expectedNoteDto.description) }
                }
            }
    }
    @Test
    fun updateCustomerNoteAndRetrieve() {
       /* mockMvc.post("/API/customers/1/notes") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedNoteDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.description") { value(expectedNoteDto.description) }
            }
        }*/

        val updatedNoteDto = CustomerNoteDto(id = 0L, customerId = 1L, description = "UPDATED DESCRIPTION")
        mockMvc.put("/API/customers/1/notes/1") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(updatedNoteDto)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.description") { value(updatedNoteDto.description) }
            }
        }

        mockMvc.get("/API/customers/1/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$") { isArray() }
                    jsonPath("$.length()") { value(1) }
                    jsonPath("$[0].description") { value(updatedNoteDto.description) }
                }
            }
    }

    @Test
    fun deleteCustomer() {
        var newCustomerId: Long? = null

        mockMvc.post("/API/customers") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedCustomerDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.name") { value(expectedCustomerDto.name) }
                jsonPath("$.surname") { value(expectedCustomerDto.surname) }
                jsonPath("$.ssnCode") { value(expectedCustomerDto.ssnCode) }
                jsonPath("$.category") { value(expectedCustomerDto.category) }
            }
        }.andReturn().response.contentAsString.let {
            newCustomerId = ObjectMapper().readTree(it).get("id").asLong()
        }

        mockMvc.delete("/API/customers/$newCustomerId")
            .andExpect {
                status { isOk() }
            }
        mockMvc.get("/API/customers/$newCustomerId")
            .andExpect {
                status { isNotFound() }
            }
        mockMvc.get("/API/customers/$newCustomerId/notes")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun addNoteAndDeleteCustomer() {
        var newNoteId: Long? = null

        mockMvc.post("/API/customers/2/notes") {
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(expectedNoteDto)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                jsonPath("$.description") { value(expectedNoteDto.description) }
            }
        }.andReturn().response.contentAsString.let {
            newNoteId = ObjectMapper().readTree(it).get("id").asLong()
        }

        mockMvc.delete("/API/customers/2/notes/$newNoteId")
            .andExpect {
                status { isOk() }
            }
        mockMvc.get("/API/customers/2/notes")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    jsonPath("$.length()") { value(0) }
                }
            }
    }
















}

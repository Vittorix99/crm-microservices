package crmext.customer;

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
import wa.lab5.controller.CustomerController
import wa.lab5.dto.customer.CustomerDto
import wa.lab5.dto.customer.CustomerNoteDto
import wa.lab5.exceptions.customer.CustomerNotFoundException
import wa.lab5.model.customer.Customer
import wa.lab5.model.customer.CustomerNote
import wa.lab5.services.customer.CustomerService

@WebMvcTest(controllers = arrayOf(CustomerController::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class CustomerControllerUnitTest(@Autowired val mockMvc:MockMvc) {

    @MockkBean
    lateinit var customerService: CustomerService


    private val expectedCustomerDto = CustomerDto(
        id = 1L,
        name = "Simone",
        surname = "Geraci",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedCustomer = Customer(expectedCustomerDto)

    private val expectedCustomerDto2 = CustomerDto(
        id = 1L,
        name = "Carlo",
        surname = "Rossi",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedCustomer2 = Customer(expectedCustomerDto2)

    private val expectedCustomerNoteDto = CustomerNoteDto(
        id = 0L,
        customerId = 0L,
        description = "NOTE TITLE",

    )

    private val expectedCustomerNote = CustomerNote(expectedCustomerNoteDto)

    private val expectedCustomerNoteDto2 = CustomerNoteDto(
        id = 1L,
        customerId = 1L,

        description = "NOTE DESCRIPTION 2"
    )

    private val expectedCustomerNote2 = CustomerNote(expectedCustomerNoteDto2)

    @Test
    fun getCustomer() {
        val profId = 0L

        every { customerService.getCustomerById(any()) } returns expectedCustomerDto

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers/$profId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedCustomerDto)))
    }

    @Test
    fun getCustomerNotFound() {
        val customerId = 0L

        every { customerService.getCustomerById(any()) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers/$customerId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun createCustomer() {
        every { customerService.createCustomer(any()) } returns expectedCustomerDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedCustomerDto)))
    }




    @Test
    fun updateCustomer() {
        every { customerService.updateCustomer(0L, expectedCustomerDto) } returns expectedCustomerDto

        mockMvc.perform(MockMvcRequestBuilders.put("/API/customers/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedCustomerDto)))
    }

    @Test
    fun updateCustomerNotFound() {
        val customerId = 0L

        every { customerService.updateCustomer(customerId, expectedCustomerDto) } throws CustomerNotFoundException(customerId.toString())

        mockMvc.perform(MockMvcRequestBuilders.put("/API/customers/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun deleteCustomer() {
        every { customerService.deleteCustomer(0L) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/customers/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteCustomerNotFound() {
        every { customerService.deleteCustomer(0L) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/customers/0"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addNoteToCustomer() {
        every { customerService.addNoteToCustomer(0L, expectedCustomerNoteDto) } returns expectedCustomerNoteDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/customers/0/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun addNoteToCustomerNotFound() {
        every { customerService.addNoteToCustomer(0L, expectedCustomerNoteDto) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.post("/API/customers/0/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getCustomerNotes() {
        every { customerService.getCustomerNotes(0L) } returns listOf(expectedCustomerNoteDto, expectedCustomerNoteDto2)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf(expectedCustomerNoteDto, expectedCustomerNoteDto2))))
    }

    @Test
    fun getCustomerNotesNotFound() {
        every { customerService.getCustomerNotes(0L) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getCustomerNotesEmpty() {
        every { customerService.getCustomerNotes(0L) } returns listOf()

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers/0/notes"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf<CustomerNoteDto>())))
    }

    @Test
    fun removeNoteFromCustomer() {
        every { customerService.deleteNoteFromCustomer(0L, 0L) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/customers/0/notes/0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun removeNoteFromCustomerNotFound() {
        every { customerService.deleteNoteFromCustomer(0L, 0L) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/customers/0/notes/0"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateCustomerNote() {
        every { customerService.updateCustomerNote(0L, 0L, expectedCustomerNoteDto) } returns expectedCustomerNoteDto

        mockMvc.perform(MockMvcRequestBuilders.put("/API/customers/0/notes/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun updateCustomerNoteCustomerNotFound() {
        every { customerService.updateCustomerNote(0L, 0L, expectedCustomerNoteDto) } throws CustomerNotFoundException("0")

        mockMvc.perform(MockMvcRequestBuilders.put("/API/customers/0/notes/0")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedCustomerNoteDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }




    @Test
    fun getAllCustomers() {
        every { customerService.getAllCustomers(any(), any(), ) } returns listOf(expectedCustomerDto, expectedCustomerDto2)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf(expectedCustomerDto, expectedCustomerDto2))))
    }

    @Test
    fun getAllCustomersEmpty() {
        every { customerService.getAllCustomers(any(), any()) } returns listOf()

        mockMvc.perform(MockMvcRequestBuilders.get("/API/customers"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(listOf<CustomerDto>())))
    }


}

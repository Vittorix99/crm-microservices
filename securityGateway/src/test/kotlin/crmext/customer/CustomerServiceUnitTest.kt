package crmext.customer

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import wa.lab5.dto.customer.CustomerDto
import wa.lab5.dto.customer.CustomerNoteDto
import wa.lab5.exceptions.customer.CustomerNotFoundException
import wa.lab5.exceptions.customer.CustomerNoteNotFoundException
import wa.lab5.model.contact.ContactCategory
import wa.lab5.model.customer.Customer
import wa.lab5.model.customer.CustomerNote
import wa.lab5.repository.customer.CustomerNoteRepository
import wa.lab5.repository.customer.CustomerRepository
import wa.lab5.services.customer.CustomerService

class CustomerServiceUnitTest {
    private val customerRepository = mockk<CustomerRepository>()
    private val customerNoteRepository = mockk<CustomerNoteRepository>()



    private val customerService: CustomerService = CustomerService(
        customerRepository,
        customerNoteRepository,

    )




    @Test
    fun testCreateCustomerSuccessfully() {
        // Prepare
        val expectedCustomerDto = CustomerDto(id = 1L, name = "John", surname = "Doe", ssnCode = "SSN12345", category = "CUSTOMER")
        val expectedCustomer = Customer(1L, "John", "Doe", "SSN12345", ContactCategory.CUSTOMER, mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

        // Mock the save operation to return the expectedCustomer when any Customer object is passed
        every { customerRepository.save(any()) } returns expectedCustomer

        // Execute the creation method, which should use the mocked repository
        val customer = customerService.createCustomer(expectedCustomerDto)

        // Assertions to verify that the returned Customer matches the expected DTO values
        Assertions.assertEquals(customer.name, expectedCustomerDto.name, "Check name")
        Assertions.assertEquals(customer.surname, expectedCustomerDto.surname, "Check surname")
        Assertions.assertEquals(customer.ssnCode, expectedCustomerDto.ssnCode, "Check SSN code")
        Assertions.assertEquals(customer.category, expectedCustomerDto.category, "Check category")
    }
    @Test
    fun getCustomerById() {
        // Setup a test ID and expected result
        val id = 0L
        val expectedCustomer = Customer(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN12345",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val expectedCustomerDto = CustomerDto(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN12345",
            category = "CUSTOMER"
        )

        // Mock the repository call to return the expected customer when any Long is passed
        every { customerRepository.findCustomerById(any()) } returns expectedCustomer

        // Execute the method under test
        val customer = customerService.getCustomerById(id)

        // Assertions to check if the returned customer matches the expected DTO values
        Assertions.assertEquals(customer.name, expectedCustomerDto.name, "Check name")
        Assertions.assertEquals(customer.surname, expectedCustomerDto.surname, "Check surname")
        Assertions.assertEquals(customer.ssnCode, expectedCustomerDto.ssnCode, "Check SSN code")
        Assertions.assertEquals(customer.category, expectedCustomerDto.category, "Check category")
    }
@Test
    fun getCustomerByIdNotFound() {
        // Setup a test ID that will be used to trigger the exception
        val id = 0L

        // Mock the repository call to throw CustomerNotFoundException when any Long is passed
        every { customerRepository.findCustomerById(any()) } throws CustomerNotFoundException(id.toString())

        // Assert that the exception is thrown when getCustomerById is called with the test ID
        assertThrows<CustomerNotFoundException> {
            customerService.getCustomerById(id)
        }
    }
    @Test
    fun updateCustomer() {
        // Arrange
        val id = 0L
        val initialCustomer = Customer(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN123",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val updatedCustomer = Customer(
            id = id,
            name = "Jane",
            surname = "Doe",
            ssnCode = "SSN456",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val updatedCustomerDto = CustomerDto(
            id = id,
            name = "Jane",
            surname = "Doe",
            ssnCode = "SSN456",
            category = "CUSTOMER"
        )

        // Mock the repository responses
        every { customerRepository.findCustomerById(any()) } returns initialCustomer
        every { customerRepository.save(any()) } returns updatedCustomer

        // Act
        val resultDto = customerService.updateCustomer(id, updatedCustomerDto)

        // Assert
        Assertions.assertEquals(updatedCustomerDto.name, resultDto.name, "Check name")
        Assertions.assertEquals(updatedCustomerDto.surname, resultDto.surname, "Check surname")
        Assertions.assertEquals(updatedCustomerDto.ssnCode, resultDto.ssnCode, "Check SSN code")
        Assertions.assertEquals(updatedCustomerDto.category, resultDto.category, "Check category")
    }

    @Test
    fun updateCustomerNotFound() {
        // Arrange
        val id = 0L
        val updatedCustomerDto = CustomerDto(
            id = id,
            name = "Jane",
            surname = "Doe",
            ssnCode = "SSN456",
            category = "CUSTOMER"
        )

        // Mock the repository to throw CustomerNotFoundException when findCustomerById is called
        every { customerRepository.findCustomerById(any()) } throws CustomerNotFoundException(id.toString())

        // Act & Assert
        assertThrows<CustomerNotFoundException> {
            customerService.updateCustomer(id, updatedCustomerDto)
        }
    }
    @Test
    fun deleteCustomer() {
        val id = 0L
        val expectedCustomer = Customer(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN12345",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )

        every { customerRepository.findCustomerById(any()) } returns expectedCustomer
        every { customerRepository.delete(any()) } returns Unit

        assert(customerService.deleteCustomer(0L).equals(Unit))
    }

    @Test
    fun deleteCustomerNotFound() {
        every { customerRepository.findCustomerById(any()) } throws CustomerNotFoundException("0")
        assertThrows<CustomerNotFoundException> {
            customerService.deleteCustomer(0L)
        }
    }
    @Test
    fun addNoteToCustomer() {
        // Arrange
        val id = 0L
        val expectedCustomer = Customer(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN12345",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val expectedNoteDto = CustomerNoteDto(
            description = "Follow up on meeting.",
            customerId = id
        )
        val expectedNote = CustomerNote(
            description = "Follow up on meeting.",
            customer = expectedCustomer
        )
        val expectedCustomerAfter = Customer(
            id = id,
            name = "John",
            surname = "Doe",
            ssnCode = "SSN12345",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        expectedCustomerAfter.customerNotes.add(expectedNote)

        // Mock the repository methods
        every { customerRepository.findCustomerById(id) } returns expectedCustomer
        every { customerNoteRepository.save(any()) } returns expectedNote
        every { customerRepository.save(any()) } returns expectedCustomerAfter



        // Act
        val note = customerService.addNoteToCustomer(id, customerNoteDto = expectedNoteDto)

        // Assert
        Assertions.assertEquals(expectedNoteDto.description, note.description)
        Assertions.assertEquals(note.id , expectedCustomerAfter.customerNotes[0].id)
    }

    @Test
    fun addNoteToCustomerNotFound() {
        val id = 0L

        val expectedNoteDto = CustomerNoteDto(
            description = "Follow up on meeting.",
            customerId = id
        )
        every { customerRepository.findCustomerById(any()) } throws CustomerNotFoundException("0")

        assertThrows<CustomerNotFoundException> {
            customerService.addNoteToCustomer(0L, expectedNoteDto)
        }
    }

    @Test
    fun getCustomerNotes() {
        val customerId = 0L
        val expectedCustomer = Customer(
            id = customerId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456789",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val expectedNote = CustomerNote(
            id = 1L,
            description = "Meeting follow-up required.",
            customer = expectedCustomer
        )
        val expectedNoteDto = CustomerNoteDto(
            id = 1L,
            description = "Meeting follow-up required.",
            customerId = customerId
        )

        every { customerRepository.findCustomerById(customerId) } returns expectedCustomer
        every { customerNoteRepository.findCustomersNoteByCustomerId(customerId) } returns listOf(expectedNote)

        val customerNotes = customerService.getCustomerNotes(customerId)

        Assertions.assertEquals(listOf(expectedNoteDto.description), customerNotes.map { it.description })
    }
    @Test
    fun getCustomerNotesEmpty() {
        val customerId = 0L
        val expectedCustomer = Customer(
            id = customerId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456789",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )

        every { customerRepository.findCustomerById(any()) } returns expectedCustomer
        every { customerNoteRepository.findCustomersNoteByCustomerId(customerId) } returns listOf()

        val customerNotes = customerService.getCustomerNotes(customerId)

        assert(customerNotes.isEmpty())
    }
    @Test
    fun getCustomerNotesNotFound() {
        val customerId = 0L

        every { customerRepository.findCustomerById(customerId) } throws CustomerNotFoundException(customerId.toString())

        assertThrows<CustomerNotFoundException> {
            customerService.getCustomerNotes(customerId)
        }
    }
    @Test
    fun deleteNoteFromCustomer() {
        val customerId = 0L
        val noteId = 1L
        val expectedCustomer = Customer(
            id = customerId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456789",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val expectedNote = CustomerNote(
            id = noteId,
            description = "Meeting follow-up required.",
            customer = expectedCustomer
        )

        every { customerRepository.findCustomerById(customerId) } returns expectedCustomer
        every { customerNoteRepository.findCustomerNoteById(noteId) } returns expectedNote
        every { customerRepository.save(any()) } returns expectedCustomer


        every { customerNoteRepository.delete(expectedNote) } just Runs

        val result = customerService.deleteNoteFromCustomer(noteId, customerId)

        Assertions.assertEquals(Unit, result)

    }
    @Test
    fun deleteNoteFromCustomerNotFound() {
        val customerId = 0L
        val noteId = 1L

        // Setup: Mock the customer repository to return a valid customer
        val expectedCustomer = Customer(
            id = customerId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456789",
            category = ContactCategory.CUSTOMER,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        every { customerRepository.findCustomerById(customerId) } returns expectedCustomer

        // Setup: Mock the customer note repository to throw an exception when trying to find a specific note
        every { customerNoteRepository.findCustomerNoteById(noteId) } throws CustomerNoteNotFoundException("0")

        // Action and Assertion: Check that the correct exception is thrown
        assertThrows<CustomerNoteNotFoundException> {
            customerService.deleteNoteFromCustomer(noteId, customerId)
        }
    }

































}
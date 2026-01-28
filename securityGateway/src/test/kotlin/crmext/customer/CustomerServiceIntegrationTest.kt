package crmext.customer

import crmext.CrmExtIntegrationTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import wa.lab5.dto.customer.CustomerDto
import wa.lab5.dto.customer.CustomerNoteDto
import wa.lab5.exceptions.customer.CustomerNotFoundException
import wa.lab5.services.customer.CustomerService

class CustomerServiceIntegrationTest: CrmExtIntegrationTest() {

    @Autowired
    private lateinit var customerService: CustomerService

    private val expectedCustomerDto = CustomerDto(
        id = 1L,
        name = "Simone",
        surname = "Geraci",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedCustomerDto2 = CustomerDto(
        id = 1L,
        name = "Carlo",
        surname = "Rossi",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedCustomerDto3 = CustomerDto(
        id = 1L,
        name = "Giovanni",
        surname = "Bianchi",
        ssnCode = "TESTSSN",
        category = "CUSTOMER"
    )

    private val expectedNoteDto = CustomerNoteDto(id = 1L, description = "NOTE DESCRIPTION", customerId = 0L)
    private val expectedNoteDto2 = CustomerNoteDto(id = 2L, description = "NOTE DESCRIPTION1", customerId = 0L)

    @Test // create customer, add note, retrieve note and customer
    fun customerFlow1() {
        val customer = customerService.createCustomer(expectedCustomerDto)
        val note = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)

        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customer.equals(customerRetrieved))
        assert(note.customerId == customer.id!!)
        assert(notes.contains(note))
    }

    @Test // create customer, add note, remove note, retrieve note and customer
    fun customerFlow2() {
        val customer = customerService.createCustomer(expectedCustomerDto)
        val note = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)

        customerService.deleteNoteFromCustomer(note.id!!, customer.id!!)
        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customer.equals(customerRetrieved))
        assert(!notes.contains(note))
    }

    @Test
    fun customerFlow3() { // create customer, update it, retrieve it, add note, update note, retrieve note and customer
        val customer = customerService.createCustomer(expectedCustomerDto)
        val customerUpdated = customerService.updateCustomer(customer.id!!, expectedCustomerDto2)
        val note = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)

        val updatedNote = CustomerNoteDto(id = note.id!!, description = "UPDATED DESCRIPTION", customerId = note.customerId)
        val noteUpdated = customerService.updateCustomerNote(note.id!!, customer.id!!, updatedNote)

        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customerUpdated.equals(customerRetrieved))
        assert(noteUpdated.customerId == customer.id!!)
        assert(notes.contains(noteUpdated))
    }

    @Test
    fun customerFlow4() { // create customer, add note, delete customer, retrieve all
        val customer = customerService.createCustomer(expectedCustomerDto)
        val note =  customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)
        customerService.deleteNoteFromCustomer(note.id!!, customerId = customer.id!!)

        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customer.equals(customerRetrieved))
        assert(!notes.contains(note))
    }

    @Test
    fun customerFlow5() { // create three customers and retrieve them all
        val customer = customerService.createCustomer(expectedCustomerDto)
        val customer2 = customerService.createCustomer(expectedCustomerDto2)
        val customer3 = customerService.createCustomer(expectedCustomerDto3)

        val customers = customerService.getAllCustomers(0, 10)

        assert(customers.containsAll(listOf(customer, customer2, customer3)))
    }

    @Test
    fun customerFlow6() { // create professional, add note, update note, retrieve note and professional
        val customer = customerService.createCustomer(expectedCustomerDto)
        val note = customerService.addNoteToCustomer( customer.id!!, expectedNoteDto)

        val updatedNote = CustomerNoteDto(id = note.id!!, customerId = note.customerId, description = "UPDATED DESCRIPTION")
        val noteUpdated = customerService.updateCustomerNote(note.id!!, customer.id!!, updatedNote)

        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customer.equals(customerRetrieved))
        assert(noteUpdated.customerId == customer.id!!)
        assert(notes.contains(noteUpdated))
    }

    @Test
    fun customerFlow7() { // create customer, add 2 notes, delete one, retrieve notes and customer
        val customer = customerService.createCustomer(expectedCustomerDto)
        val note1 = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)
        val note2 = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto2)

        customerService.deleteNoteFromCustomer(note1.id!!, customer.id!!)
        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customer.equals(customerRetrieved))
        assert(notes.contains(note2))
        assert(!notes.contains(note1))
    }

    @Test
    fun customerFlow8() { // create customer, update it, retrieve it, add two notes, delete one and retrieve all
        val customer = customerService.createCustomer(expectedCustomerDto)
        val customerUpdated = customerService.updateCustomer(customer.id!!, expectedCustomerDto2)
        val note1 = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)
        val note2 = customerService.addNoteToCustomer(customer.id!!, expectedNoteDto2)

        customerService.deleteNoteFromCustomer(note1.id!!, customer.id!!)
        val notes = customerService.getCustomerNotes(customer.id!!)
        val customerRetrieved = customerService.getCustomerById(customer.id!!)

        assert(customerUpdated.equals(customerRetrieved))
        assert(notes.contains(note2))
        assert(!notes.contains(note1))
    }

    @Test
    fun customerFlow9() { // create customer, update it, retrieve it, add two notes, assert correct creation, delete customer and retrieve all
        val customer = customerService.createCustomer(expectedCustomerDto)
        customerService.addNoteToCustomer(customer.id!!, expectedNoteDto)
        customerService.addNoteToCustomer(customer.id!!, expectedNoteDto2)

        customerService.deleteCustomer(customer.id!!)

        assertThrows<CustomerNotFoundException> {
            val notes = customerService.getCustomerNotes(customer.id!!)
            assert(notes.isEmpty())
            customerService.getCustomerById(customer.id!!)
        }
    }
}

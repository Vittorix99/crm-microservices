package org.example.walab4.services.customer

import org.example.walab4.dto.customer.CustomerDto
import org.example.walab4.dto.customer.CustomerNoteDto
import org.example.walab4.dto.customer.toDto
import org.example.walab4.dto.jobOffer.JobOfferDTO
import org.example.walab4.dto.jobOffer.toDto
import org.example.walab4.exceptions.customer.CustomerNotFoundException
import org.example.walab4.exceptions.customer.CustomerNoteNotFoundException
import org.example.walab4.model.contact.ContactCategory
import org.example.walab4.model.customer.Customer
import org.example.walab4.model.customer.CustomerNote
import org.example.walab4.repository.customer.CustomerNoteRepository
import org.example.walab4.repository.customer.CustomerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerNoteRepository: CustomerNoteRepository
) : ICustomerService {

    fun createCustomer(dto: CustomerDto): CustomerDto {
        val newCustomer = Customer(dto)
        return customerRepository.save(newCustomer).toDto()
    }

    fun getCustomerById(customerId: Long): CustomerDto {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())
        return customer.toDto()
    }

    fun getAllCustomers(
        page: Int,
        limit: Int,
        name: String?,
        surname: String?,
        ssnCode: String?,
        email: String?,
        address: String?,
        telephone: String?
    ): List<CustomerDto> {
        val paging = PageRequest.of(page, limit)
        var customers = customerRepository.findAll(paging).content

        customers = name?.let { customers.filter { it.name.equals(name, ignoreCase = true) } } ?: customers
        customers = surname?.let { customers.filter { it.surname.equals(surname, ignoreCase = true) } } ?: customers
        customers = ssnCode?.let { customers.filter { it.ssnCode.equals(ssnCode, ignoreCase = true) } } ?: customers

        /* val emailEntity = email?.let { emailRepository.findEmailByEmail(email) }
        customers = emailEntity?.let { customers.filter { it.emails?.contains(emailEntity) == true } } ?: customers

        val addressEntity = address?.let { addressRepository.findAddressByName(address) }
        customers = addressEntity?.let { customers.filter { it.addresses?.contains(addressEntity) == true } } ?: customers

        val telephoneEntity = telephone?.let { telephoneRepository.findTelephoneByNumber(telephone) }
        customers = telephoneEntity?.let { customers.filter { it.telephoneNumbers?.contains(telephoneEntity) == true } } ?: customers*/

        return customers.map { it.toDto() }
    }





    fun updateCustomer(customerId: Long, dto: CustomerDto): CustomerDto {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())

        customer.apply {
            this.name = dto.name
            this.surname = dto.surname
            this.ssnCode = dto.ssnCode
            this.category = ContactCategory.valueOf(dto.category)


        }

        return customerRepository.save(customer).toDto()
    }

    fun deleteCustomer(customerId: Long) {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())

        customerRepository.delete(customer)
    }


    fun addNoteToCustomer(customerId: Long, customerNoteDto: CustomerNoteDto): CustomerNoteDto {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())
        val note = CustomerNote(customerNoteDto)

        customer.addNote(note)
        customerRepository.save(customer)
        return note.toDto()
    }

    fun getCustomerNotes(customerId: Long): List<CustomerNoteDto> {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())
        val notes = customerNoteRepository.findCustomersNoteByCustomerId(customerId)

        return notes.map { it.toDto() }
    }

    fun deleteNoteFromCustomer(noteId: Long, customerId: Long) {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())
        val note = customerNoteRepository.findCustomerNoteById(noteId) ?: throw CustomerNotFoundException(noteId.toString())

        customer.customerNotes.remove(note)
        customerRepository.save(customer)

        customerNoteRepository.delete(note)
    }

    fun updateCustomerNote(noteId: Long, customerId:  Long, noteDto: CustomerNoteDto): CustomerNoteDto {
        val customer = customerRepository.findCustomerById(customerId) ?: throw CustomerNotFoundException(customerId.toString())
        val note = customerNoteRepository.findCustomerNoteById(noteId) ?: throw CustomerNoteNotFoundException(noteId.toString())

        note.apply {
            description = noteDto.description
            title = noteDto.title

        }

        return customerNoteRepository.save(note).toDto()
    }

    fun getCustomerJobOffers(customerId: Long): List<JobOfferDTO> {
        val customer = customerRepository.findById(customerId)
            .orElseThrow { CustomerNotFoundException(customerId.toString()) }

        return customer.jobOffers.map { it-> it.toDto() } // Mappa la lista di JobOffer in JobOfferDTO
    }

}
package org.example.walab4.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.dto.customer.CustomerDto
import org.example.walab4.dto.customer.CustomerNoteDto
import org.example.walab4.dto.jobOffer.JobOfferDTO
import org.example.walab4.exceptions.customer.InvalidCustomerPropsException
import org.example.walab4.services.customer.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/customers")
class CustomerController(val customerService: CustomerService) {

    private val LOGGER: Logger = LogManager.getLogger()
    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomer(
        @PathVariable customerId: Long
    ): CustomerDto {
        val customerDto = customerService.getCustomerById(customerId = customerId)
        LOGGER.info("[GET - API/professionals/$customerId] - SUCCESS - Customer ${customerId} retrieved correctly")
        return customerDto
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getCustomers(
        @RequestParam(value = "page", defaultValue = "0") @Min(0) page: Int,
        @RequestParam(value = "limit", defaultValue = "15") @Min(1) @Max(50) limit: Int,
        @RequestParam(value = "name", required = false) name: String?,
        @RequestParam(value = "surname", required = false) surname: String?,
        @RequestParam(value = "ssnCode", required = false) ssnCode: String?,
        @RequestParam(value = "email", required = false) email: String?,
        @RequestParam(value = "address", required = false) address: String?,
        @RequestParam(value = "telephone", required = false) telephone: String?
    ): List<CustomerDto> {
        val customerDtos = customerService.getAllCustomers(page, limit, name, surname, ssnCode, email, address, telephone)
        LOGGER.info("[GET - API/customers] - SUCCESS - Customers retrieved correctly")
        return customerDtos
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody customerDto: CustomerDto): CustomerDto {
        return try {
            customerService.createCustomer(customerDto)
        } catch (iae: IllegalArgumentException) {
            throw InvalidCustomerPropsException()
        }.also {
            LOGGER.info("[POST - API/customers] - SUCCESS - Customer ${it.id} created correctly")
        }
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCustomer(
        @PathVariable customerId: Long,
        @RequestBody customerDto: CustomerDto
    ): CustomerDto {
        return try {
            customerService.updateCustomer(customerId, customerDto)
        } catch (iae: IllegalArgumentException) {
            throw InvalidCustomerPropsException()
        }.also {
            LOGGER.info("[PUT - API/customers/$customerId] - SUCCESS - Customer $customerId updated correctly")
        }
    }


    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteCustomer(@PathVariable customerId: Long) {
        customerService.deleteCustomer(customerId)
        LOGGER.info("[DELETE - API/customers/$customerId] - SUCCESS - Customer $customerId deleted correctly")
    }


    @GetMapping("/{customerId}/joboffers")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerJobOffers(
        @PathVariable customerId: Long
    ): List<JobOfferDTO> {
        val joboffers = customerService.getCustomerJobOffers(customerId)
        LOGGER.info("[GET - API/customers/$customerId]/joboffers - SUCCESS - Contact $customerId Job Offers retrieved correctly")
        return joboffers
    }





    @PostMapping("/{customerId}/notes")
    @ResponseStatus(HttpStatus.CREATED)
    fun addNoteToCustomer(
        @PathVariable customerId: Long,
        @RequestBody note: CustomerNoteDto
    ): CustomerNoteDto {
        return customerService.addNoteToCustomer(customerId, note).also {
            LOGGER.info("[POST - API/customers/$customerId/notes] - SUCCESS - Note ${it.id} added correctly")
        }
    }

    @GetMapping("/{customerId}/notes")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerNotes(@PathVariable customerId: Long): List<CustomerNoteDto> {
        return customerService.getCustomerNotes(customerId).also {
            LOGGER.info("[GET - API/customers/$customerId/notes] - SUCCESS - Notes retrieved correctly")
        }
    }

    @DeleteMapping("/{customerId}/notes/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    fun removeNoteFromCustomer(
        @PathVariable customerId: Long,
        @PathVariable noteId: Long
    ) {
        customerService.deleteNoteFromCustomer(noteId, customerId)
        LOGGER.info("[DELETE - API/customers/$customerId/notes/$noteId] - SUCCESS - Note $noteId removed correctly from customer $customerId")
    }

    @PutMapping("/{customerId}/notes/{noteId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCustomerNote(
        @PathVariable customerId: Long,
        @PathVariable noteId: Long,
        @RequestBody noteDto: CustomerNoteDto
    ): CustomerNoteDto {
        return customerService.updateCustomerNote(noteId, customerId, noteDto).also {
            LOGGER.info("[PUT - API/customers/$customerId/notes/$noteId] - SUCCESS - Note $noteId updated correctly")
        }
    }


}
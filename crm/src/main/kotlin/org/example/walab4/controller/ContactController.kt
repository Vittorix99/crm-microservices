package org.example.walab4.controller

import org.example.walab4.services.contact.IContactService
import org.example.walab4.exceptions.contact.InvalidCategoryValueException
import org.example.walab4.model.contact.ContactCategory
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.dto.contact.AddressDTO
import org.example.walab4.dto.contact.ContactDto
import org.example.walab4.dto.contact.EmailDTO
import org.example.walab4.dto.contact.TelephoneDTO
import org.example.walab4.services.kafka.IKafkaService
import org.example.walab4.services.kafka.KafkaService
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/contacts")
class ContactController(
    private val contactService: IContactService,
    private val kafkaService: IKafkaService
) {

    private val LOGGER: Logger = LogManager.getLogger()

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllContacts(
        @RequestParam(value = "page") page: Int=0,
        @RequestParam(value = "limit")
        @Valid
        @Min(0) @Max(50)
        limit: Int=10   ,
        @RequestParam(value = "name", required = false) name: String?,
        @RequestParam(value = "surname", required = false) surname: String?,
        @RequestParam(value = "email", required = false) email: String?,
        @RequestParam(value = "address", required = false) address: String?,
        @RequestParam(value = "telephone", required = false) telephone: String?
    ) : List<ContactDto>? {

        val userDetails = SecurityContextHolder
            .getContext()
            .authentication
            .principal

        val contacts = contactService.getAllContacts(page, limit, name, surname, email, address, telephone)
        kafkaService.sendMessage("topic1", "Richiesta all messages")
        LOGGER.info("[GET - API/contacts] - SUCCESS - Contacts retrieved correctly")

        return contacts
    }


    @GetMapping("/{contactId}")
    @ResponseStatus(HttpStatus.OK)
    fun getContactById(@PathVariable contactId: Long) : ContactDto? {

        val contact =  contactService.getContact(contactId)

        LOGGER.info("[GET - API/contacts${contactId}] - SUCCESS - Contact retrieved correctly")

        return contact
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun saveContact(
        @RequestBody contactDto: ContactDto,
    ) : ContactDto? {

        val contact = contactService.saveContact(contactDto)

        LOGGER.info("[POST - API/contacts] - SUCCESS - Contact saved successfully")

        return contact
    }

    @GetMapping("/{contactId}/email")
    @ResponseStatus(HttpStatus.OK)
    fun getContactEmails(
        @PathVariable contactId: Long
    ) : List<EmailDTO> {

        val emails = contactService.getContactEmails(contactId)

        LOGGER.info("[GET - API/contacts${contactId}/email] - SUCCESS - emails get successfully")

        return emails
    }


    @PostMapping("/{contactId}/email")
    @ResponseStatus(HttpStatus.CREATED)
    fun addEmailToContact(
        @PathVariable contactId: Long,
        @RequestBody email: EmailDTO
    ) : ContactDto? {

        val contact = contactService.addContactEmail(contactId, email)

        LOGGER.info("[POST - API/contacts${contactId}/email] - SUCCESS - email saved successfully")

        return contact
    }

    @GetMapping("/{contactId}/address")
    @ResponseStatus(HttpStatus.OK)
    fun getContactAddresses(
        @PathVariable contactId: Long
    ) : List<AddressDTO> {

        val addresses = contactService.getContactAddresses(contactId)

        LOGGER.info("[GET - API/contacts${contactId}/addresses] - SUCCESS - addresses get successfully")

        return addresses
    }


    @PostMapping("/{contactId}/address")
    @ResponseStatus(HttpStatus.CREATED)
    fun addAddressToContact(
        @PathVariable contactId: Long,
        @RequestBody address: AddressDTO
    ) : ContactDto? {

        val contact = contactService.addContactAddress(contactId, address)

        LOGGER.info("[POST - API/contacts${contactId}/address] - SUCCESS - address saved successfully")

        return contact
    }


    @PostMapping("/{contactId}/telephone")
    @ResponseStatus(HttpStatus.CREATED)
    fun addTelephoneToContact(
        @PathVariable contactId: Long,
        @RequestBody telephone: TelephoneDTO
    ) : ContactDto? {

        val contact = contactService.addContactTelephone(contactId, telephone)

        LOGGER.info("[POST - API/contacts${contactId}/telephone] - SUCCESS - telephone number saved successfully")

        return contact
    }

    @GetMapping("/{contactId}/telephone")
    @ResponseStatus(HttpStatus.OK)
    fun getContactTelephones(
        @PathVariable contactId: Long
    ) : List<TelephoneDTO> {

        val telephones = contactService.getContactTelephones(contactId)

        LOGGER.info("[GET - API/contacts${contactId}/telephone] - SUCCESS - telephones get successfully")

        return telephones
    }


    @DeleteMapping("/{contactId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteContact(@PathVariable contactId: Long) {

        contactService.deleteContact(contactId)

        LOGGER.info("[DELETE - API/contacts${contactId}] - SUCCESS - contact deleted successfully")
    }


    @DeleteMapping("/{contactId}/email/{emailId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteContactEmail(
        @PathVariable contactId: Long,
        @PathVariable emailId: Long
    ) {

        contactService.deleteContactEmail(contactId, emailId)

        LOGGER.info("[DELETE - API/contacts${contactId}/email/{emailId}] - SUCCESS - email deleted successfully")
    }


    @DeleteMapping("/{contactId}/address/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteContactAddress(
        @PathVariable contactId: Long,
        @PathVariable addressId: Long
    ) {

        contactService.deleteContactAddress(contactId, addressId)

        LOGGER.info("[DELETE - API/contacts${contactId}/address/{addressId}] - SUCCESS - address deleted successfully")
    }


    @DeleteMapping("/{contactId}/telephone/{telephoneId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteContactTelephone(
        @PathVariable contactId: Long,
        @PathVariable telephoneId: Long
    ) {

        contactService.deleteContactTelephone(contactId, telephoneId)

        LOGGER.info("[DELETE - API/contacts${contactId}/telephone/{telephoneId}] - SUCCESS - telephone number deleted successfully")
    }


    @PutMapping("/{contactId}/category")
    @ResponseStatus(HttpStatus.OK)
    fun updateContactCategory(@PathVariable contactId: Long,
                              @RequestParam category: String
    ) : ContactDto? {

        val contactCategory: ContactCategory
        try {
            contactCategory = ContactCategory.valueOf(category.uppercase())
        } catch (iae: IllegalArgumentException) {
            throw InvalidCategoryValueException(contactId.toString(), category)
        }
        val contact = contactService.updateContactCategory(contactId, contactCategory)

        LOGGER.info("[PUT - API/contacts${contactId}/category] - SUCCESS - category updated successfully")

        return contact
    }


    @PutMapping("/{contactId}/email/{emailId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateContactEmail(@PathVariable contactId: Long,
                           @PathVariable emailId: Long,
                           @RequestBody newEmail: EmailDTO
    ) : ContactDto? {

        val contact = contactService.updateContactEmail(contactId, emailId, newEmail)

        LOGGER.info("[PUT - API/contacts${contactId}/email/${emailId}] - SUCCESS - email updated successfully")

        return contact
    }


    @PutMapping("/{contactId}/address/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateContactAddress(@PathVariable contactId: Long,
                             @PathVariable addressId: Long,
                             @RequestBody newAddress: AddressDTO
    ) : ContactDto? {

        val contact = contactService.updateContactAddress(contactId, addressId, newAddress)

        LOGGER.info("[PUT - API/contacts${contactId}/address/${addressId}] - SUCCESS - address updated successfully")

        return contact
    }


    @PutMapping("/{contactId}/telephone/{telephoneId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateContactTelephone(@PathVariable contactId: Long,
                               @PathVariable telephoneId: Long,
                               @RequestBody newTelephone: TelephoneDTO
    ) : ContactDto? {

        val contact = contactService.updateContactTelephone(contactId, telephoneId, newTelephone)

        LOGGER.info("[PUT - API/contacts${contactId}/telephone/${telephoneId}] - SUCCESS - telephone number updated successfully")

        return contact
    }
}
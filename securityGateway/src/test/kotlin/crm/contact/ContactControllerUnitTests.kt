package crm.contact

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import wa.lab5.Lab5Application
import wa.lab5.controller.ContactController
import wa.lab5.dto.contact.*
import wa.lab5.exceptions.contact.*
import wa.lab5.model.contact.*
import wa.lab5.services.contact.ContactService

@WebMvcTest(controllers = arrayOf(ContactController::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class ContactControllerUnitTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var contactService: ContactService

    @Test
    fun getAllContactsSimple() {
        val expectedContacts: List<ContactDto> = listOf(
            Contact(id = 0L, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = null),
            Contact(id = 1L, name = "Davide", surname = "Palatroni", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = null, addresses = null, telephoneNumbers = null, emails = null),
            Contact(id = 2L, name = "Vittorio", surname = "Di Giorgio", ssnCode = "SSN_TEST", category = ContactCategory.RECRUITER, messages = null, addresses = null, telephoneNumbers = null, emails = null)
        ).map { it.toDto() }

        every { contactService.getAllContacts(any(), any(), any(), any(), any(), any(), any()) } returns expectedContacts

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContacts)))
    }

    @Test
    fun getAllContactsEmpty() {
        val expectedContacts: List<ContactDto> = emptyList<Contact>().map { it.toDto() }

        every { contactService.getAllContacts(any(), any(), any(), any(), any(), any(), any()) } returns expectedContacts

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContacts)))
    }

    @Test
    fun getContactByIdSimple() {
        val contactId = 0L

        val expectedContact: ContactDto = Contact(id = 0L, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = null).toDto()

        every { contactService.getContact(contactId) } returns expectedContact

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts/$contactId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContact)))
    }

    @Test
    fun getContactByIdSimple404() {
        val contactId = 0L

        every { contactService.getContact(contactId) } throws ContactNotFoundException(contactId.toString(), null)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts/$contactId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun saveContactSimple() {
        val expectedContact: Contact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = null)
        val expectedContactDto = expectedContact.toDto()

        every { contactService.saveContact(any()) } returns expectedContactDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(expectedContactDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun addEmailToContactSimple() {
        val email = Email(1L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()

        val contactId = 0L
        val expectedContact: Contact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf(email))
        val expectedContactDto = expectedContact.toDto()

        every { contactService.addContactEmail( contactId, emailDto ) } returns expectedContactDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(emailDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun addAddressToContactSimple() {
        val address = Address(0L, "Via Ternavasso 21", mutableListOf())
        val addressDto = address.toDto()

        val contactId = 0L
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(address), telephoneNumbers = mutableListOf(), emails = mutableListOf())
        val expectedContactDto = expectedContact.toDto()

        every { contactService.addContactAddress( contactId, addressDto ) } returns expectedContactDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto).replace("name", "address")))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun addTelephoneToContactSimple() {
        val telephone = Telephone(1L, "332345323", mutableListOf())
        val telephoneDto = telephone.toDto()

        val contactId = 0L
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(telephone), emails = mutableListOf())
        val expectedContactDto = expectedContact.toDto()

        every { contactService.addContactTelephone( contactId, telephoneDto ) } returns expectedContactDto

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/telephone")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun addEmailToContact404() {
        val email = Email(1L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()

        val contactId = 0L

        every { contactService.addContactEmail( contactId, emailDto ) } throws ContactNotFoundException(contactId.toString(), null)

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(emailDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addAddressToContact404() {
        val address = Address(0L, "Via Ternavasso 21", mutableListOf())
        val addressDto = address.toDto()

        val contactId = 0L
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(address), telephoneNumbers = mutableListOf(), emails = mutableListOf())
        val expectedContactDto = expectedContact.toDto()

        every { contactService.addContactAddress( contactId, addressDto ) } throws ContactNotFoundException(contactId.toString(), null)

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto).replace("name", "address")))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addTelephoneToContact404() {
        val telephone = Telephone(1L, "332345323", mutableListOf())
        val telephoneDto = telephone.toDto()

        val contactId = 0L
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(telephone), emails = mutableListOf())
        val expectedContactDto = expectedContact.toDto()

        every { contactService.addContactTelephone( contactId, telephoneDto ) } throws ContactNotFoundException(contactId.toString(), null)

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/telephone")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun deleteContactSimple() {
        val contactId = 0L

        every { contactService.deleteContact(contactId) } returns Unit

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteContact404() {
        val contactId = 0L

        every { contactService.deleteContact(contactId) } throws ContactNotFoundException(contactId.toString(), null)

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun `deleteContactEmail success`() {
        val contactId = 0L
        val emailId = 1L

        every { contactService.deleteContactEmail(contactId, emailId) } returns  Unit

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        verify { contactService.deleteContactEmail(contactId, emailId) }
    }

    @Test
    fun `deleteContactEmail contact not found`() {
        val contactId = 0L
        val emailId = 1L

        every { contactService.deleteContactEmail(contactId, emailId) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactEmail email not found`() {
        val contactId = 0L
        val emailId = 1L

        every { contactService.deleteContactEmail(contactId, emailId) } throws EmailNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactEmail email not associated with contact`() {
        val contactId = 0L
        val emailId = 1L

        every { contactService.deleteContactEmail(contactId, emailId) } throws EmailNotPresentInContactException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactAddress success`() {
        val contactId = 0L
        val addressId = 1L

        every { contactService.deleteContactAddress(contactId, addressId)} returns  Unit

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        verify { contactService.deleteContactAddress(contactId, addressId) }
    }

    @Test
    fun `deleteContactAddress contact not found`() {
        val contactId = 0L
        val addressId = 1L

        every { contactService.deleteContactAddress(contactId, addressId) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactAddress address not found`() {
        val contactId = 0L
        val addressId = 1L

        every { contactService.deleteContactAddress(contactId, addressId) } throws AddressNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactTelephone success`() {
        val contactId = 0L
        val telephoneId = 1L

        every { contactService.deleteContactTelephone(contactId, telephoneId) } returns Unit

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)

        verify { contactService.deleteContactTelephone(contactId, telephoneId) }
    }

    @Test
    fun `deleteContactTelephone contact not found`() {
        val contactId = 0L
        val telephoneId = 1L

        every { contactService.deleteContactTelephone(contactId, telephoneId) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deleteContactTelephone telephone not found`() {
        val contactId = 0L
        val telephoneId = 1L

        every { contactService.deleteContactTelephone(contactId, telephoneId) } throws TelephoneNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactEmail success`() {
        val contactId = 0L
        val emailId = 1L
        val newEmail = EmailDTO("new_email@example.com")

        val expectedContactDto = ContactDto(
            id = contactId,
            name = "ExampleName",
            surname = "ExampleSurname",
            ssnCode = "1234567890",
            category = ContactCategory.CUSTOMER.toString()
        )

        every { contactService.updateContactEmail(contactId, emailId, newEmail) } returns expectedContactDto

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newEmail))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun `updateContactEmail contact not found`() {
        val contactId = 0L
        val emailId = 1L
        val newEmail = EmailDTO("new_email@example.com")

        every { contactService.updateContactEmail(contactId, emailId, newEmail) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newEmail))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactEmail email not found`() {
        val contactId = 0L
        val emailId = 1L
        val newEmail = EmailDTO("new_email@example.com")

        every { contactService.updateContactEmail(contactId, emailId, newEmail) } throws EmailNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newEmail))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactEmail email not associated with contact`() {
        val contactId = 0L
        val emailId = 1L
        val newEmail = EmailDTO("new_email@example.com")

        every { contactService.updateContactEmail(contactId, emailId, newEmail) } throws EmailNotPresentInContactException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newEmail))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }


    @Test
    fun `updateContactAddress success`() {
        val contactId = 0L
        val addressId = 1L
        val newAddress = AddressDTO("New Street")
        val expectedContactDto = ContactDto(
            id = contactId,
            name = "ExampleName",
            surname = "ExampleSurname",
            ssnCode = "1234567890",
            category = ContactCategory.CUSTOMER.toString()
        )

        every { contactService.updateContactAddress(contactId, addressId, newAddress) } returns expectedContactDto

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newAddress))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun `updateContactAddress contact not found`() {
        val contactId = 0L
        val addressId = 1L
        val newAddress = AddressDTO("New Street")

        every { contactService.updateContactAddress(contactId, addressId, newAddress) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newAddress))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactAddress address not found`() {
        val contactId = 0L
        val addressId = 1L
        val newAddress = AddressDTO("New Street")

        every { contactService.updateContactAddress(contactId, addressId, newAddress) } throws AddressNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/address/$addressId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newAddress))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactTelephone success`() {
        val contactId = 0L
        val telephoneId = 1L
        val newTelephone = TelephoneDTO("1234567890")

        val expectedContactDto = ContactDto(
            id = contactId,
            name = "ExampleName",
            surname = "ExampleSurname",
            ssnCode = "1234567890",
            category = ContactCategory.CUSTOMER.toString()
        )

        every { contactService.updateContactTelephone(contactId, telephoneId, newTelephone) } returns expectedContactDto

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newTelephone))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().json(ObjectMapper().writeValueAsString(expectedContactDto)))
    }

    @Test
    fun `updateContactTelephone contact not found`() {
        val contactId = 0L
        val telephoneId = 1L
        val newTelephone = TelephoneDTO("1234567890")

        every { contactService.updateContactTelephone(contactId, telephoneId, newTelephone) } throws ContactNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newTelephone))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `updateContactTelephone telephone not found`() {
        val contactId = 0L
        val telephoneId = 1L
        val newTelephone = TelephoneDTO("1234567890")

        every { contactService.updateContactTelephone(contactId, telephoneId, newTelephone) } throws TelephoneNotFoundException()

        mockMvc.perform(
            MockMvcRequestBuilders.put("/API/contacts/$contactId/telephone/$telephoneId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(newTelephone))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }






}






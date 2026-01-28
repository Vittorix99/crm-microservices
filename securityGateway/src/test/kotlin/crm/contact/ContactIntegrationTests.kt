package org.crm.contact;

import com.fasterxml.jackson.databind.ObjectMapper
import crm.message.IntegrationTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import wa.lab5.dto.contact.TelephoneDTO
import wa.lab5.dto.contact.toDto
import wa.lab5.model.contact.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ContactIntegrationTests: IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val contact1 = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf()).toDto()
    private val contact2 = Contact(name = "Davide", surname = "Palatroni", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf()).toDto()
    private val contact3 = Contact(name = "Vittorio", surname = "Di Giorgio", ssnCode = "SSN_TEST", category = ContactCategory.RECRUITER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf()).toDto()
    private val contactToBeDeleted = Contact(name = "contactToBeDeleted", surname = "Di contactToBeDeleted", ssnCode = "SSN_ToBeDeleted", category = ContactCategory.RECRUITER, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf()).toDto()
    private val contactToBeUpdated = Contact(name = "contactToBeUpdated", surname = "contactToBeUpdated", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf()).toDto()
    private val emailToBeDeleted = Email(id=5L, email = "email@toDelete.com", mutableListOf())
    private val emailtoUpdate = Email(id=5L, email = "email@toUpdate.com", mutableListOf())
    private val addressToBeDeleted = Address(id = 5L, name="Via To Be Deleted, 30", mutableListOf())
    private val telephoneToBeDeleted = Telephone(id = 5L, number = "+39 345193730", mutableListOf())


    @BeforeAll
    @Test
    fun saveContacts(): Unit {

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(contact1)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact1.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact1.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact1.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact1.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(contact2)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(contact3)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact3.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact3.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact3.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact3.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(contactToBeDeleted)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contactToBeDeleted.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contactToBeDeleted.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contactToBeDeleted.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contactToBeDeleted.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(contactToBeUpdated)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contactToBeUpdated.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contactToBeUpdated.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contactToBeUpdated.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contactToBeUpdated.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts/2/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(emailToBeDeleted.toDto()))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts/2/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(addressToBeDeleted.toDto()))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/API/contacts/2/telephone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(telephoneToBeDeleted.toDto()))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun getAllContactsSimple() {
        val expectedContacts = mutableListOf(contact1, contact2, contact3)

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(contact1.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(contact1.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(contact1.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].ssnCode").value(contact1.ssnCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].ssnCode").value(contact2.ssnCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value(contact3.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].surname").value(contact3.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].category").value(contact3.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].ssnCode").value(contact3.ssnCode))
    }

    @Test
    fun getAllContactsWithPaginated() {
        val expectedContacts = mutableListOf(contact1, contact2, contact3)

        /* 3 contacts in db,
        second page with limit 2, contact3 expected */
        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts")
            .param("page", "1")
            .param("limit", "2"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(contact3.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(contact3.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(contact3.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].ssnCode").value(contact3.ssnCode))
    }

    @Test
    fun getAllContactsWithFilteredByName() {
        val expectedContacts = mutableListOf(contact1, contact2, contact3)

        /* 3 contacts in db,
        second page with limit 2, contact3 expected */
        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts")
            .param("name", "Simone"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(contact1.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value(contact1.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(contact1.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].ssnCode").value(contact1.ssnCode))
    }


    @Test
    fun getContactById() {
        /* id that will be generated from db for contact2*/
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts/$contactId"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun getContactById404() {
        /* id that will be generated from db for contact2*/
        val contactId = 20L

        mockMvc.perform(MockMvcRequestBuilders.get("/API/contacts/$contactId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addEmailToContactSimple() {
        val email = Email(1L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/email")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(emailDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun addAddressToContactSimple() {
        val address = Address(0L, "Via Ternavasso 21", mutableListOf())
        val addressDto = address.toDto()
        val contactId = 2L


        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun addTelephoneToContactSimple() {
        val telephone = Telephone(1L, "332345323", mutableListOf())
        val telephoneDto = telephone.toDto()
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/telephone")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun addEmailToContact404() {
        val email = Email(1L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()
        val contactId = 10L // not exists

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
        val contactId = 20L // not exists


        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/address")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun addTelephoneToContact404() {
        val telephone = Telephone(1L, "332345323", mutableListOf())
        val telephoneDto = telephone.toDto()
        val contactId = 20L // not exists

        mockMvc.perform(MockMvcRequestBuilders.post("/API/contacts/$contactId/telephone")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateContactTelephoneSimple() {
        val contactId = 2L
        val telephoneId = 1L
        val telephoneDto = TelephoneDTO(number = "33491247592")

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/telephone/$telephoneId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun updateContactEmailSimple() {
        val contactId = 2L
        val emailId = 2L
        val email = Email(0L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(emailDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun updateContactAddressSimple() {
        val addressId = 1L
        val address = Address(0L, "Via Ternavasso 21", mutableListOf())
        val addressDto = address.toDto()
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/address/$addressId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contact2.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contact2.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(contact2.category))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contact2.ssnCode))
    }

    @Test
    fun updateContactCategorySimple() {
        val contactId = 5L
        val newCat = ContactCategory.RECRUITER.name

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/category")
            .contentType(MediaType.APPLICATION_JSON)
            .param("category", newCat))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(contactToBeUpdated.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(contactToBeUpdated.surname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.ssnCode").value(contactToBeUpdated.ssnCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(newCat))
    }

    @Test
    fun updateContactTelephone404() {
        val contactId = 200L
        val telephoneId = 1L
        val telephoneDto = TelephoneDTO(number = "33491247592")

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/telephone/$telephoneId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(telephoneDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateContactEmail404() {
        val contactId = 200L
        val emailId = 2L
        val email = Email(0L, "email@di.prova", mutableListOf())
        val emailDto = email.toDto()

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/email/$emailId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(emailDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateContactAddress404() {
        val addressId = 1L
        val address = Address(0L, "Via Ternavasso 21", mutableListOf())
        val addressDto = address.toDto()
        val contactId = 200L

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/address/$addressId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(addressDto)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateContactCategory404() {
        val contactId = 100L
        val newCat = ContactCategory.RECRUITER.name

        mockMvc.perform(MockMvcRequestBuilders.put("/API/contacts/$contactId/category")
            .contentType(MediaType.APPLICATION_JSON)
            .param("category", newCat))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun deleteContactSimple() {
        val contactId = 4L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteContact404() {
        val contactId = 10L //not exists

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun deleteContactEmailSimple(){
        val emailId = 1L
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteContactEmail404(){
        val emailId = 1L
        val contactId = 9L /*Not Exists*/

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun deleteContactEmail404EmailNotFound(){
        val emailId = 10L
        val contactId = 1L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/email/$emailId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun deleteContactAddressSimple(){
        val addressId = 1L
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteContactAddress404(){
        val addressId = 1L
        val contactId = 10L /*Does not exist*/

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun deleteContactAddress404AddressNotFound(){
        val addressId = 10L
        val contactId = 1L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/address/$addressId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }
    @Test
    fun deleteContactTelephoneSimple(){
        val telephoneId = 1L
        val contactId = 2L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/$telephoneId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun deleteContactTelephone404(){
        val telephoneId = 1L
        val contactId = 10L /*Does not exist*/

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/${telephoneId}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun deleteContactTelephone404AddressNotFound(){
        val telephoneId = 10L
        val contactId = 1L

        mockMvc.perform(MockMvcRequestBuilders.delete("/API/contacts/$contactId/telephone/$telephoneId")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


}
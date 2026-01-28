package crm.contact


import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import wa.lab5.dto.contact.AddressDTO
import wa.lab5.dto.contact.EmailDTO
import wa.lab5.dto.contact.TelephoneDTO
import wa.lab5.dto.contact.toDto
import wa.lab5.exceptions.contact.*
import wa.lab5.model.contact.*
import wa.lab5.repository.contact.AddressRepository
import wa.lab5.repository.contact.ContactRepository
import wa.lab5.repository.contact.EmailRepository
import wa.lab5.repository.contact.TelephoneRepository
import wa.lab5.services.contact.ContactService


class ContactServiceUnitTests {
    val contactRepository = mockk<ContactRepository>()
    val emailRepository = mockk<EmailRepository>()
    val telephoneRepository = mockk<TelephoneRepository>()
    val addressRepository = mockk<AddressRepository>()
    val contactService = ContactService(contactRepository, emailRepository, telephoneRepository, addressRepository)

    @Test
    fun getAllContactsSimple() {
        val page = 0
        val limit = 10

        val expectedContacts: List<Contact> = listOf(
            Contact(id = 0L, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = null),
            Contact(id = 1L, name = "Davide", surname = "Palatroni", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = null, addresses = null, telephoneNumbers = null, emails = null),
            Contact(id = 2L, name = "Vittorio", surname = "Di Giorgio", ssnCode = "SSN_TEST", category = ContactCategory.RECRUITER, messages = null, addresses = null, telephoneNumbers = null, emails = null)
        )

        val expectedDtos = expectedContacts.map { it.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedContacts)

        every { contactRepository.findAll(pageRequest) } returns pageImpl

        val actualContacts = contactService.getAllContacts(page, limit, null, null, null, null, null)

        assertEquals(expectedDtos, actualContacts)
    }

    @Test
    fun getAllContactsEmpty() {
        val page = 0
        val limit = 10

        val expectedContacts: List<Contact> = emptyList()

        val expectedDtos = expectedContacts.map { it.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedContacts)

        every { contactRepository.findAll(pageRequest) } returns pageImpl

        val actualContacts = contactService.getAllContacts(page, limit, null, null, null, null, null)

        assertEquals(expectedDtos, actualContacts)
    }

    @Test
    fun getAllContactsNameFiltered() {
        val page = 0
        val limit = 10
        val nameFilter = "Davide"

        val expectedContacts: List<Contact> = listOf(
            Contact(id = 0L, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(0L, "sgeraci@polito.it", mutableListOf()), Email(1L, "sgeraci2@polito.it", mutableListOf()))),
            Contact(id = 1L, name = "Davide", surname = "Palatroni", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(2L, "dpalatroni@polito.it", mutableListOf()), Email(3L, "dpalatroni2@polito.it", mutableListOf()))),
            Contact(id = 2L, name = "Vittorio", surname = "Di Giorgio", ssnCode = "SSN_TEST", category = ContactCategory.RECRUITER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(4L, "vgiorgio@polito.it", mutableListOf()), Email(5L, "vgiorgio2@polito.it", mutableListOf())))
        )

        val expectedDto = expectedContacts.filter { it.name == nameFilter }.map { itx -> itx.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedContacts)

        every { contactRepository.findAll(pageRequest) } returns pageImpl

        val actualContacts = contactService.getAllContacts(page, limit, nameFilter, null, null, null, null)

        assertEquals(expectedDto, actualContacts)
    }

    @Test
    fun getAllContactsCategoryFiltered() {
        val page = 0
        val limit = 10

        val email = Email(0L, "sgeraci@polito.it", mutableListOf())
        val emailFilter = "sgeraci@polito.it"


        val expectedContacts: List<Contact> = listOf(
            Contact(id = 0L, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(email, Email(1L, "sgeraci2@polito.it", mutableListOf()))),
            Contact(id = 1L, name = "Davide", surname = "Palatroni", ssnCode = "SSN_TEST", category = ContactCategory.PROFESSIONAL, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(2L, "dpalatroni@polito.it", mutableListOf()), Email(3L, "dpalatroni2@polito.it", mutableListOf()))),
            Contact(id = 2L, name = "Vittorio", surname = "Di Giorgio", ssnCode = "SSN_TEST", category = ContactCategory.RECRUITER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(4L, "vgiorgio@polito.it", mutableListOf()), Email(5L, "vgiorgio2@polito.it", mutableListOf())))
        )

        val expectedDto = expectedContacts.filter { it.emails!!.any { itx -> itx.email == emailFilter } }.map { itk -> itk.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedContacts)

        every { contactRepository.findAll(pageRequest) } returns pageImpl
        every { emailRepository.findEmailByEmail(emailFilter) } returns email

        val actualContacts = contactService.getAllContacts(page, limit, null, null, emailFilter, null, null)

        assertEquals(expectedDto, actualContacts)
    }

    @Test
    fun getContactByIdSimple() {
        val contactId = 0L
        val expectedContact = Contact(id = contactId, name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(1L, "sgeraci@polito.it", mutableListOf())))

        every { contactRepository.findContactById(contactId) } returns expectedContact

        val actualContact  = contactService.getContact(contactId)
        assertEquals(expectedContact.toDto(), actualContact)
    }

    @Test
    fun getContactByIdNotFound() {
        val contactId = 0L
        val expectedContact = null

        every { contactRepository.findContactById(contactId) } returns expectedContact

        assertThrows<ContactNotFoundException> {
            contactService.getContact(contactId)
        }
    }

    @Test
    fun `deleteContactEmail should delete email from contact`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", emails = mutableListOf(), messages = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf(), category = ContactCategory.UNKNOWN )
        val existingEmail = Email(id = emailId, email = "example@example.com", contacts = mutableListOf(existingContact))
        existingContact.emails?.add(existingEmail)
        val capContact = slot<Contact>()
        val capEmail = slot<Email>()


        // Configurazione dei mock per ritornare i dati corretti
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns existingEmail
        every { contactRepository.save( capture(capContact) ) } returns existingContact
        every { emailRepository.save( capture(capEmail) ) } returns existingEmail



        every { emailRepository.delete(existingEmail) } just runs


        // Act
        contactService.deleteContactEmail(contactId, emailId)

        // Assert
        verify(exactly = 1) { contactRepository.findContactById(contactId) }
        verify(exactly = 1) { emailRepository.findEmailById(emailId) }
        verify(exactly = 1) { contactRepository.save(existingContact) }
        verify(exactly = 1) { emailRepository.save(existingEmail) }
        verify(exactly = 1) { emailRepository.delete(existingEmail) }
        assertFalse(existingContact.emails?.contains(existingEmail) ?: false)
    }

    @Test
    fun `deleteContactEmail should throw ContactNotFoundException if contact does not exist`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.deleteContactEmail(contactId, emailId)
        }
    }

    @Test
    fun `deleteContactEmail should throw EmailNotFoundException if email does not exist`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns null

        // Act & Assert
        assertThrows(EmailNotFoundException::class.java) {
            contactService.deleteContactEmail(contactId, emailId)
        }
    }

    @Test
    fun `deleteContactEmail should throw EmailNotPresentInContactException if email is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        val existingEmail = Email(id = emailId, email = "example@example.com", contacts = mutableListOf())
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns existingEmail

        // Act & Assert
        assertThrows(EmailNotPresentInContactException::class.java) {
            contactService.deleteContactEmail(contactId, emailId)
        }
    }

    @Test
    fun `deleteContactTelephone should delete telephone from contact`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        val existingTelephone = Telephone(id = telephoneId, number = "123456789", contacts = mutableListOf(existingContact))
        existingContact.telephoneNumbers?.add(existingTelephone)
        val capContact = slot<Contact>()
        val capTelephone = slot<Telephone>()

        // Configurazione dei mock per ritornare i dati corretti e catturare gli oggetti passati come argomenti
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns existingTelephone
        every { contactRepository.save(capture(capContact)) } returns  existingContact
        every { telephoneRepository.save(capture(capTelephone)) } returns existingTelephone
        every { telephoneRepository.delete(existingTelephone) } just runs

        // Act
        contactService.deleteContactTelephone(contactId, telephoneId)

        // Assert
        verify(exactly = 1) { contactRepository.save(existingContact) }
        verify(exactly = 1) { telephoneRepository.save(existingTelephone) }
        assertFalse(existingContact.telephoneNumbers?.contains(existingTelephone) ?: false)
    }


    @Test
    fun `deleteContactTelephone should throw ContactNotFoundException if contact does not exist`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.deleteContactTelephone(contactId, telephoneId)
        }
    }

    @Test
    fun `deleteContactTelephone should throw TelephoneNotFoundException if telephone does not exist`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns null

        // Act & Assert
        assertThrows(TelephoneNotFoundException::class.java) {
            contactService.deleteContactTelephone(contactId, telephoneId)
        }
    }

    @Test
    fun `deleteContactTelephone should throw TelephoneNotPresentInContactException if telephone is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        val existingTelephone = Telephone(id = telephoneId, number = "123456789", contacts = mutableListOf())
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns existingTelephone

        // Act & Assert
        assertThrows(TelephoneNotPresentInContactException::class.java) {
            contactService.deleteContactTelephone(contactId, telephoneId)
        }
    }
    @Test
    fun `updateContactTelephone should update telephone number and return contact DTO`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingTelephone = Telephone(
            id = telephoneId,
            number = "123456789",
            contacts = mutableListOf(existingContact)
        )
        existingContact.telephoneNumbers?.add(existingTelephone)
        val newTelephone = TelephoneDTO(number = "987654321")

        val capTelephone = slot<Telephone>()

        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns existingTelephone
        every { telephoneRepository.save(capture(capTelephone)) } returns existingTelephone
        // Act
        val result = contactService.updateContactTelephone(contactId, telephoneId, newTelephone)

        // Assert
        verify(exactly = 1) { telephoneRepository.save(existingTelephone) }
        assertEquals(existingContact.telephoneNumbers?.first()?.number, newTelephone.number)
        assertEquals(existingContact.toDto(), result)

    }

    @Test
    fun `updateContactTelephone should throw ContactNotFoundException if contact does not exist`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val newTelephone = TelephoneDTO(number = "987654321")
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.updateContactTelephone(contactId, telephoneId, newTelephone)
        }
    }

    @Test
    fun `updateContactTelephone should throw TelephoneNotFoundException if telephone does not exist`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        val newTelephone = TelephoneDTO(number = "987654321")

        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns null

        // Act & Assert
        assertThrows(TelephoneNotFoundException::class.java) {
            contactService.updateContactTelephone(contactId, telephoneId, newTelephone)
        }
    }

    @Test
    fun `updateContactTelephone should throw TelephoneNotPresentInContactException if telephone is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val telephoneId = 2L
        val existingContact = Contact(id = contactId, name = "John", surname = "Doe", ssnCode = "123456", category = ContactCategory.UNKNOWN, messages = mutableListOf(), emails = mutableListOf(), addresses = mutableListOf(), telephoneNumbers = mutableListOf())
        val existingTelephone = Telephone(id = telephoneId, number = "123456789", contacts = mutableListOf())
        val newTelephone = TelephoneDTO(number = "987654321")
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { telephoneRepository.findTelephoneById(telephoneId) } returns existingTelephone

        // Act & Assert
        assertThrows(TelephoneNotPresentInContactException::class.java) {
            contactService.updateContactTelephone(contactId, telephoneId, newTelephone)
        }
    }



    @Test
    fun `deleteContactAddress should delete address from contact`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingAddress = Address(
            id = addressId,
            name = "Home",
            contacts = mutableListOf(existingContact)
        )
        existingContact.addresses?.add(existingAddress)
        val capAddress = slot<Address>()
        val capContact = slot<Contact>()
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns existingAddress
        every { contactRepository.save(capture(capContact)) } returns existingContact
        every { addressRepository.save(capture(capAddress)) } returns existingAddress
        every { addressRepository.delete(existingAddress) } just runs

        // Act
        contactService.deleteContactAddress(contactId, addressId)

        // Assert
        verify(exactly = 1) { contactRepository.save(existingContact) }
        verify(exactly = 1) { addressRepository.save(existingAddress) }
        assertFalse(existingContact.addresses?.contains(existingAddress) ?: false)

    }

    @Test
    fun `deleteContactAddress should throw ContactNotFoundException when contact does not exist`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.deleteContactAddress(contactId, addressId)
        }
    }

    @Test
    fun `deleteContactAddress should throw AddressNotFoundException when address does not exist`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns null

        // Act & Assert
        assertThrows(AddressNotFoundException::class.java) {
            contactService.deleteContactAddress(contactId, addressId)
        }
    }

    @Test
    fun `deleteContactAddress should throw AddressNotPresentInContactException when address is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingAddress = Address(
            id = addressId,
            name = "Home",
            contacts = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns existingAddress

        // Act & Assert
        assertThrows(AddressNotPresentInContactException::class.java) {
            contactService.deleteContactAddress(contactId, addressId)
        }
    }

    @Test
    fun `updateContactAddress should update address name and return contact DTO`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val newName = "New Home"
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingAddress = Address(
            id = addressId,
            name = "Home",
            contacts = mutableListOf(existingContact)
        )
        existingContact.addresses?.add(existingAddress)
        val newAddress = AddressDTO(address = newName)

        val capAddress = slot<Address>()

        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns existingAddress
        every { addressRepository.save(capture(capAddress)) } answers { capAddress.captured }

        // Act
        val result = contactService.updateContactAddress(contactId, addressId, newAddress)

        // Assert
        verify(exactly = 1) { addressRepository.save(existingAddress) }
        assertEquals(newName, capAddress.captured.name)
        assertEquals(existingContact.toDto(), result)
    }


    @Test
    fun `updateContactAddress should throw ContactNotFoundException when contact does not exist`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.updateContactAddress(contactId, addressId, AddressDTO("New Home"))
        }
    }

    @Test
    fun `updateContactAddress should throw AddressNotFoundException when address does not exist`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns null

        // Act & Assert
        assertThrows(AddressNotFoundException::class.java) {
            contactService.updateContactAddress(contactId, addressId, AddressDTO("New Home"))
        }
    }

    @Test
    fun `updateContactAddress should throw AddressNotPresentInContactException when address is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val addressId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingAddress = Address(
            id = addressId,
            name = "Home",
            contacts = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { addressRepository.findAddressById(addressId) } returns existingAddress

        // Act & Assert
        assertThrows(AddressNotPresentInContactException::class.java) {
            contactService.updateContactAddress(contactId, addressId, AddressDTO("New Home"))
        }
    }


    @Test
    fun `updateContactEmail should update email address and return contact DTO`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val newEmail = "new@example.com"
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingEmail = Email(
            id = emailId,
            email = "old@example.com",
            contacts = mutableListOf(existingContact)
        )
        existingContact.emails?.add(existingEmail)

        val capEmail = slot<Email>()

        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns existingEmail
        every { emailRepository.save(capture(capEmail)) } answers { capEmail.captured }

        // Act
        val result = contactService.updateContactEmail(contactId, emailId, EmailDTO(newEmail))

        // Assert
        verify(exactly = 1) { emailRepository.save(existingEmail) }
        assertEquals(newEmail, capEmail.captured.email)
        assertEquals(existingContact.toDto(), result)
    }


    @Test
    fun `updateContactEmail should throw ContactNotFoundException when contact does not exist`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.updateContactEmail(contactId, emailId, EmailDTO("new@example.com"))
        }
    }

    @Test
    fun `updateContactEmail should throw EmailNotFoundException when email does not exist`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns null

        // Act & Assert
        assertThrows(EmailNotFoundException::class.java) {
            contactService.updateContactEmail(contactId, emailId, EmailDTO("new@example.com"))
        }
    }

    @Test
    fun `updateContactEmail should throw EmailNotPresentInContactException when email is not associated with contact`() {
        // Arrange
        val contactId = 1L
        val emailId = 2L
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )
        val existingEmail = Email(
            id = emailId,
            email = "old@example.com",
            contacts = mutableListOf()
        )
        every { contactRepository.findContactById(contactId) } returns existingContact
        every { emailRepository.findEmailById(emailId) } returns existingEmail

        // Act & Assert
        assertThrows(EmailNotPresentInContactException::class.java) {
            contactService.updateContactEmail(contactId, emailId, EmailDTO("new@example.com"))
        }
    }

    @Test
    fun `updateContactCategory should update contact category and return contact DTO`() {
        // Arrange
        val contactId = 1L
        val newCategory = ContactCategory.CUSTOMER
        val existingContact = Contact(
            id = contactId,
            name = "John",
            surname = "Doe",
            ssnCode = "123456",
            category = ContactCategory.UNKNOWN,
            messages = mutableListOf(),
            emails = mutableListOf(),
            addresses = mutableListOf(),
            telephoneNumbers = mutableListOf()
        )

        val capContact = slot<Contact>()

        every { contactRepository.findContactById(contactId) } returns existingContact
        every { contactRepository.save(capture(capContact)) } answers { capContact.captured }

        // Act
        val result = contactService.updateContactCategory(contactId, newCategory)

        // Assert
        verify(exactly = 1) { contactRepository.save(existingContact) }
        assertEquals(newCategory, capContact.captured.category)
        assertEquals(existingContact.toDto(), result)
    }


    @Test
    fun `updateContactCategory should throw ContactNotFoundException when contact does not exist`() {
        // Arrange
        val contactId = 1L
        val newCategory = ContactCategory.CUSTOMER
        every { contactRepository.findContactById(contactId) } returns null

        // Act & Assert
        assertThrows(ContactNotFoundException::class.java) {
            contactService.updateContactCategory(contactId, newCategory)
        }
    }
















// GetContact



    @Test
    fun saveContactSimple() {
        val contactId = 0L
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(1L, "sgeraci@polito.it", mutableListOf())))
        val contactDto = expectedContact.toDto()

        val capturedContact = slot<Contact>()

        every { contactRepository.save( capture(capturedContact) ) } returns expectedContact

        val actualSavedContact = contactService.saveContact(contactDto)

        verify(exactly = 1) { contactRepository.save( capturedContact.captured ) }

        assert(capturedContact.isCaptured)
        assertEquals(contactDto.name, capturedContact.captured.name )
        assertEquals(contactDto.surname, capturedContact.captured.surname)
        assertEquals(expectedContact.toDto(), actualSavedContact)
    }

    @Test
    fun addEmailToContactSimple() {
        val emailStr = "email@test.com"
        val email = Email(1L, emailStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf())
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(1L, "email@test.com", mutableListOf())))


        every { contactRepository.findContactById(contactId) } returns oldContact
        every { emailRepository.findEmailByEmail(emailStr) } returns email
        every { contactRepository.save( oldContact ) } returns expectedContact

        val actualContact = contactService.addContactEmail(contactId, email.toDto())

        verify(exactly = 1) { contactRepository.save( oldContact ) }

        assertEquals(expectedContact.toDto(), actualContact)
    }


    @Test
    fun addEmailToContactNotFound() {
        val emailStr = "email@test.com"
        val email = Email(1L, emailStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf())
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(1L, "email@test.com", mutableListOf())))

        every { contactRepository.findContactById(contactId) } returns null

        assertThrows<ContactNotFoundException> {
            contactService.addContactEmail(contactId, email.toDto())
        }

        verify(exactly = 0) { contactRepository.save( any() ) }
    }

    @Test
    fun addEmailToContactNewMail() {
        val emailStr = "email@test.com"
        val email = Email(1L, emailStr, mutableListOf())
        val contactId = 0L

        val capturedContact = slot<Contact>()

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf())
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf(Email(1L, "email@test.com", mutableListOf())))

        every { emailRepository.findEmailByEmail(emailStr) } returns null
        every { contactRepository.findContactById(contactId) } returns oldContact
        every { contactRepository.save( capture(capturedContact) ) } returns expectedContact

        val actualContact = contactService.addContactEmail(contactId, email.toDto())

        verify(exactly = 1) { contactRepository.save( any() ) }

        assert(capturedContact.captured.emails!!.any { it.email == emailStr })
        assertEquals(expectedContact.toDto(), actualContact)
    }

    @Test
    fun addAddressToContactSimple() {
        val addressStr = "Via Ternavasso 21"
        val address = Address(1L, addressStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = null, emails = mutableListOf())
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = mutableListOf(Address(1L, addressStr, mutableListOf())), telephoneNumbers = null, emails = mutableListOf())

        every { contactRepository.findContactById(contactId) } returns oldContact
        every { addressRepository.findAddressByName(addressStr) } returns address
        every { contactRepository.save( oldContact ) } returns expectedContact

        val actualContact = contactService.addContactAddress(contactId, address.toDto())

        verify(exactly = 1) { contactRepository.save( oldContact ) }

        assertEquals(expectedContact.toDto(), actualContact)
    }


    @Test
    fun addAddressToContactNotFound() {
        val addressStr = "Via Ternavasso 21"
        val address = Address(1L, addressStr, mutableListOf())
        val contactId = 0L

        every { contactRepository.findContactById(contactId) } returns null

        assertThrows<ContactNotFoundException> {
            contactService.addContactAddress(contactId, address.toDto())
        }

        verify(exactly = 0) { contactRepository.save( any() ) }
    }

    @Test
    fun addAddressToContactNewAddress() {
        val addressStr = "Via Ternavasso 21"
        val address = Address(1L, addressStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = mutableListOf(), telephoneNumbers = null, emails = null)
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = mutableListOf(Address(1L, addressStr, mutableListOf())), telephoneNumbers = null, emails = null)

        val capturedContact = slot<Contact>()

        every { addressRepository.findAddressByName(addressStr) } returns null
        every { contactRepository.findContactById(contactId) } returns oldContact
        every { contactRepository.save( capture(capturedContact) ) } returns expectedContact

        val actualContact = contactService.addContactAddress(contactId, address.toDto())

        verify(exactly = 1) { contactRepository.save( any() ) }

        assert(capturedContact.captured.addresses!!.any { it.name == addressStr })
        assertEquals(expectedContact.toDto(), actualContact)
    }

    @Test
    fun addTelephoneToContactSimple() {
        val telephoneStr = "+39 389110274"
        val telephone = Telephone(1L, telephoneStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = mutableListOf(), emails = null)
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = mutableListOf(), emails = null)
        expectedContact.telephoneNumbers?.add(telephone)

        every { contactRepository.findContactById(contactId) } returns oldContact
        every { telephoneRepository.findTelephoneByNumber(telephoneStr) } returns telephone
        every { contactRepository.save( oldContact ) } returns expectedContact

        val actualContact = contactService.addContactTelephone(contactId, telephone.toDto())

        verify(exactly = 1) { contactRepository.save( oldContact ) }

        assertEquals(expectedContact.toDto(), actualContact)
    }


    @Test
    fun addTelephoneToContactNotFound() {
        val telephoneStr = "+39 389110274"
        val telephone = Telephone(1L, telephoneStr, mutableListOf())
        val contactId = 0L

        every { contactRepository.findContactById(contactId) } returns null

        assertThrows<ContactNotFoundException> {
            contactService.addContactTelephone(contactId, telephone.toDto())
        }

        verify(exactly = 0) { contactRepository.save( any() ) }
    }

    @Test
    fun addTelephoneToContactNewAddress() {
        val telephoneStr = "+39 389110274"
        val telephone = Telephone(1L, telephoneStr, mutableListOf())
        val contactId = 0L

        val oldContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = mutableListOf(), emails = null)
        val expectedContact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = null, telephoneNumbers = mutableListOf(), emails = null)
        expectedContact.telephoneNumbers?.add(telephone)

        val capturedContact = slot<Contact>()

        every { telephoneRepository.findTelephoneByNumber(telephoneStr) } returns null
        every { contactRepository.findContactById(contactId) } returns oldContact
        every { contactRepository.save( capture(capturedContact) ) } returns expectedContact

        val actualContact = contactService.addContactTelephone(contactId, telephone.toDto())

        verify(exactly = 1) { contactRepository.save( any() ) }

        assert(capturedContact.captured.telephoneNumbers!!.any { it.number == telephoneStr })
        assertEquals(expectedContact.toDto(), actualContact)
    }

    @Test
    fun deleteContactSimple() {
        val contactId = 0L
        val telephone = Telephone(1L, "332345323", mutableListOf())
        val address = Address(1L, "Via Ternavasso 21", mutableListOf())
        val email = Email(1L, "email@di.prova", mutableListOf())

        val contact = Contact(name = "Simone", surname = "Geraci", ssnCode = "SSN_TEST", category = ContactCategory.CUSTOMER, messages = null, addresses = mutableListOf(), telephoneNumbers = mutableListOf(), emails = mutableListOf())
        contact.addAddress(address)
        contact.addEmail(email)
        contact.addTelephone(telephone)

        val capturedEmails = slot<List<Email>>()
        val capturedAddresses = slot<List<Address>>()
        val capturedTelephone = slot<List<Telephone>>()

        every { contactRepository.findContactById( contactId ) } returns contact
        every { emailRepository.findEmailByEmail( email.email ) } returns email
        every { addressRepository.findAddressByName( address.name ) } returns address
        every { telephoneRepository.findTelephoneByNumber( telephone.number ) } returns telephone

        every { emailRepository.findAll() } returns listOf(email)
        every { telephoneRepository.findAll() } returns listOf(telephone)
        every { addressRepository.findAll() } returns listOf(address)

        every { emailRepository.deleteAll(capture(capturedEmails)) } returns Unit
        every { telephoneRepository.deleteAll(capture(capturedTelephone)) } returns Unit
        every { addressRepository.deleteAll(capture(capturedAddresses)) } returns Unit

        every { contactRepository.delete( contact ) } returns Unit

        contactService.deleteContact(contactId)

        verify(exactly = 1) { contactRepository.delete( contact ) }

        /* delete also email/addresses/telephones if empty*/
        assert(capturedEmails.captured.find { it.email == email.email }!!.contacts!!.isEmpty())
        assert(capturedTelephone.captured.find { it.number == telephone.number }!!.contacts!!.isEmpty())
        assert(capturedAddresses.captured.find { it.name == address.name }!!.contacts!!.isEmpty())
    }

    @Test
    fun deleteContactNotFound() {
        val contactId = 0L

        every { contactRepository.findContactById( contactId ) } returns null

        assertThrows<ContactNotFoundException> {
            contactService.deleteContact(contactId)
        }

        verify(exactly = 0) { contactRepository.delete( any() ) }
    }
}
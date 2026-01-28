package org.example.walab4.services.contact


import org.example.walab4.dto.contact.*
import org.example.walab4.dto.message.EmailDto
import org.example.walab4.exceptions.contact.*
import org.example.walab4.model.contact.*
import org.example.walab4.repository.contact.AddressRepository
import org.example.walab4.repository.contact.ContactRepository
import org.example.walab4.repository.contact.EmailRepository
import org.example.walab4.repository.contact.TelephoneRepository
import org.example.walab4.services.contact.IContactService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ContactService(private val contactRepo: ContactRepository,
                     private val emailRepo: EmailRepository,
                     private val telephoneRepo: TelephoneRepository,
                     private val addressRepo: AddressRepository
): IContactService {

    override fun getAllContacts(
        page: Int,
        limit: Int,
        name: String?,
        surname: String?,
        email: String?,
        address: String?,
        telephone: String?
    ): List<ContactDto> {

        val paging = PageRequest.of(page, limit)

        val contacts = contactRepo.findAll(paging).content
        var filteredContacts = name?.let { contacts.filter { it.name.equals(name) } }?: contacts
        filteredContacts = surname?.let { contacts.filter { it.surname.equals(surname) } }?: filteredContacts

        val emailID = email?.let { emailRepo.findEmailByEmail(email)?: throw EmailNotFoundException( email ) }
        filteredContacts = emailID?.let { contacts.filter { it.emails?.contains(emailID) ?: false } }?: filteredContacts

        val addressID = address?.let { addressRepo.findAddressByName(address)?: throw AddressNotFoundException( address ) }
        filteredContacts = addressID?.let { contacts.filter { it.addresses?.contains(addressID) ?: false } }?: filteredContacts

        val telephoneID = telephone?.let { telephoneRepo.findTelephoneByNumber(telephone)?: TelephoneNotFoundException( telephone ) }
        filteredContacts = telephoneID?.let { contacts.filter { it.telephoneNumbers?.contains(telephoneID) ?: false } }?: filteredContacts

        return filteredContacts.map { it.toDto() }
    }

    override fun getContact(contactId: Long): ContactDto {
        //testVit
        val contact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException( contactId.toString() )

        return contact.toDto()
    }

    override fun saveContact(contactDTO: ContactDto): ContactDto {

        val contact = Contact( name = contactDTO.name, surname = contactDTO.surname, ssnCode = contactDTO.ssnCode, category = ContactCategory.valueOf(contactDTO.category),
            emails = mutableListOf(), telephoneNumbers = mutableListOf(), addresses = mutableListOf(), messages = mutableListOf()
        )


        val savedContact = contactRepo.save(contact)

        return savedContact.toDto()
    }


    override fun deleteContact(contactId: Long) {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val emailEntities = emailRepo.findAll()
        val addressEntities = addressRepo.findAll()
        val telephoneEntities = telephoneRepo.findAll()
        val emailIterator = emailEntities.iterator()
        val addressIterator = addressEntities.iterator()
        val telephoneIterator = telephoneEntities.iterator()

        while (emailIterator.hasNext()){
            val element = emailIterator.next()
            val existingElement = emailRepo.findEmailByEmail(element.email)!!
            existingElement.contacts.remove(existingContact)
            if (existingElement.contacts.isNotEmpty()) emailIterator.remove()
        }

        while (addressIterator.hasNext()){
            val element = addressIterator.next()
            val existingElement = addressRepo.findAddressByName(element.name)!!
            existingElement.contacts.remove(existingContact)
            if (existingElement.contacts.isNotEmpty()) addressIterator.remove()
        }

        while (telephoneIterator.hasNext()){
            val element = telephoneIterator.next()
            val existingElement = telephoneRepo.findTelephoneByNumber(element.number)!!
            existingElement.contacts.remove(existingContact)
            if (existingElement.contacts.isNotEmpty()) telephoneIterator.remove()
        }

        contactRepo.delete(existingContact)
        emailRepo.deleteAll(emailEntities)
        addressRepo.deleteAll(addressEntities)
        telephoneRepo.deleteAll(telephoneEntities)
    }

    override fun deleteContactEmail(contactId: Long, emailId: Long) {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingMail = emailRepo.findEmailById(emailId)?: throw EmailNotFoundException()

        if (!existingContact.emails?.contains(existingMail)!!) throw EmailNotPresentInContactException(contactId.toString(),emailId.toString())

        existingContact.emails?.remove(existingMail)
        existingMail.contacts.remove(existingContact)
        contactRepo.save(existingContact)
        emailRepo.save(existingMail)

        if (existingMail.contacts.isEmpty()) emailRepo.delete(existingMail)
    }

    override fun addContactTelephone(contactId: Long, telephone: TelephoneDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val telephoneAdded = telephoneRepo.findTelephoneByNumber(telephone.number)?: Telephone( number = telephone.number, contacts = mutableListOf())

        existingContact.telephoneNumbers?.add(telephoneAdded)
        telephoneAdded.contacts.add(existingContact)

        contactRepo.save(existingContact)

        return existingContact.toDto()
    }

    override fun deleteContactTelephone(contactId: Long, telephoneId: Long) {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingTelephone = telephoneRepo.findTelephoneById(telephoneId)?: throw TelephoneNotFoundException()

        if (!existingContact.telephoneNumbers?.contains(existingTelephone)!!)
            throw TelephoneNotPresentInContactException(contactId.toString(),telephoneId.toString())

        existingContact.telephoneNumbers?.remove(existingTelephone)
        existingTelephone.contacts.remove(existingContact)
        contactRepo.save(existingContact)
        telephoneRepo.save(existingTelephone)

        if (existingTelephone.contacts.isEmpty()) telephoneRepo.delete(existingTelephone)
    }

    override fun updateContactTelephone(contactId: Long, telephoneId: Long, newTelephone: TelephoneDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingTelephone = telephoneRepo.findTelephoneById(telephoneId)?: throw TelephoneNotFoundException()

        if (!existingContact.telephoneNumbers?.contains(existingTelephone)!!)
            throw TelephoneNotPresentInContactException(contactId.toString(),telephoneId.toString())

        existingTelephone.apply {
            number = newTelephone.number
        }

        telephoneRepo.save(existingTelephone)

        return existingContact.toDto()
    }

    override fun addContactAddress(contactId: Long, address: AddressDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val addressAdded = addressRepo.findAddressByName(address.address)?: Address( name = address.address, contacts = mutableListOf())

        existingContact.addresses?.add(addressAdded)
        addressAdded.contacts.add(existingContact)

        contactRepo.save(existingContact)

        return existingContact.toDto()
    }

    override fun deleteContactAddress(contactId: Long, addressId: Long) {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingAddress = addressRepo.findAddressById(addressId)?: throw AddressNotFoundException()

        if (!existingContact.addresses?.contains(existingAddress)!!)
            throw AddressNotPresentInContactException(contactId.toString(),addressId.toString())

        existingContact.addresses?.remove(existingAddress)
        existingAddress.contacts.remove(existingContact)
        contactRepo.save(existingContact)
        addressRepo.save(existingAddress)

        if (existingAddress.contacts.isEmpty()) addressRepo.delete(existingAddress)
    }

    override fun updateContactAddress(contactId: Long, addressId: Long, newAddress: AddressDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingAddress = addressRepo.findAddressById(addressId)?: throw AddressNotFoundException()

        if (!existingContact.addresses?.contains(existingAddress)!!)
            throw AddressNotPresentInContactException(contactId.toString(), addressId.toString())

        existingAddress.apply {
            name = newAddress.address
        }

        addressRepo.save(existingAddress)

        return existingContact.toDto()
    }

    override fun getContactEmails(contactId: Long): List<EmailDTO> {
        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val emails = emailRepo.findEmailsByContacts(listOf(existingContact))

        return emails.map { it.toDto() }
    }

    override fun getContactAddresses(contactId: Long): List<AddressDTO> {
        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val addresses = addressRepo.findAddressesByContacts(listOf(existingContact))

        return addresses.map { it.toDto() }
    }

    override fun getContactTelephones(contactId: Long): List<TelephoneDTO> {
        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val telephones = telephoneRepo.findTelephonesByContacts(listOf(existingContact))

        return telephones.map { it.toDto() }
    }

    override fun updateContactEmail(contactId: Long, emailId: Long, newEmail: EmailDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val existingEmail = emailRepo.findEmailById(emailId)?: throw EmailNotFoundException()

        if (!existingContact.emails?.contains(existingEmail)!!)
            throw EmailNotPresentInContactException(contactId.toString(), emailId.toString())

        existingEmail.apply {
            email = newEmail.email
        }

        emailRepo.save(existingEmail)

        return existingContact.toDto()
    }

    override fun addContactEmail(contactId: Long, email: EmailDTO): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()
        val emailAdded = emailRepo.findEmailByEmail(email.email)?: Email(email = email.email, contacts = mutableListOf() )

        existingContact.emails?.add(emailAdded)
        emailAdded.contacts.add(existingContact)

        contactRepo.save(existingContact)

        return existingContact.toDto()
    }

    override fun updateContactCategory(contactId: Long, newCategory: ContactCategory): ContactDto {

        val existingContact = contactRepo.findContactById(contactId)?: throw ContactNotFoundException()

        existingContact.apply {
            category = newCategory
        }

        contactRepo.save(existingContact)

        return existingContact.toDto()
    }

}

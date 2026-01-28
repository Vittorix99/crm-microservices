package org.example.walab4.services.contact

import org.example.walab4.dto.contact.AddressDTO
import org.example.walab4.dto.contact.ContactDto
import org.example.walab4.dto.contact.EmailDTO
import org.example.walab4.dto.contact.TelephoneDTO
import org.example.walab4.dto.message.EmailDto
import org.example.walab4.model.contact.ContactCategory

interface IContactService {
    fun getAllContacts(page: Int, limit: Int, name: String?, surname: String?, email: String?, address: String?, telephone: String?): List<ContactDto>

    fun getContact(contactId: Long): ContactDto

    fun saveContact(contactDTO: ContactDto): ContactDto

    fun addContactEmail(contactId: Long, email: EmailDTO): ContactDto

    fun updateContactCategory(contactId: Long, newCategory: ContactCategory): ContactDto

    fun deleteContact(contactId: Long)

    fun deleteContactEmail(contactId: Long, emailId: Long)

    fun addContactTelephone(contactId: Long, telephone: TelephoneDTO): ContactDto

    fun deleteContactTelephone(contactId: Long, telephoneId: Long)

    fun updateContactTelephone(contactId: Long, telephoneId: Long, newTelephone: TelephoneDTO): ContactDto

    fun addContactAddress(contactId: Long, address: AddressDTO): ContactDto

    fun deleteContactAddress(contactId: Long, addressId: Long)

    fun updateContactEmail(contactId: Long, emailId: Long, newEmail: EmailDTO): ContactDto

    fun updateContactAddress(contactId: Long, addressId: Long, newAddress: AddressDTO): ContactDto

    fun getContactEmails(contactId: Long): List<EmailDTO>

    fun getContactAddresses(contactId: Long): List<AddressDTO>

    fun getContactTelephones(contactId: Long): List<TelephoneDTO>
}
package org.example.walab4.repository.contact

import org.example.walab4.model.contact.Contact
import org.example.walab4.model.contact.Telephone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TelephoneRepository: JpaRepository<Telephone, Long> {

    fun findTelephoneById(id: Long): Telephone?

    fun findTelephoneByNumber(number: String): Telephone?

    fun findTelephonesByContacts(contacts: List<Contact>): List<Telephone>
}
package org.example.walab4.repository.contact

import org.example.walab4.model.contact.Contact
import org.example.walab4.model.contact.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailRepository: JpaRepository<Email, Long> {

    fun findEmailByEmail(email: String): Email?

    fun findEmailById(id: Long): Email?

    fun findEmailsByContacts(contacts: List<Contact>): List<Email>
}
package org.example.walab4.repository.contact

import org.example.walab4.model.contact.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository: JpaRepository<Contact, Long> {
    fun findContactById(id: Long): Contact?;
    @Query("SELECT c FROM Contact c LEFT JOIN c.emails e WHERE e.email = :email")
    fun findByEmail(email: String?): List<Contact>

    @Query("SELECT c FROM Contact c LEFT JOIN c.telephoneNumbers t WHERE t.number = :phoneNumber")
    fun findByTelephone(phoneNumber: String?): List<Contact>




}
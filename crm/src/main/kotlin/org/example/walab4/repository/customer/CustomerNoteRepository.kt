package org.example.walab4.repository.customer

import org.example.walab4.model.customer.CustomerNote
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerNoteRepository: JpaRepository<CustomerNote, Long> {
    fun findCustomerNoteById(noteId: Long): CustomerNote?
    fun findCustomersNoteByCustomerId(customerId: Long): List<CustomerNote>
}
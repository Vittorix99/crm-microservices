package org.example.walab4.repository.message

import org.example.walab4.model.message.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message, Long> {
    fun findMessageById(id: Long): Message?;

}


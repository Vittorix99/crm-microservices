package org.example.walab4.repository.message

import org.example.walab4.model.message.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findEventById(eventId:Long) : Event;
    fun findEventsByMessageId (messageId : Long) : List<Event>;

}
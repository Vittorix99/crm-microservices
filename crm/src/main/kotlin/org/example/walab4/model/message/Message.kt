package org.example.walab4.model.message

import jakarta.persistence.*
import org.example.walab4.model.contact.Contact
import org.example.walab4.model.message.Event
import java.util.Date

enum class  MessageStatus{
    RECEIVED, READ, PROCESSING, DONE, FAILED, DISCARDED
}

enum class Channel {
    PHONECALL, TEXTMESSAGE, EMAIL
}

enum class Priority {
    LOW, MEDIUM, HIGH
}

@Entity
open class Message (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,
    open var date : Date,
    open var subject: String?,
    @Column(columnDefinition = "TEXT")
    open var body: String?,
    open var channel: Channel,
    open var state: MessageStatus,
    open var priority: Priority,

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    open var sender : Contact,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "message")
    open var events: MutableList<Event>?,
)
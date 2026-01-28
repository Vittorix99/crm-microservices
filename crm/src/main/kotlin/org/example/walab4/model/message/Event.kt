package org.example.walab4.model.message

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
open class Event (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   open var id: Long = 0,
   open var initialState: MessageStatus,
   open var finalState: MessageStatus,
   open var comments: String = "",
   open var timestamp: Timestamp,

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
  open  var message: Message
)
package org.example.walab4.dto.message

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.message.Channel
import org.example.walab4.model.message.Message
import org.example.walab4.model.message.MessageStatus
import org.example.walab4.model.message.Priority

data class MessageDto(
    @JsonProperty("id")
    var id : Long? = null,

    @JsonProperty("sender")
    var sender : String,

    @JsonProperty("subject")
    var subject: String?,

    @JsonProperty("body")
    var body: String?,

    @JsonProperty("channel")
    var channel: Channel,

    @JsonProperty ("status")
    var status: MessageStatus?,

    @JsonProperty("priority")
    var priority: Priority,

    @JsonProperty("transitions")
    var transitions: Array<MessageStatus>,

    @JsonProperty("contactId")
    var contactId : Long? = null
)

fun Message.toDto(transitions: Array<MessageStatus>): MessageDto {
    return MessageDto(this.id, this.sender.emails?.firstOrNull()?.email!!, this.subject, this.body, this.channel, this.state, this.priority, transitions, this.sender.id)
}

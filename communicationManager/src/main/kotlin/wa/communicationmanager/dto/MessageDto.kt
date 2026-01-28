package wa.communicationmanager.dto

import com.fasterxml.jackson.annotation.JsonProperty
import wa.communicationmanager.message.Channel
import wa.communicationmanager.message.MessageStatus
import wa.communicationmanager.message.Priority

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

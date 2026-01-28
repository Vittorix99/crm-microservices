package org.example.walab4.dto.message

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.message.Event
import org.example.walab4.model.message.MessageStatus
import java.sql.Timestamp

data class EventDto(

    @JsonProperty("initial_state")
    var initialState: MessageStatus,

    @JsonProperty("final_state")
    var finalState: MessageStatus,

    @JsonProperty("comments")
    var comments: String,

    @JsonProperty("timestamp")
    var timestamp: Timestamp,
)

fun Event.toDto(): EventDto {
    return EventDto(this.initialState, this.finalState, this.comments, this.timestamp)
}

package wa.communicationmanager.dto

import com.fasterxml.jackson.annotation.JsonProperty
import wa.communicationmanager.message.MessageStatus
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

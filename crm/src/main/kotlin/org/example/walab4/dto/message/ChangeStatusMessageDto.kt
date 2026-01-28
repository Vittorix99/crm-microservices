package org.example.walab4.dto.message

import com.fasterxml.jackson.annotation.JsonProperty

data class ChangeStatusMessageDto(
    @JsonProperty("stateStr")
    var stateStr: String,
    @JsonProperty("comment")
    var comment: String,
)

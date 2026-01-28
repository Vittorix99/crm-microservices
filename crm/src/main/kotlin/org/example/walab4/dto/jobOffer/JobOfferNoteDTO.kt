package org.example.walab4.dto.jobOffer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.jobOffer.JobOfferNote

data class JobOfferNoteDTO(

    @JsonProperty("id")
    var id: Long?=null,

    @JsonProperty("description")
    var description: String,
)

fun JobOfferNote.toDto(): JobOfferNoteDTO {
    return JobOfferNoteDTO(this.id, this.description)
}
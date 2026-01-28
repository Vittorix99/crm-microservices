package org.example.walab4.dto.customer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.customer.CustomerNote


data class CustomerNoteDto(
    @JsonProperty()
    var id: Long? = null,
    @JsonProperty()
    var title:String,

    @JsonProperty()
    var description: String,

    @JsonProperty()
    var contactId: Long?
)

fun CustomerNote.toDto(): CustomerNoteDto {
    return CustomerNoteDto(this.id,this.title, this.description,

        this.customer?.id ?: null)
}
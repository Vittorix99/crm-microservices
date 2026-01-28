package org.example.walab4.dto.contact

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.contact.Email

data class EmailDTO(
    @JsonProperty()
    var id: Long?,

    @JsonProperty("email")
    val email: String
)

fun Email.toDto(): EmailDTO {
    return EmailDTO(this.id, this.email)
}

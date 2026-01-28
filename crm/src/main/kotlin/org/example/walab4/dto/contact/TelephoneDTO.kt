package org.example.walab4.dto.contact

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.contact.Telephone

data class TelephoneDTO(
    @JsonProperty()
    var id: Long?,

    @JsonProperty()
    val number: String
)

fun Telephone.toDto(): TelephoneDTO {
    return TelephoneDTO(this.id, this.number)
}
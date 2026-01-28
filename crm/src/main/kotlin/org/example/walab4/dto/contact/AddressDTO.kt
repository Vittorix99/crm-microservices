package org.example.walab4.dto.contact

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.contact.Address

data class AddressDTO(
    @JsonProperty()
    var id: Long?,

    @JsonProperty()
    var address: String,
)

fun Address.toDto(): AddressDTO {
    return AddressDTO(this.id, this.name)
}

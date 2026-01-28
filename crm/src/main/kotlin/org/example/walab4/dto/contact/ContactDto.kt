package org.example.walab4.dto.contact

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.contact.Contact
import org.example.walab4.model.contact.ContactCategory

data class ContactDto(
    @JsonProperty
    var id: Long?,

    @JsonProperty
    var name: String?,

    @JsonProperty
    var surname: String?,

    @JsonProperty
    var ssnCode: String?,

    @JsonProperty
    var category: String = ContactCategory.UNKNOWN.name,

    @JsonProperty
    var emails: MutableList<EmailDTO> = mutableListOf(),

    @JsonProperty
    var addresses: MutableList<AddressDTO> = mutableListOf(),

    @JsonProperty
    var numbers: MutableList<TelephoneDTO> = mutableListOf()
)

fun Contact.toDto(): ContactDto {
    return ContactDto(
        id = this.id,
        name = this.name,
        surname = this.surname,
        ssnCode = this.ssnCode,
        category = this.category.name,
        emails = this.emails?.map { it.toDto() }?.toMutableList() ?: mutableListOf(),
        addresses = this.addresses?.map { it.toDto() }?.toMutableList() ?: mutableListOf(),
        numbers = this.telephoneNumbers?.map { it.toDto() }?.toMutableList() ?: mutableListOf()
    )
}

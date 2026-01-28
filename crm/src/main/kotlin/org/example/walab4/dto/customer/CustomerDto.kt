package org.example.walab4.dto.customer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.customer.Customer

data class CustomerDto(
    @JsonProperty
    var id: Long?,

    @JsonProperty
    var name: String?,

    @JsonProperty
    var surname: String?,

    @JsonProperty
    var ssnCode: String?,

    @JsonProperty
    var category: String,

    @JsonProperty
    var emails: MutableList<String>? = mutableListOf(),

    @JsonProperty
    var addresses: MutableList<String>? = mutableListOf(),

    @JsonProperty
    var numbers: MutableList<String>? = mutableListOf()
)

fun Customer.toDto(): CustomerDto {
    return CustomerDto(
        id = this.id,
        name = this.name,
        surname = this.surname,
        ssnCode = this.ssnCode,
        category = this.category.name,
        emails = this.emails?.map { it.email }?.toMutableList() ?: mutableListOf(),
        addresses = this.addresses?.map { it.name }?.toMutableList() ?: mutableListOf(),
        numbers = this.telephoneNumbers?.map { it.number }?.toMutableList() ?: mutableListOf()
    )
}

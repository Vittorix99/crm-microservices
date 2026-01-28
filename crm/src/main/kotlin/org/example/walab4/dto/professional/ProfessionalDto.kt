package org.example.walab4.dto.professional

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.dto.contact.AddressDTO
import org.example.walab4.dto.contact.EmailDTO
import org.example.walab4.dto.contact.TelephoneDTO
import org.example.walab4.dto.contact.toDto
import org.example.walab4.model.professional.Professional

data class ProfessionalDto(
    @JsonProperty
    var id: Long?,

    @JsonProperty("name")
    var name: String?,

    @JsonProperty("surname")
    var surname: String?,

    @JsonProperty("ssnCode")
    var ssnCode: String?,

    @JsonProperty("category")
    var category: String = "PROFESSIONAL",

    @JsonProperty("location")
    var location: String,

    @JsonProperty("dailyRate")
    var dailyRate: Double,

    @JsonProperty("state")
    var state: String,

    @JsonProperty("emails")
    var emails: MutableList<EmailDTO>? = mutableListOf(),

    @JsonProperty("addresses")
    var addresses: MutableList<AddressDTO>? = mutableListOf(),

    @JsonProperty("numbers")
    var numbers: MutableList<TelephoneDTO>? = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        return other is ProfessionalDto &&
                this.name == other.name &&
                this.surname == other.surname &&
                this.ssnCode == other.ssnCode &&
                this.category == other.category &&
                this.location == other.location &&
                this.dailyRate == other.dailyRate &&
                this.state == other.state
    }
}

fun Professional.toDto(): ProfessionalDto {
    return ProfessionalDto(
        id = this.id,
        name = this.name,
        surname = this.surname,
        ssnCode = this.ssnCode,
        category = this.category.name,
        location = this.location,
        dailyRate = this.dailyRate,
        state = this.state.name,
        emails = this.emails?.map { it.toDto() }?.toMutableList() ?: mutableListOf(),
        addresses = this.addresses?.map { it.toDto() }?.toMutableList() ?: mutableListOf(),
        numbers = this.telephoneNumbers?.map { it.toDto() }?.toMutableList() ?: mutableListOf()
    )
}

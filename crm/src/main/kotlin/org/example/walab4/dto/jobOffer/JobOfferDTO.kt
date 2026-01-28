package org.example.walab4.dto.jobOffer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.jobOffer.JobOffer

data class JobOfferDTO(

    @JsonProperty("id")
    var id: Long?=null,

    @JsonProperty("description")
    var description: String?=null,

    @JsonProperty("status")
    var status: String?=null,

    @JsonProperty("duration")
    var duration: Int,

    @JsonProperty("value")
    var value: Double?=null,

    @JsonProperty("professional")
    var professional : Long?=null,

    @JsonProperty("customer")
    var customer:Long,

    @JsonProperty("interview")
    var interview: List<Long?> = mutableListOf(),
)

fun JobOffer.toDto(): JobOfferDTO {
    return JobOfferDTO(this.id, this.description, this.status.name, this.duration, this.value,
        this.professional?.id, this.customer.id, this.interviews.map { it.id }
    )
}
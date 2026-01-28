package org.example.walab4.dto.jobOffer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.jobOffer.Proposal

data class ProposalDTO(
    @JsonProperty("id")
    var id: Long?,

    @JsonProperty("status")
    var status: String = "PENDING",

    @JsonProperty("jobOffer")
    var jobOffer: Long?,

    @JsonProperty("professional")
    var professional: Long?,

    @JsonProperty("description")
    var description: String?
)

fun Proposal.toDto() : ProposalDTO {
    return ProposalDTO(this.id, this.status.toString(), this.jobOffer?.id, this.professional?.id, this.description)
}
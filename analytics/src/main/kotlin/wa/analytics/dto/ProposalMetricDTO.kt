package wa.analytics.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import wa.analytics.model.ProposalMetric
import java.time.LocalDate

data class ProposalMetricDTO (
    @JsonProperty("id")
    var id: Long? = null,
    @JsonProperty("description")
    var description: String? = null,
    @JsonProperty("status")
    var status: String? = null,
    @JsonProperty("date")
    var date: String = LocalDate.now().toString()
)

fun ProposalMetric.toDto(): ProposalMetricDTO {
    return ProposalMetricDTO(this.id, this.description, this.status, this.time.toString())
}
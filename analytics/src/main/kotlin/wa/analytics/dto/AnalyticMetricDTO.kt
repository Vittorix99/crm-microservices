package wa.analytics.dto

import com.fasterxml.jackson.annotation.JsonProperty
import wa.analytics.model.AnalyticMetric

data class AnalyticMetricDTO(
    @JsonProperty()
    var name: String,
    @JsonProperty()
    var count: Int
)

fun AnalyticMetric.toDto(): AnalyticMetricDTO {
    return AnalyticMetricDTO(this.name, this.count)
}

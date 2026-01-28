package org.example.walab4.dto.jobOffer

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.dto.professional.ProfessionalDto
import org.example.walab4.dto.professional.toDto
import org.example.walab4.model.jobOffer.Interview
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class InterviewDTO(
    @JsonProperty("id")
    var id: Long?= null,
    @JsonProperty("feedback")
    var feedback: String,
    @JsonProperty("date")
    var date: String,
    @JsonProperty("jobOffer")
    var jobOffer: Long?=null,
    @JsonProperty()
    var professional: Long?=null,
)

fun InterviewDTO.toInterview(): Interview {
    val instant = Instant.parse(this.date)

    return Interview(
        this.id, this.feedback, instant.atZone(ZoneId.systemDefault()).toLocalDate(), null, null
    )
}

fun Interview.toDto() : InterviewDTO {
    return InterviewDTO(this.id,this.feedback, this.date.format(DateTimeFormatter.ISO_DATE), this.jobOffer?.id, this.professional?.let { it.id } )
}
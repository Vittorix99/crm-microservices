package org.example.walab4.dto.professional

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.professional.ProfessionalNote

data class ProfessionalNoteDto(
    @JsonProperty()
    var id: Long? = null,
    @JsonProperty()
    var title: String,
    @JsonProperty()
    var description: String,
    @JsonProperty()
    var contactId:Long?

)

fun ProfessionalNote.toDto(): ProfessionalNoteDto {
    return ProfessionalNoteDto(this.id, this.title, this.description, this.professional?.id?:null )
}
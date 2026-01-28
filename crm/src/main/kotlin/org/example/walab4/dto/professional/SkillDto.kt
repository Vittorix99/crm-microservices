package org.example.walab4.dto.professional

import com.fasterxml.jackson.annotation.JsonProperty
import org.example.walab4.model.professional.Skill

data class SkillDto(
    @JsonProperty()
    var id: Long?,
    @JsonProperty()
    var skill: String
) {
    override fun equals(other: Any?): Boolean {
        return this.skill == (other as SkillDto).skill
    }
}
fun Skill.toDto(): SkillDto {
    return SkillDto(this.id, this.skill!!)
}


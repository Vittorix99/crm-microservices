package org.example.walab4.model.professional

import jakarta.persistence.*
import org.example.walab4.dto.professional.SkillDto
import org.example.walab4.model.jobOffer.JobOffer

@Entity
class Skill (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var skill: String? = null,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    val jobOffers: MutableList<JobOffer> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    val professionals: MutableList<Professional> = mutableListOf()
) {
    constructor(skillDto: SkillDto): this(skill = skillDto.skill)
}
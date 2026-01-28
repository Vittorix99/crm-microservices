package org.example.walab4.repository.professional

import org.example.walab4.model.professional.Professional
import org.example.walab4.model.professional.Skill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SkillRepository : JpaRepository<Skill, Long>{
    fun findSkillById(id: Long): Skill?
    fun findSkillBySkill(skill: String): Skill?
    fun findSkillsByProfessionals(professionals: List<Professional>): List<Skill>
    @Query("SELECT c FROM Skill c LEFT JOIN c.professionals p WHERE p.id = :professionalId")
    fun findSkillsByProfessional(professionalId: Long): List<Skill>

    @Query("SELECT c FROM Skill c LEFT JOIN c.jobOffers jo WHERE jo.id = :jobOfferId")
    fun findSkillByJobOffers(jobOfferId: Long): List<Skill>
}
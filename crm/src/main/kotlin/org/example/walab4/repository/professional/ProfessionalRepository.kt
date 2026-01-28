package org.example.walab4.repository.professional

import org.example.walab4.model.professional.EmploymentState
import org.example.walab4.model.professional.Professional
import org.example.walab4.model.professional.Skill
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProfessionalRepository: JpaRepository<Professional, Long> {
    fun findProfessionalById(id: Long): Professional?
    @Query("SELECT p FROM Professional p LEFT JOIN p.ownedSkills s WHERE s.skill = :skill")
    fun findProfessionalsByOwnedSkill(skill: String):  List<Professional>
    fun findProfessionalsByState(state: EmploymentState): List<Professional>
    fun findProfessionalsByLocation(location: String): List<Professional>
    @Query("SELECT p FROM Professional p left join p.ownedSkills s " +
            "WHERE (s IN :skills)" +
            "AND (:state IS NULL OR p.state = :state)" +
            "and (:location IS NULL OR p.location = :location)"
    )
    fun findAllProfessionalFilteredBySkillStateLocation(skills: List<Skill>?, state: EmploymentState?, location: String?, pageable: PageRequest): List<Professional>
    @Query("SELECT p FROM Professional p " +
            "WHERE (:state IS NULL OR p.state = :state)" +
            "and (:location IS NULL OR p.location = :location)"
    )
    fun findAllProfessionalFilteredByStateAndLocation(state: EmploymentState?, location: String?, pageable: PageRequest): List<Professional>

}
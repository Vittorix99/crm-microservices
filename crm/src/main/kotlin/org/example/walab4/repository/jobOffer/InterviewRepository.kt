package org.example.walab4.repository.jobOffer

import org.example.walab4.dto.jobOffer.JobOfferDTO
import org.example.walab4.model.jobOffer.Interview
import org.example.walab4.model.jobOffer.JobOffer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InterviewRepository: JpaRepository<Interview, Long> {

    fun findInterviewById(interviewId: Long): Interview?

    @Query("SELECT i FROM Interview i LEFT JOIN i.jobOffer jo WHERE jo.id = :jobOfferId")
    fun findInterviewsByJobOfferId(jobOfferId: Long): List<Interview>

    @Query("SELECT i FROM Interview i LEFT JOIN i.professional c WHERE c.id = :professionalId")
    fun findInterviewsByCandidates(professionalId: Long): List<Interview>

}
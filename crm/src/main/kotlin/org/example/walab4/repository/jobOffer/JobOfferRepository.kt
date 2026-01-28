package org.example.walab4.repository.jobOffer

import org.example.walab4.model.jobOffer.JobOfferStatus
import org.example.walab4.model.jobOffer.JobOffer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JobOfferRepository: JpaRepository<JobOffer, Long> {

    fun findJobOfferById(id: Long): JobOffer?
    @Query("SELECT jo FROM JobOffer jo WHERE jo.customer.id = :customerId AND jo.status NOT IN ( org.example.walab4.model.jobOffer.JobOfferStatus.ABORTED, org.example.walab4.model.jobOffer.JobOfferStatus.CONSOLIDATED, org.example.walab4.model.jobOffer.JobOfferStatus.DONE)")
    fun findOpenJobOffersByCustomerId(customerId: Long, pageable: Pageable):Page<JobOffer>?
    @Query("SELECT jo FROM JobOffer jo WHERE jo.professional.id = :professionalId AND jo.status IN (org.example.walab4.model.jobOffer.JobOfferStatus.CONSOLIDATED, org.example.walab4.model.jobOffer.JobOfferStatus.DONE)")
    fun findAcceptedJobOffersByProfessionalId(professionalId: Long, pageable: Pageable):Page<JobOffer>?

    @Query("SELECT jo FROM JobOffer jo WHERE (:customerId IS NULL OR jo.customer.id = :customerId) AND (:professionalId IS NULL OR jo.professional.id = :professionalId)")
    fun findAllFiltered(
        @Param("customerId") customerId: Long?,
        @Param("professionalId") professionalId: Long?,
        pageable: Pageable
    ): Page<JobOffer>?











}


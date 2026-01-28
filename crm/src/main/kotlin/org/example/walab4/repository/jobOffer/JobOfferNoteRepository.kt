package org.example.walab4.repository.jobOffer

import org.example.walab4.model.jobOffer.JobOfferNote
import org.springframework.data.jpa.repository.JpaRepository

interface JobOfferNoteRepository: JpaRepository<JobOfferNote,Long> {
    fun findJobOfferNotesById(noteId: Long): JobOfferNote?

}
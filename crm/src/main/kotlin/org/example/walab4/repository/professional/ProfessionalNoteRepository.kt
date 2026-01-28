package org.example.walab4.repository.professional

import org.example.walab4.model.jobOffer.JobOfferNote
import org.example.walab4.model.professional.ProfessionalNote
import org.springframework.data.jpa.repository.JpaRepository

interface ProfessionalNoteRepository: JpaRepository<ProfessionalNote, Long> {
    fun findProfessionalNoteById(noteId: Long): ProfessionalNote?
    fun findProfessionalsNoteByProfessionalId(professionalId: Long): List<ProfessionalNote>

}
package org.example.walab4.services.professional

import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.professional.ProfessionalDto
import org.example.walab4.model.jobOffer.Interview
import org.example.walab4.model.professional.Professional

interface IProfessionalService {

    fun addInterviewToProfessional(professionalId: Long, interviewDTO: InterviewDTO): ProfessionalDto
}
package org.example.walab4.services.interview

import org.example.walab4.dto.jobOffer.InterviewDTO
import org.example.walab4.dto.jobOffer.toDto
import org.example.walab4.exceptions.interview.InterviewNotFoundException
import org.example.walab4.exceptions.professional.ProfessionalNotFoundException
import org.example.walab4.repository.jobOffer.InterviewRepository
import org.example.walab4.repository.jobOffer.JobOfferRepository
import org.example.walab4.repository.professional.ProfessionalRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InterviewService (
    private val interviewRepository: InterviewRepository,
    private val professionalRepository: ProfessionalRepository,
    private val jobOfferRepository: JobOfferRepository
) : IInterviewService {
    override fun addInterviewCandidate(interviewId: Long, professionalId: Long): InterviewDTO {
        var resProfessional = professionalRepository.findProfessionalById(professionalId) ?: throw ProfessionalNotFoundException()
        var resInterview = interviewRepository.findInterviewById(interviewId) ?: throw InterviewNotFoundException()

        resProfessional.addInterview(resInterview);

        /* Salviamo entrambi gli oggetti*/
        professionalRepository.save(resProfessional);
        interviewRepository.save(resInterview);

        return resInterview.toDto();
    }
}
package org.example.walab4.services.interview

import org.example.walab4.dto.jobOffer.InterviewDTO

interface IInterviewService {

    fun addInterviewCandidate(interviewId: Long, professionalId: Long): InterviewDTO
}
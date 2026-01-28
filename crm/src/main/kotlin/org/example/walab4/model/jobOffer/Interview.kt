package org.example.walab4.model.jobOffer

import jakarta.persistence.*
import org.example.walab4.model.professional.Professional
import java.time.LocalDate

@Entity
class Interview (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var feedback: String,
    var date: LocalDate,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name="professional_id", unique = false, nullable = true)
    var professional: Professional?,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name = "job_offer", unique = false)
    var jobOffer: JobOffer?
) {
    fun addCandidate(candidate: Professional) {
        this.professional = professional
        candidate.interviews.add(this)
    }
}

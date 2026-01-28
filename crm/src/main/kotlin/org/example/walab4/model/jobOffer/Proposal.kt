package org.example.walab4.model.jobOffer

import jakarta.persistence.*
import org.example.walab4.model.professional.Professional

@Entity
class Proposal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ProposalStatus = ProposalStatus.PENDING,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_offer_id")
    var jobOffer: JobOffer,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professional_id")
    var professional: Professional,

    @Column(nullable = true)
    var description: String?
) {
    // Additional methods or business logic can be added here.
}
enum class ProposalStatus {
    PENDING, ACCEPTED, ABORTED
}
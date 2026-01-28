package org.example.walab4.model.jobOffer

import jakarta.persistence.*

@Entity
class JobOfferNote (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    var id: Long? = null,
    var description: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name="job_offer_id", nullable = false)
    var jobOffer: JobOffer,
){
    fun copy(id: Long?= this.id, description: String = this.description, jobOffer: JobOffer = this.jobOffer): JobOfferNote {
        return JobOfferNote(id, description, jobOffer)
    }
}

package org.example.walab4.model.professional

import jakarta.persistence.*
import org.example.walab4.dto.professional.ProfessionalNoteDto

@Entity
class ProfessionalNote (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    var id: Long? = null,
    var title: String,
    var description: String,

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name="professional_id", nullable = false)
    var professional: Professional?,
) {
    constructor(dto: ProfessionalNoteDto): this(
        null, dto.title, dto.description, null
    )

}
package org.example.walab4.model.customer

import jakarta.persistence.*
import org.example.walab4.dto.customer.CustomerNoteDto

@Entity
@Table(name = "customer_note")
class CustomerNote (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    var id: Long? = null,
    var title: String,
    var description: String,


    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable = false)
    var customer: Customer?,
){
    constructor(dto: CustomerNoteDto): this(
        id=null,
        dto.title,
        dto.description,
        null

    )

}


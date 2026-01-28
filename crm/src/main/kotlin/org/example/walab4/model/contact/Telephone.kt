package org.example.walab4.model.contact

import jakarta.persistence.*
import org.example.walab4.model.contact.Contact

@Entity
class Telephone (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var number: String,

    @ManyToMany(mappedBy = "telephoneNumbers")
    var contacts: MutableList<Contact>
)
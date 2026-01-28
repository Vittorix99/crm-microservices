package org.example.walab4.model.contact

import jakarta.persistence.*

@Entity
class Address (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String,

    @ManyToMany(mappedBy = "addresses")
    var contacts: MutableList<Contact>
)
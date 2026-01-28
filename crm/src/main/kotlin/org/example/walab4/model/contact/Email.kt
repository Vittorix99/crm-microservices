package org.example.walab4.model.contact

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.example.walab4.model.contact.Contact

@Entity
class Email (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var email: String,

    @ManyToMany(mappedBy = "emails")
    var contacts: MutableList<Contact>
)
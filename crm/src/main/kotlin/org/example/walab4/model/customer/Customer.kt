package org.example.walab4.model.customer

import jakarta.persistence.*
import org.example.walab4.dto.customer.CustomerDto
import org.example.walab4.model.jobOffer.JobOffer
import org.example.walab4.model.contact.*
import org.example.walab4.model.message.Message

@Entity
@DiscriminatorValue("1")
open class Customer (
    id: Long = 0L,
    name: String?,
    surname: String?,
    ssnCode: String?,
    category: ContactCategory,
    messages: MutableList<Message>?,
    emails: MutableList<Email>?,
    addresses: MutableList<Address>?,
    telephoneNumbers: MutableList<Telephone>?,




    // extension
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "customer")
    open var jobOffers: MutableList<JobOffer> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    open var customerNotes: MutableList<CustomerNote> = mutableListOf()
) : Contact(id, name, surname, ssnCode, category, messages, emails, addresses, telephoneNumbers) {

    constructor(dto: CustomerDto):this(
        0L, dto.name, dto.surname, dto.ssnCode, ContactCategory.valueOf(dto.category), null, null, null, null
    )



    fun addNote(note: CustomerNote) {
        this.customerNotes.add(note)
        note.customer = this
    }

    fun addJobOffer(jobOffer: JobOffer) {
        this.jobOffers.add(jobOffer)
        jobOffer.customer = this
    }
}
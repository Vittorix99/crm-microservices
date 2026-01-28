package org.example.walab4.model.contact

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import org.example.walab4.model.message.Message

@Entity
@DiscriminatorValue("3")
open class Unknown (
    id: Long = 0L,
    name: String?,
    surname: String?,
    ssnCode: String?,
    category: ContactCategory,
    messages: MutableList<Message>?,
    emails: MutableList<Email>?,
    addresses: MutableList<Address>?,
    telephoneNumbers: MutableList<Telephone>?,
): Contact(id, name, surname, ssnCode, category, messages, emails, addresses, telephoneNumbers)
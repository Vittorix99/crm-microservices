package org.example.walab4.repository.contact

import org.example.walab4.model.contact.Address
import org.example.walab4.model.contact.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository: JpaRepository<Address, Long> {

    fun findAddressById(id: Long): Address?

    fun findAddressByName(address: String): Address?

    fun findAddressesByContacts(contacts: List<Contact>): List<Address>
}
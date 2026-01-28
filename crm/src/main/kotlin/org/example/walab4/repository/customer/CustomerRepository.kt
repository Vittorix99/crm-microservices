package org.example.walab4.repository.customer

import org.example.walab4.model.customer.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, Long> {
    fun findCustomerById(customerId: Long): Customer?
}
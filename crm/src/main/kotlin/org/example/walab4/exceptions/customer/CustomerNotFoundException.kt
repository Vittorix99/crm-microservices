package org.example.walab4.exceptions.customer

class CustomerNotFoundException(customerId: String? = null, cause: Throwable? = null): RuntimeException("Customer with id $customerId not present in the Repository", cause)
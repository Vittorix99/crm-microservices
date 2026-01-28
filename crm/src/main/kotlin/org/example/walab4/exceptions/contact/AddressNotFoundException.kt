package org.example.walab4.exceptions.contact

class AddressNotFoundException (addressId: String? = null, cause: Throwable? = null): RuntimeException("Address Not Present in the Repository", cause)
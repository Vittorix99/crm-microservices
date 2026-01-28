package org.example.walab4.exceptions.contact

class AddressNotPresentInContactException (contactId: String? = null, addressId: String? = null, cause: Throwable? = null): RuntimeException("Address $addressId not present in contact $contactId", cause)
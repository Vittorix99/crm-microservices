package org.example.walab4.exceptions.professional

class ProfessionalNotFoundException(professionalId: String? = null, cause: Throwable? = null): RuntimeException("Professional Not Present in the Repository", cause )
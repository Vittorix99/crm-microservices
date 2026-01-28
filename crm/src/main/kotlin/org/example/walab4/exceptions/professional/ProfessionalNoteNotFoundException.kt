package org.example.walab4.exceptions.professional

class ProfessionalNoteNotFoundException(noteId: String? = null, cause: Throwable? = null): RuntimeException("Note $noteId not found", cause )
package org.example.walab4.exceptions.customer

class CustomerNoteNotFoundException(noteId: String? = null, cause: Throwable? = null): RuntimeException("Note $noteId not found", cause )
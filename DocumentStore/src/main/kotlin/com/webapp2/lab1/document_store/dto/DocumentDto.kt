package com.webapp2.lab1.document_store.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.webapp2.lab1.document_store.model.Document
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

data class DocumentDto(
    @JsonProperty("content") val content: ByteArray
)


fun Document.toDto(): DocumentDto{
    try {
        val content = this.content
        return DocumentDto(content)
    } catch (e: IOException) {
        throw RuntimeException("Impossibile leggere il contenuto del file", e)
    }

}



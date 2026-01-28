package com.webapp2.lab1.document_store.dto

import ch.qos.logback.core.util.FileSize

import com.fasterxml.jackson.annotation.JsonProperty
import com.webapp2.lab1.document_store.model.DocumentMetadata
import java.util.*

data class DocumentMetadataDto (
    @JsonProperty("metadata_id")
    var metadataId: Long,
    @JsonProperty("file_name")
    val fileName: String,
    @JsonProperty("file_size")
    val fileSize: Long?,
    @JsonProperty("content_type")
    val contentType: String?
)

fun DocumentMetadata.toDto(): DocumentMetadataDto {
    return DocumentMetadataDto(this.id, this.name, this.size, this.contentType)
}

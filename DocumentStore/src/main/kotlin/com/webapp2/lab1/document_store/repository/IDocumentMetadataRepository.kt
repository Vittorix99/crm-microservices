package com.webapp2.lab1.document_store.repository

import com.webapp2.lab1.document_store.model.DocumentMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IDocumentMetadataRepository: JpaRepository<DocumentMetadata, Long> {
    fun findDocumentMetadataByDocumentId(id: Long): DocumentMetadata?
    fun  findDocumentMetadataByName(name:String):DocumentMetadata?
    fun findDocumentMetadataById(id: Long): DocumentMetadata?
}
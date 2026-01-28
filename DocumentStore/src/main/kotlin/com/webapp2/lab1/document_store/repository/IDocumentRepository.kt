package com.webapp2.lab1.document_store.repository

import com.webapp2.lab1.document_store.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IDocumentRepository : JpaRepository<Document,Long>{
    fun findDocumentById(id: Long): Document?;
    fun findDocumentByMetadataId(metadataId: Long) : Document?;

}


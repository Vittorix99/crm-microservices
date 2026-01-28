package com.webapp2.lab1.document_store.model

import jakarta.persistence.*

@Entity
class Document{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    lateinit var content: ByteArray
    @OneToOne(mappedBy = "document", cascade = [CascadeType.ALL], orphanRemoval = true)
    var metadata: DocumentMetadata? = null

    fun addMetadata(metadata: DocumentMetadata) {
        metadata.document = this;
        this.metadata = metadata;
    }
}



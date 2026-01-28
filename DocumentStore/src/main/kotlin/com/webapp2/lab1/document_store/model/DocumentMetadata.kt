package com.webapp2.lab1.document_store.model

import jakarta.persistence.*
import java.util.Date


@Entity
class DocumentMetadata (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var size: Long?,
    var contentType: String?,
    val createdTimestamp: Date,
    var  lastUpdate: Date? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    var document: Document? = null

)
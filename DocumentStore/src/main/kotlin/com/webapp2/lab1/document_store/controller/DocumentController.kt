package com.webapp2.lab1.document_store.controller

import com.webapp2.lab1.document_store.dto.DocumentDto
import com.webapp2.lab1.document_store.dto.DocumentMetadataDto

import com.webapp2.lab1.document_store.exceptions.DocumentMetadataNotFoundException
import com.webapp2.lab1.document_store.exceptions.InvalidDocumentException

import com.webapp2.lab1.document_store.dto.toDto

import com.webapp2.lab1.document_store.service.DocumentService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/documents")
class DocumentController {

    @Autowired
    private lateinit var documentService: DocumentService
    private val LOGGER: Logger = LogManager.getLogger()

    //@Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun uploadDocument(
        @RequestParam(value = "fileV") fileV: MultipartFile,
    ): DocumentMetadataDto{
        val savedMetadata = documentService.saveDocumentAndMetaData(fileV)
        LOGGER.info("Document with name ${fileV.originalFilename.toString()} uploaded successfully")
        return savedMetadata
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllDocuments(
        @RequestParam pageSize: Int=0,
        @RequestParam limit: Int=20
    ): List<DocumentMetadataDto> {
        val metadataDatabase = documentService.getAllDocuments(pageSize, limit).map{ it -> it.toDto()};

        LOGGER.info("All documents retrieved correctly")
        return metadataDatabase
    }

    @GetMapping("/{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun getDocumentMetadata(@PathVariable metadataId: Long): DocumentMetadataDto {
        val documentMetadata = documentService.findDocumentMetadata(metadataId);

        LOGGER.info("Document with metadataId ${metadataId} retrieved")
        return documentMetadata

    }

    @DeleteMapping("/{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteMetadata(@PathVariable("metadataId") metadataId: Long): Unit {
        val documentMetadata = documentService.deleteDocument(metadataId);

        LOGGER.info("Document with metadataId ${metadataId} deleted")
        return Unit
    }


    @GetMapping("/{metadataId}/data")
    @ResponseStatus(HttpStatus.FOUND)
    fun getContentOfDocument (@PathVariable metadataId:Long): DocumentDto
    {
        val documentDto =  documentService.findDocument(metadataId)
        LOGGER.info("Document with metadataId $metadataId found successfully")
        return documentDto
    }

    @PutMapping("/{metadataId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateDocument(
        @PathVariable metadataId: Long,
        @RequestParam("file") file: MultipartFile
    ): DocumentMetadataDto {
        val documentMetadataDto = DocumentMetadataDto(metadataId, file.originalFilename.toString(), file.size, file.contentType)
        val documentDto = DocumentDto(file.bytes);
        val updatedDocumentDto = documentService.updateDocument(metadataId, documentMetadataDto, documentDto)

        LOGGER.info("Document metadata $metadataId updated successfully")
        LOGGER.info("Document with metadataId $metadataId updated successfully")

        return updatedDocumentDto
    }
}
package com.webapp2.lab1.document_store.service

import com.webapp2.lab1.document_store.dto.DocumentDto
import com.webapp2.lab1.document_store.dto.DocumentMetadataDto
import com.webapp2.lab1.document_store.dto.toDto
import com.webapp2.lab1.document_store.exceptions.DocumentAlreadyPresentException
import com.webapp2.lab1.document_store.exceptions.DocumentMetadataNotFoundException
import com.webapp2.lab1.document_store.exceptions.DocumentNotFoundException
import com.webapp2.lab1.document_store.exceptions.InvalidDocumentException
import com.webapp2.lab1.document_store.model.Document
import com.webapp2.lab1.document_store.model.DocumentMetadata
import com.webapp2.lab1.document_store.repository.IDocumentMetadataRepository
import com.webapp2.lab1.document_store.repository.IDocumentRepository
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@org.springframework.transaction.annotation.Transactional
class DocumentService(private val documentRepository: IDocumentRepository, private val documentMetadataRepository: IDocumentMetadataRepository) {
    private val LOGGER: Logger = LogManager.getLogger()

    /* Return all the documents inside the repo under the shape of DTO */
    fun listAll(): List<DocumentMetadataDto> {
        return documentMetadataRepository.findAll().map{
            it.toDto()
        }
    }


    fun findDocumentMetadata( metadataId: Long): DocumentMetadataDto {
        val metadata = documentMetadataRepository.findDocumentMetadataByDocumentId(metadataId)
            ?: throw DocumentMetadataNotFoundException(metadataId.toString())
        return metadata.toDto()
    }
    fun findDocument (metadataId: Long):DocumentDto{

        val document = documentRepository.findDocumentByMetadataId(metadataId)?: throw DocumentNotFoundException("Document with metadataId : $metadataId not found")

        return document.toDto()
    }

    fun saveDocumentAndMetaData(documentMultipartFile: MultipartFile): DocumentMetadataDto {
        // Controllo se il documento esiste già nel sistema
        val existingDocument = documentMetadataRepository.findDocumentMetadataByName(documentMultipartFile.originalFilename.toString())
        if (existingDocument != null) {

            throw DocumentAlreadyPresentException()

          
        }

        // Controllo se il file è valido
        if (documentMultipartFile.isEmpty || documentMultipartFile.size <= 0) {
            throw InvalidDocumentException()
        }

        // Salva il contenuto del file come documento
        val content: ByteArray = documentMultipartFile.inputStream.readAllBytes()
        val document = Document()
        document.content = content

        // Salva i metadati del documento
        val savedMetadata = DocumentMetadata(
            name = documentMultipartFile.originalFilename.toString(),
            size = documentMultipartFile.size,
            contentType = documentMultipartFile.contentType,
            createdTimestamp = Date()
        )
        document.addMetadata(savedMetadata)
        documentRepository.save(document)
        documentMetadataRepository.save(savedMetadata)

        // Restituisce i metadati salvati come DTO
        return savedMetadata.toDto()
    }

    fun getAllDocuments(page:Int, limit: Int): List<DocumentMetadata>{
        return documentMetadataRepository.findAll(PageRequest.of(page,limit)).content


    }


    fun getDocumentDataById(metadataId: Long): DocumentDto {
        val metadata = documentRepository.findDocumentById(metadataId)
            ?: throw DocumentMetadataNotFoundException(metadataId.toString())

        return metadata.toDto();
    }




    fun updateDocument( metadataId: Long, metadataDTO: DocumentMetadataDto, documentDto: DocumentDto): DocumentMetadataDto {
        val existingMetadata = documentMetadataRepository.findDocumentMetadataById(metadataId)
            ?: throw DocumentMetadataNotFoundException(metadataId.toString())

        val existingDocument = documentRepository.findDocumentByMetadataId(metadataId)
            ?: throw DocumentNotFoundException(metadataId.toString())

        existingMetadata.apply {
            name = metadataDTO.fileName
            size = metadataDTO.fileSize
            contentType = metadataDTO.contentType?: ""
            lastUpdate = Date()
        }

        existingDocument.apply {
            content = documentDto.content
        }

        documentMetadataRepository.save(existingMetadata)
        documentRepository.save(existingDocument)

        return existingMetadata.toDto()
    }

    fun deleteDocument(metadataId: Long) {
        val existingMetadata = documentMetadataRepository.findDocumentMetadataById(metadataId)
            ?: throw DocumentMetadataNotFoundException(metadataId.toString())

        val existingDocument = documentRepository.findDocumentByMetadataId(metadataId)
            ?: throw DocumentNotFoundException(metadataId.toString())

        documentRepository.delete(existingDocument)
        documentMetadataRepository.delete(existingMetadata)

        // Log delle modifiche a livello di informazioni
    }



}
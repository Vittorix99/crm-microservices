package com.webapp2.lab1.document_store

import com.webapp2.lab1.document_store.controller.DocumentController
import com.webapp2.lab1.document_store.dto.toDto
import com.webapp2.lab1.document_store.exceptions.DocumentAlreadyPresentException
import com.webapp2.lab1.document_store.exceptions.DocumentMetadataNotFoundException
import com.webapp2.lab1.document_store.exceptions.DocumentNotFoundException
import com.webapp2.lab1.document_store.exceptions.InvalidDocumentException
import com.webapp2.lab1.document_store.model.Document
import com.webapp2.lab1.document_store.model.DocumentMetadata
import com.webapp2.lab1.document_store.repository.IDocumentMetadataRepository
import com.webapp2.lab1.document_store.repository.IDocumentRepository
import com.webapp2.lab1.document_store.service.DocumentService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.multipart.MultipartFile
import java.util.*

class DocumentServiceTest {
    val documentRepository = mockk<IDocumentRepository>();
    val documentMetadataRepository: IDocumentMetadataRepository = mockk();
    val documentService: DocumentService = DocumentService(documentRepository, documentMetadataRepository)
    val documentController: DocumentController = mockk<DocumentController>();

    // todo: api a test
    @Test
    fun findDocumentMetadataForValidId() {
        // Given
        val metadataId = 1L
        val documentId = 100L
        val document = Document()
        document.id = documentId

        val metadata = DocumentMetadata(id = metadataId, name = "Document1", size = 1024, contentType = "text/plain", createdTimestamp = Date(), document = document)
        every { documentMetadataRepository.findDocumentMetadataByDocumentId(metadataId) } returns metadata

        // When
        val actualMetadata = documentService.findDocumentMetadata(metadataId)

        // Then
        assertEquals(metadata.toDto(), actualMetadata)
    }

    @Test
    fun findDocumentMetadataForInvalidId() {
        // Given
        val metadataId = 1L
        every { documentMetadataRepository.findDocumentMetadataByDocumentId(metadataId) } returns null

        // When / Then
        assertThrows<DocumentMetadataNotFoundException> {
            documentService.findDocumentMetadata(metadataId)
        }
    }



    @Test
    fun getAllDocuments () {
        // Given
        val page = 0
        val limit = 10
        val expectedMetadataList = listOf(
            DocumentMetadata(id = 1, name = "Document1", size = 1024, contentType = "text/plain", createdTimestamp = Date()),
            DocumentMetadata(id = 2, name = "Document2", size = 2048, contentType = "application/pdf", createdTimestamp = Date())
        )
        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedMetadataList)
        every { documentMetadataRepository.findAll(pageRequest) } returns pageImpl

        // When
        val actualMetadataList = documentService.getAllDocuments(page, limit)

        // Then
        assertEquals(expectedMetadataList, actualMetadataList)
    }

    // todo: api b test
    @Test
    fun getDocumentMetadataFromMetadataId() {
        val mockMetadata = DocumentMetadata(1, "Test.pdf", 12, "application/pdf", Date())
        every { documentMetadataRepository.findDocumentMetadataByDocumentId(1) } returns mockMetadata;

        val result = documentService.findDocumentMetadata(1);
        assert(mockMetadata.equals(mockMetadata))
    }
    @Test
    fun getDocumentMetadataFromMetadataIdNotFound() {

    }

    // todo: api c test
    @Test
    fun getDocumentContentFromMetadataId() {
        val mockMetadataContent = Document()
        mockMetadataContent.content = "Questo è un file di testo".toByteArray()
        every { documentRepository.findDocumentById(any()) } returns mockMetadataContent;

        val result = documentService.getDocumentDataById(1);

        assertEquals(result, mockMetadataContent.toDto())
        verify(exactly = 1) { documentRepository.findDocumentById(any()) }
    }
    @Test
    fun getDocumentContentFromMetadataIdNotFound() {
        val mockMetadataContent = Document()
        mockMetadataContent.content = "Questo è un file di testo".toByteArray()
        every { documentRepository.findDocumentByMetadataId(3) } returns null;

        assertThrows<DocumentNotFoundException>{
            val result = documentService.findDocument(3)
        }
        verify(exactly = 1) { documentRepository.findDocumentByMetadataId(3) }
    }

    // todo: api d test
    @Test
    fun uploadDocumentTest() {
        val mockFile: MultipartFile = mockk()
        val mockMetadata = DocumentMetadata(0, "Lab1.pdf",1703120L, "application/pdf", Date())
        val mockDocument = Document();
        every { mockFile.inputStream.readAllBytes() } returns "TXkgRnJpZW5kIDExIDN4DQpQdXJyZWx5IDEzIDJ4DQpQdXJyZWx5IGNvbGxlY3RvciA1MCAxeA0KUHVycmVsaWx5IHVsdGkgMTAgMXgNClByZXR0eSA3IDN4DQo=".toByteArray();
        every { mockFile.originalFilename.toString() } returns "Lab1.pdf";
        every { mockFile.size } returns 1703120;
        every { mockFile.contentType } returns "application/pdf"
        every { mockFile.isEmpty } returns false
        every { documentMetadataRepository.findDocumentMetadataByName("Lab1.pdf") } returns null
        every { documentRepository.save<Document>(any()) } returnsArgument 0
        every { documentMetadataRepository.save<DocumentMetadata>(any()) } returnsArgument 0



        val result = documentService.saveDocumentAndMetaData(mockFile)
        assertEquals(result, mockMetadata.toDto())
        verify(exactly = 1) { documentMetadataRepository.findDocumentMetadataByName(any())}
        verify(exactly = 1) { documentRepository.save<Document>(any()) }
        verify(exactly = 1) { documentMetadataRepository.save<DocumentMetadata>(any()) }


    }

    @Test
    fun uploadNullDocumentTest() {
        val mockFile: MultipartFile = mockk()
        val mockMetadata = DocumentMetadata(0, "Lab1.pdf",1703120L, "application/pdf", Date())
        val mockDocument = Document();
        every { mockFile.inputStream.readAllBytes() } returns "TXkgRnJpZW5kIDExIDN4DQpQdXJyZWx5IDEzIDJ4DQpQdXJyZWx5IGNvbGxlY3RvciA1MCAxeA0KUHVycmVsaWx5IHVsdGkgMTAgMXgNClByZXR0eSA3IDN4DQo=".toByteArray();
        every { mockFile.originalFilename.toString() } returns "Lab1.pdf";
        every { mockFile.size } returns 1703120;
        every { mockFile.contentType } returns "application/pdf"
        every { mockFile.isEmpty } returns true
        every { documentMetadataRepository.findDocumentMetadataByName("Lab1.pdf") } returns null
        every { documentRepository.save<Document>(any()) } returnsArgument 0
        every { documentMetadataRepository.save<DocumentMetadata>(any()) } returnsArgument 0

        assertThrows<InvalidDocumentException>{
            val result = documentService.saveDocumentAndMetaData(mockFile)
        }
    }

    @Test
    fun uploadDocumentAlreadyPresentTest(){

        val mockFile: MultipartFile = mockk()
        val mockMetadata = DocumentMetadata(0, "Lab1.pdf",1703120L, "application/pdf", Date())
        val mockDocument = Document();
        every { mockFile.inputStream.readAllBytes() } returns "TXkgRnJpZW5kIDExIDN4DQpQdXJyZWx5IDEzIDJ4DQpQdXJyZWx5IGNvbGxlY3RvciA1MCAxeA0KUHVycmVsaWx5IHVsdGkgMTAgMXgNClByZXR0eSA3IDN4DQo=".toByteArray();
        every { mockFile.originalFilename.toString() } returns "Lab1.pdf";
        every { mockFile.size } returns 1703120;
        every { mockFile.contentType } returns "application/pdf"
        every { mockFile.isEmpty } returns true
        every { documentMetadataRepository.findDocumentMetadataByName("Lab1.pdf") } returns mockMetadata
        every { documentRepository.save<Document>(any()) } returnsArgument 0
        every { documentMetadataRepository.save<DocumentMetadata>(any()) } returnsArgument 0

        assertThrows<DocumentAlreadyPresentException>{
            val result = documentService.saveDocumentAndMetaData(mockFile)
        }
    }

    // todo: api e test
    @Test
    fun updateDocumentContentTest() {
        val metadataIdMock: Long = 1;

        // updated file and metadata
        val newDocumentMetadata: DocumentMetadata = DocumentMetadata(metadataIdMock, "newFile.pdf", 222, "application/pdf", Date());
        val newDocument: Document = Document()
        newDocument.content = "New content".toByteArray()
        newDocument.addMetadata(newDocumentMetadata)

        // document and metadata in db - mocked
        val oldDocumentMetadata: DocumentMetadata = DocumentMetadata(metadataIdMock, "oldFile.pdf", 111, "application/pdf", Date());
        val oldDocument: Document = Document()
        oldDocument.content = "Old content".toByteArray()
        oldDocument.addMetadata(oldDocumentMetadata)

        val capturedDocument = slot<Document>()
        val capturedDocumentMetadata = slot<DocumentMetadata>()

        every { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) } returns oldDocumentMetadata;
        every { documentRepository.findDocumentByMetadataId(metadataIdMock) } returns oldDocument;
        every { documentMetadataRepository.save(capture(capturedDocumentMetadata)) } returns newDocumentMetadata;
        every { documentRepository.save(capture(capturedDocument)) } returns newDocument;

        val result = documentService.updateDocument(metadataIdMock, newDocumentMetadata.toDto(), newDocument.toDto());

        assertEquals(newDocumentMetadata.toDto(), result);

        // assert that the save function for DocumentRepository and DocumentMetadataRepository
        // is called with the correct value
        assertEquals(capturedDocument.isCaptured, true)
        assertEquals(capturedDocument.captured.content, newDocument.content)
        assertEquals(capturedDocumentMetadata.isCaptured, true)
        assertEquals(capturedDocumentMetadata.captured.size, newDocumentMetadata.size)
        assertEquals(capturedDocumentMetadata.captured.name, newDocumentMetadata.name)
        assertEquals(capturedDocumentMetadata.captured.contentType, newDocumentMetadata.contentType)
        assertEquals(capturedDocument.captured.metadata?.id, capturedDocumentMetadata.captured.id)

        verify(exactly = 1) { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) };
        verify(exactly = 1) { documentRepository.findDocumentByMetadataId(metadataIdMock) };
        verify(exactly = 1) { documentMetadataRepository.save( capture(capturedDocumentMetadata) ) };
        verify(exactly = 1) { documentRepository.save( capture(capturedDocument) ) };
    }
    @Test
    fun updateDocumentContentNotFoundTest() {
        val metadataIdMock: Long = 1;

        // updated file and metadata
        val newDocumentMetadata: DocumentMetadata = DocumentMetadata(metadataIdMock, "newFile.pdf", 222, "application/pdf", Date());
        val newDocument: Document = Document()
        newDocument.content = "New content".toByteArray()
        newDocument.addMetadata(newDocumentMetadata)

        every { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) } returns null;
        every { documentRepository.findDocumentByMetadataId(metadataIdMock) } returns null;

        assertThrows<DocumentMetadataNotFoundException> {
            documentService.updateDocument(metadataIdMock, newDocumentMetadata.toDto(), newDocument.toDto());
        }

        verify(exactly = 0) { documentRepository.findDocumentByMetadataId(metadataIdMock) };
        verify(exactly = 1) { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) };
        verify(exactly = 0) { documentMetadataRepository.save( any() ) };
        verify(exactly = 0) { documentRepository.save( any() ) };
    }

    // todo: api f test
    @Test
    fun deleteDocumentTest() {
        val metadataIdMock: Long = 1;

        // document and metadata in db - mocked
        val oldDocumentMetadata: DocumentMetadata = DocumentMetadata(metadataIdMock, "oldFile.pdf", 111, "application/pdf", Date());
        val oldDocument: Document = Document()
        oldDocument.content = "Old content".toByteArray()
        oldDocument.addMetadata(oldDocumentMetadata)

        val capturedDocument = slot<Document>()
        val capturedDocumentMetadata = slot<DocumentMetadata>()

        every { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) } returns oldDocumentMetadata;
        every { documentRepository.findDocumentByMetadataId(metadataIdMock) } returns oldDocument;
        every { documentMetadataRepository.delete(capture(capturedDocumentMetadata)) } returns Unit;
        every { documentRepository.delete(capture(capturedDocument)) } returns Unit;

        val result = documentService.deleteDocument(metadataIdMock);

        assertEquals(result, Unit);

        // assert that the save function for DocumentRepository and DocumentMetadataRepository
        // is called with the correct value
        assertEquals(capturedDocumentMetadata.isCaptured, true)
        assertEquals(capturedDocumentMetadata.captured.id, metadataIdMock)
        assertEquals(capturedDocument.isCaptured, true)
        assertEquals(capturedDocument.captured.metadata?.id, metadataIdMock)
        assertEquals(capturedDocument.captured.metadata?.id, capturedDocumentMetadata.captured.id)

        verify(exactly = 1) { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) };
        verify(exactly = 1) { documentRepository.findDocumentByMetadataId(metadataIdMock) };
        verify(exactly = 1) { documentMetadataRepository.delete( capture(capturedDocumentMetadata) ) };
        verify(exactly = 1) { documentRepository.delete( capture(capturedDocument) ) };
    }
    @Test
    fun deleteDocumentNotFoundTest() {
        val metadataIdMock: Long = 1;

        val capturedDocument = slot<Document>()
        val capturedDocumentMetadata = slot<DocumentMetadata>()

        every { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) } returns null;
        every { documentRepository.findDocumentByMetadataId(metadataIdMock) } returns null;

        assertThrows<DocumentMetadataNotFoundException> {
            documentService.deleteDocument(metadataIdMock);
        }

        verify(exactly = 1) { documentMetadataRepository.findDocumentMetadataById(metadataIdMock) };
        verify(exactly = 0) { documentRepository.findDocumentByMetadataId(metadataIdMock) };
        verify(exactly = 0) { documentMetadataRepository.delete( capture(capturedDocumentMetadata) ) };
        verify(exactly = 0) { documentRepository.delete( capture(capturedDocument) ) };
    }
}

package document_store

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import wa.lab5.dto.documents.DocumentDto
import wa.lab5.dto.documents.DocumentMetadataDto
import wa.lab5.exceptions.documents.DocumentAlreadyPresentException
import wa.lab5.exceptions.documents.DocumentMetadataNotFoundException
import wa.lab5.dto.documents.toDto
import wa.lab5.exceptions.documents.DocumentNotFoundException
import wa.lab5.exceptions.documents.InvalidDocumentException
import wa.lab5.model.documents.DocumentMetadata
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import wa.lab5.Lab5Application
import wa.lab5.controller.ProfessionalController
import wa.lab5.services.documents.DocumentService
import java.util.*


@WebMvcTest(controllers = arrayOf(DocumentControllerTest::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class DocumentControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var documentService: DocumentService

    @Test
    fun `getAllDocuments returns list of document metadata`() {
        // Given
        val expectedMetadataList = listOf(
            DocumentMetadata(
                id = 1L,
                name = "Document1.txt",
                size = 1024,
                createdTimestamp = Date(),
                contentType = "text/plain"
            ),
            DocumentMetadata(
                id = 2L,
                name = "Document2.pdf",
                size = 2048,
                createdTimestamp = Date(),
                contentType = "application/pdf"
            )
        )
        every { documentService.getAllDocuments(any(), any()) } returns expectedMetadataList

        // When / Then
        mockMvc.perform(get("/API/documents"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedMetadataList.map { it.toDto() }))
            )
}
    @Test
    fun getDocumentMetadataWithValidId() {
        // Given
        val metadataId = 1L
        val expectedMetadata = DocumentMetadataDto(metadataId= metadataId, fileName = "Document1.txt", fileSize = 1024, contentType = "text/plain")
        every { documentService.findDocumentMetadata(metadataId) } returns expectedMetadata

        // When / Then
        mockMvc.perform(get("/API/documents/$metadataId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedMetadata))
            )
    }

    @Test
    fun `getDocumentMetadata returns 404 when document not found`() {
        // Given
        val metadataId = 1L
        every { documentService.findDocumentMetadata(metadataId) } throws DocumentMetadataNotFoundException("Document not found")

        // When / Then
        mockMvc.perform(get("/API/documents/$metadataId"))
            .andExpect(status().isNotFound)
    }

    /* API C: Example */
    @Test
    fun getDocumentDataControllerTest(){
        val exampleOfDocumentDto = DocumentDto("This is an example".toByteArray() )
        val exampleDocumentMetadataDto = DocumentMetadataDto(1, "Lab1.pdf",1703120L, "application/pdf")

        every { documentService.findDocument(1) } returns exampleOfDocumentDto
        every { documentService.findDocumentMetadata(1) } returns exampleDocumentMetadataDto


        mockMvc.perform(get("/API/documents/1/data"))
            .andExpect(status().isFound())
            .andExpect(content().contentType(MediaType.parseMediaType(exampleDocumentMetadataDto.contentType!!)))
            .andExpect(content().bytes(exampleOfDocumentDto.content)
            )
    }

    @Test
    fun getDocumentDataControllerTestWrongResult(){
        every { documentService.findDocument(2) } throws DocumentNotFoundException("Document with metadataId : 2 not found")

        mockMvc.perform(get("/API/documents/2/data"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* API b */


    @Test
    fun uploadDocumentTestController() {
        val exampleDocumentMetadataDto = DocumentMetadataDto(1, "Lab1.pdf",1703120L, "application/pdf")
        val jsonFile = MockMultipartFile("file", "Lab1.pdf", "application/pdf", "{\"key1\": \"value1\"}".toByteArray())
        every {  documentService.saveDocumentAndMetaData(any())} returns exampleDocumentMetadataDto

        mockMvc.perform(multipart("/API/documents").file(jsonFile))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(exampleDocumentMetadataDto))
            )

    }

    @Test
    fun uploadNullDocumentTestController(){
        val jsonFile = MockMultipartFile("file", "Lab1.pdf", "application/pdf", "{\"key1\": \"value1\"}".toByteArray())
        every { documentService.saveDocumentAndMetaData(any()) } throws InvalidDocumentException()


        mockMvc.perform(multipart("/API/documents").file(jsonFile))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun uploadDocumentAlreadyPresentTestController(){
        val jsonFile = MockMultipartFile("file", "Lab1.pdf", "application/pdf", "{\"key1\": \"value1\"}".toByteArray())
        every { documentService.saveDocumentAndMetaData(any()) } throws DocumentAlreadyPresentException()


        mockMvc.perform(multipart("/API/documents").file(jsonFile))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun updateDocumentTest() {
        // document and metadata in db - mocked
        val file = MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "File content".toByteArray())

        val documentMetadata = DocumentMetadata(
            name = file.name,
            contentType = file.contentType,
            size = file.size,
            createdTimestamp = Date(),
            lastUpdate = Date()
        ).toDto()

        every { documentService.updateDocument(any(), any(), any()) } returns documentMetadata

        val objectMapper = ObjectMapper()


        mockMvc.perform(multipart(HttpMethod.PUT, String.format("/API/documents/1")).file(file))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(documentMetadata)))
    }

    @Test
    fun updateDocumentNotFoundTest() {
        // document and metadata in db - mocked
        val file = MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "File content".toByteArray())
        val metadataId: Long = 1

        every { documentService.updateDocument(any(), any(), any()) } throws DocumentMetadataNotFoundException(metadataId.toString())

        mockMvc.perform(multipart(HttpMethod.PUT, String.format("/API/documents/1")).file(file))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateInvalidDocumentTest() {
        // document and metadata in db - mocked
        val file = MockMultipartFile("file", "test.txt", MediaType.MULTIPART_FORM_DATA_VALUE, "".toByteArray())
        val metadataId: Long = 1

        every { documentService.updateDocument(any(), any(), any()) } throws InvalidDocumentException(metadataId.toString())

        mockMvc.perform(multipart(HttpMethod.PUT, String.format("/API/documents/1")).file(file))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun deleteDocumentAndMetadataTest() {
        val metadataId: Long = 1

        every { documentService.deleteDocument( metadataId ) } returns Unit

        mockMvc.perform(delete( String.format("/API/documents/1")))
            .andExpect(status().isOk)
    }

    @Test
    fun deleteDocumentAndMetadataNotFoundTest() {
        val metadataId: Long = 1

        every { documentService.deleteDocument( any() ) } throws DocumentMetadataNotFoundException(metadataId.toString())

        mockMvc.perform(delete( String.format("/API/documents/1")))
            .andExpect(status().isNotFound)
    }




}
package crmext.joboffer

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import wa.lab5.Lab5Application
import wa.lab5.controller.JobOfferController
import wa.lab5.dto.jobOffer.JobOfferNoteDTO
import wa.lab5.dto.jobOffer.toDto
import wa.lab5.model.jobOffer.Interview
import wa.lab5.exceptions.customer.CustomerNotFoundException
import wa.lab5.exceptions.jobOffer.InterviewAlreadyPresentException
import wa.lab5.exceptions.jobOffer.InvalidTransitionException
import wa.lab5.exceptions.jobOffer.JobOfferNotFoundException
import wa.lab5.model.contact.Address
import wa.lab5.model.contact.ContactCategory
import wa.lab5.model.contact.Email
import wa.lab5.model.contact.Telephone
import wa.lab5.model.customer.Customer
import wa.lab5.model.jobOffer.JobOffer
import wa.lab5.model.jobOffer.JobOfferStatus
import wa.lab5.model.message.Message
import wa.lab5.model.professional.EmploymentState
import wa.lab5.model.professional.Professional
import wa.lab5.services.jobOffer.JobOfferService
import java.time.LocalDate


@WebMvcTest(controllers = arrayOf(JobOfferController::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class JobOfferControllerUnitTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var jobOfferService: JobOfferService

    private val professional = Professional(
        4L,
        "Marco",
        "Bianchi",
        "marco.bianchi",
        ContactCategory.PROFESSIONAL,
        mutableListOf<Message>(),
        mutableListOf<Email>(),
        mutableListOf<Address>(),
        mutableListOf<Telephone>(),
        "Torino",
        2.0
    )

    private val interview = Interview(
        id = 1,
        feedback = "Good Interview",
        date = LocalDate.now(),
        candidates = mutableListOf(professional)
    )

    private var listOfCustomer = listOf(
        Customer(1L, "Mario", "Rossi", "mrslasaofdoaash", ContactCategory.CUSTOMER, mutableListOf<Message>(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>()),
        Customer(2L, "Luigi", "Rossi", "sashaskaldada", ContactCategory.CUSTOMER, mutableListOf<Message>(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>()),
        Customer(3L, "Davide", "Palatroni", "asjdakdsa", ContactCategory.CUSTOMER, mutableListOf<Message>(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>())
    )


    private var expectedJobOffer = listOf(
        JobOffer(id = 1L, description = "Example of description 1", status = JobOfferStatus.CREATED, duration = 3, value = null, customer = listOfCustomer[0]),
        JobOffer(id = 2L, description = "Example of description 2", status = JobOfferStatus.DONE, duration = 8, value = null, customer = listOfCustomer[1]),
        JobOffer(id = 3L, description = "Example of description 3", status = JobOfferStatus.ABORTED, duration = 5, value = null, customer = listOfCustomer[0]),
        JobOffer(id = 4L, description = "Example of description 4", status = JobOfferStatus.CONSOLIDATED, duration = 4, value = null, customer = listOfCustomer[1], professional = professional),
    )

    @BeforeEach
    fun setUp() {
        listOfCustomer = listOf(

            Customer(
                1L,
                "Mario",
                "Rossi",
                "mrslasaofdoaash",
                ContactCategory.CUSTOMER,
                mutableListOf<Message>(),
                mutableListOf<Email>(),
                mutableListOf<Address>(),
                mutableListOf<Telephone>()
            ),
            Customer(
                2L,
                "Luigi",
                "Rossi",
                "sashaskaldada",
                ContactCategory.CUSTOMER,
                mutableListOf<Message>(),
                mutableListOf<Email>(),
                mutableListOf<Address>(),
                mutableListOf<Telephone>()
            ),
            Customer(
                3L,
                "Davide",
                "Palatroni",
                "asjdakdsa",
                ContactCategory.PROFESSIONAL,
                mutableListOf<Message>(),
                mutableListOf<Email>(),
                mutableListOf<Address>(),
                mutableListOf<Telephone>()
            )
        )

        var listOfProfessional = listOf(
            Professional(
                id = 1L,
                name = "Anna",
                surname = "Verdi",
                ssnCode = "anvrsda",
                category = ContactCategory.PROFESSIONAL,
                mutableListOf<Message>(),
                emails = mutableListOf(),
                addresses = mutableListOf(),
                telephoneNumbers = mutableListOf(),
                location = "NY",
                dailyRate = 500.0,
                state = EmploymentState.EMPLOYED,
                ownedSkills = mutableListOf(),
                interviews = mutableListOf(),
                jobOffer = null,
                notes = mutableListOf()
            ),
            Professional(
                id = 2L,
                name = "Luca",
                surname = "Neri",
                ssnCode = "lucneri",
                category = ContactCategory.PROFESSIONAL,
                mutableListOf<Message>(),
                emails = mutableListOf(),
                addresses = mutableListOf(),
                telephoneNumbers = mutableListOf(),
                location = "LA",
                dailyRate = 400.0,
                state = EmploymentState.EMPLOYED,
                ownedSkills = mutableListOf(),
                interviews = mutableListOf(),
                jobOffer = null,
                notes = mutableListOf()
            )
        )
        expectedJobOffer = listOf(
            JobOffer(
                id = 1L,
                description = "Example of description 1",
                status = JobOfferStatus.CREATED,
                duration = 3,
                value = null,
                customer = listOfCustomer[0]
            ),
            JobOffer(
                id = 2L,
                description = "Example of description 2",
                status = JobOfferStatus.DONE,
                duration = 8,
                value = null,
                customer = listOfCustomer[1]
            ),
            JobOffer(
                id = 3L,
                description = "Example of description 3",
                status = JobOfferStatus.ABORTED,
                duration = 5,
                value = null,
                customer = listOfCustomer[0]
            ),
            JobOffer(
                id = 4L,
                description = "Example of description 4",
                status = JobOfferStatus.CONSOLIDATED,
                duration = 4,
                value = null,
                customer = listOfCustomer[0],
                professional = listOfProfessional[0]
            ),



            )
        listOfProfessional[0].jobOffer = expectedJobOffer[3]
    }

    private val exampleOfNote = JobOfferNoteDTO(id = 1, description = "Example of Description ")

    private val jobOfferToSave = JobOffer(id = 5L, description = "Example of description 5", status = JobOfferStatus.CREATED, duration = 7, value = null, customer = listOfCustomer[1])

    /* Update Job Offer Status*/

    @Test
    fun updateJobOfferStatusSimple(){
        val jobOfferId = 1L
        val newStatus = JobOfferStatus.SELECTION_PHASE
        val returnedDto = expectedJobOffer[0].toDto().copy(status = newStatus.name)

        every { jobOfferService.updateJobOfferStatus(jobOfferId = jobOfferId, newStatus) } returns returnedDto

        mockMvc.perform(post("/API/joboffers/$jobOfferId")
            .param("status", "SELECTION_PHASE"))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(returnedDto)))
    }

    @Test
    fun updateJobOfferStatusJobOfferNotFound(){
        val jobOfferId = 1L
        val newStatus = JobOfferStatus.SELECTION_PHASE


        every { jobOfferService.updateJobOfferStatus(jobOfferId = jobOfferId, newStatus) } throws JobOfferNotFoundException(jobOfferId)

        mockMvc.perform(post("/API/joboffers/$jobOfferId")
            .param("status", "SELECTION_PHASE"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

    }

    @Test
    fun updateJobOfferStatusInvalidTransition(){
        val jobOfferId = 1L
        val newStatus = JobOfferStatus.DONE


        every { jobOfferService.updateJobOfferStatus(jobOfferId = jobOfferId, newStatus) } throws InvalidTransitionException(newState = newStatus)

        mockMvc.perform(post("/API/joboffers/$jobOfferId")
            .param("status", "DONE"))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun updateJobOfferStatusInvalidJobOfferStatus(){
        val jobOfferId = 1L

        mockMvc.perform(post("/API/joboffers/$jobOfferId")
            .param("status", "FASE DI SELEZIONE"))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Update Job Offer Description Controller Unit Test */

    @Test
    fun updateJobOfferDescriptionSimple(){
        val jobOfferId = 1L
        val newDescription = "Description Edited"
        val returnedDto = expectedJobOffer[0].toDto()

        every { jobOfferService.updateJobOfferDescription(jobOfferId, newDescription) } returns returnedDto.copy(description = newDescription)

        mockMvc.perform(put("/API/joboffers/$jobOfferId/description")
            .param("newDescription", newDescription))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(returnedDto.copy(description = newDescription))))
    }

    @Test
    fun updateJobOfferDescriptionJobOfferNotFound(){
        val jobOfferId = 1L
        val newDescription = "Description Edited"

        every { jobOfferService.updateJobOfferDescription(jobOfferId, newDescription) } throws JobOfferNotFoundException(jobOfferId)

        mockMvc.perform(put("/API/joboffers/$jobOfferId/description")
            .param("newDescription", newDescription))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Add Note to JobOffer Controller Unit Test */

    @Test
    fun addNoteToJobOfferSimple(){
        val jobOfferId = 1L
        val returnedDto = expectedJobOffer[0].toDto()

        every { jobOfferService.addNoteToJobOffer(jobOfferId, exampleOfNote) } returns returnedDto

        mockMvc.perform(post("/API/joboffers/$jobOfferId/note")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(exampleOfNote)))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect( content().json(ObjectMapper().writeValueAsString(returnedDto)))
    }

    @Test
    fun addNoteJobOfferJobOfferNotFound(){
        val jobOfferId = 1L

        every { jobOfferService.addNoteToJobOffer(jobOfferId, exampleOfNote) } throws JobOfferNotFoundException()

        mockMvc.perform(post("/API/joboffers/$jobOfferId/note")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(exampleOfNote)))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Add Interview To Job Offer Controller Unit Test */

    @Test
    fun addInterviewJobOfferSimple(){

        val jobOfferId = 1L
        val returnedDto = expectedJobOffer[0].toDto()

        every { jobOfferService.addInterviewToJobOffer(jobOfferId, interviewDto = interview.toDto()) } returns returnedDto

        mockMvc.perform(post("/API/joboffers/$jobOfferId/interview")
            .contentType(MediaType.APPLICATION_JSON)
            .content( ObjectMapper().writeValueAsString(interview.toDto())))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect( content().json(ObjectMapper().writeValueAsString(returnedDto)))

    }

    @Test
    fun addInterviewJobOfferJobOfferNotFound(){
        val jobOfferId = 1L

        every { jobOfferService.addInterviewToJobOffer(jobOfferId, interviewDto = interview.toDto()) } throws JobOfferNotFoundException()

        mockMvc.perform(post("/API/joboffers/$jobOfferId/interview")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(interview.toDto())))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

    }

    @Test
    fun addInterviewJobOfferInterviewAlreadyPresent(){
        val jobOfferId = 2L

        every { jobOfferService.addInterviewToJobOffer(jobOfferId, interviewDto = interview.toDto()) } throws InterviewAlreadyPresentException()

        mockMvc.perform(post("/API/joboffers/$jobOfferId/interview")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(interview.toDto())))
            .andExpect(status().isConflict)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Get job offer by ID */
    @Test
    fun getJobOfferByIdSimple() {

        val jobOfferId = 1L

        val expectedJobOffer = expectedJobOffer.find { it.id == jobOfferId }

        every { jobOfferService.getJobOffer(jobOfferId) } returns expectedJobOffer!!.toDto()

        mockMvc.perform(get("/API/joboffers/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedJobOffer.toDto())))
    }

    @Test
    fun getJobOfferByIdNotFound() {

        val jobOfferId = 5L

        every { jobOfferService.getJobOffer(jobOfferId) } throws JobOfferNotFoundException()

        mockMvc.perform(get("/API/joboffers/5"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Save job offer */
    @Test
    fun saveJobOfferSimple() {

        val jobOfferToSaveDTO = jobOfferToSave.toDto()

        every {  jobOfferService.saveJobOffer(any())} returns jobOfferToSaveDTO

        mockMvc.perform(post("/API/joboffers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(jobOfferToSaveDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(jobOfferToSaveDTO)))
    }

    @Test
    fun saveJobOfferCustomerNotFound() {

        val jobOfferToSaveDTO = jobOfferToSave.toDto()

        jobOfferToSaveDTO.customer = 10L

        every {  jobOfferService.saveJobOffer(any())} throws CustomerNotFoundException()

        mockMvc.perform(post("/API/joboffers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(jobOfferToSaveDTO)))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Get open job offers for customer */
    @Test
    fun getOpenJobOffersForCustomerSimple() {

        val customerId = 1L

        val expectedJobOffersDtos = expectedJobOffer.filter {
            it.customer.id == customerId && !(
                    it.status == JobOfferStatus.CONSOLIDATED ||
                    it.status == JobOfferStatus.DONE ||
                    it.status == JobOfferStatus.ABORTED
            )
        }.map { it.toDto() }

        every { jobOfferService.getOpenJobOffersForCustomer( customerId, any(), any() ) } returns expectedJobOffersDtos

        mockMvc.perform(get("/API/joboffers/open/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedJobOffersDtos)))
    }

    @Test
    fun getOpenJobOffersForCustomerEmpty() {
        val customerId = 1L
        val page = 0
        val limit = 10

        every { jobOfferService.getOpenJobOffersForCustomer(customerId, page, limit) } returns listOf()

        mockMvc.perform(get("/API/joboffers/open/$customerId").param("page", page.toString()).param("limit", limit.toString()))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun getOpenJobOffersForCustomerNotFound() {

        val customerId = 3L

        every { jobOfferService.getOpenJobOffersForCustomer( customerId, any(), any() ) } throws JobOfferNotFoundException()

        mockMvc.perform(get("/API/joboffers/open/3"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Get accepted job offers for professional */
    @Test
    fun getAcceptedJobOffersForProfessionalSimple() {

        val professionalId = 4L

        val expectedJobOffersDtos = expectedJobOffer.filter {
            it.professional?.id == professionalId && (
                    it.status == JobOfferStatus.CONSOLIDATED ||
                    it.status == JobOfferStatus.DONE
            )
        }.map { it.toDto() }

        every { jobOfferService.getAcceptedJobOffersForProfessional( professionalId, any(), any() ) } returns expectedJobOffersDtos

        mockMvc.perform(get("/API/joboffers/accepted/4"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedJobOffersDtos)))
    }

    @Test
    fun getAcceptedJobOffersForProfessionalEmpty() {
        val professionalId = 1L
        val page = 0
        val size = 10

        every { jobOfferService.getAcceptedJobOffersForProfessional(professionalId, page, size) } returns listOf()

        mockMvc.perform(get("/API/joboffers/accepted/$professionalId").param("page", page.toString()).param("size", size.toString()))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun getAcceptedJobOffersForProfessionalNotFound() {

        val professionalId = 3L

        every { jobOfferService.getAcceptedJobOffersForProfessional( professionalId, any(), any() ) } throws JobOfferNotFoundException()

        mockMvc.perform(get("/API/joboffers/accepted/3"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* Get job offers, eventually filtered by customer and/or professional */
    @Test
    fun getJobOffersFiltersSimple() {

        val expectedDtos = expectedJobOffer.map { it.toDto() }

        every { jobOfferService.getRegisteredJobOffersByParams( any(), any(), any() , any() ) } returns expectedDtos

        mockMvc.perform(get("/API/joboffers/aborted"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedDtos)))
    }

    @Test
    fun getJobOffersByParamsEmpty() {
        val customerId = 1L
        val professionalId = 2L
        val page = 0
        val size = 10

        every { jobOfferService.getRegisteredJobOffersByParams(customerId, professionalId, page, size) } returns listOf()

        mockMvc.perform(get("/API/joboffers/aborted").param("customer", customerId.toString()).param("professional", professionalId.toString()).param("page", page.toString()).param("size", size.toString()))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun getJobOffersFiltersNotFound() {

        every { jobOfferService.getRegisteredJobOffersByParams( any(), any(), any(), any() ) } throws JobOfferNotFoundException()

        mockMvc.perform(get("/API/joboffers/aborted"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun getJobOffersFiltersCustomer() {

        val customerId = 2L

        val expectedDtos = expectedJobOffer.filter { it.customer.id == customerId }.map { it.toDto() }

        every { jobOfferService.getRegisteredJobOffersByParams( customerId, any(), any() , any() ) } returns expectedDtos

        mockMvc.perform(get("/API/joboffers/aborted")
            .param("customer", customerId.toString()))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedDtos)))
    }

    @Test
    fun getJobOffersFiltersProfessional() {

        val professionalId = 4L
        val expectedDtos = expectedJobOffer.filter { it.professional?.id == professionalId }.map { it.toDto() }

        every { jobOfferService.getRegisteredJobOffersByParams( any(), professionalId, any() , any() ) } returns expectedDtos

        mockMvc.perform(get("/API/joboffers/aborted")
            .param("professional", professionalId.toString()))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().json(ObjectMapper().writeValueAsString(expectedDtos)))
    }

    /* GetJobOfferNotesForJobOffer Controller Unit Test */

    @Test
    fun getNotesForJobOfferSimple(){

        val jobOfferId = 1L
        val listOfNotes = listOf(exampleOfNote)

        every { jobOfferService.getJobOfferNotesForJobOffer(jobOfferId) } returns listOfNotes

        mockMvc.perform(get("/API/joboffers/$jobOfferId/note"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isNotEmpty)
            .andExpect(content().json(ObjectMapper().writeValueAsString(listOfNotes)))
    }

    @Test
    fun getNotesForJobOfferEmpty(){

        val jobOfferId = 1L
        val listOfNotes = listOf<JobOfferNoteDTO>()

        every { jobOfferService.getJobOfferNotesForJobOffer(jobOfferId) } returns listOfNotes

        mockMvc.perform(get("/API/joboffers/$jobOfferId/note"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
            .andExpect(content().json(ObjectMapper().writeValueAsString(listOfNotes)))
    }

    @Test
    fun getNotesForJobOfferJobOfferNotFound(){

        val jobOfferId = 1L

        every { jobOfferService.getJobOfferNotesForJobOffer(jobOfferId) } throws JobOfferNotFoundException()

        mockMvc.perform(get("/API/joboffers/$jobOfferId/note"))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

    }

}
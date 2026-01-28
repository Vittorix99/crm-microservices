package crmext.joboffer

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import wa.lab5.model.jobOffer.JobOfferNote
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import org.springframework.data.domain.Page
import wa.lab5.configurations.JobOfferTransitionsConf
import wa.lab5.dto.jobOffer.toDto
import wa.lab5.model.jobOffer.Interview
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
import wa.lab5.repository.customer.CustomerRepository
import wa.lab5.repository.jobOffer.JobOfferNoteRepository
import wa.lab5.repository.jobOffer.JobOfferRepository
import wa.lab5.repository.professional.ProfessionalRepository
import wa.lab5.services.jobOffer.JobOfferService

class JobOfferServiceUnitTest {


    private val jobOfferRepository = mockk<JobOfferRepository>()
    private val jobOfferNoteRepository = mockk<JobOfferNoteRepository>()
    private val transitions = mockk<JobOfferTransitionsConf>()
    private val professionalRepository = mockk<ProfessionalRepository>()
    private val customerRepository = mockk<CustomerRepository>()
    private val jobOfferService = JobOfferService(
        jobOfferRepository,
        jobOfferNoteRepository,
        transitions,
        customerRepository,
        professionalRepository
    )

    private var listOfCustomer: List<Customer> = listOf(

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

    private var listOfProfessional: List<Professional> = listOf(
        Professional(
            id = 1L,
            name = "Anna",
            surname = "Verdi",
            ssnCode = "anvrsda",
            category = ContactCategory.PROFESSIONAL,
            messages = mutableListOf<Message>(),
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
            messages = mutableListOf<Message>(),
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

    private var expectedJobOffer: List<JobOffer> = listOf(
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
            customer = listOfCustomer[1],
            interview = Interview(feedback = "Good Feedback", date = LocalDate.now())
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
        )
    )
       
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

    private val interview = Interview(id = 1, feedback = "Good Interview",
        date = LocalDate.now(),
        candidates = mutableListOf(professional)
    )

    private val jobOfferNote = JobOfferNote(description = "This is a JobOffer Note", id = 1, jobOffer = expectedJobOffer[0] )


    private val jobOfferToSave = JobOffer(
        id = 5L,
        description = "Example of description 5",
        status = JobOfferStatus.CREATED,
        duration = 7,
        value = null,
        customer = listOfCustomer[1]
    )

    /* Update Job Offer Status Unit Test */

    @Test
    fun updateJobOfferStatusSimpleTest(){
        val jobOfferId = 1L
        val newJobOfferStatus = JobOfferStatus.SELECTION_PHASE
        val returnedJobOffer = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedJobOffer
        every { transitions.created } returns arrayOf(JobOfferStatus.SELECTION_PHASE)
        every { jobOfferRepository.save<JobOffer>( any() ) } returns returnedJobOffer


        val actualJobOffer = jobOfferService.updateJobOfferStatus(jobOfferId, newJobOfferStatus)
        assertEquals(actualJobOffer, returnedJobOffer.toDto().copy(status = newJobOfferStatus.name))

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 1){ transitions.created }
        verify(exactly = 1){ jobOfferRepository.save<JobOffer>( any() ) }
    }

    @Test
    fun updateJobOfferStatusJobOfferNotFound(){
        val jobOfferId = 1L
        val newJobOfferStatus = JobOfferStatus.SELECTION_PHASE
        val returnedJobOffer = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns null


        assertThrows<JobOfferNotFoundException> {
            val actualJobOffer = jobOfferService.updateJobOfferStatus(jobOfferId, newJobOfferStatus)
        }

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 0){ transitions.created }
        verify(exactly = 0){ jobOfferRepository.save<JobOffer>( any() ) }
    }

    @Test
    fun updateJobOfferStatusInvalidTransition(){
        val jobOfferId = 1L
        val newJobOfferStatus = JobOfferStatus.CANDIDATE_PROPOSAL
        val returnedJobOffer = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedJobOffer
        every { transitions.created } returns arrayOf(JobOfferStatus.SELECTION_PHASE)


        assertThrows<InvalidTransitionException> {
            val actualJobOffer = jobOfferService.updateJobOfferStatus(jobOfferId, newJobOfferStatus)
        }

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 1){ transitions.created }
        verify(exactly = 0){ jobOfferRepository.save<JobOffer>( any() ) }
    }

    /* Update Job Offer Description Unit Test */

    @Test
    fun updateJobOfferDescriptionSimpleTest(){
        val jobOfferId = 1L
        val newJobOfferDescription = "Description Edited"
        val returnedJobOffer = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedJobOffer
        every { jobOfferRepository.save<JobOffer>( any( ) ) } returns returnedJobOffer

        val actualJobOffer = jobOfferService.updateJobOfferDescription(jobOfferId, newJobOfferDescription)

        assertEquals(actualJobOffer, returnedJobOffer.toDto().copy(description = newJobOfferDescription))

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 1){ jobOfferRepository.save<JobOffer>( any() ) }

    }

    @Test
    fun updateJobOfferDescriptionJobOfferNotFound(){

        val jobOfferId = 1L
        val newJobOfferDescription = "Description Edited"

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns null

        assertThrows<JobOfferNotFoundException> {
            val actualJobOffer = jobOfferService.updateJobOfferDescription(jobOfferId, newJobOfferDescription)
        }

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 0){ jobOfferRepository.save<JobOffer>( any() ) }
    }

    /* Get job offer by ID */
    @Test
    fun getJobOfferById() {

        val expectedJobOfferSingle = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(expectedJobOfferSingle.id!!) } returns expectedJobOfferSingle

        val actualJobOffer  = jobOfferService.getJobOffer(expectedJobOfferSingle.id!!)
        assertEquals(expectedJobOfferSingle.toDto(), actualJobOffer)
    }

    @Test
    fun getJobOfferByIdNotFound() {

        val jobOfferId = 1L
        val expectedJobOfferSingle = null

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns expectedJobOfferSingle

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getJobOffer(jobOfferId)
        }
    }

    /* Save job offer */
    @Test
    fun saveJobOfferSimple() {

        val jobOfferToSaveDTO = jobOfferToSave.toDto()

        val capturedJobOffer = slot<JobOffer>()

        every { customerRepository.findCustomerById(jobOfferToSaveDTO.customer) } returns jobOfferToSave.customer
        every { professionalRepository.findProfessionalById(any()) } returns null
        every { jobOfferRepository.save(capture(capturedJobOffer)) } returns jobOfferToSave

        val savedJobOffer = jobOfferService.saveJobOffer(jobOfferToSaveDTO)

        verify(exactly = 1) { jobOfferRepository.save( capturedJobOffer.captured ) }

        assertEquals(jobOfferToSaveDTO, savedJobOffer)
    }

    /* Get open job offers for customer */
    @Test
    fun getOpenJobOffersForCustomerSimple() {

        val page = 0
        val limit = 10
        val customerId = 1L

        val expectedJobOffers = expectedJobOffer.filter {
            it.customer.id == customerId && !(
                    it.status == JobOfferStatus.CONSOLIDATED ||
                    it.status == JobOfferStatus.DONE ||
                    it.status == JobOfferStatus.ABORTED
            )
        }

        val expectedJobOffersDTOs = expectedJobOffers.map { it.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedJobOffers)

        every { jobOfferRepository.findOpenJobOffersByCustomerId(customerId, pageRequest) } returns pageImpl
        val actualJobOffers = jobOfferService.getOpenJobOffersForCustomer(customerId, page, limit)

        assertEquals(expectedJobOffersDTOs, actualJobOffers)
    }

    @Test
    fun getOpenJobOffersForCustomerNotFound() {

        val page = 0
        val limit = 10
        val customerId = 3L

        val pageRequest = PageRequest.of(page, limit)

        val expectedJobOffers = null

        every { jobOfferRepository.findOpenJobOffersByCustomerId(customerId, pageRequest) } returns expectedJobOffers

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getOpenJobOffersForCustomer(customerId, page, limit)
        }
    }

    /* Get accepted job offers for professional */
    @Test
    fun getAcceptedJobOffersForProfessionalSimple() {

        val page = 0
        val limit = 10
        val professionalId = 4L

        val expectedJobOffers = expectedJobOffer.filter {
            it.professional?.id == professionalId && (
                    it.status == JobOfferStatus.CONSOLIDATED ||
                    it.status == JobOfferStatus.DONE
            )
        }

        val expectedJobOffersDTOs = expectedJobOffers.map { it.toDto() }

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedJobOffers)

        every { jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId, pageRequest) } returns pageImpl
        val actualJobOffers = jobOfferService.getAcceptedJobOffersForProfessional(professionalId, page, limit)

        assertEquals(expectedJobOffersDTOs, actualJobOffers)
    }

    @Test
    fun getAcceptedJobOffersForProfessionalNotFound() {

        val page = 0
        val limit = 10
        val professionalId = 3L

        val pageRequest = PageRequest.of(page, limit)

        val expectedJobOffers = null

        every { jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId, pageRequest) } returns expectedJobOffers

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getAcceptedJobOffersForProfessional(professionalId, page, limit)
        }
    }

    /* Get job offers, eventually filtered by customer and/or professional */
    @Test
    fun getJobOffersFiltersSimple() {

        val page = 0
        val limit = 10

        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedJobOffer)
        val expectedDTOs = expectedJobOffer.map { it.toDto() }

        every { jobOfferRepository.findAllFiltered(null, null, pageRequest) } returns pageImpl
        val actualJobOffers = jobOfferService.getRegisteredJobOffersByParams(null, null, page, limit)

        assertEquals(expectedDTOs, actualJobOffers)
    }

    @Test
    fun getJobOffersFiltersNotFound() {

        val page = 0
        val limit = 10

        val pageRequest = PageRequest.of(page, limit)

        val expectedJobOffers = null

        every { jobOfferRepository.findAllFiltered(null, null, pageRequest) } returns expectedJobOffers

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getRegisteredJobOffersByParams(null, null, page, limit)
        }
    }

    // Vittorio's Tests
    @Test
    fun `getOpenJobOffersForCustomer should return job offers when customer has open job offers`() {
        val customerId = 1L
        val pageable = PageRequest.of(0, 10)
        val openJobOffers = expectedJobOffer.filter {
            it.status != JobOfferStatus.ABORTED &&
                    it.status != JobOfferStatus.CONSOLIDATED &&
                    it.status != JobOfferStatus.DONE
        }
        val jobOfferPage: Page<JobOffer> = PageImpl(openJobOffers, pageable, openJobOffers.size.toLong())

        every { jobOfferRepository.findOpenJobOffersByCustomerId(customerId, pageable) } returns jobOfferPage

        val result = jobOfferService.getOpenJobOffersForCustomer(customerId, 0, 10)
        assertEquals(openJobOffers.size, result.size)
        assertEquals(openJobOffers[0].description, result[0].description)
        assertEquals(openJobOffers[0].status.name, result[0].status)
        assertTrue(result.all { it.customer == customerId })

    }

    @Test
    fun `getOpenJobOffersForCustomer should throw exception when repo returns null`() {
        val customerId = 1L
        val pageable = PageRequest.of(0, 10)

        every { jobOfferRepository.findOpenJobOffersByCustomerId(customerId, pageable) } returns null

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getOpenJobOffersForCustomer(customerId, 0, 10)
        }
    }

    @Test
    fun getJobOffersFiltersCustomer() {

        val page = 0
        val limit = 10
        val customerId = 2L

        val expectedJobOffers = expectedJobOffer.filter { it.customer.id == customerId }
        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedJobOffers)

        val expectedJobOffersDTOs = expectedJobOffers.map { it.toDto() }

        every { jobOfferRepository.findAllFiltered(customerId, null, pageRequest) } returns pageImpl
        val actualJobOffers = jobOfferService.getRegisteredJobOffersByParams(customerId, null, page, limit)

        assertEquals(expectedJobOffersDTOs, actualJobOffers)
    }

    @Test
    fun getJobOffersFiltersProfessional() {

        val page = 0
        val limit = 10
        val professionalId = 4L

        val expectedJobOffers = expectedJobOffer.filter { it.professional?.id == professionalId }
        val pageRequest = PageRequest.of(page, limit)
        val pageImpl = PageImpl(expectedJobOffers)

        val expectedJobOffersDTOs = expectedJobOffers.map { it.toDto() }

        every { jobOfferRepository.findAllFiltered(null, professionalId, pageRequest) } returns pageImpl
        val actualJobOffers = jobOfferService.getRegisteredJobOffersByParams(null, professionalId, page, limit)

        assertEquals(expectedJobOffersDTOs, actualJobOffers)
    }


    /* Add Note to Job Offer Service Unit Test */
    @Test
    fun addNoteToJobOfferSimple(){

        val jobOfferId = 1L
        val returnedJobOffer = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedJobOffer
        every { jobOfferRepository.save<JobOffer>( any() ) } returns returnedJobOffer.copy(jobOfferNotes = mutableListOf(jobOfferNote.copy(jobOffer = returnedJobOffer)))
        every { jobOfferNoteRepository.save<JobOfferNote>( any() )} returns jobOfferNote.copy(jobOffer = returnedJobOffer)

        val actualJobOffer = jobOfferService.addNoteToJobOffer(jobOfferId, jobOfferNote.toDto())

        assertEquals(actualJobOffer, returnedJobOffer.toDto())

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 1){ jobOfferRepository.save<JobOffer>( any() ) }
        verify(exactly = 1) { jobOfferNoteRepository.save<JobOfferNote>( any() ) }

    }

    @Test
    fun addNoteToJobOfferJobOfferNotFoundException(){

        val jobOfferId = 1L

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns null

        assertThrows<JobOfferNotFoundException> {
            val actualJobOffer = jobOfferService.addNoteToJobOffer(jobOfferId, jobOfferNote.toDto())
        }

        verify(exactly = 1){ jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 0){ jobOfferRepository.save<JobOffer>( any() ) }
        verify(exactly = 0) { jobOfferNoteRepository.save<JobOfferNote>( any() ) }
    }

    /* Add Interview Job Offer Service Unit Test */
    @Test
    fun addInterviewJobOfferSimple(){

        val jobOfferId = 1L
        val returnedDto = expectedJobOffer[0]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedDto
        every { jobOfferRepository.save<JobOffer>( any() ) } returns returnedDto.copy(interview = interview)

        val actualValue = jobOfferService.addInterviewToJobOffer(jobOfferId, interview.toDto())

        assertEquals(actualValue, returnedDto.toDto())

        verify(exactly = 1) { jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 1) { jobOfferRepository.save<JobOffer>( any() ) }
    }

    @Test
    fun addInterviewJobOfferJobOfferNotFound(){
        val jobOfferId = 1L

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns null

        assertThrows<JobOfferNotFoundException> {
            val actualValue = jobOfferService.addInterviewToJobOffer(jobOfferId, interview.toDto())
        }

        verify(exactly = 1) { jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 0) { jobOfferRepository.save<JobOffer>( any() ) }
    }

    @Test
    fun addInterviewJobOfferAlreadyInterviewInserted(){
        val jobOfferId = 2L
        val returnedDto = expectedJobOffer[1]

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedDto

        assertThrows<InterviewAlreadyPresentException> {
            val actualValue = jobOfferService.addInterviewToJobOffer(jobOfferId, interview.toDto())
        }

        verify(exactly = 1) { jobOfferRepository.findJobOfferById(jobOfferId) }
        verify(exactly = 0) { jobOfferRepository.save<JobOffer>( any() ) }
    }

    /* Get JobOfferNotes Service Unit Test */
    @Test
    fun getJobOfferNotesForJobOfferSimple(){
        val jobOfferId = 1L
        val returnedDto = expectedJobOffer[0]
        returnedDto.jobOfferNotes.add(jobOfferNote)

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns returnedDto

        val actualNotes = jobOfferService.getJobOfferNotesForJobOffer(jobOfferId)

        assertEquals(actualNotes, listOf(jobOfferNote.toDto()))

        verify(exactly = 1) { jobOfferRepository.findJobOfferById(jobOfferId) }
    }

    @Test
    fun getJobOffersNotesForJobOfferJobOfferNotFound(){
        val jobOfferId = 1L

        every { jobOfferRepository.findJobOfferById(jobOfferId) } returns null

        assertThrows<JobOfferNotFoundException> {
            val actualNotes = jobOfferService.getJobOfferNotesForJobOffer(jobOfferId)
        }
        verify(exactly = 1) { jobOfferRepository.findJobOfferById(jobOfferId) }
    }


    fun `getAcceptedJobOffersForProfessional should return job offers when professional has accepted job offers`() {
        val professionalId = 1L
        val pageable = PageRequest.of(0, 10)
        val acceptedJobOffers = expectedJobOffer.filter { it.status == JobOfferStatus.CONSOLIDATED }
        val jobOfferPage: Page<JobOffer> = PageImpl(acceptedJobOffers, pageable, acceptedJobOffers.size.toLong())

        every { jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId, pageable) } returns jobOfferPage

        val result = jobOfferService.getAcceptedJobOffersForProfessional(professionalId, 0, 10)
        assertEquals(acceptedJobOffers.size, result.size)
        assertEquals(acceptedJobOffers[0].description, result[0].description)
        assertEquals(acceptedJobOffers[0].status.name, result[0].status)
        assertTrue(result.all { it.professional == professionalId })

    }
    @Test
    fun `getAcceptedJobOffersForProfessional should handle empty result`() {
        val professionalId = 1L
        val pageable = PageRequest.of(0, 10)
        val emptyJobOfferPage: Page<JobOffer> = PageImpl(emptyList(), pageable, 0)

        every { jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId, pageable) } returns emptyJobOfferPage

        val result = jobOfferService.getAcceptedJobOffersForProfessional(professionalId, 0, 10)
        assertEquals(0, result.size)
    }


    @Test
    fun `getAcceptedJobOffersForProfessional should throw exception when repository returns null`() {
        val professionalId = 1L
        val pageable = PageRequest.of(0, 10)

        every { jobOfferRepository.findAcceptedJobOffersByProfessionalId(professionalId, pageable) } returns null

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getAcceptedJobOffersForProfessional(professionalId, 0, 10)
        }
    }

    @Test
    fun `getRegisteredJobOffersByParams should return job offers based on customer or professional`() {
        val customerId = 1L
        val professionalId = 2L
        val pageable = PageRequest.of(0, 10)
        val filteredJobOffers = expectedJobOffer.filter {
            it.customer.id == customerId || it.professional?.id == professionalId
        }
        val jobOfferPage: Page<JobOffer> = PageImpl(filteredJobOffers, pageable, filteredJobOffers.size.toLong())

        every { jobOfferRepository.findAllFiltered(customerId, professionalId, pageable) } returns jobOfferPage

        val result = jobOfferService.getRegisteredJobOffersByParams(customerId, professionalId, 0, 10)
        assertEquals(filteredJobOffers.size, result.size)
        assertEquals(filteredJobOffers[0].description, result[0].description)
        assertEquals(filteredJobOffers[0].status.name, result[0].status)
        assertTrue(result.all { it.customer == customerId || it.professional==professionalId })


    }

    @Test
    fun `getRegisteredJobOffersByParams should throw exception when repo returns null`() {
        val customerId = 1L
        val professionalId = 2L
        val pageable = PageRequest.of(0, 10)

        every { jobOfferRepository.findAllFiltered(customerId, professionalId, pageable) } returns null

        assertThrows<JobOfferNotFoundException> {
            jobOfferService.getRegisteredJobOffersByParams(customerId, professionalId, 0, 10)
        }
    }

    //End Vittorio's Test
}

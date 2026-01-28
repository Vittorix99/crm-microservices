package crmext.joboffer

import com.fasterxml.jackson.databind.ObjectMapper
import crmext.CrmExtIntegrationTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import wa.lab5.dto.customer.toDto
import wa.lab5.dto.jobOffer.JobOfferDTO
import wa.lab5.dto.jobOffer.JobOfferNoteDTO
import wa.lab5.dto.jobOffer.toDto
import wa.lab5.dto.professional.toDto
import wa.lab5.model.jobOffer.Interview
import wa.lab5.model.contact.Address
import wa.lab5.model.contact.ContactCategory
import wa.lab5.model.contact.Email
import wa.lab5.model.contact.Telephone
import wa.lab5.model.customer.Customer
import wa.lab5.model.jobOffer.JobOffer
import wa.lab5.model.jobOffer.JobOfferStatus
import wa.lab5.model.professional.EmploymentState
import wa.lab5.model.professional.Professional
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class JobOfferControllerIntegrationTest: CrmExtIntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc
    private val listOfCustomer = listOf(
        Customer(1L, "Mario", "Rossi", "mrslasaofdoaash", ContactCategory.CUSTOMER, mutableListOf(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>()),
        Customer(2L, "Luigi", "Rossi", "sashaskaldada", ContactCategory.CUSTOMER, mutableListOf(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>()),
        Customer(3L, "Davide", "Palatroni", "asjdakdsa", ContactCategory.CUSTOMER, mutableListOf(), mutableListOf<Email>(), mutableListOf<Address>(), mutableListOf<Telephone>())
    )



    private val expectedJobOffer = listOf(
        JobOffer(id = 1L, description = "Example of description 1", status = JobOfferStatus.CREATED, duration = 3, value = null, customer = listOfCustomer[0]),
        JobOffer(id = 2L, description = "Example of description 2", status = JobOfferStatus.CREATED, duration = 8, value = null, customer = listOfCustomer[1]),
        JobOffer(id = 3L, description = "Example of description 3", status = JobOfferStatus.CREATED, duration = 5, value = null, customer = listOfCustomer[0]),
        JobOffer(id = 4L, status = JobOfferStatus.CREATED, duration = 4, value = null, customer = listOfCustomer[1]),
        JobOffer(id = 5L, status = JobOfferStatus.CREATED, value = null, customer = listOfCustomer[2], duration = 10),
        JobOffer(id = 6L, status = JobOfferStatus.CREATED, value = null, customer = listOfCustomer[1], duration=2)
    )

    private val exampleOfNote = JobOfferNoteDTO(id = null, description = "Example of Description ")

    private val listOfProfessional = listOf(
        Professional(
            id = 4L,
            name = "Anna",
            surname = "Verdi",
            ssnCode = "anvrsda",
            category = ContactCategory.PROFESSIONAL,
            messages = mutableListOf(),
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
            id = 5L,
            name = "Luca",
            surname = "Neri",
            ssnCode = "lucneri",
            category = ContactCategory.PROFESSIONAL,
            messages = mutableListOf(),
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

    private val interview = Interview(
        id = 1,
        feedback = "Good Interview",
        date = LocalDate.now(),
        candidates = listOfProfessional.toMutableList()
    )
    @BeforeAll
    fun populateDB(): Unit {

        listOfCustomer.forEach{
            mockMvc.post("/API/customers"){
                contentType = MediaType.APPLICATION_JSON
                content = ObjectMapper().writeValueAsString(it.toDto())
            }.andExpect {
                    status {
                        isCreated()
                    }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(ObjectMapper().writeValueAsString(it.toDto()))
                    }
                }
        }

        expectedJobOffer.forEach{
            mockMvc.post("/API/joboffers"){
                contentType = MediaType.APPLICATION_JSON
                content = ObjectMapper().writeValueAsString(it.toDto())
            }.andExpect {
                status {
                    isCreated()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(ObjectMapper().writeValueAsString(it.toDto()))
                }
            }
        }

        listOfProfessional.forEach{
            mockMvc.post("/API/professionals"){
                contentType = MediaType.APPLICATION_JSON
                content = ObjectMapper().writeValueAsString(it.toDto())
            }.andExpect {
                status {
                    isCreated()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    println(ObjectMapper().writeValueAsString(it.toDto()))
                    json(ObjectMapper().writeValueAsString(it.toDto()))
                }
            }
        }
    }

    /* Get JobOfferById Integration Test*/

    @Test
    fun getJobOfferByIdSimple(){

        mockMvc.get("/API/joboffers/1")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(ObjectMapper().writeValueAsString(expectedJobOffer[0].toDto()))
                }
            }
    }

    @Test
    fun getJobOfferByIdNotFound(){

        mockMvc.get("/API/joboffers/9")
            .andExpect {
                status {
                    isNotFound()
                }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }

    }

    /* Update JobOfferStatus Integration Test*/
    @Test
    fun updateJobOfferStatusSimple(){

        mockMvc.post("/API/joboffers/1"){
            param("status", "SELECTION_PHASE")
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[0].toDto().copy(status = JobOfferStatus.SELECTION_PHASE.name)))
            }
        }
    }

    @Test
    fun updateJobOfferStatusJobOfferNotFound(){

        mockMvc.post("/API/joboffers/9"){
            param("status", "SELECTION_PHASE")
        }.andExpect {
            status {
                isNotFound()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    @Test
    fun updateJobOfferStatusInvalidTransition(){
        mockMvc.post("/API/joboffers/2"){
            param("status", "DONE")
        }.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    @Test
    fun updateJobOfferStatusInvalidJobOfferStatus(){
        mockMvc.post("/API/joboffers/2"){
            param("status", "statusDone")
        }.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    /* Update JobOffer Description Integration Test */

    @Test
    fun updateJobOfferDescriptionSimple(){

        val newDescription = "Edited Description"

        mockMvc.put("/API/joboffers/2/description"){
            param("newDescription", newDescription)
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[1].toDto().copy(description = newDescription)))
            }
        }
    }

    @Test
    fun updateJobOfferDescriptionJobOfferNotFound(){
        val newDescription = "Edited Description"

        mockMvc.put("/API/joboffers/9/description"){
            param("newDescription", newDescription)
        }.andExpect {
            status {
                isNotFound()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    /* Add Note to JobOffer Integration Test */
    @Test
    fun addNoteToJobOfferSimple(){

        mockMvc.post("/API/joboffers/3/note"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(exampleOfNote)
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[2].toDto()))
            }
        }
    }

    @Test
    fun addNoteToJobOfferJobOfferNotFound(){
        mockMvc.post("/API/joboffers/9/note"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(exampleOfNote)
        }.andExpect {
            status {
                isNotFound()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    /* Add Interview to Job Offer Integration Test */

    @Test
    fun addInterviewToJobOfferSimple(){

        mockMvc.post("/API/joboffers/4/interview"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(interview.toDto())
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[3].toDto()))
            }
        }
    }

    @Test
    fun addInterviewToJobOfferJobOfferNotFound(){

        mockMvc.post("/API/joboffers/9/interview"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(interview.toDto())
        }.andExpect {
            status {
                isNotFound()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    @Test
    fun addInterviewToJobOfferInterviewAlreadyPresent(){

        mockMvc.post("/API/joboffers/5/interview"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(interview.toDto())
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[4].toDto()))
            }
        }

        mockMvc.post("/API/joboffers/5/interview"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(interview.toDto())
        }.andExpect {
            status {
                isConflict()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    /* Get JobOfferNotes Integration Test */
    @Test
    fun getJobOffersNoteSimple(){
        mockMvc.post("/API/joboffers/6/note"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(exampleOfNote)
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(expectedJobOffer[5].toDto()))
            }
        }

        mockMvc.get("/API/joboffers/6/note")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(ObjectMapper().writeValueAsString(listOf(exampleOfNote.copy(id = 1L))))
                }
            }
    }

    @Test
    fun getJonOffersNoteEmpty(){

        mockMvc.get("/API/joboffers/1/note")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(ObjectMapper().writeValueAsString(listOf<JobOfferNoteDTO>()))
                    jsonPath("$"){
                        isArray()
                        isEmpty()
                    }
                }
            }
    }

    @Test
    fun getJobOffersNoteJobOfferNotFound(){

        mockMvc.get("/API/joboffers/9/note")
            .andExpect {
                status {
                    isNotFound()
                }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }
    }

    /* Save jobOffer Integration Test */

    @Test
    fun saveJobOfferSimple(){

        var jobOfferToSave = JobOffer(id = 7L, duration = 10, status = JobOfferStatus.CREATED, customer = listOfCustomer[0])

        mockMvc.post("/API/joboffers"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(jobOfferToSave.toDto())
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(jobOfferToSave.toDto()))
            }
        }
    }

    @Test
    fun saveJobOfferCustomerNotFound(){
        val jobOfferToSave = JobOfferDTO(duration = 10, customer = 9)

        mockMvc.post("/API/joboffers"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(jobOfferToSave)
        }.andExpect {
            status {
                isNotFound()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

    @Test
    fun getOpenJobOffersForCustomerIntegrationTest() {
        mockMvc.get("/API/joboffers/open/1")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
    }

    @Test
    fun getAcceptedJobOffersForProfessionalIntegrationTest() {
        mockMvc.get("/API/joboffers/accepted/4")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
    }

    @Test
    fun getJobOffersByParamsIntegrationTest() {
        mockMvc.get("/API/joboffers/aborted?customer=1&professional=4")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                }
            }
    }





}
package crm.message

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.testcontainers.containers.PostgreSQLContainer
import wa.lab5.Lab5Application
import wa.lab5.dto.message.MessageDto
import wa.lab5.model.contact.Contact
import wa.lab5.model.contact.ContactCategory
import wa.lab5.model.contact.Email
import wa.lab5.model.contact.Telephone
import wa.lab5.model.message.Channel
import wa.lab5.model.message.MessageStatus
import wa.lab5.model.message.Priority

@SpringBootTest
@ContextConfiguration(initializers = [IntegrationTest.Initializer::class],classes = arrayOf(Lab5Application::class))
abstract class IntegrationTest {
    companion object {
        private val db = PostgreSQLContainer("postgres:latest")
    }

    internal class Initializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            db.start()
            TestPropertyValues.of(
                "spring.datasource.url=${db.jdbcUrl}",
                "spring.datasource.username=${db.username}",
                "spring.datasource.password=${db.password}"
            ).applyTo(applicationContext.environment)
        }
    }
}
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class MessageIntegrationTests: IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val messageExample = MessageDto(
        "mario.rossi@gmail.com",
        "Me",
        body = "This is the body",
        channel = Channel.EMAIL,
        MessageStatus.RECEIVED,
        Priority.HIGH
    )
    private val contactExample = Contact(
        name = null,
        surname = null,
        ssnCode = null,
        emails = mutableListOf(Email(1L, messageExample.sender, mutableListOf())),
        addresses = mutableListOf(),
        category = ContactCategory.UNKNOWN,
        telephoneNumbers = mutableListOf(),
        messages = mutableListOf()

    )

    private val contactExample2 = Contact(
        name = null,
        surname = null,
        ssnCode = null,
        emails = mutableListOf(),
        addresses = mutableListOf(),
        category = ContactCategory.UNKNOWN,
        telephoneNumbers = mutableListOf(Telephone(number = "3923513416", contacts = mutableListOf())),
        messages = mutableListOf()

    )

    @BeforeAll
    fun populateDB(): Unit {

        mockMvc.post("/API/messages"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(messageExample)
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                /* PER QUESTO CONTROLLO DOBBIAMO ASPETTARE CHE VITTORIO CORREGGA IL DTO*/
                json(ObjectMapper().writeValueAsString(messageExample.copy(contactExample.toString())))
            }
        }

        mockMvc.post("/API/messages/1"){
            param("comment", "This is an example of comment")
            param("stateStr", MessageStatus.READ.name)
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(messageExample.copy(status = MessageStatus.READ, sender = contactExample.toString())))
            }
        }

        mockMvc.post("/API/messages"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(messageExample.copy(sender = "3923513416", channel = Channel.PHONECALL, priority = Priority.LOW ))
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                /* PER QUESTO CONTROLLO DOBBIAMO ASPETTARE CHE VITTORIO CORREGGA IL DTO*/
                json(ObjectMapper().writeValueAsString(messageExample.copy(channel = Channel.PHONECALL, priority = Priority.LOW, sender = contactExample2.toString())))
            }
        }
    }

    /* Get All Messages Integration Test */
    @Test
    fun getAllMessagesIntegrationTest(){

        mockMvc.get("/API/messages"){}
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$"){
                        isArray()
                        isNotEmpty()
                    }
                    jsonPath("$.length()"){
                        value(2)
                    }
                    jsonPath("$[0].sender"){
                        value(contactExample.toString())
                    }
                    jsonPath("$[0].subject"){
                        value("Me")
                    }
                    jsonPath("$[0].body"){
                        value("This is the body")
                    }
                    jsonPath("$[0].status"){
                        value(MessageStatus.READ.name)
                    }
                    jsonPath("$[0].priority"){
                        value(Priority.HIGH.name)
                    }
                    jsonPath("$[0].channel"){
                        value(Channel.EMAIL.name)
                    }

                }
            }
    }

    @Test
    fun getAllMessagesWithSortingIntegrationTest(){

        mockMvc.get("/API/messages"){
            param("sorting", "channel")
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$"){
                    isArray()
                    isNotEmpty()
                }
                jsonPath("$.length()"){
                    value(2)
                }
                jsonPath("$[0].sender"){
                    value(contactExample2.toString())
                }
                jsonPath("$[0].subject"){
                    value("Me")
                }
                jsonPath("$[0].body"){
                    value("This is the body")
                }
                jsonPath("$[0].status"){
                    value(MessageStatus.RECEIVED.name)
                }
                jsonPath("$[0].priority"){
                    value(Priority.LOW.name)
                }
                jsonPath("$[0].channel"){
                    value(Channel.PHONECALL.name)
                }

            }
        }


    }

    @Test
    fun getAllMessagesWithFilteringIntegrationTest(){

        mockMvc.get("/API/messages"){
            param("filtering", "RECEIVED")
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$"){
                    isArray()
                    isNotEmpty()
                }
                jsonPath("$.length()"){
                    value(1)
                }
                jsonPath("$[0].sender"){
                    value(contactExample2.toString())
                }
                jsonPath("$[0].subject"){
                    value("Me")
                }
                jsonPath("$[0].body"){
                    value("This is the body")
                }
                jsonPath("$[0].status"){
                    value(MessageStatus.RECEIVED.name)
                }
                jsonPath("$[0].priority"){
                    value(Priority.LOW.name)
                }
                jsonPath("$[0].channel"){
                    value(Channel.PHONECALL.name)
                }

            }
        }
    }

    @Test
    fun getAllMessagesWithFilteringEmptyArrayIntegrationTest(){

        mockMvc.get("/API/messages"){
            param("filtering", "DISCARDED")
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$"){
                    isArray()
                    isEmpty()
                }

            }
        }


    }

    /* Get Messages By Id Integration Test */
    @Test
    fun getMessageByIdIntegrationTest(){

        mockMvc.get("/API/messages/1")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$.sender"){
                        value(contactExample.toString())
                    }
                    jsonPath("$.subject"){
                        value("Me")
                    }
                    jsonPath("$.body"){
                        value("This is the body")
                    }
                    jsonPath("$.status"){
                        value(MessageStatus.READ.name)
                    }
                    jsonPath("$.priority"){
                        value(Priority.HIGH.name)
                    }
                    jsonPath("$.channel"){
                        value(Channel.EMAIL.name)
                    }
                }
            }
    }

    @Test
    fun getMessageByIdMessageNotFoundIntegrationTest(){
        mockMvc.get("/API/messages/9")
            .andExpect {
                status {
                    isNotFound()
                }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }
    }

    /* Save Message Integration Test */
    @Test
    fun saveMessageIntegrationTest(){
        val mockObject = MessageDto(
            sender = "3923513416",
            status = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body"
        )

        mockMvc.post("/API/messages"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(mockObject)
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                /*jsonPath("$.sender"){
                        value(mockObject.sender)
                    }*/
                jsonPath("$.subject"){
                    value(mockObject.subject)
                }
                jsonPath("$.body"){
                    value(mockObject.body)
                }
                jsonPath("$.status"){
                    value(mockObject.status?.name)
                }
                jsonPath("$.priority"){
                    value(mockObject.priority.name)
                }
                jsonPath("$.channel"){
                    value(mockObject.channel.name)
                }
            }
        }
    }

    /*Change Message Status Integration Test*/

    @Test
    fun changeMessageStatusIntegrationTest(){

        mockMvc.post("/API/messages/1"){
            param("comment", "This is an example of comment")
            param("stateStr", MessageStatus.PROCESSING.name)
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(messageExample.copy(status = MessageStatus.PROCESSING, sender = contactExample.toString())))
            }
        }
    }
    @Test
    fun changeStatusInvalidStateValueIntegrationTest(){
        mockMvc.post("/API/messages/1"){
            param("comment", "This is an example of comment")
            param("stateStr", "DoneStatus")
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
    fun changeStatusMessageNotFoundIntegrationTest(){

        mockMvc.post("/API/messages/9"){
            param("comment", "This is an example of comment")
            param("stateStr", MessageStatus.READ.name)
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
    fun changeStatusInvalidTransitionIntegrationTest(){
        mockMvc.post("/API/messages/1"){
            param("comment", "This is an example of comment")
            param("stateStr", MessageStatus.RECEIVED.name)
        }.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }


    /*Get History Integration Test*/
    @Test
    fun getMessageHistoryIntegrationTest(){

        mockMvc.get("/API/messages/1/history")
            .andExpect {
                status {
                    isOk()
                }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    jsonPath("$"){
                        isArray()
                        isNotEmpty()
                    }
                    jsonPath("$[0].initial_state"){
                        value(MessageStatus.RECEIVED.name)
                    }
                    jsonPath("$[0].final_state"){
                        value(MessageStatus.READ.name)
                    }
                    jsonPath("$[0].comments"){
                        value("This is an example of comment")
                    }
                }
            }
    }

    fun getHistoryMessageMessageNotFoundIntegrationTest(){

        mockMvc.get("/API/messages/9/history")
            .andExpect {
                status {
                    isNotFound()
                }
                content {
                    contentType(MediaType.APPLICATION_PROBLEM_JSON)
                }
            }
    }

    /*Change Priority Integration Test*/
    @Test
    fun changePriorityIntegrationTest(){

        val messageExample2 = MessageDto(
            "mario.rossi@gmail.com",
            "Me",
            body = "This is the body",
            channel = Channel.EMAIL,
            MessageStatus.RECEIVED,
            Priority.HIGH
        )

        mockMvc.post("/API/messages"){
            contentType = MediaType.APPLICATION_JSON
            content = ObjectMapper().writeValueAsString(messageExample2)
        }.andExpect {
            status {
                isCreated()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                /* PER QUESTO CONTROLLO DOBBIAMO ASPETTARE CHE VITTORIO CORREGGA IL DTO*/
                json(ObjectMapper().writeValueAsString(messageExample2.copy(sender = contactExample.toString())))
            }
        }

        mockMvc.put(
            "/API/messages/3/priority"
        ){
            contentType = MediaType.APPLICATION_JSON
            param("priorityStr", Priority.LOW.name)
        }.andExpect {
            status {
                isOk()
            }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(ObjectMapper().writeValueAsString(messageExample2.copy(priority = Priority.LOW, sender = contactExample.toString())))
            }
        }
    }
    @Test
    fun changePriorityMessageNotFoundExceptionIntegrationTest(){

        mockMvc.put(
            "/API/messages/9/priority"
        ){
            contentType = MediaType.APPLICATION_JSON
            param("priorityStr", Priority.LOW.name)
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
    fun changePriorityInvalidPriorityValueIntegrationTest(){

        mockMvc.put(
            "/API/messages/1/priority"
        ){
            contentType = MediaType.APPLICATION_JSON
            param("priorityStr", "LowPriority")
        }.andExpect {
            status {
                isBadRequest()
            }
            content {
                contentType(MediaType.APPLICATION_PROBLEM_JSON)
            }
        }
    }

}
package crm.message

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import wa.lab5.dto.message.EventDto
import wa.lab5.dto.message.MessageDto
import wa.lab5.exceptions.message.MessageNotFoundException
import wa.lab5.model.message.*
import wa.lab5.services.message.MessageService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import wa.lab5.Lab5Application
import wa.lab5.controller.MessageController
import wa.lab5.exceptions.message.InvalidTargetStateException
import java.sql.Timestamp

@WebMvcTest(controllers = arrayOf(MessageController::class))
@ContextConfiguration(classes = arrayOf(Lab5Application::class))
class MessageControllerUnitTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var messageService: MessageService

    /* API get all messages */

    @Test
    fun getAllMessages() {

        val expectedMessageList = listOf(
            MessageDto(
                sender = "mario.rossi@gmail.com",
                subject = "Subject",
                body = "Body",
                channel = Channel.EMAIL,
                status = MessageStatus.READ,
                priority = Priority.MEDIUM
            ),
            MessageDto(
                sender = "luigi.verdi@gmail.com",
                subject = "Subject2",
                body = "Body2",
                channel = Channel.EMAIL,
                status = MessageStatus.DONE,
                priority = Priority.HIGH
            )
        )

        every { messageService.getAllMessages(any(), any(), any(), any()) } returns expectedMessageList

        mockMvc.perform(get("/API/messages"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].sender").value("mario.rossi@gmail.com"))
            .andExpect(jsonPath("$[0].subject").value("Subject"))
            .andExpect(jsonPath("$[0].body").value("Body"))
            .andExpect(jsonPath("$[0].channel").value(Channel.EMAIL.name))
            .andExpect(jsonPath("$[0].status").value(MessageStatus.READ.name))
            .andExpect(jsonPath("$[0].priority").value(Priority.MEDIUM.name))
            .andExpect(jsonPath("$[1].sender").value("luigi.verdi@gmail.com"))
            .andExpect(jsonPath("$[1].subject").value("Subject2"))
            .andExpect(jsonPath("$[1].body").value("Body2"))
            .andExpect(jsonPath("$[1].channel").value(Channel.EMAIL.name))
            .andExpect(jsonPath("$[1].status").value(MessageStatus.DONE.name))
            .andExpect(jsonPath("$[1].priority").value(Priority.HIGH.name))
    }

    /* API get message by ID */

    @Test
    fun getMessageByIdTest() {

        val mockMessage = MessageDto(
            sender = "luigi.verdi@gmail.com",
            subject = "Subject",
            body = "Body",
            channel = Channel.EMAIL,
            status = MessageStatus.DONE,
            priority = Priority.LOW
        )

        every { messageService.getMessage(1L) } returns mockMessage

        mockMvc.perform(get("/API/messages/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sender").value(mockMessage.sender))
            .andExpect(jsonPath("$.body").value(mockMessage.body))
            .andExpect(jsonPath("$.channel").value(mockMessage.channel.name))
            .andExpect(jsonPath("$.status").value(mockMessage.status?.name))
            .andExpect(jsonPath("$.priority").value(mockMessage.priority.name))
            .andExpect(jsonPath("$.subject").value(mockMessage.subject))
    }


    @Test
    fun getMessageByIdMessageNotFoundTest() {

        every { messageService.getMessage(1L) } throws MessageNotFoundException("Message not found")

        mockMvc.perform(get("/API/messages/1"))
            .andExpect(status().isNotFound)
    }

    /* API save message */

    @Test
    fun saveMessageTest() {

        val mockMessageToSave =  MessageDto(
            sender = "mario.rossi@gmail.com",
            subject = "Subject",
            body = "Body",
            channel = Channel.EMAIL,
            status = MessageStatus.READ,
            priority = Priority.MEDIUM
        )


        every {  messageService.createMessage(any())} returns mockMessageToSave

        mockMvc.perform(post("/API/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{"
                    + "\"sender\":\"mario.rossi@gmail.com\","
                    + "\"date\":\"2019-01-22\","
                    + "\"subject\":\"Subject\","
                    + "\"body\":\"Body\","
                    + "\"channel\":\""+ Channel.EMAIL.name+"\","
                    + "\"status\":\""+MessageStatus.READ.name+"\","
                    + "\"priority\":\""+Priority.MEDIUM.name+"\"}"
            ))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sender").value(mockMessageToSave.sender))
            .andExpect(jsonPath("$.body").value(mockMessageToSave.body))
            .andExpect(jsonPath("$.channel").value(mockMessageToSave.channel.name))
            .andExpect(jsonPath("$.status").value(mockMessageToSave.status?.name))
            .andExpect(jsonPath("$.priority").value(mockMessageToSave.priority.name))
            .andExpect(jsonPath("$.subject").value(mockMessageToSave.subject))
    }


    /*API change message status*/
    @Test
    fun changeMessageStatusTest(){

        var mockMessage = MessageDto(
            "mario.rossi@gmail.com",
            body = "This is an example of body",
            channel = Channel.EMAIL,
            status = MessageStatus.DONE,
            priority = Priority.HIGH,
            subject = "Work Survey")

        every { messageService.changeState(1L, MessageStatus.DONE, "Example of comment") } returns mockMessage

        mockMvc.perform(post("/API/messages/1")
            .param("comment","Example of comment")
            .param("stateStr", "done"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sender").value(mockMessage.sender))
            .andExpect(jsonPath("$.body").value(mockMessage.body))
            .andExpect(jsonPath("$.channel").value(mockMessage.channel.name))
            .andExpect(jsonPath("$.status").value(mockMessage.status?.name))
            .andExpect(jsonPath("$.priority").value(mockMessage.priority.name))
            .andExpect(jsonPath("$.subject").value(mockMessage.subject))

    }

    @Test
    fun changeMessageStatusInvalidStatusValueTest(){

        mockMvc.perform(post("/API/messages/1")
            .param("comment","Example of comment")
            .param("stateStr", "readState"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))

    }

    @Test
    fun changeMessageStatusMessageNotFoundTest(){

        every { messageService.changeState(1L, MessageStatus.DONE, "Example of comment") } throws MessageNotFoundException()

        mockMvc.perform(post("/API/messages/1")
            .param("comment","Example of comment")
            .param("stateStr", "done"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun changeMessageStatusInvalidTransitionTest(){
        every { messageService.changeState(1L, MessageStatus.DONE, "Example of comment") } throws InvalidTargetStateException(1L.toString(),MessageStatus.DONE)

        mockMvc.perform(post("/API/messages/1")
            .param("comment","Example of comment")
            .param("stateStr", "done"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /*API get message history*/
    @Test
    fun getMessageHistoryTest(){
        var mockEventDto = EventDto(MessageStatus.READ, MessageStatus.DISCARDED, "Questo è un esempio di commento", Timestamp(System.currentTimeMillis()))
        var mockList = listOf(
            mockEventDto,
            mockEventDto.copy(MessageStatus.READ, MessageStatus.PROCESSING, "Questo è un esempio di commento parte 2"),
            mockEventDto.copy(MessageStatus.READ, MessageStatus.DONE, "Questo è un esempio di commento parte 3")
        )

        every { messageService.getMessageEvents(1L) } returns mockList

        mockMvc.perform(get("/API/messages/1/history"))
            .andExpect(jsonPath("$").isArray)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0].initial_state").value(MessageStatus.READ.name))
            .andExpect(jsonPath("$[0].final_state").value(MessageStatus.DISCARDED.name))
            .andExpect(jsonPath("$[0].comments").value("Questo è un esempio di commento"))
            .andExpect(jsonPath("$[1].initial_state").value(MessageStatus.READ.name))
            .andExpect(jsonPath("$[1].final_state").value(MessageStatus.PROCESSING.name))
            .andExpect(jsonPath("$[1].comments").value("Questo è un esempio di commento parte 2"))
            .andExpect(jsonPath("$[2].initial_state").value(MessageStatus.READ.name))
            .andExpect(jsonPath("$[2].final_state").value(MessageStatus.DONE.name))
            .andExpect(jsonPath("$[2].comments").value("Questo è un esempio di commento parte 3"))


    }

    @Test
    fun getMessageHistoryTestWrong(){

        every { messageService.getMessageEvents(1L) } throws MessageNotFoundException(1L.toString())

        mockMvc.perform(get("/API/messages/1/history"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    /* API change priority Message */

    @Test
    fun changePriorityMessageTest(){
        var mockMessage = MessageDto(
            "mario.rossi@gmail.com",
            body = "This is an example of body",
            channel = Channel.EMAIL,
            status = MessageStatus.READ,
            priority = Priority.HIGH,
            subject = "Work Survey")

        every { messageService.changePriorityMessage(1L, Priority.HIGH) } returns mockMessage

        mockMvc.perform(put("/API/messages/1/priority")
            .param("priorityStr", "HIGH"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sender").value(mockMessage.sender))
            .andExpect(jsonPath("$.body").value(mockMessage.body))
            .andExpect(jsonPath("$.channel").value(mockMessage.channel.name))
            .andExpect(jsonPath("$.priority").value(mockMessage.priority.name))
            .andExpect(jsonPath("$.subject").value(mockMessage.subject))

    }

    @Test
    fun changePriorityMessageInvalidPriorityValueTest(){

        mockMvc.perform(put("/API/messages/1/priority")
            .param("priorityStr", "HighPriority"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun changePriorityMessageMessageNotFoundTest(){

        every { messageService.changePriorityMessage(1L, Priority.HIGH) } throws MessageNotFoundException(1L.toString())

        mockMvc.perform(put("/API/messages/1/priority")
            .param("priorityStr", "high"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


}
package crm.message

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.camel.ProducerTemplate
import wa.lab5.model.message.Event
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import wa.lab5.configurations.MsgTransitionsConfigurations
import wa.lab5.dto.message.MessageDto
import wa.lab5.dto.message.toDto
import wa.lab5.exceptions.message.InvalidTargetStateException
import wa.lab5.exceptions.message.MessageNotFoundException
import wa.lab5.model.contact.Contact
import wa.lab5.model.contact.ContactCategory
import wa.lab5.model.message.Channel
import wa.lab5.model.message.Message
import wa.lab5.model.message.MessageStatus
import wa.lab5.model.message.Priority
import wa.lab5.repository.contact.ContactRepository
import wa.lab5.repository.message.EventRepository
import wa.lab5.repository.message.MessageRepository
import wa.lab5.services.message.MessageService
import java.sql.Timestamp
import java.util.*

class MessageServiceUnitTests {
    val messageRepository = mockk<MessageRepository>()
    val contactRepository = mockk<ContactRepository>()
    val eventRepository = mockk<EventRepository>()
    val transitions = mockk<MsgTransitionsConfigurations>()
    val producer = mockk<ProducerTemplate>()
    val messageService: MessageService = MessageService(messageRepository, contactRepository, eventRepository, transitions, producer)


    /* get All Messages Service Unit Test */
    @Test
    fun getAllMessages() {
        val messageId = 1L
        val page = 0
        val limit = 20
        val sort = null
        val filter = null

        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.READ,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        val mockMessage2 = Message(
            sender = Contact(
                name = "Luigi",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        val mockList = listOf(mockMessage, mockMessage2)

        every { messageRepository.findAll( PageRequest.of(page, limit ) ).content } returns mockList

        val result = messageService.getAllMessages(page, limit, sort, filter)
        assertEquals(result, mockList.map { it.toDto() })

        verify (exactly = 1) { messageRepository.findAll( PageRequest.of(page, limit ) ).content  }
    }

    /* Create Message Service Unit Test */
    @Test
    fun createMessageServiceUnitTest(){
        val mockMessageDto = MessageDto(
            sender = "luigi.verdi@gmail.com",
            subject = "Subject",
            body = "Body",
            channel = Channel.EMAIL,
            status = MessageStatus.DONE,
            priority = Priority.LOW
        )

        //val result = messageService.createMessage(mockMessageDto)
    }

    @Test
    fun createMessageServiceInvalidContactUnitTest(){

    }


    /* Get Message Service Unit Test */
    @Test
    fun getMessageServiceUnitTest(){
        val messageId = 1L
        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        every { messageRepository.findMessageById(messageId) } returns mockMessage
        val result = messageService.getMessage(messageId)

        assertEquals(result, mockMessage.toDto())
        verify (exactly = 1){  messageRepository.findMessageById(messageId) }

    }

    @Test
    fun getMessageServiceMessageNotFoundUnitTest(){
        val messageId = 1L

        every { messageRepository.findMessageById(messageId) } returns null
        assertThrows<MessageNotFoundException> {
            val result = messageService.getMessage(messageId)
        }
        verify (exactly = 1){  messageRepository.findMessageById(messageId) }
    }

    /* Change Status Service Unit Test */
    @Test
    fun changeStatusServiceUnitTest(){
        val messageId = 1L
        val newStatus = MessageStatus.READ
        val comment = "Example of Comment 1201931893"

        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        val mockEvent = Event(id=0, MessageStatus.RECEIVED, MessageStatus.READ, "Example of Comment 1201931893", Timestamp(System.currentTimeMillis()), message = mockMessage)

        every { messageRepository.findMessageById(messageId) } returns mockMessage
        every { transitions.received } returns arrayOf(MessageStatus.READ)
        every { messageRepository.save<Message>( any()) } returns mockMessage
        every { eventRepository.save<Event>( any() ) } returns mockEvent

        val result = messageService.changeState(messageId, newStatus, comment)

        assertEquals(result, mockMessage.toDto().copy(status = MessageStatus.READ))
        verify (exactly = 1){ messageRepository.findMessageById(messageId) }
        verify (exactly = 1){ transitions.received }
        verify (exactly = 1){ messageRepository.save(any()) }
        verify (exactly = 1){ eventRepository.save(any()) }
    }

    @Test
    fun changeMessageStatusServiceMessageNotFoundUnitTest(){
        val messageId = 1L
        val newStatus = MessageStatus.READ
        val comment = "Example of Comment 1201931893"

        every { messageRepository.findMessageById(messageId) } returns null

        assertThrows<MessageNotFoundException> {
            val result = messageService.changeState(messageId, newStatus, comment)
        }
        verify (exactly = 1) { messageRepository.findMessageById(messageId) }
        verify (exactly = 0){ transitions.received }
        verify (exactly = 0){ messageRepository.save(any()) }
        verify (exactly = 0){ eventRepository.save(any()) }
    }

    @Test
    fun changeMessageStatusServiceInvalidTransitionUnitTest(){
        val messageId = 1L
        val newStatus = MessageStatus.DISCARDED
        val comment = "Example of Comment 1201931893"

        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        every { messageRepository.findMessageById(messageId) } returns mockMessage
        every { transitions.received } returns arrayOf(MessageStatus.READ)

        assertThrows<InvalidTargetStateException> {
            val result = messageService.changeState(messageId, newStatus, comment)
        }
        verify (exactly = 1) { messageRepository.findMessageById(messageId) }
        verify (exactly = 1){ transitions.received }
        verify (exactly = 0){ messageRepository.save(any()) }
        verify (exactly = 0){ eventRepository.save(any()) }
    }

    /* Get History Service Unit Test */
    @Test
    fun getHistoryServiceUnitTest(){
        val messageId = 1L
        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        val mockEvent = Event(id = 1L, MessageStatus.READ, MessageStatus.DISCARDED, "Questo è un esempio di commento", Timestamp(System.currentTimeMillis()), message = mockMessage)
        val mockEvent2 = Event(id = 2L, MessageStatus.READ, MessageStatus.PROCESSING, "Questo è un esempio di commento parte 2", Timestamp(System.currentTimeMillis()), message = mockMessage)
        val mockEvent3 = Event(id = 3L, MessageStatus.PROCESSING, MessageStatus.DONE, "Questo è un esempio di commento parte 3", Timestamp(System.currentTimeMillis()), message = mockMessage)
        val mockListEvent = listOf(
            mockEvent,
            mockEvent2,
            mockEvent3
        )

        every { messageRepository.findMessageById(messageId)} returns mockMessage
        every { eventRepository.findEventsByMessageId(messageId) } returns mockListEvent
        val result = messageService.getMessageEvents(messageId)

        assertEquals(result, mockListEvent.map { it.toDto() })
        verify (exactly = 1) { messageRepository.findMessageById(messageId) }
        verify (exactly = 1) { eventRepository.findEventsByMessageId(messageId) }
    }

    @Test
    fun getMessageHistoryServiceMessageNotFoundUnitTest(){
        val messageId = 1L

        every { messageRepository.findMessageById(messageId) } returns null

        assertThrows<MessageNotFoundException> {
            val result = messageService.getMessageEvents(messageId)
        }
        verify (exactly = 1) { messageRepository.findMessageById(messageId) }
        verify (exactly = 0) { eventRepository.findEventsByMessageId(messageId) }
    }

    /* Change Priority Service Unit Test*/

    @Test
    fun changeMessagePriorityServiceUnitTest(){
        val messageId = 1L
        val priority = Priority.HIGH

        val mockMessage = Message(
            sender = Contact(
                name = "Mario",
                surname = "Rossi",
                ssnCode = null,
                category = ContactCategory.UNKNOWN,
                messages = mutableListOf(),
                emails =  mutableListOf(),
                telephoneNumbers = mutableListOf(),
                addresses = mutableListOf()
            ),
            date = Date(System.currentTimeMillis()),
            state = MessageStatus.RECEIVED,
            channel = Channel.TEXTMESSAGE,
            priority = Priority.LOW,
            subject = "ExampleSubject",
            body = "Example of body",
            events = mutableListOf()
        )

        every { messageRepository.findMessageById(messageId) } returns mockMessage
        every { messageRepository.save<Message>( any() ) } returns mockMessage
        val result = messageService.changePriorityMessage(messageId, priority)

        assertEquals(result, mockMessage.toDto().copy(priority = Priority.HIGH))
        verify (exactly = 1){ messageRepository.findMessageById(messageId) }
        verify (exactly = 1){ messageRepository.save( any() ) }

    }

    @Test
    fun changeMessagePriorityServiceMessageNotFoundUnitTest(){
        val messageId = 1L
        val priority = Priority.HIGH

        every { messageRepository.findMessageById(messageId) } returns null
        assertThrows<MessageNotFoundException> {
            val result = messageService.changePriorityMessage(messageId, priority)
        }

        verify (exactly = 1){ messageRepository.findMessageById(messageId) }
        verify (exactly = 0){ messageRepository.save( any() ) }

    }
}

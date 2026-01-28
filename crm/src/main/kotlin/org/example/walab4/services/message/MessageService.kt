package org.example.walab4.services.message

import jakarta.persistence.EntityManager
import org.apache.camel.CamelExecutionException
import org.apache.camel.ProducerTemplate
import org.example.walab4.configurations.MsgTransitionsConfigurations
import org.example.walab4.dto.message.EmailDto
import org.example.walab4.dto.message.EventDto
import org.example.walab4.dto.message.MessageDto
import org.example.walab4.dto.message.toDto
import org.example.walab4.exceptions.mail.MailNotSentException
import org.example.walab4.exceptions.message.InvalidContactException
import org.example.walab4.exceptions.message.InvalidTargetStateException
import org.example.walab4.exceptions.message.MessageNotFoundException
import org.example.walab4.model.*
import org.example.walab4.model.contact.*
import org.example.walab4.model.message.*
import org.example.walab4.repository.contact.ContactRepository
import org.example.walab4.repository.message.EventRepository
import org.example.walab4.repository.message.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.sql.Timestamp
import java.util.*

@Service
@Transactional
class MessageService(
    private val messageRepo: MessageRepository,
    private val contactRepo : ContactRepository,
    private val eventRepo: EventRepository,
    private val transitions: MsgTransitionsConfigurations
) :
    IMessageService {

    @Autowired
    private lateinit var entityManager: EntityManager
    override fun getAllMessages(page: Int, limit: Int, sort: String?, stateFilter: List<MessageStatus>?, priorityFilter: List<Priority>?): List<MessageDto> {

        val paging = if (sort != null) {
            PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, sort))
        } else {
            PageRequest.of(page, limit)
        }
        val messages = messageRepo.findAll(paging).content
        var filteredMessages = messages.filter { stateFilter?.contains(it.state) ?: true }
        filteredMessages =  filteredMessages.filter { priorityFilter?.contains(it.priority) ?: true }
        return filteredMessages.map {
            val allowedTransitions: Array<MessageStatus>
            when(it.state){
                MessageStatus.READ      -> allowedTransitions = this.transitions.read
                MessageStatus.RECEIVED  -> allowedTransitions = this.transitions.received
                MessageStatus.DONE      -> allowedTransitions = this.transitions.done
                MessageStatus.PROCESSING-> allowedTransitions = this.transitions.processing
                MessageStatus.DISCARDED -> allowedTransitions = this.transitions.discarded
                MessageStatus.FAILED    -> allowedTransitions = this.transitions.failed
            }
            it.toDto(allowedTransitions)
        }

    }

    override fun createMessage(messageDto: MessageDto): MessageDto {


        var contact: Contact? = when (messageDto.channel) {
            Channel.EMAIL -> {
              contactRepo.findByEmail(messageDto.sender).firstOrNull()

            }
            Channel.PHONECALL, Channel.TEXTMESSAGE -> {
             contactRepo.findByTelephone(messageDto.sender).firstOrNull()

            }
            else -> null // Gestione del caso in cui il canale non è gestito
        }

        if (contact == null){

            val newContact = Unknown(

                name = null,
                surname = null,
                ssnCode = null,
                addresses = mutableListOf(),
                emails = mutableListOf(),
                telephoneNumbers = mutableListOf(),
                messages = mutableListOf(),
                category = ContactCategory.UNKNOWN

            )


            if (messageDto.channel == Channel.EMAIL){
                val email = Email(email = messageDto.sender, contacts =  mutableListOf(newContact))
                newContact.emails?.add(email)
            }else{
                val number = Telephone(number = messageDto.sender, contacts = mutableListOf(newContact) )
                newContact.telephoneNumbers?.add(number)
            }




            val savedContact = contactRepo.save(newContact?:throw InvalidContactException("Invalid Contact"))
            contact = savedContact
            //updateContactCategory(savedContact.id, categoryValue = savedContact.category.ordinal)
        }
        else {
         /*   val contactDtoFields = messageDto.sender.javaClass.declaredFields
            contactDtoFields.forEach { field ->
                field.isAccessible = true
                val dtoValue = field.get(messageDto.sender)
                val contactField = try {
                    contact.javaClass.getDeclaredField(field.name)
                } catch (e: NoSuchFieldException) {
                    println("Cambia il nome del field")
                    null
                }
                if (contactField != null) {
                    contactField.isAccessible = true
                    val contactValue = contactField.get(contact)
                    if (dtoValue != contactValue && dtoValue!=null) {
                        contactField.set(contact, dtoValue)
                    }
                }
            }*/
            contactRepo.save(contact)


        }

        val resContact : Contact = contact?: throw InvalidContactException("Invalid Contact")

        val message = Message(sender =  resContact, date = Date(System.currentTimeMillis()), subject = messageDto.subject, body = messageDto.body,
                            channel = messageDto.channel, state = MessageStatus.RECEIVED, priority = messageDto.priority, events = mutableListOf()
        )
        val savedMessage = messageRepo.save(message)
        return savedMessage.toDto(transitions.received)
    }
    private fun updateContactCategory(contactId: Long, categoryValue: Int) {
        entityManager.createNativeQuery("UPDATE contact SET category = :categoryValue WHERE id = :contactId")
            .setParameter("categoryValue", categoryValue)
            .setParameter("contactId", contactId)
            .executeUpdate()
    }
    override fun getMessage(messageId: Long): MessageDto {
        val message = messageRepo.findMessageById(messageId)?: throw MessageNotFoundException(messageId.toString())
        val allowedTransitions: Array<MessageStatus>
        when(message.state){
            MessageStatus.READ      -> allowedTransitions = this.transitions.read
            MessageStatus.RECEIVED  -> allowedTransitions = this.transitions.received
            MessageStatus.DONE      -> allowedTransitions = this.transitions.done
            MessageStatus.PROCESSING-> allowedTransitions = this.transitions.processing
            MessageStatus.DISCARDED -> allowedTransitions = this.transitions.discarded
            MessageStatus.FAILED    -> allowedTransitions = this.transitions.failed
        }
        return message.toDto(allowedTransitions)
    }

    override fun changeState(messageId: Long, newState: MessageStatus, comment: String): MessageDto {
        val message = messageRepo.findMessageById(messageId) ?: throw MessageNotFoundException(messageId.toString())
        val initialState = message.state
        val finalState = newState

        val allowedTransitions: Array<MessageStatus>
        when(message.state){
            MessageStatus.READ      -> allowedTransitions = this.transitions.read
            MessageStatus.RECEIVED  -> allowedTransitions = this.transitions.received
            MessageStatus.DONE      -> allowedTransitions = this.transitions.done
            MessageStatus.PROCESSING-> allowedTransitions = this.transitions.processing
            MessageStatus.DISCARDED -> allowedTransitions = this.transitions.discarded
            MessageStatus.FAILED    -> allowedTransitions = this.transitions.failed
        }

        if(!allowedTransitions.contains(newState))
            throw InvalidTargetStateException(messageId.toString(), newState)

        message.apply { message.state = newState }

        val event = Event(
            message = message,
            initialState = initialState,
            finalState = finalState,
            comments = comment,
            timestamp = Timestamp(Date().time)
        )

        this.messageRepo.save(message)
        this.eventRepo.save(event)

        val nextTransitions: Array<MessageStatus>
        when(message.state){
            MessageStatus.READ      -> nextTransitions = this.transitions.read
            MessageStatus.RECEIVED  -> nextTransitions = this.transitions.received
            MessageStatus.DONE      -> nextTransitions = this.transitions.done
            MessageStatus.PROCESSING-> nextTransitions = this.transitions.processing
            MessageStatus.DISCARDED -> nextTransitions = this.transitions.discarded
            MessageStatus.FAILED    -> nextTransitions = this.transitions.failed
        }

        return message.toDto(nextTransitions)
    }

    override fun getMessageEvents(messageId: Long): List<EventDto> {
        val msg: Message = messageRepo.findMessageById(messageId) ?: throw MessageNotFoundException(messageId.toString())

        val history = eventRepo.findEventsByMessageId(messageId)
        return history.map { it.toDto() }
    }

    override fun changePriorityMessage(messageId: Long, priority: Priority): MessageDto {
        val msg: Message = messageRepo.findMessageById(messageId) ?: throw MessageNotFoundException(messageId.toString())

        msg.apply { msg.priority = priority }
        messageRepo.save(msg)

        return msg.toDto(arrayOf())
    }
}
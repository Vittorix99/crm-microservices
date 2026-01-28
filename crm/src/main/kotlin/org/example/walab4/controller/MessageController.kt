package org.example.walab4.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.example.walab4.dto.message.ChangeStatusMessageDto
import org.example.walab4.dto.message.EventDto
import org.example.walab4.dto.message.MessageDto
import org.example.walab4.exceptions.message.InvalidPriorityValueException
import org.example.walab4.exceptions.message.InvalidStateValueException
import org.example.walab4.model.message.MessageStatus
import org.example.walab4.model.message.Priority
import org.example.walab4.services.message.IMessageService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

const val PAGE_DEFAULT = 0
const val LIMIT_DEFAULT = 15


@Validated
@RestController
@RequestMapping("/messages")
class MessageController(private val messageService: IMessageService) {

    private val LOGGER: Logger = LogManager.getLogger()

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllMessages(
        @RequestParam("page") page: Int?,
        @RequestParam("limit")
        @Valid
        @Min(0) @Max(1000)
        limit: Int?,
        @RequestParam("sorting", required = false) sorting: String?,
        @RequestParam("msgStatusFilter", required = false) msgStatusFilter: List<MessageStatus>?,
        @RequestParam("msgPriorityFilter", required = false) msgPriorityFilter: List<Priority>?,
        ): List<MessageDto>?
    {
        val pageParam = page ?: PAGE_DEFAULT
        val limitParam = limit ?: LIMIT_DEFAULT

        val res = messageService.getAllMessages(pageParam,limitParam,sorting,msgStatusFilter, msgPriorityFilter)

        LOGGER.info("[GET - API/messages] - SUCCESS - Messages retrieved correctly")

        return res
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMessage(
        @RequestBody messageDto: MessageDto
    ) : MessageDto?{
        val message = messageService.createMessage(messageDto)
        LOGGER.info("[POST - API/messages] - SUCCESS - Message saved successfully")
        return  message


    }

    @GetMapping("/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    fun getMessageById(
        @PathVariable messageId: Long
    ):MessageDto?{
        val message =  messageService.getMessage(messageId)
        LOGGER.info("[GET - API/messages/${messageId}] - SUCCESS - Message retrieved correctly")
        return message


    }

    @PostMapping("/{messageId}")
    @ResponseStatus(HttpStatus.OK)
    fun changeMessageStatus(
        @PathVariable messageId: Long,
        @RequestBody request: ChangeStatusMessageDto
    ):MessageDto{
        val stateStr = request.stateStr
        val comment = request.comment

        val state: MessageStatus
        try {
            state = MessageStatus.valueOf(stateStr.uppercase())
        } catch (iae: IllegalArgumentException) {
            throw InvalidStateValueException(messageId.toString(), stateStr)
        }

        val msgModified: MessageDto = messageService.changeState(messageId, state, comment)
        LOGGER.info("[POST - API/messages/${messageId}] - SUCCESS - Message state modified successfully")
        return  msgModified
    }
    @GetMapping("/{messageId}/history")
    @ResponseStatus(HttpStatus.OK)
    fun getHistoryMessage(
        @PathVariable messageId: Long
    ): List<EventDto>
    {
        val msgHistory: List<EventDto> = messageService.getMessageEvents(messageId)
        LOGGER.info("[GET - API/messages/$messageId/history - SUCCESS] \n $msgHistory")
        return msgHistory
    }

    @PutMapping("/{messageId}/priority")
    @ResponseStatus(HttpStatus.OK)
    fun changePriorityMessage(
        @PathVariable messageId: Long,
        @Validated @RequestParam priorityStr: String
    ): MessageDto?
    {
        val priority: Priority
        try {
            priority = Priority.valueOf(priorityStr.uppercase())
        } catch (iae: IllegalArgumentException) {
            throw InvalidPriorityValueException(messageId.toString(), priorityStr)
        }

        val updatedMsg: MessageDto = messageService.changePriorityMessage(messageId, priority)
        LOGGER.info("PUT - API/messages/$messageId/priority - SUCCESS - Message priority modified successfully] \n $updatedMsg")

        return updatedMsg
    }
}

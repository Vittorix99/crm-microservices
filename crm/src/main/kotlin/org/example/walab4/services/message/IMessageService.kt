package org.example.walab4.services.message

import org.example.walab4.dto.message.EmailDto
import org.example.walab4.dto.message.EventDto
import org.example.walab4.dto.message.MessageDto
import org.example.walab4.model.message.MessageStatus
import org.example.walab4.model.message.Priority

interface IMessageService {
    fun getAllMessages(page: Int, limit: Int, sort: String?, stateFilter: List<MessageStatus>?, priorityFilter: List<Priority>?): List<MessageDto>;

    fun createMessage(messageDto: MessageDto): MessageDto;

    fun getMessage(messageId: Long): MessageDto;

    fun changeState(messageId: Long, messageStatus: MessageStatus, comment: String): MessageDto;

    fun getMessageEvents(messageId: Long): List<EventDto>;

    fun changePriorityMessage(messageId: Long, priority: Priority): MessageDto;
}










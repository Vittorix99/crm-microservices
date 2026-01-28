package wa.communicationmanager.routes

import wa.communicationmanager.message.Channel
import wa.communicationmanager.dto.MessageDto
import wa.communicationmanager.message.MessageStatus
import wa.communicationmanager.message.Priority

data class GmailInfo(
    val body: String?,
    val headers : MutableMap<String,Any>
)

fun GmailInfo.toMessageDto(): MessageDto {
    val from = extractEmail(this.headers["CamelGoogleMailStreamFrom"].toString()) ?:""
    val subject = this.headers["CamelGoogleMailStreamSubject"].toString()
    val body = this.body?:""

    val dto = MessageDto(
        body= body,
        subject = subject,
        channel = Channel.EMAIL,
        priority = Priority.MEDIUM,
        sender = from,
        status = MessageStatus.RECEIVED,
        transitions = arrayOf()
    )
    return dto

}


fun extractEmail(headerValue: String): String? {
    // Regex to match email enclosed in < and >
    val bracketPattern = "<([^>]+)>".toRegex()
    bracketPattern.find(headerValue)?.groupValues?.get(1)?.let {
        return it
    }

    // Regex to match a plain email address if the first pattern doesn't match
    val emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b".toRegex()
    return emailPattern.find(headerValue)?.value
}
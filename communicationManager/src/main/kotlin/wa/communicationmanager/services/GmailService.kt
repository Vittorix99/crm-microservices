package wa.communicationmanager.services

import org.apache.camel.CamelExecutionException
import org.apache.camel.ProducerTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import wa.communicationmanager.dto.EmailDto
import wa.communicationmanager.exceptions.email.MailNotSentException
import java.util.*

@Service
@Transactional
class GmailService(
    private val producerTemplate: ProducerTemplate
){

    fun sendMessage(emailDto: EmailDto): EmailDto {
        val headers = mapOf(
            "CamelGoogleMailFrom" to "wa2group15@gmail.com",
            "CamelGoogleMailTo" to emailDto.recipient,
            "CamelGoogleMailSubject" to emailDto.subject,
            "CamelGoogleMailTimestamp" to System.currentTimeMillis(),
            "CamelGoogleMailDirection" to "OUTBOUND",
        )

        val emailRaw = """
            To: ${emailDto.recipient}
            From: wa2group15@gmail.com
            Subject: ${emailDto.subject}
            Content-Type: text/plain;charset=UTF-8
            
            ${emailDto.body}
            """.trimIndent()

        var emailEncoded = Base64.getEncoder().encodeToString(emailRaw.toByteArray(charset = Charsets.UTF_8 ))
        var message = com.google.api.services.gmail.model.Message().setRaw(emailEncoded)

        try {
            producerTemplate.sendBodyAndHeaders("direct:sendEmail", message, headers)
        }catch (e: CamelExecutionException){
            throw MailNotSentException(e)
        }

        return emailDto
    }
}
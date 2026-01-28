package wa.communicationmanager.controllers

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import wa.communicationmanager.dto.EmailDto
import wa.communicationmanager.services.GmailService

@RestController
@RequestMapping("/gmail")
class GmailController(
    private val gmailService: GmailService
) {
    private val LOGGER: Logger = LogManager.getLogger()

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    fun sendEmail(
        @RequestBody emailDto: EmailDto
    ) {
        gmailService.sendMessage(emailDto)
        LOGGER.info("[POST - gmailsend] - SUCCESS - Email sent correctly")
    }
}
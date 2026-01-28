package wa.communicationmanager.routes

import com.google.api.services.gmail.model.Message
import com.google.gson.Gson
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.http.base.HttpOperationFailedException
import org.springframework.beans.factory.annotation.Value
import wa.communicationmanager.auth.KeycloakAuthService

import org.springframework.stereotype.Component
import java.util.*

@Component
class EmailRouteBuilder(
    private val keycloakAuthService: KeycloakAuthService
) : RouteBuilder(){
    @Value("\${camel.component.google-mail.client-id}") private val clientId: String? = null
    @Value("\${camel.component.google-mail.client-secret}") private val clientSecret: String? = null
    @Value("\${camel.component.google-mail.refresh-token}") private val refreshToken: String? = null

    override fun configure() {

        from("google-mail-stream://users/messages")
            .routeId("receiveEmail")
            .process { exchange ->
                try {
                    var body=""
                    if(exchange.getIn().getBody()!= null) {
                        body = exchange.getIn().getBody().toString()

                    }

                    val headers = exchange.getIn().headers
                    val emailInfo = GmailInfo(body, headers)
                    val messageDto = emailInfo.toMessageDto()

                    val accessToken = keycloakAuthService.getAccessToken()

                    exchange.getIn().body = Gson().toJson(messageDto)
                    exchange.message.setHeader("Authorization", "Bearer $accessToken")
                } catch (e: Exception) {
                    println("Error processing Message:")
                    e.printStackTrace()
                }
            }
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader("CamelGoogleMail.clientId", constant(clientId))
            .setHeader("CamelGoogleMail.clientSecret", constant(clientSecret))
            .setHeader("CamelGoogleMail.applicationName", constant("wa2-lab4"))
            .setHeader("CamelGoogleMail.refreshToken", constant(refreshToken))
            .setHeader("CamelGoogleMail.userId", constant("me"))
            .to("http://localhost:8080/messages")
            .errorHandler(deadLetterChannel("log:errors?level=ERROR").onExceptionOccurred { exchange ->
                val cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception::class.java)
                if (cause is HttpOperationFailedException) {
                    println("Failed URL: ${cause.uri}")
                    println("Response Code: ${cause.statusCode}")
                    println("Response Body: ${cause.responseBody}")
                    println("Headers: ${cause.responseHeaders}")
                    println("Stacktrace: ${cause.printStackTrace()}")
                }
            })


        from("direct:sendEmail")
            .routeId("sendEmail")
            .process{ exchange ->
                val headers = exchange.`in`
                headers.setHeader("CamelGoogleMailTo", headers.getHeader("CamelGoogleMailTo", String::class.java))
                headers.setHeader("CamelGoogleMailSubject", headers.getHeader("CamelGoogleMailSubject", String::class.java))
                headers.setHeader("CamelGoogleMailContent", headers.getBody(Message::class.java))
                headers.setHeader("CamelGoogleMailTimestamp", headers.getHeader("CamelGoogleMailTimestamp", Date::class.java))
                headers.setHeader("CamelGoogleMailDirection", headers.getHeader("CamelGoogleMailDirection", String::class.java))
            }
            .setHeader("CamelGoogleMail.clientId", constant(clientId))
            .setHeader("CamelGoogleMail.clientSecret", constant(clientSecret))
            .setHeader("CamelGoogleMail.applicationName", constant("wa2-lab4"))
            .setHeader("CamelGoogleMail.refreshToken", constant(refreshToken))
            .setHeader("CamelGoogleMail.userId", constant("me"))
            .to("google-mail://messages/send")
    }

    public fun createEmailMessage(emailBody: String, from:String, to:String, subject: String): Message {

        var emailRaw = """
            To: $to
            From: $from
            Subject: $subject
            Content-Type: text/plain;charset=UTF-8
            $emailBody
            """.trimIndent()
        var emailEncoded = Base64.getEncoder().encodeToString(emailRaw.toByteArray(charset = Charsets.UTF_8 ))
        var message = Message().setRaw(emailEncoded)
        return message
    }

}

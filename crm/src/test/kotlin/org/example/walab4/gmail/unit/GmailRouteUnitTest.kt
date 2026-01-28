package org.example.walab4.gmail.unit

import com.google.api.services.gmail.model.Message
import org.apache.camel.EndpointInject
import org.apache.camel.ExchangePattern
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.http.HttpEndpoint
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.junit5.CamelTestSupport
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.example.walab4.TestWaLab4Application
import org.example.walab4.dto.message.MessageDto
import org.example.walab4.model.message.Channel
import org.example.walab4.model.message.MessageStatus
import org.example.walab4.model.message.Priority
import org.example.walab4.routes.EmailRouteBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(classes = [GmailRouteUnitTest::class])
public class GmailRouteUnitTest: CamelTestSupport(){
    @Value("\${camel.component.google-mail.client-id}") private val clientId: String? = null
    @Value("\${camel.component.google-mail.client-secret}") private val clientSecret: String? = null
    @Value("\${camel.component.google-mail.refresh-token}") private val refreshToken: String? = null

    @Autowired(required = true)
    lateinit var template: ProducerTemplate

    @EndpointInject("mock:google-mail-stream://users/messages")
    lateinit var mockMailStreamEndpoint: MockEndpoint

    @Override
    override fun createRouteBuilder(): RouteBuilder {
        return EmailRouteBuilder()
    }

    @Test
    fun testRoute() {
        val mockMessageEndpoint = context.getEndpoint("http://localhost:8080/API/messages") as MockEndpoint

        template.send(mockMailStreamEndpoint, ExchangePattern.InOnly) {
            it.message.body = "TEST"
            it.message.setHeader("userId", "me")
        }

        val emailRaw =
            """
            To: me
            From: anyone@test.com
            Subject: TEST
            Content-Type: text/plain;charset=UTF-8
            TEST BODY
            """.trimIndent()

        val encodedEmail = Base64.getEncoder().encodeToString(emailRaw.toByteArray(charset = Charsets.UTF_8))
        var message = Message().setRaw(encodedEmail)
        template.sendBodyAndHeader(context.getEndpoint("http://localhost:8080/API/messages"), message, "userId", "me")

        val expectedMessage = MessageDto(
            body = "TEST BODY",
            subject = "TEST",
            channel = Channel.EMAIL,
            priority = Priority.MEDIUM,
            sender = "anyone@test.com",
            status = MessageStatus.RECEIVED
        )

        mockMessageEndpoint.expectedBodiesReceived(expectedMessage);
        MockEndpoint.assertIsSatisfied(context)
    }
}
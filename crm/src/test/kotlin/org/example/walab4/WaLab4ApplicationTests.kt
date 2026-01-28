package org.example.walab4

import com.google.api.services.gmail.model.Message
import org.apache.camel.*
import org.apache.camel.builder.AdviceWith
import org.apache.camel.builder.AdviceWithRouteBuilder
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.apache.camel.test.spring.junit5.MockEndpoints
import org.apache.camel.test.spring.junit5.MockEndpointsAndSkip
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@CamelSpringBootTest
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestWaLab4Application::class])
@MockEndpoints
@EnableAutoConfiguration
@MockEndpointsAndSkip("google-mail:messages/send")
class WaLab4ApplicationTests {

	@Autowired
	private lateinit var template: ProducerTemplate

	@Autowired
	private lateinit var context: CamelContext

	@EndpointInject("mock:google-mail:messages/send")
	private lateinit var mock: MockEndpoint

	@EndpointInject("mock:google-mail-stream://users/messages")
	private lateinit var mock2: MockEndpoint



	@EndpointInject("mock:http:localhost:8080/API/messages")
	private lateinit var mockEndpoint: MockEndpoint

	private var from: String = "username"
	@Test
	@Throws(InterruptedException::class)
	fun sendEmail_success() {
		val headers = mapOf(
			"CamelGoogleMailFrom" to from,
			"CamelGoogleMailTo" to "receiver@example.org",
			"CamelGoogleMailSubject" to "This is a subject",
			"CamelGoogleMailTimestamp" to System.currentTimeMillis(),
			"CamelGoogleMailDirection" to "OUTBOUND"
		)
		val body = "This is a body"


		val emailRaw = """
            To: "receiver@example.org",
            From: wa2group15@gmail.com
            Subject: "This is a subject",
            Content-Type: text/plain;charset=UTF-8
            
            ${body}
            """.trimIndent()

		var emailEncoded = Base64.getEncoder().encodeToString(emailRaw.toByteArray(charset = Charsets.UTF_8 ))
		var message = Message().setRaw(emailEncoded)

		template.sendBodyAndHeaders("direct:sendEmail", message, headers)


		mock.expectedHeaderReceived("CamelGoogleMailContent", message)
		mock.assertIsSatisfied()
	}

	@Test
	fun receivedEmail_success(){
		val emailId = "18f96c7a0d3a8c5a"
		val testHeaders = mapOf(
			"CamelGoogleMailId" to emailId

		)

		AdviceWith.adviceWith(
			context, "receiveEmail"
		) { a: AdviceWithRouteBuilder ->
			a.replaceFromWith("direct:receiveEmail")
			a.weaveByToUri("http://localhost:8080/API/messages").replace().to("mock:foo")
			a.weaveAddLast().log("Message sent to endpoint: \${body}") // Log finale della rotta
		}

		// Assicurati che la route sia avviata
		context.start()
		assertEquals(ServiceStatus.Started, context.getStatus());
		template.sendBodyAndHeaders("direct:receiveEmail",null , testHeaders)

		mock2.expectedHeaderReceived(Exchange.HTTP_METHOD, "POST")


		mock2.expectedHeaderReceived(Exchange.CONTENT_TYPE, "application/json")

		MockEndpoint.assertIsSatisfied()


	}

}

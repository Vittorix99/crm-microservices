package wa.communicationmanager.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class KeycloakAuthService(
    @Value("\${keycloak.token-uri}") private val tokenUri: String,
    @Value("\${keycloak.client-id}") private val clientId: String,
    @Value("\${keycloak.client-secret}") private val clientSecret: String
) {
    private val restTemplate = RestTemplate()

    fun getAccessToken(): String? {
        val headers = HttpHeaders()
        headers.setBasicAuth(clientId, clientSecret)
        headers.contentType = org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED

        val body = "grant_type=client_credentials"

        val entity = HttpEntity(body, headers)

        val uri = UriComponentsBuilder
            .fromUriString(tokenUri)
            .build()
            .toUri()

        val response = restTemplate.exchange(
            uri, HttpMethod.POST, entity,
            object : ParameterizedTypeReference<Map<String, Any>>() {}
        )

        return response.body?.get("access_token")?.toString()
    }
}
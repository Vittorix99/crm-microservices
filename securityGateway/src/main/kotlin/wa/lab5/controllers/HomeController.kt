package wa.lab5.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.http.HttpHeaders
import java.security.Principal
import java.util.Date

@RestController
class HomeController(val authorizedClientService: OAuth2AuthorizedClientService) {


    @GetMapping("/")
    fun home (principal: Principal?):Principal?{
        return principal
    }

    @GetMapping("/secure")
    fun secure(principal: Principal?):Principal?{
        return principal
    }
    @GetMapping("/token")
    fun getIdToken(@AuthenticationPrincipal authentication: OAuth2AuthenticationToken): String? {
      val client = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
          authentication.authorizedClientRegistrationId,
          authentication.name
      )
        return client?.accessToken?.tokenValue
    }

    @GetMapping("/me")
    fun me(
        @CookieValue(value = "XSRF-TOKEN", required = false) xsrf: String?,
        authentication: Authentication?,
    ): Map<String, Any?> {
        val principal: OidcUser? = authentication?.principal as OidcUser?
        val name = principal?.preferredUsername ?: ""
        return mapOf(
            "name" to name,
            "loginUrl" to "/oauth2/authorization/projectclient",
            "logoutUrl" to "/logout",
            "principal" to principal,
            "xsfrToken" to xsrf
        )
    }
}
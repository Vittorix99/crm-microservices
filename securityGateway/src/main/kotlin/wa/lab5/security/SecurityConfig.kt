package wa.lab5.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler


@Configuration
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(val crr: ClientRegistrationRepository) {
    private val REALM_ACCESS_CLAIM = "realm_access"
    private val ROLES_CLAIM = "roles"

    fun oidcLogoutSuccessHandler() = OidcClientInitiatedLogoutSuccessHandler(crr)
        .also { it.setPostLogoutRedirectUri("http://localhost:8080/") }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .authorizeHttpRequests {
                it.requestMatchers("/",  "/logout", "/ui/**").permitAll()
                it.requestMatchers("/secure").authenticated()
                it.anyRequest().permitAll()
            }
            .oauth2Login {  } // for authorization code flow
            .oauth2ResourceServer { it.jwt {} } // for client-credentials flow
            .csrf() {
                it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                it.csrfTokenRequestHandler(SpaCsrfTokenRequestHandler())
            }
            .addFilterAfter(CsrfCookieFilter(), BasicAuthenticationFilter::class.java)
            .logout {
                it.logoutUrl("/logout")

                it.logoutSuccessHandler(oidcLogoutSuccessHandler())

            }
            .build()
    }

    @Bean
    fun userAuthoritiesMapperForKeycloak(): GrantedAuthoritiesMapper {
        return GrantedAuthoritiesMapper { authorities ->
            val mappedAuthorities = mutableSetOf<GrantedAuthority>()
            val authority = authorities.first()

            val userInfo = (authority as OidcUserAuthority).userInfo

            if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                val realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM)
                val roles = realmAccess[ROLES_CLAIM] as Collection<String>
                mappedAuthorities.addAll( roles.map { role -> GrantedAuthority { role } } )
            }

            mappedAuthorities
        }
    }





}
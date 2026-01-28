package com.webapp2.lab1.document_store.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

    private val REALM_ACCESS_CLAIM = "realm_access"
    private val ROLES_CLAIM = "roles"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests {
            it.anyRequest().permitAll()  //or whatever other policy is needed
        }
            .oauth2ResourceServer { it.jwt {} }
            .sessionManagement { it.sessionCreationPolicy( SessionCreationPolicy.STATELESS) }
            .csrf { it.disable() }
            .cors { it.disable() }
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
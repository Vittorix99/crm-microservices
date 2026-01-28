# Security Gateway Service

## Purpose
Acts as the OAuth2/OIDC entry point for the SPA. It handles login/logout with Keycloak, exposes user info to the frontend, and provides CSRF cookies suitable for a SPA.

## Main API routes
- `GET /` (returns the current principal)
- `GET /secure` (requires authentication)
- `GET /token` (returns an access token for API calls)
- `GET /me` (user info + login/logout URLs)
- `POST /logout`

## Security
- OAuth2 login (authorization code flow) for the SPA.
- JWT resource server for service-to-service calls.
- CSRF cookie (`XSRF-TOKEN`) exposed for the SPA.

## Configuration
Default server port: `8080`

You must configure OAuth2 client and provider settings, for example:
- `spring.security.oauth2.client.registration.projectclient.client-id`
- `spring.security.oauth2.client.registration.projectclient.client-secret`
- `spring.security.oauth2.client.registration.projectclient.scope`
- `spring.security.oauth2.client.provider.*` (issuer, authorization URI, token URI, jwk-set URI)
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`

The `/me` endpoint uses the login URL `/oauth2/authorization/projectclient`, so the client registration id must be `projectclient`.

## Run locally
```bash
./gradlew bootRun
```

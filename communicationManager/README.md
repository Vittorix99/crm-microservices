# Communication Manager Service

## Purpose
Integrates Gmail with the CRM using Apache Camel. It can:
- Receive Gmail messages via `google-mail-stream` and forward them to the CRM API.
- Send outbound emails through Gmail using the Gmail API.

## Main API routes
- `POST /gmail/send` (send an email)

## Key flows
1. Inbound Gmail:
   - Camel `google-mail-stream` reads new messages.
   - The message is converted to a CRM `MessageDto`.
   - A Keycloak access token is fetched using client credentials.
   - The message is POSTed to `http://localhost:8080/messages` (via `securityGateway`).
2. Outbound Gmail:
   - `/gmail/send` triggers `direct:sendEmail`.
   - Camel sends the message using `google-mail://messages/send`.

## Security
Configured as a JWT resource server (Keycloak). All routes require authentication.

## Configuration
Default server port: `8084`

Key properties (see `src/main/resources/application.properties` and `application.yml`):
- Gmail:
  - `gmail.client-id`
  - `gmail.client-secret`
  - `gmail.refresh-token`
  - `gmail.mail`
  - `camel.component.google-mail.*`
- Keycloak client credentials (for server-to-server calls):
  - `keycloak.token-uri`
  - `keycloak.client-id`
  - `keycloak.client-secret`
- Resource server:
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`

## Run locally
```bash
./gradlew bootRun
```

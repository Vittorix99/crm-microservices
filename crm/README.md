# CRM Service

## Purpose
Core CRM domain service. It manages contacts, customers, professionals, job offers, messages, notes, skills, proposals, and interviews. It also publishes Kafka events used by the analytics service.

## Main API routes (selected)
- `GET /home`
- `GET /customers`, `POST /customers`, `GET/PUT/DELETE /customers/{customerId}`
- `GET /customers/{customerId}/joboffers`
- `POST/GET/PUT/DELETE /customers/{customerId}/notes`
- `GET /contacts`, `POST /contacts`, `GET/PUT/DELETE /contacts/{contactId}`
- `GET/POST/PUT/DELETE /contacts/{contactId}/email`
- `GET/POST/PUT/DELETE /contacts/{contactId}/address`
- `GET/POST/PUT/DELETE /contacts/{contactId}/telephone`
- `PUT /contacts/{contactId}/category`
- `GET /professionals`, `POST /professionals`, `GET/PUT/DELETE /professionals/{professionalId}`
- `GET/POST/DELETE /professionals/{professionalId}/skills`
- `GET/POST/PUT/DELETE /professionals/{professionalId}/notes`
- `POST /professionals/{professionalId}/interviews`
- `GET /joboffers`, `POST /joboffers`, `GET/PUT/DELETE /joboffers/{jobOfferId}`
- `GET /joboffers/open/{customerId}`
- `GET /joboffers/accepted/{professionalId}`
- `GET/POST/PUT/DELETE /joboffers/{jobOfferId}/skills`
- `GET/POST/DELETE /joboffers/{jobOfferId}/proposals`
- `GET /joboffers/{jobOfferId}/interviews`
- `GET/POST /messages`, `GET /messages/{messageId}`
- `POST /messages/{messageId}` (state change)
- `GET /messages/{messageId}/history`
- `PUT /messages/{messageId}/priority`

## Kafka events
This service publishes events consumed by `analytics`:
- `job_offer_skill`
- `delete_job_offer_skill`
- `proposals`
- `update_proposals`
- `delete_proposals`

## Security
Configured as a JWT resource server (Keycloak). In `SecurityConfig`:
- `/contacts` requires role `GUEST`.
- All other routes require authentication.

## Configuration
Default server port: `8081`

Key properties (see `src/main/resources/application.properties` and `application.yml`):
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `spring.kafka.producer.bootstrap-servers`
- Gmail/Camel settings (if enabled):
  - `gmail.client-id`, `gmail.client-secret`, `gmail.refresh-token`, `gmail.mail`
  - `camel.component.google-mail.*`

## Run locally
```bash
./gradlew bootRun
```

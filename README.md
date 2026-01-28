# CRM Microservices Project

This repository contains a microservice-based CRM system implemented in Kotlin/Spring Boot, plus a React/Vite frontend. The services communicate over REST, use Kafka for event-driven analytics, and rely on Keycloak-issued JWTs for authentication/authorization.

## Architecture (high level)
- `securityGateway`: OAuth2/OIDC entry point for the SPA; exposes `/me` and `/token`, handles login/logout and CSRF cookies.
- `crm`: core CRM domain (contacts, customers, professionals, job offers, messages, notes, skills, proposals).
- `analytics`: consumes Kafka events from CRM and exposes aggregated metrics.
- `communicationManager`: Gmail integration via Apache Camel; receives emails, posts them to CRM via the gateway, and sends outbound emails.
- `DocumentStore`: document upload/storage with metadata.
- `wa2fe`: React SPA frontend.

## Services and ports
| Service | Purpose | Default port |
| --- | --- | --- |
| `securityGateway` | OAuth2 login, `/me`, `/token` | 8080 |
| `crm` | Core CRM APIs | 8081 |
| `DocumentStore` | Documents + metadata | 8082 |
| `analytics` | Metrics APIs + Kafka consumers | 8083 |
| `communicationManager` | Gmail integration | 8084 |
| `wa2fe` | React SPA (Vite dev server) | 5173 |

Infrastructure from `compose.yaml`:
- Postgres: 5432
- Kafka: 9092
- Kafka UI: 9091
- Keycloak: 9090

## Configuration and secrets (what you must set)
The services expect `application.yml` files to be present in each microservice (for example: issuer URI, ports, routes, and Keycloak/OAuth settings). Some defaults also live in `application.properties`. **All sensitive values in this repo have been replaced with placeholders** and must be provided via environment variables or local-only config files before running in any shared environment.

Required secrets (examples of env var names used in this repo):
- `GMAIL_CLIENT_ID`
- `GMAIL_CLIENT_SECRET`
- `GMAIL_REFRESH_TOKEN`
- `GMAIL_MAIL`
- `KEYCLOAK_CLIENT_SECRET`
- `OAUTH_AUTH_CODE_CLIENT_SECRET`
- `OAUTH_CLIENT_CREDENTIALS_SECRET`
- `SECURITY_DB_PASSWORD`
- `CRM_DB_PASSWORD`
- `ANALYTICS_DB_PASSWORD`
- `POSTGRES_PASSWORD`
- `KEYCLOAK_ADMIN_PASSWORD`

Global / shared:
- Keycloak realm and clients:
  - Realm: `projectWAII`
  - Issuer: `http://localhost:9090/realms/projectWAII`
  - Clients used by the system:
    - `projectclient` (authorization code flow, used by `securityGateway` and the SPA)
    - `credential-flow-client` (client credentials flow, used by `communicationManager`)
  - Realm roles used by the UI and services: `ROLE_ADMIN`, `ROLE_RECRUITER`, `ROLE_OPERATOR`, `ROLE_MANAGER`, `ROLE_GUEST`
- Postgres (from `compose.yaml`):
  - `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`
- Kafka:
  - Bootstrap servers (default `127.0.0.1:9092`)

Service-specific:
- `crm`:
  - `spring.datasource.*` (host/user/password/db)
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  - `spring.kafka.producer.bootstrap-servers`
  - Gmail/Camel config (if enabled): `gmail.client-id`, `gmail.client-secret`, `gmail.refresh-token`, `gmail.mail`
- `analytics`:
  - `spring.datasource.*`
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  - `spring.kafka.consumer.*`
- `communicationManager`:
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  - `keycloak.token-uri`, `keycloak.client-id`, `keycloak.client-secret`
  - `gmail.client-id`, `gmail.client-secret`, `gmail.refresh-token`, `gmail.mail`
  - `camel.component.google-mail.*`
- `DocumentStore`:
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
  - `spring.servlet.multipart.*`
  - `spring.datasource.*` (if you want to persist documents in Postgres)
- `securityGateway`:
  - `spring.security.oauth2.client.registration.projectclient.*`
  - `spring.security.oauth2.client.provider.*`
  - `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `wa2fe`:
  - `VITE_BACKEND_URL` (defaults to `http://localhost:8080` in `.env`)

## Running locally
1. Start infrastructure:
   ```bash
   docker compose -f compose.yaml up -d
   ```
2. Configure Keycloak:
   - Create realm `projectWAII`.
   - Create client `projectclient` (authorization code flow).
   - Create client `credential-flow-client` (client credentials flow).
   - Create roles `ROLE_ADMIN`, `ROLE_RECRUITER`, `ROLE_OPERATOR`, `ROLE_MANAGER`, `ROLE_GUEST`.
   - Create users and assign roles.
3. Run the microservices (one terminal per service):
   ```bash
   cd crm && ./gradlew bootRun
   cd analytics && ./gradlew bootRun
   cd communicationManager && ./gradlew bootRun
   cd DocumentStore && ./gradlew bootRun
   cd securityGateway && ./gradlew bootRun
   ```
4. Run the frontend:
   ```bash
   cd wa2fe
   npm install
   npm run dev
   ```
   The SPA is served on `http://localhost:5173/ui`.

## Notes
- Database URLs in `application.properties` use `host.docker.internal`, which is convenient when Postgres runs in Docker and services run on the host. Adjust as needed if everything runs inside containers.
- Kafka topics used by the system are created dynamically by the CRM service when publishing analytics events.

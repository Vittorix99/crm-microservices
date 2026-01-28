# Analytics Service

## Purpose
Consumes Kafka events produced by the CRM service and stores aggregated metrics in Postgres. Exposes read-only analytics endpoints for the UI.

## Main API routes
- `GET /analytics` (skills metrics)
- `GET /analytics/proposals` (proposal metrics)

## Kafka topics consumed
- `job_offer_skill`
- `delete_job_offer_skill`
- `proposals`
- `delete_proposals`
- `update_proposals`

## Security
Configured as a JWT resource server (Keycloak). All routes require authentication.

## Configuration
Default server port: `8083`

Key properties (see `src/main/resources/application.properties` and `application.yml`):
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `spring.kafka.consumer.bootstrap-servers`
- `spring.kafka.consumer.group-id`

## Run locally
```bash
./gradlew bootRun
```

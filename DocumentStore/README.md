# Document Store Service

## Purpose
Stores documents and their metadata. Provides a REST API for upload, download, update, and deletion.

## Main API routes
- `POST /documents` (multipart upload, form field `fileV`)
- `GET /documents` (paged metadata list)
- `GET /documents/{metadataId}` (metadata)
- `GET /documents/{metadataId}/data` (document content)
- `PUT /documents/{metadataId}` (replace file)
- `DELETE /documents/{metadataId}`

## Security
Configured as a JWT resource server (Keycloak). Current security config permits all requests, but JWT validation is wired and can be enforced by tightening the rules.

## Configuration
Default server port: `8082`

Key properties (see `src/main/resources/application.properties` and `application.yml`):
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`
- `spring.servlet.multipart.max-file-size`
- `spring.servlet.multipart.max-request-size`
- `spring.jpa.*`
- `spring.datasource.*` (not set in the repo; configure if you want Postgres persistence)

## Run locally
```bash
./gradlew bootRun
```

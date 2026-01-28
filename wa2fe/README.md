# CRM Frontend (wa2fe)

## Overview
React + Vite single-page application for the CRM. It relies on the `securityGateway` for authentication and uses role-based routing to control access to pages.

## Routes (base path: `/ui`)
- `/ui` (login)
- `/ui/home`
- `/ui/professionals`
- `/ui/customers`
- `/ui/messages`
- `/ui/analytics`
- `/ui/joboffers`
- `/ui/joboffers/:jobofferid`
- `/ui/profile`
- `/ui/documents`

Access is controlled by `PrivateRoute` based on Keycloak roles (`ROLE_ADMIN`, `ROLE_RECRUITER`, `ROLE_OPERATOR`, `ROLE_MANAGER`).

## Project structure
- `src/api`: API wrappers for CRM, analytics, documents, Gmail, security
- `src/pages`: page-level views
- `src/components`: UI components (cards, forms, tables, job offer detail, etc.)
- `src/layouts`: base and authenticated layouts
- `src/contexts`: `AuthProvider` (fetches `/me` and exposes user/role state)
- `src/hooks`: reusable hooks for data and UI behavior
- `src/utils`: shared helpers

## Configuration
- `VITE_BACKEND_URL` in `.env` (default `http://localhost:8080`)
- Vite base path is `/ui` (see `vite.config.js`)

## Scripts
```bash
npm install
npm run dev
npm run build
npm run preview
npm run lint
```

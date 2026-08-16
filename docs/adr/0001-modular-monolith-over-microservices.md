# ADR 0001: Start as a modular monolith, not microservices

**Status:** Accepted

## Context
The app needs to be "microservice-ready" without paying the operational cost of
distributed systems (network calls, distributed transactions, service mesh, N
deployment pipelines) before there's a real reason to.

## Decision
Build a single deployable Spring Boot application, internally split into Maven
modules with strict dependency direction (`app-domain` → `app-persistence` /
`app-service` → `app-api` → `app-boot`). `app-domain` and `app-common` have zero
Spring dependency.

## Consequences
- A bounded context can be extracted into its own microservice by lifting its
  module(s) out — the seams already exist.
- We still need to decide and document *when* a module actually gets extracted
  (e.g. independent scaling need, independent release cadence, team ownership
  boundary) — extraction should be a deliberate decision, not a default.
- Service discovery / config server / gateway dependencies are already present in
  `app-config` as commented-out starters, so adding them later is a config change,
  not a redesign.

# enterprise-app

Spring Boot 3 / Java 21 enterprise starter — modular monolith, cloud/container/microservice-ready.

## Module layout

| Module | Purpose | Depends on |
|---|---|---|
| `app-domain` | Pure domain models. Zero framework dependency. | — |
| `app-common` | Shared exceptions, utils. | — |
| `app-persistence` | JPA entities, repositories, Flyway migrations. | `app-domain` |
| `app-service` | Business logic / use cases. | `app-domain`, `app-persistence`, `app-common` |
| `app-api` | REST controllers, DTOs, global exception handling, OpenAPI. | `app-service`, `app-common` |
| `app-config` | Cross-cutting Spring config (CORS, OpenAPI metadata, rate limiting). | — |
| `app-boot` | The runnable artifact. Wires everything, holds profiles + Dockerfile. | all of the above |

`app-domain` and `app-common` are deliberately framework-free — that's what makes a
future microservice extraction a lift-and-shift instead of a rewrite. See
`docs/adr/0001-modular-monolith-over-microservices.md`.

## Testing

- `mvn test` — unit tests only (Surefire), no Docker required, fast.
- `mvn verify` — also runs integration tests (Failsafe), which spin up real containers via
  Testcontainers (e.g. `CustomerApiIntegrationIT` uses a real Postgres). **Requires Docker
  Desktop (or another Docker-compatible engine) running locally.**
- No Docker available locally? `mvn clean verify -DskipITs` skips integration tests and
  runs everything else. CI always runs the full `mvn clean verify` — GitHub Actions'
  `ubuntu-latest` runners ship Docker preinstalled, so ITs execute there regardless of what's
  set up on any given developer's machine.
- Naming convention: unit tests end in `*Test`, integration tests end in `*IT` — this is
  what routes them to Surefire vs. Failsafe respectively, so keep following it for new tests.

## Running locally

```bash
docker compose up --build
```

This starts Postgres + the app on the `local` profile (port 8080). Or run against a local
Postgres directly:

```bash
mvn -pl app-boot spring-boot:run -Dspring-boot.run.profiles=local
```

Swagger UI: `http://localhost:8080/swagger-ui.html`
Health: `http://localhost:8080/actuator/health`

## Git branching strategy

- `main` — always reflects PROD. Every commit here is tagged `vX.Y.Z` by CI on merge.
- `develop` — integration branch, auto-deploys to DEV.
- `release/x.y.0` — cut from `develop` when ready to test; deploys to UAT; only
  bugfixes land here; merged back to `main` **and** `develop` on sign-off.
- `feature/JIRA-123-short-desc` — branched from `develop`, PR back into `develop`.
- `hotfix/x.y.z` — branched from `main` for an emergency PROD fix, merged to both
  `main` and `develop`.

Branch protection: no direct pushes to `main` / `develop` / `release/*`; PR review +
passing CI required.

## Versioning

- Root `pom.xml` version is the single source of truth, inherited by every module.
- `develop` always carries `-SNAPSHOT`. CI strips it when cutting a `release/*` branch.
- CI tags `main` commits `vX.Y.Z` automatically on merge — never tag by hand.
- `git-commit-id-maven-plugin` embeds commit hash / branch / build time into the JAR.
  Check what's actually deployed via `GET /actuator/info`.

## Environments

| Profile | Trigger | Config file |
|---|---|---|
| `local` | Developer machine / docker-compose | `application-local.yml` |
| `dev` | Merge to `develop` | `application-dev.yml` |
| `uat` | Push to `release/*` | `application-uat.yml` |
| `prod` | Merge to `main` (tagged release) | `application-prod.yml` |

Set via `SPRING_PROFILES_ACTIVE` env var at deploy time — never hardcoded in a committed
file. No secrets live in any of these YAMLs; they're all resolved from environment
variables / a secret manager at runtime.

## CI/CD

See `.github/workflows/ci-cd.yml`: build → test → OWASP dependency scan → build & push
image → deploy, with `uat` and `prod` GitHub Environments configured for required
manual approval before deploy.

**Note:** this scaffold uses system `mvn` in CI/Docker rather than committing a Maven
Wrapper binary. To pin an exact Maven version for fully reproducible builds, run
`mvn -N io.takari:maven:wrapper` locally once, commit the generated `mvnw` / `.mvn/`,
and swap `mvn` for `./mvnw` in `.github/workflows/ci-cd.yml` and `app-boot/Dockerfile`.

## What's intentionally stubbed, not built

These are commented-out dependency notes in `app-config/pom.xml` rather than wired in,
so you add them when there's an actual need rather than carrying the operational weight
from day one:

- Spring Cloud Config Server client
- Eureka/Consul service discovery client
- Kafka/RabbitMQ messaging
- API Gateway (would sit in front as its own deployable, not a module here)

## Still worth adding as the project matures

- Distributed tracing (OpenTelemetry) once you have more than one service talking to another
- Audit logging (separate from application logs) — likely a compliance requirement
- Feature flags
- SonarQube quality gate in CI
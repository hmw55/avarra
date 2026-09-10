# Avarra Backend

The Avarra backend is a Java and Spring Boot application responsible for server-side application logic, persistence, authentication, and the APIs that support the Avarra game client.

Avarra is being developed as a persistent single-player browser-based fantasy tabletop RPG. The backend is designed to keep account, game, and persistence concerns on the server while exposing a REST API to the frontend.

> **Current development status:** The backend currently includes the database and authentication foundations. Game domain systems will be introduced incrementally as development progresses.

---

## Technology Stack

The backend currently uses:

- **Java 21**
- **Spring Boot 4**
- **Spring MVC** for REST APIs
- **Spring Security** for authentication and HTTP security
- **Spring Data JPA / Hibernate** for persistence
- **PostgreSQL** as the relational database
- **Flyway** for the database schema migrations
- **Maven** for dependency management and builds
- **JUnit** and **Mockito** for automated testing
- **Docker Compose** for the local PostgreSQL development environment

---

## Project Structure

Backend source code is located under:

```text
src/main/java/dev/hollandwesley/avarra/
```

The application uses feature- and domain-oriented packages. Within a feature, code is separated by responsibility when that separation represents a meaningful architectural boundary.

Current major areas include:

```text
dev.hollandwesley.avarra
├── auth/
│   ├── api/           HTTP authentication endpoints, requests, responses, and API errors
│   ├── application/   Authentication and account-related application use cases
│   ├── security/      Spring Security configuration and authentication security services
│   └── validation/    Validation rules currently owned by authentication inputs
├── user/
│   ├── domain/        User account domain model
│   └── persistence/   User account database access
└── AvarraApplication.java
```

Database migrations are stored in:

```text
src/main/resources/db/migration/
```

Tests mirror the application's package structure under:

```text
src/test/java/dev/hollandwesley/avarra/
```

As Avarra's game systems are implemented, additional feature and domain packages will be introduced rather than placing game logic inside authentication or user-account packages.

---

## Configuration

Avarra uses environment variables for configuration that should not be committed to source control.

The backend currently expects the following database configuration:

| Variable | Purpose |
|----------|---------|
| `DB_HOST` | PostgreSQL host |
| `DB_PORT` | PostgreSQL port |
| `DB_NAME` | PostgreSQL database name |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |

Safe development defaults exist for the host, port, and database name. Database credentials are supplied through environment variables.

A repository-level `.env.example` documents the expected local configuration without containing real credentials.

**Never commit passwords, tokens, private keys, production environment values, database connection strings containing credentials, or other secrets to the repository.**

For complete local environment setup instructions, see `../docs/DEVELOPMENT.md`.

---

## Database and Migrations

PostgreSQL is the backend's relational database.

For local development, PostgreSQL runs through the repository-level Docker Compose configuration:

```text
../compose.yaml
```

Database schema changes are managed by **Flyway**.

Hibernate is configured to validate the database schema rather than create or modify it automatically. This keeps schema ownership with the migration history and makes database changes explicit and reproducible.

Migration files are immutable after they become part of the project's migration history. Changes to an existing schema should be introduced through a new migration rather than by editing a previously applied migration.

The initial migration establishes the registered user account schema.

---

## Authentication 

Avarra uses **Spring Security with server-side HTTP sessions** for registered-user authentication. 

The current authentication flow supports: 

- User registration
- Case-insensitive username lookup
- BCrypt password hashing
- Recovery-code generation and hashing, with the raw code returned only at registration
- Username and password login
- Server-side authenticated sessions
- Session fixation protection
- Retrieval of the currently authenticated user
- CSRF protection

Authentication is deliberately separated from future game-character identity. A registered account represents a user of Avarra; characters and game state are separate domain concepts.

---

## Current Authentication Endpoints

| Method | Endpoint | Authentication Required | Purpose |
|--------|----------|-------------------------|---------|
| `POST` | `/api/auth/register` | No | Create a registered Avarra account |
| `POST` | `/api/auth/login` | No | Authenticate credentials and establish a session |
| `GET` | `/api/auth/me` | Yes | Return the currently authenticated account |

Logout, account recovery, persistent login, and production deployment hardening are planned but are not part of the current implementation.

More detailed authentication documentation is maintained with the authentication package.

---

## Security Principles

Security-sensitive behavior should remain explicit and testable.

Current backend practices include:

- Raw passwords are never persisted.
- Passwords are hashed using BCrypt.
- BCrypt's input limitation is enforced before hashing rather than silently truncating passwords.
- Raw recovery codes are returned only when initially generated and are not persisted.
- Recovery codes are stored as hashes.
- Usernames are unique case-insensitively.
- Persistence entities are not used directly as public API responses.
- Credential hashes are never exposed through API responses.
- CSRF protection remains enabled for session-authenticated requests.
- Successful login applies session fixation protection as part of establishing the authenticated session.
- Secrets and environment-specific credentials stay outside version control.

Security controls should not be removed merely to simplify deployment or testing. 

---

## Running the Backend

The local PostgreSQL service must be running and the required environment variables must be available to the Spring Boot process.

From the repository root, PostgreSQL can be started with: 

```bash
docker compose up -d
```

Then, from the backend directory, run the application with:

```bash
./mvnw spring-boot:run
```

See `../docs/DEVELOPMENT.md` for the complete development setup and environment-loading workflow.

---

## Testing

Run the backend test suite from the `backend` directory:

```bash
./mvnw test
```

For a clean rebuild, particularly after moving classes or changing package structure, run:

```bash
./mvnw clean test
```

The `clean` goal removes previously compiled build artifacts before rebuilding and can prevent stale classes from interfering with the test run after structural refactors.

The test suite currently covers multiple layers of the application, including: 

- Request validation
- User persistence
- Password hashing
- Recovery-code generation
- User registration behavior
- Authentication API behavior
- Spring Security configuration
- User loading for authentication
- Real session persistence across HTTP requests
- Session fixation protection

Integration tests use the local PostgreSQL development database where real persistence behavior is important.

Tests that write persistent data must clean up after themselves so repeated test runs remain deterministic.

---

## Development Conventions

Backend changes should remain small, intentional, and testable. 

When introducing or changing backend behavior:

1. Keep domain responsibilities separated.
2. Use Flyway for schema changes.
3. Validate input at application boundaries.
4. Avoid exposing persistence entities directly through APIs.
5. Keep credentials and secrets outside source control.
6. Add automated tests for meaningful behavior and security boundaries.
7. Run the complete backend test suite before committing.
8. Review the diff for accidental secrets, generated files, and unrelated changes.
9. Commit a coherent unit of work with a descriptive commit message.

Important classes, public APIs, security behavior, and non-obvious architecture decisions should include useful documentation. Comments should explain **why** behavior exists when the reason is not apparent from the code rather than narrating when the code already expresses.

---

## Current Development Boundary

The current backend milestone is focused on establishing the account and authentication foundation before game-domain development begins.

Current authentication progress:

```text
Registration        Complete 
Login               Complete 
Session persistence Complete 
Session security    Complete 
Current user (/me)  Complete 
Logout              Next
```

After logout is implemented, development can proceed into frontend authentication. 

The following authentication capabilities are intentionally deferred:

- Account recovery flow
- Persistent or remember-me login
- Production cookie, CORS, CSRF, and deployment hardening

These features should be implemented when their corresponding application or deployment requirements are reached rather than speculatively.
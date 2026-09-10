# Avarra Authentication

This package contains the registered-user authentication foundation for the Avarra backend. 

Avarra uses Spring Security with server-side HTTP sessions. Authentication is intentionally modeled separately from game-character identity: a registered user account establishes who is using Avarra, while characters and game state belong to separate game-domain systems.

---

## Package Structure

The authentication feature is divided into four areas:

```text
auth/
├── api/           HTTP endpoints, request and response models, and API error handling
├── application/   Authentication-related application use cases
├── security/      Spring Security configuration and authentication security services
└── validation/    Validation rules owned by authentication inputs
```

### `api`

Contains the HTTP boundary for authentication.

Current responsibilities include:

- User registration
- Username/password login
- Retrieval of the currently authenticated user
- Authentication request and response models
- Translation of authentication and validation failures into HTTP responses

Persistence entities are not exposed directly through this layer.

### `application`

Contains authentication-related application use cases and application-level failures. 

Registration currently coordinates:

1. Case-insensitive username availability checking
2. Password hashing
3. Recovery-code generation
4. Recovery-code hashing
5. User creation
6. Persistence of the registered account
7. Returning the raw recovery code once in the registration result

The raw password and raw recovery code are never persisted.

### `security`

Contains Spring Security configuration and services used to authenticate registered users.

Current responsibilities include:

- BCrypt password encoding
- Loading registered users for authentication
- Authentication manager configuration
- Security filter-chain configuration
- Server-side security-context persistence
- Session fixation protection
- Recovery-code generation

### `validation`

Contains validation rules currently owned by authentication inputs.

These rules include application-specific constraints that are not adequately represented by standard Bean Validation annotations alone.

For example, password length is constrained by UTF-8 byte length because BCrypt processes at most 72 bytes of password input. Avarra rejects passwords beyond that boundary rather than silently truncating them.

---

## Authentication Model

Avarra currently uses a username-and-password authentication backed by server-side HTTP sessions. 

At a high level:

```text
Username + password
        │
        ▼
POST /api/auth/login
        │
        ▼
Spring Security AuthenticationManager
        │
        ▼
Registered user lookup
        │
        ▼
Password verification
        │
        ▼
Authenticated SecurityContext
        │
        ▼
Session fixation protection
        │
        ▼
Server-side HTTP session
        │
        ▼
Subsequent authenticated requests
```

The browser is expected to use the session cookie for subsequent authenticated API requests. The frontend does not need to store a bearer token.

---

## Registration

Registered accounts are created through:

```http
POST /api/auth/register
```

Registration accepts:
- `username`
- `password`
- optional `email`

### Username

Usernames:

- Are required
- Must contain 3-32 characters
- May contain ASCII letters, numbers, and underscores
- Preserve the capitalization supplied by the user
- Are unique case-insensitively

Case-insensitive uniqueness is enforces both by application behavior and by database unique index on the lowercase username.

### Password

Passwords: 

- Are required
- Must contain at least 12 characters
- Must not exceed 72 UTF-8 bytes

The byte-length restriction exists because BCrypt has a 72-byte input boundary. Passwords exceeding that boundary are rejected rather than silently truncated.

Raw passwords are never persisted.

### Email

Email addresses are currently:

- Optional
- Validated when supplied
- Limited to 254 characters
- Unverified
- Not currently required to be unique

Email-based account recovery is not part of the current authentication model.

---

## Password Storage

Avarra uses Spring Security's `PasswordEncoder` with BCrypt.

Only password hashes are persisted. Authentication compares submitted credentials against the stored Bcrypt hash through Spring Security.

The application does not store or expose raw passwords.

---

## Recovery Codes

Registration generates a recovery code in the following form:

```text
AVARRA-XXXX-XXXX-XXXX-XXXX
```

Recovery codes are generated using a cryptographically secure random source and an alphabet designed to avoid visually ambiguous characters. 

The raw recovery code is returned to the user when the account is created. Only its BCrypt hash is persisted.

The account-recovery flow itself has not yet been implemented. When recovery is introduced, the stored hash will allow a submitted recovery code to be verified without storing the original code.

---

## Login and Sessions

Registered users authenticate through:

```http
POST /api/auth/login
```

Successful authentication establishes a server-side authenticated session.

The login flow: 

1. Receives the submitted username and password.
2. Delegates credential authentication to Spring Security.
3. Creates an authenticated security context.
4. Applies session fixation protection while establishing the authenticated session.
5. Persists the security context to the HTTP session.
6. Returns the authenticated username.

Invalid credentials return an authentication error without exposing whether a particular credential component was incorrect. 


---

## Current User

Authenticated clients can retrieve the current account through: 

```http
GET /api/auth/me
```

This endpoint requires an authenticated session and currently returns the authenticated username.

It provides the frontend with a server-authoritative way to determine which registered user owns the current session. 

--- 

## CSRF Protection

CSRF protection remains enabled.

Public accessibility of an endpoint does not bypass CSRF protection. For example, registration and login may be available without an authentication account while still being subject to the applicable CSRF requirements. 

CSRF protection should not be disabled merely to simplify the frontend integration. The final frontend/backend deployment model will determine the production CSRF and cookie configuration.

---

## Security Boundaries

Authentication code should preserve the following boundaries:

- Raw passwords much never be persisted of logged.
- Raw recover codes must not be persisted.
- Password and recovery-code hashes must never be exposed through API responses.
- Persistence entities must not be returned directly as public API models.
- Username uniqueness must remain case-insensitive.
- Passwords exceeding BCrypt's support input boundary must be rejected rather than silently truncated.
- Authentication errors should not unnecessarily reveal credential details.
- Session fixation protection must remain part of successful authentication.
- CSRF protection should remain enabled for the session-based authentication model.
- Secrets and environment-specific credentials must remain outside source control.

---

## Testing

Authentication behavior is covered at multiple levels.

Current tests verify:

- Registration request validation
- Registration service behavior
- BCrypt password hashing
- Recovery-code generation
- Registered-user loading
- Authentication API behavior
- Spring Security configuration
- CSRF enforcement
- Session persistence across requests
- Session fixation protection
- User persistence behavior relevant to authentication

Integration test use real PostgreSQL persistence where database behavior is part of the behavior being verified.

---

## Current Development Status

The authentication foundation currently stands at:

```text
Registration        Complete
Login               Complete
Session persistence Complete
Session security    Complete
Current user (/me)  Complete
Logout              Next
```

Logout is the next backend authentication capability to implement. 

The following capabilities are intentionally deferred:

- Account recovery
- Persistent or remember-me login
- Production cookie configuration
- Production CORS and CSRF integration
- Deployment-specific authentication hardening.

These capabilities should be introduced when their corresponding application or deployment requirements are reached rather than implemented speculatively.

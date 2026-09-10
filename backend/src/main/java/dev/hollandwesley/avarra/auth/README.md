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
- CSRF-token retrieval for browser clients
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
- Session logout and invalidation
- API authentication failure handling

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

Case-insensitive uniqueness is enforced both by application behavior and by database unique index on the lowercase username.

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

## Logout

Authenticated sessions are terminated through:

```http
POST /api/auth/logout
```

Logout is handled by Spring Security rather than by application controller logic.

A successful logout:

1. Clears the authenticated security context.
2. Invalidates the server-side HTTP session.
3. Returns `204 No Content`.

Logout remains protected by CSRF. Requests attempting to logout without a valid CSRF token are rejected.

After logout, protected API endpoints require authentication again. Unauthenticated requests to protected API resources return `401 Unauthorized`.

## CSRF Protection

CSRF protection remains enabled for Avarra's session-based authentication model.

Browser clients can retrieve the current CSRF token through:

```http
GET /api/auth/csrf
```

The endpoint is publicly accessible so that the frontend can obtain a CSRF token before submitting authentication or other state-changing requests.

Public accessibility does not bypass CSRF protection. Registration, login, and logout remain subject to the applicable CSRF requirements even when the user does not yet have an authenticated account.

The React frontend should obtain a current CSRF token before state-changing requests and refresh the token when authentication state changes are required by Spring Security's session and CSRF behavior.

CSRF protection should not be disabled merely to simplify frontend integration. Final production CSRF and cookie behavior will be configured according to the eventual frontend/backend deployment model.

---

## CORS

The backend currently allows credential API requests from the local Vite development frontend:

```text
https://localhost:5173
```

Cors is configured for `/api/**` requests and allows the browser to include the session cookie with cross-origin request during local development.

The development configuration does not use a wildcard origin because credentialed browser requests require an explicit trusted origin.

Production frontend origins are intentionally not configured yet. They will be added when the deployment topology and public frontend/backend hosts are finalized.

---

## Security Boundaries

Authentication code should preserve the following boundaries:

- Raw passwords must never be persisted or logged.
- Raw recovery codes must not be persisted.
- Password and recovery-code hashes must never be exposed through API responses.
- Persistence entities must not be returned directly as public API models.
- Username uniqueness must remain case-insensitive.
- Passwords exceeding BCrypt's supported input boundary must be rejected rather than silently truncated.
- Authentication errors should not unnecessarily reveal credential details.
- Session fixation protection must remain part of successful authentication.
- CSRF protection should remain enabled for the session-based authentication model.
- Secrets and environment-specific credentials must remain outside source control.
- Logout must invalidate the authenticated server-side session.
- Logout must remain protected by CSRF.
- Unauthenticated access to protected API resources should return `401 Unauthorized`.

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
- Public CSRF-token retrieval
- Local frontend CORS preflight behavior
- Session persistence across requests
- Session fixation protection
- User persistence behavior relevant to authentication
- Logout session invalidation
- Logout CSRF enforcement
- Unauthenticated protected-resource handling.

Integration tests use real PostgreSQL persistence where database behavior is part of the behavior being verified.

---

## Current Development Status

The authentication foundation currently stands at:

```text
Registration        Complete
Login               Complete
Session persistence Complete
Session security    Complete
Current user (/me)  Complete
Logout              Complete
```

The backend authentication foundation is ready for local frontend integration. CSRF-token retrieval, credentialed local CORS, session authentication, logout, and unauthenticated protected-resource behavior are implemented and tested.

The next authentication work should happen through integration with the React frontend rather than through additional speculative backend features.

The following capabilities remain intentionally deferred:

- Account recovery
- Persistent or remember-me login
- Final production cookie configuration
- Deployment-specific authentication hardening

These capabilities should be introduced when their corresponding application or deployment requirements are reached rather than implemented speculatively.
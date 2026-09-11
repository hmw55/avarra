# Journey

The `journey` package owns the backend foundation for Avarra's registered-user Journey persistence and APIs.

A Journey represents a persistent playthrough or save belonging to a registered user. It is intentionally separate from account identity, character identity, and eventual game state.

---

## Domain Boundary

Avarra treats the following as separate concepts:

```text
User Account
    │
    ├── Journey
    │     └── Future character and game-state data
    │
    └── Journey
          └── Future character and game-state data
```

A registered User may own zero or more Journeys.

A Journey belongs to exactly one registered User.

The current Journey model does not attempt to represent a character, kingdom, game progress, world state, or other gameplay systems. Those concepts will be introduced when their corresponding systems are implemented.

---

## Package Structure

```text
journey/
├── api/
│   ├── JourneyController.java
│   └── JourneySummaryResponse.java
├── domain/
│   └── Journey.java
├── persistence/
│   └── JourneyRepository.java
└── service/
    └── JourneyService.java
```

### `api`

Owns the HTTP boundary for Journey operations and the response models exposed to clients.

Persistence entities are not returned directly through the API.

### `domain`

Contains the Journey persistence domain model and its relationship to the registered User that owns it.

### `persistence`

Contains Spring Data repository access for Journey records.

### `service`

Coordinates Journey application behavior between the HTTP layer, User persistence, and Journey persistence.

---

## Current Persistence Model

The current Journey record contains:

- `id` — UUID primary key
- `user` — required owning registered User
- `name` — optional Journey name
- `createdAt` — creation timestamp
- `updatedAt` — last-update timestamp

The database relationship is established by the `user_id` foreign key in the `journeys` table.

Deleting a registered User cascades to that User's Journeys.

Journey schema changes are managed through Flyway migrations.

---

## Current API

### `GET /api/journeys`

Returns Journey summaries belonging to the currently authenticated registered user.

Authentication is required.

The endpoint does not accept a user ID from the client. The owning User is resolved from the authenticated Spring Security principal, preventing clients from selecting another account's Journey collection through the request.

Each Journey summary currently contains:

- Journey ID
- Optional name
- Creation timestamp
- Update timestamp

---

## Guest Journeys

Guest journeys do not use the backend Journey table or `/api/journeys`.

Guest save state is intentionally browser-local and separate from registered-user persistence.

This preserves the distinction between:

- registered account persistence;
- guest browser-local persistence; and
- authentication/session state.

A future account flow may allow guest progress to be attached or imported into a registered account, but that behavior is not currently implemented.

---

## Journey Creation

Journey creation is intentionally deferred.

Entering the frontend Journey preparation flow does not create a persisted Journey. Viewing lore, reaching character preparation, or otherwise exploring pre-game screens does not yet constitute a saved playthrough.

A Journey should be created only when the application reaches a meaningful persistence boundary for beginning a game.

---

## Deferred Systems

The following concerns are intentionally outside the current Journey
implementation:

- Journey creation API
- Character persistence
- Character selection persistence
- Kingdom or origin persistence
- Game-state persistence
- World-state persistence
- Progress tracking
- Last-played metadata
- Guest-to-account Journey import
- Save management

These systems should be added as gameplay requirements reach the backend rather than speculatively expanding the Journey model.

---

## Testing

Journey behavior is covered at multiple layers:

- Persistence tests verify that Journeys can be stored and loaded.
- Repository tests verify Journey lookup by owning User.
- Service tests verify registered-user Journey retrieval and missing-user behavior.
- Controller tests verify the authenticated Journey HTTP response contract.
- Application security tests cover unauthenticated access to protected backend resources.

The Journey package should continue to keep persistence ownership, API
boundaries, and registered-user authorization explicit as the game systems
expand.
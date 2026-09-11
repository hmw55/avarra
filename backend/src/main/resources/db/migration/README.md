# Database Migrations

Avarra uses Flyway to manage the PostgreSQL database schema.

Migration files in this directory are the authoritative history of schema
changes. This document provides a higher-level overview of the current schema,
relationships, and migration conventions without duplicating the SQL contained
in individual migrations.

---

## Migration Conventions

Migration files follow Flyway's versioned migration naming convention:

```text
V{version}__{description}.sql
```

For example:

```text
V1__create_user_table.sql
V2__create_journey_table.sql
```

Once a migration has been applied and committed, it should be treated as immutable.

Changes to an existing schema must be introduced through a new migration rather than by editing an earlier migration.

Hibernate is configured with schema validation rather than automatic schema creation or modification. Flyway owns database schema changes.

--

## Migration History

### V1 — User Accounts

Establishes persistence for registered Avarra user accounts.

The `users` table owns account-level identity and authentication data. Game characters, journeys, and game state are intentionally separate from the
user account model.

### V2 — Journeys

Establishes the initial persistence foundation for registered-user journeys.

Each Journey belongs to exactly one registered User, while a User may own zero or more Journeys.

The initial journeys table contains:

- `id` — UUID primary key
- `user_id` — required foreign key to users
- `name` — optional journey name
- `created_at` — creation timestamp
- `updated_at` — last-update timestamp

An index on `user_id` supports retrieval of Journeys by their owning User.

Deleting a User cascades to that User's Journeys.

Guest journeys are intentionally not stored in this table. Guest save data remains a browser-local concern and is separate from registered-user persistence.

---

## Current Relationships

```text
users
  │
  │ 1
  │
  └──────────< journeys
                0..*
```

A registered User may have zero or more Journeys.

A Journey must belong to one registered user.

Future character and game-state relationships will be documented here when their schemas are introduced.

---

## Current Tables

### `users`

Owns registered account and authentication persistence.

Introduced by V1.

### `journeys`

Owns the persistence identity and account relationship for registered-user Journeys.

Introduced by V2.

The Journey schema is intentionally minimal. Character data, world state, progress, and other gameplay concerns should not be added until their corresponding domain systems are implemented.

---

## Security and Data Boundaries

Database design should preserve the separation between:

```text
User Account
Journey / Save
Character
Game State
```

Authentication credentials belong to the User account.

Journey records establish ownership of registered-user playthroughs.

Characters and game state will receive their own persistence boundaries as those systems are implemented.

Guest saves do not use registered-user database persistence.

---

## Adding a Migration

When a schema change is required:

1. Do not modify an existing applied migration.
2. Create the next versioned Flyway migration.
3. Keep the migration focused on one coherent schema change.
4. Update this README when the migration introduces or materially changes a documented table, relationship, constraint, or persistence boundary.
5. Run the backend test suite against the migrated schema.
6. Review the migration and related code before committing.

The migration SQL remains the authoritative description of exactly how a schema
change is performed. This README documents the architectural meaning of the
resulting schema.

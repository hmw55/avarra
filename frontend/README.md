# Avarra Frontend

The Avarra frontend is a React and TypeScript web client for the Avarra persistent single-player fantasy tabletop RPG.

The frontend provides the player-facing interface for entering Avarra, authentication or continuing as a guest, managing journeys, exploring world information, and eventually interacting with the game itself.

---

## Technology

- React
- TypeScript
- Vite
- React Router
- Oxlint

The frontend communicates with the Java/Spring Boot backend through REST APIs.

---

## Development

Install dependencies:

```bash
npm install
```

Start the Vite development server:

```bash
npm run dev
```

The local frontend is served at:

```text
http://localhost:5173
```

The backend is expected to be available at:

```text
http://localhost:8080
```

Create a production build:

```bash
npm run build
```

Run the linter:

```bash
npm run lint
```

---

## Application Flow

The current pre-game flow is organized around a shared entry interface.

```text
Home
  ↓
Enter Avarra
  ├── Log In
  ├── Create Account
  └── Continue as Guest
          ↓
      Journeys
          ↓
   New Journey Flow
          ↓
     Gameplay
```

The new-journey and gameplay flows are still under development.

`/journeys` represents journey selection and preparation rather than active gameplay. Routes under `/play` are reserved for the eventual gameplay interface.

---

## Authentication

Registered-player authentication uses the Spring Security session managed by the backend.

Frontend requests include credentials so the browser session cookie can be used across requests. CSRF tokens are retrieved from the backend for protected state-changing operations such as logout.

On application startup, the authentication provider checks the current session and restores the authenticated user when one exists.

Guest play does not require an account. Guest persistence and save-state management will be implemented separately from registered-player sessions.

### Account Recovery

Avarra currently uses a one-time recovery code rather than email-based password recovery. The recovery code is generated during registration, shown to the player once, and must be stored somewhere safe for future account recovery.

Email-based recovery was intentionally omitted from the initial release to avoid adding unnecessary infrastructure and ongoing costs to a free game. The recovery-code system also provided an opportunity to design and implement a recovery approach I had not built before.

Email-based account recovery may be added in the future.

---

## Pre-Game Interface

Pre-game screens share the `EntryLayout` component.

This includes screens such as:

- Home
- Entry
- Login
- Registration
- Journey selection
- About
- Lore
- Future character and class selection

`EntryLayout` owns the common presentation shell, including the background, framed content panel, navigation, and footer. Individual pages provide their own content and behavior.

The eventual gameplay client will intentionally use a different full-screen interface rather than `EntryLayout`.

---

## Visual Design

The pre-game interface uses an old-school fantasy PC game aesthetic with textured panels, hard beveled controls, and deliberately tactile interface elements.

Shared interface textures are stored under:

```text
src/assets/ui
```

Shared background artwork is stored under:

```text
src/assets/backgrounds/
```

Asset provenance and attribution are documented in `src/assets/README.md`.

---

## Project Structure

```text
src/
├── api/                 Shared HTTP infrastructure
├── assets/              Images, textures, and visual assets
│   ├── backgrounds/
│   └── ui/
├── auth/                Authentication API, state, and types
├── components/
│   ├── layout/          Shared page layouts
│   └── navigation/      Shared navigation components
├── pages/               Route-level page components
├── App.tsx              Application routes
├── index.css            Global styles
└── main.tsx             React application bootstrap
```

Components should be introduced when an interface or behavior is genuinely shared rather than in anticipation of possible future reuse.

---

## Current Journey-State Limitations

Journey selection currently uses temporary actions while the save system is being developed.

The intended behavior is:

- Registered players may eventually maintain multiple journeys.
- "Continue Game" should only appear when an existing save is available.
- Guest players will have one locally persisted journey at a time.
- A guest with an existing journey should continue that journey rather than
  create additional saves.
- Registered and guest save state will remain separate from authentication
  state.

These behaviors will be implemented with the game save system rather than
simulated in the current frontend.

## Console Easter Eggs

Pages intentionally include harmless developer-console messages.

These are part of Avarra's presentation and are not debugging statements.
Console easter eggs must never contain credentials, secrets, sensitive
application state, or spoiler-sensitive information.

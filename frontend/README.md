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
   Prepare Journey
      ├── Explore Avarra
      │      ├── Continent
      │      ├── History
      │      ├── Mana
      │      └── Kingdoms
      │
      └── Choose Character
             ↓
        Future Gameplay
```

`/journeys` is the entry point for selecting whether to begin or continue a journey.

Starting a new journey leads to `/journeys/new`, which serves as the preparation screen before character selection. Players may optionally explore preparation-specific lore at `/journeys/new/lore`.

Entering the preparation flow does not create a persisted journey. Journey creation will only occur when the application reaches a meaningful save boundary.

`/play` remains reserved for the eventual gameplay interface.

---

## Authentication

Registered-player authentication uses the Spring Security session managed by the backend.

Frontend requests include credentials so the browser session cookie can be used across requests. CSRF tokens are retrieved from the backend for protected state-changing operations such as logout.

On application startup, the authentication provider checks the current session and restores the authenticated user when one exists.

Guest play does not require an account.

Guest mode is tracked separately from authenticated account state. The current browser session remembers when the player has chosen guest mode so that refreshing a pre-game page does not incorrectly return the interface to registered-player behavior.

Guest journey persistence is also separate from guest session state. Guest journeys use a versioned browser-local save representation rather than the registered-user Journey API.

This separation is intentional:

- Authentication state identifies a registered session.
- Guest-mode state identifies that the current browser session is playing without an account.
- Guest journey state represents the actual browser-local save.

Registered and guest persistence will remain separate unless a future account-import or attachment flow explicitly connects them.

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
- Journey preparation
- Preparation lore
- About
- Public lore
- Future character selection

`EntryLayout` owns the common presentation shell, including the background, framed content panel, navigation, and footer. Individual pages provide their own content and behavior.

Preparation-specific lore uses a separate route from the public lore interface. `/lore` remains the general public-facing lore page, while `/journeys/new/lore` supports optional learning during journey preparation.

Scrollable game-styled panels use the reusable `GameScrollPanel` component. It provides custom scroll controls designed to match Avarra's interface rather than relying on the browser's native scrollbar presentation.

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
├── journey/             Journey API and guest journey persistence
├── components/
│   ├── layout/          Shared page layouts
│   ├── navigation/      Shared navigation components
│   └── ui/              Reusable game-styled interface components
├── pages/               Route-level page components
├── App.tsx              Application routes
├── index.css            Global styles
└── main.tsx             React application bootstrap
```

Components should be introduced when an interface or behavior is genuinely shared rather than in anticipation of possible future reuse.

---

## Journey State

Registered and guest journeys deliberately use different persistence paths.

### Registered Players

Registered players retrieve their existing journeys from:

```text
GET /api/journeys
```

The frontend uses the returned journey collection to determine whether an existing registered-user journey is available.

The intended long-term model allows registered players to maintain multiple journeys.

Journey creation is not yet implemented.

### Guest Players

Guest players may maintain one browser-local journey.

Guest journey data uses a versioned local-storage representation and does not use the registered-user Journey API.

The current guest journey representation establishes the persistence boundary but does not yet contain gameplay state.

Guest-mode session state is maintained separately from the guest journey itself.

### Current Limitations

The following behavior remains intentionally incomplete:

- Registered Journey creation
- Character selection
- Character persistence
- Actual game-state persistence
- Loading an existing Journey into gameplay
- Guest-to-account Journey import or attachment
- Full registered-user save management

Opening `/journeys/new` does not create a registered or guest save. Preparation is not itself considered a persisted journey.

---

## Shared Game UI

Reusable game-styled interface behavior belongs under `components/ui/`.

`GameScrollPanel` provides a framed scrollable content region with custom up/down controls, a synchronized scrollbar thumb, and draggable thumb behavior.

The component was introduced for preparation lore but is intentionally reusable for future entry screens and in-game panels that require contained scrolling.

The eventual main gameplay viewport is not expected to use ordinary page scrolling. Individual gameplay panels may use components such as `GameScrollPanel` when their content requires independent scrolling.

---

## Console Easter Eggs

Pages intentionally include harmless developer-console messages.

These are part of Avarra's presentation and are not debugging statements.
Console easter eggs must never contain credentials, secrets, sensitive
application state, or spoiler-sensitive information.

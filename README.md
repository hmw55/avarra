# Avarra

Avarra is a persistent single-player fantasy tabletop RPG where choices have
consequences and the world remembers.

> **The world remembers.**

Avarra combines a browser-based tabletop RPG experience with a persistent
world that responds to the player's decisions, relationships, history, and
actions over time.

The project is currently under active development.

---

## The Game

Avarra is designed around the idea that a player's history should matter.

Rather than treating encounters and decisions as isolated events, the game is
being built so that choices can become part of the persistent state of a
journey and influence what happens later.

Players will be able to explore the world, create a character, make choices,
roll dice, and develop a history unique to their journey through Avarra.

Public, spoiler-safe information about the world and its lore is maintained
alongside the game as the world continues to develop.

---

## Acknowledgments

A special thank you to my girlfriend for the many hours we have spent
together planning Avarra's lore, gameplay, characters, and ideas.

Building the world together has become one of the most intimate and enjoyable
parts of this project for me. Some of my favorite moments working on Avarra
have had nothing to do with writing code—they have been the hours spent
talking through the world, asking "what if?", building on each other's ideas,
and watching Avarra become something neither of us would have created alone.

That process has helped make Avarra one of my favorite projects I have
ever built, and her creativity, excitement, and time have become an important
part of its story.

---

## Technology

Avarra is also a full-stack software engineering project built to explore the
architecture and infrastructure behind a persistent browser-based game.

### Backend

- Java 21
- Spring Boot
- Spring Security
- PostgreSQL
- JPA / Hibernate
- Flyway
- JUnit
- Mockito

### Frontend

- React
- TypeScript
- Vite
- React Router

### Infrastructure

The project is being designed around:

- Docker
- GitHub Actions
- AWS
- Linux-based deployment

Infrastructure will evolve as the game moves toward deployment.

---

## Project Structure

```text
avarra/
├── backend/        Java / Spring Boot application
├── frontend/       React / TypeScript client
├── docs/           Project and architecture documentation
└── ...
```

More detailed documentation lives with the part of the project it describes
rather than being duplicated in this README.

---

## Development

For local development setup, prerequisites, and run instructions, see
[Development Guide](docs/DEVELOPMENT.md).

Frontend-specific architecture and development information is available in
[Frontend Documentation](frontend/README.md).

---

## Current Status

Avarra is under active development.

The current development milestone focuses on the foundation required to enter
the game, including authentication, guest entry, the shared pre-game
interface, journey entry, and the path toward character creation.

The game systems themselves will be developed incrementally after this
foundation is established.

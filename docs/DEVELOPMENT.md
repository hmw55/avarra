# Avarra Development Guide

This guide documents the local development environment required to build and run Avarra.

It is intended both for contributors and for setting up Avarra on a new development machine.

## Prerequisites

Avarra currently requires:

- Git
- Java 21 JDK
- Node.js and npm
- Docker Engine
- Docker Compose

The backend uses the Maven Wrapper included in the repository, so a separate Maven installation is not required.

### Verify Prerequisites

```bash
git --version
java -version
javac -version
node --version
npm --version
docker --version
docker compose version
```

Java and `javac` should report Java 21.

---

## Clone the Repository

```bash
# SSH
git clone git@github.com:hmw55/avarra.git

# OR

# HTTPS
git clone https://github.com/hmw55/avarra.git

cd avarra
```

---

## Local Database

Avarra uses PostgreSQL for persistence. The local PostgreSQL development instance runs in Docker and is managed with Docker Compose.

### Configure the Environment

Copy the example environment file:

```bash
cp .env.example .env
```

Update `.env` with local development values. In particular, replace the example database password with a local password.

The `.env` file is ignored by Git and must never be committed.

### Start PostgreSQL

From the repository root:

```bash
docker compose up -d
```

Verify that the PostgreSQL container is running:

```bash
docker compose ps
```

To view the database logs:

```bash
docker compose logs postgres
```

To stop the local database:

```bash
docker compose down
```

The PostgreSQL data is stored in a Docker volume and persists when the container is stopped or recreated with `docker compose down`.

---

### Backend

The Avarra backend is a Java 21 application built with Spring Boot and Maven.

From the repository root: 

```bash
set -a
source .env
set +a

cd backend
./mvnw spring-boot:run
```

The Maven Wrapper will download the required Maven version and project dependencies when necessary.

Stop the backend with `Ctrl+C`

### Frontend

The Avarra frontend uses React, TypeScript, and Vite. 

From the repository root: 

```bash
cd frontend
npm install
npm run dev
```

`npm install` installs the frontend dependencies defined by the project. 

Vite will display the local development URL when the development server starts. 

Stop the frontend with `Ctrl+C`.

---

## Development Environment

Avarra is developed and tested on Linux. Development is currently performed on Arch Linux and WSL Ubuntu using Visual Studio Code.

---

## Environment Configuration and Secrets

Avarra is a public repository. Secrets and environment-specific configuration must never be committed to source control.

Use `.env.example` to document required environment variables with safe placeholder values. Store real local values in an ignored `.env` file or another appropriate local configuration source.

Never commit: 

- Passwords or database credentials
- API keys or access tokens
- Private keys
- Authentication or session secrets
- Production environment values
- Sensitive user data

Before committing changes, review staged files and confirm that no secrets or sensitive configuration are included. 

If a secret is accidentally committed, treat it as exposed and rotate or revoke it immediately. Removing it in a later commit does not remove it from Git history.

---

## Keeping This Guide Current

When a new tool, service, environment variable, or other dependency becomes required to run Avarra locally, this guide should be updated as part of the same change. 

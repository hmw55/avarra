# Avarra Development Guide

This guide documents the local development environment required to build and run Avarra.

It is intended both for contributors and for setting up Avarra on a new development machine.

## Prerequisites

Avarra currently requires:

- Git
- Java 21 JDK
- Node.js
- npm

The backend uses the Maven Wrapper included in the repository, so a separate Maven installation is not required.

### Verify Prerequisites

```bash
git --version
java -version
javac -version
node --version
npm --version
```

Java and `javac` should report Java 21.

---

## Clone the Repository

```bash
# SSH
git clone git@github.com:hmw55/avarra.git

# OR

# HTTPS
https://github.com/hmw55/avarra.git

cd avarra
```

### Backend

The Avarra backend is a Java 21 application built with Spring Boot and Maven.

From the repository root: 

```bash
cd backend
./mvnw spring-boot:run
```

The Maven Wrapper will download the required Maven version and project dependencies when necessary.

Stop the backend with `Crtl+C`

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

Avarra is currently developed using Visual Studio Code with the project and development tools running inside WSL Ubuntu or Arch Linux depending on the computer being used for development. 

---

## Keeping This Guide Current

When a new tool, service, environment variable, or other dependency becomes required to run Avarra locally, this guide should be updated as part of the same change. 
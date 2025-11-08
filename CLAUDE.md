# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a tutorial authentication server implementing Clean Architecture principles with JWT-based authentication and Role-Based Access Control (RBAC). The project is built with Kotlin and Spring Boot 3.x, using MySQL for persistence and Redis for token caching.

## Build and Development Commands

### Building the Project
```bash
./gradlew build
```

### Running the Application
```bash
./gradlew bootRun
```

### Running Tests
```bash
# Run all tests
./gradlew test

# Run architecture tests (ArchUnit validation)
./gradlew test --tests "*ArchitectureTest"

# Run specific test
./gradlew test --tests "ClassName.testMethodName"

# Generate test coverage report
./gradlew jacocoTestReport
```

### Database Setup
The application requires MySQL (port 3304) and Redis (port 6379). Connection details are in `src/main/resources/application.yml`:
- MySQL: `jdbc:mysql://localhost:3304/test`
- Redis: `localhost:6379`

## Architecture

This project follows **Clean Architecture** with strict dependency rules enforced by ArchUnit tests.

### Dependency Flow
```
infrastructure → adapter → application → domain
```

### Layer Rules

**Domain Layer** (`domain/`)
- Pure Kotlin with no external dependencies
- Contains business entities and enumerations
- No Spring or framework annotations
- Examples: `CurrentUser`, `Role`, `UserType`

**Application Layer** (`application/`)
- Depends only on domain
- Contains use case handlers and port interfaces
- Business logic orchestration
- Examples: `RegisterCommandHandler`, `UserPort`

**Adapter Layer** (`adapter/`)
- Implements ports defined in application layer
- Input adapters: REST controllers, DTOs
- Output adapters: JPA repositories, entities
- Example structure:
  - `adapter/input/api/rest/`: REST controllers
  - `adapter/output/mysql/`: JPA implementations

**Infrastructure Layer** (`infrastructure/`)
- Framework-specific configuration
- Spring Security setup
- Custom annotations like `@RequireAuth`
- Configuration classes: `SecurityConfig`, `JpaConfig`, `RedisConfig`, `WebConfig`

### Key Architectural Patterns

**Port-Adapter Pattern**: Application layer defines ports (interfaces), adapters implement them.

**Entity Mapping**: Domain entities (e.g., `CurrentUser`) are separate from JPA entities (e.g., `UserEntity`). Mappers convert between them.

**CQRS-style Commands**: Use cases accept Command objects (e.g., `RegisterCommandHandler.Command`).

## Security Implementation

### JWT Configuration
JWT settings are in `application.yml`:
- `jwt.secret-key`: Base64-encoded secret
- `jwt.expire-time`: Token expiration in milliseconds
- `jwt.issuer`: Token issuer identifier

Token provider logic is in `infrastructure/config/security/provider/TokenProvider.kt`.

### Authentication Flow
1. User registers via `/api/auth/register` (public endpoint)
2. User logs in via `/api/auth/login` (to be implemented)
3. JWT token issued and cached in Redis
4. Protected endpoints require `@RequireAuth` annotation

### Security Configuration
`SecurityConfig.kt` defines:
- Allowed public endpoints in `allowedList`
- CORS settings from `SecurityProps`
- Password encoding via BCryptPasswordEncoder
- Bearer token filter (currently commented out as TODO)

### Role Hierarchy
- **ADMIN**: Full system access
- **OPERATOR**: Content creation and management
- **AUDITOR**: Read-only access
- **USER**: Basic functionality

## Code Conventions

### Package Structure
```
demo.kotlin.auth/
├── domain/entity/          # Pure business entities
├── domain/enumerate/       # Enums (Role, UserType)
├── application/usecase/    # Command/Query handlers
├── application/port/       # Interface definitions
├── adapter/input/         # REST controllers, DTOs
├── adapter/output/        # Repository implementations
└── infrastructure/        # Spring configuration
```

### Naming Conventions
- Use case handlers end with `CommandHandler` or `QueryHandler`
- Port interfaces end with `Port`
- JPA entities end with `Entity`
- DTOs end with `Dto` or `RequestDto`/`ResponseDto`

### Transaction Management
Use `@Transactional` on use case handlers in the application layer, not in adapters or repositories.

## Current Implementation Status

### Completed
- User registration endpoint
- Domain model (User, Role, UserType)
- JPA persistence layer
- Security configuration
- JWT token provider setup

### TODO (as noted in code)
- Login endpoint implementation (currently stub)
- Logout functionality
- Token refresh mechanism
- Bearer token authentication filter (commented in SecurityConfig.kt:42-46)
- Input validation in RegisterCommandHandler.kt:15

## Testing Strategy

The project follows a 70/20/10 testing distribution:
- 70% Unit Tests: Domain logic and use cases with mocks
- 20% Integration Tests: Repository tests with `@DataJpaTest`, Controller tests with MockMvc
- 10% E2E Tests: Full application flow with `@SpringBootTest`

Architecture rules are validated using ArchUnit to ensure Clean Architecture principles are maintained.

## Dependencies

Key dependencies (from `build.gradle.kts`):
- Spring Boot 3.5.6
- Kotlin 1.9.25
- MySQL Connector
- Spring Data JPA
- Spring Data Redis
- Spring Security
- JWT (jjwt) 0.11.5
- Swagger/OpenAPI 2.8.9
- Kotlin Logging 7.0.3

Java toolchain: JDK 21

## Important Configuration Files

- `build.gradle.kts`: Gradle build configuration with allOpen plugin for JPA entities
- `gradle.properties`: Version properties (e.g., swaggerVersion)
- `application.yml`: Application configuration including datasource, JPA, Redis, and JWT settings
- `SecurityConfig.kt`: Spring Security and CORS configuration
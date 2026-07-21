# Eats Hub Catalog

Eats Hub Catalog is a reactive Spring Boot service for restaurant catalog and reservation flows. The current codebase combines a hexagonal reservation creation use case with MongoDB-backed catalog and reservation persistence components.

## Current status

Implemented so far:

- Reactive Spring Boot application using WebFlux.
- MongoDB reactive configuration and repositories.
- Reservation creation endpoint through a hexagonal architecture flow.
- Domain models for `Restaurant`, `Reservation`, and reservation status.
- Application ports for reservation creation, restaurant lookup, reservation persistence, and availability checks.
- MongoDB adapters for restaurants and reservations.
- Mock planner client for availability validation.
- Global REST exception handling.
- MapStruct mappers for legacy DTO/document mapping.
- Basic unit and context tests.
- OpenAPI/Swagger configuration.

Still in progress:

- Functional catalog handler methods currently act as placeholders.
- Some legacy persistence-oriented services coexist with the newer hexagonal reservation flow and can be refactored later.
- More REST endpoints for restaurant catalog and reservation CRUD can be added as the project evolves.

## Tech stack

- Java 17
- Spring Boot 3.5.15
- Spring WebFlux
- Spring Data Reactive MongoDB
- MongoDB
- Project Reactor
- Bean Validation / Jakarta Validation
- Lombok
- MapStruct
- Springdoc OpenAPI
- JUnit 5, Mockito, Reactor Test
- Maven Wrapper
- Docker Compose for local MongoDB

## Architecture overview

The reservation creation flow follows a hexagonal architecture style:

```text
HTTP Controller
      |
      v
CreateReservationUseCase
      |
      v
CreateReservationService
      |
      v
Application output ports
      |
      +--> RestaurantMongoAdapter -> Reactive MongoDB repository
      +--> ReservationMongoAdapter -> Reactive MongoDB repository
      +--> PlannerAvailabilityAdapter -> PlannerMSClientMock
```

Main layers:

- `adapter.in.web`: inbound HTTP adapters and REST DTOs.
- `application.port.in`: use-case contracts and command objects.
- `application.port.out`: contracts required by application services.
- `application.service`: use-case orchestration.
- `domain.model`: core domain objects.
- `domain.exception`: domain/application exceptions.
- `adapter.out`: infrastructure adapters implementing output ports.
- `infraestructure.persistence`: MongoDB documents, repositories, and legacy services.
- `interfaces`: legacy DTOs and MapStruct mappers.
- `infraestructure.config`: MongoDB and OpenAPI configuration.

## Available endpoint

### Create reservation

```http
POST /reservations
Content-Type: application/json
```

Example request:

```json
{
  "restaurantId": "00000000-0000-0000-0000-000000000001",
  "customerId": "customer-1",
  "customerName": "Alex",
  "customerEmail": "alex@example.com",
  "date": "2026-07-21",
  "time": "20:30:00",
  "partySize": 2,
  "notes": "Window table"
}
```

Successful response: `201 Created`

```json
{
  "id": "generated-reservation-uuid",
  "restaurantId": "00000000-0000-0000-0000-000000000001",
  "customerName": "Alex",
  "date": "2026-07-21",
  "time": "20:30:00",
  "partySize": 2,
  "status": "PENDING",
  "notes": "Window table"
}
```

Common error responses:

- `400 Bad Request`: validation error.
- `404 Not Found`: missing restaurant or resource.
- `409 Conflict`: business rule violation, such as closed restaurant or unavailable slot.
- `500 Internal Server Error`: unexpected error.

## Local MongoDB

A local MongoDB instance is defined in the root `docker-compose.yml` file.

From the repository root:

```powershell
docker compose up -d
```

MongoDB is exposed locally at:

- Host: `localhost`
- Port: `27018`
- Database: `catalog`
- Username: `master`
- Password: `master`
- Authentication database: `admin`

The application also reads MongoDB-specific settings from:

- `src/main/resources/application.properties`
- `src/main/resources/mongo-connection.properties`

## Build and test

From this module directory:

```powershell
.\mvnw.cmd clean package
```

Run tests only:

```powershell
.\mvnw.cmd test
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

## API documentation

When the application is running, the OpenAPI UI should be available through Springdoc Swagger UI:

```text
/swagger-ui.html
```

The exact generated OpenAPI JSON endpoint is managed by Springdoc.

## Project notes

- The mock planner client marks a configured restaurant ID as unavailable to simulate an external service response.
- Reservation creation currently stores new reservations with `PENDING` status.
- Domain-level reservation status is separated from persistence-level reservation status and mapped by the Mongo adapter.
- Javadoc comments are written in English to keep the code documentation consistent.


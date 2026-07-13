# Diagramas de arquitectura hexagonal

Estos diagramas muestran cómo va quedando la migración del módulo `eats_hub_catalog` hacia arquitectura hexagonal.

La lectura principal es:

> El centro contiene negocio y casos de uso.  
> Los bordes contienen tecnología: HTTP, Mongo, clientes externos y Spring.

---

## 1. Vista general por capas

```mermaid
flowchart TB
    subgraph IN["Adapters de entrada"]
        REST["ReservationController<br/>adapter.in.web"]
        REQ["CreateReservationRequest<br/>DTO HTTP"]
        RES["CreateReservationResponse<br/>DTO HTTP"]
    end

    subgraph APP["Application"]
        UC["CreateReservationUseCase<br/>Puerto de entrada"]
        CMD["CreateReservationCommand"]
        SVC["CreateReservationService<br/>Caso de uso"]
        RPORT["RestaurantRepositoryPort<br/>Puerto salida"]
        AVPORT["AvailabilityPort<br/>Puerto salida"]
        RSVPORT["ReservationRepositoryPort<br/>Puerto salida"]
    end

    subgraph DOM["Domain"]
        RESTAURANT["Restaurant"]
        RESERVATION["Reservation"]
        STATUS["ReservationStatus"]
        BEX["BusinessException"]
        NFEX["ResourceNotFoundException"]
    end

    subgraph OUT["Adapters de salida"]
        RESTMONGO["RestaurantMongoAdapter<br/>adapter.out.mongo"]
        RSVMONGO["ReservationMongoAdapter<br/>adapter.out.mongo"]
        PLANNER["PlannerAvailabilityAdapter<br/>adapter.out.planner"]
    end

    subgraph TECH["Infraestructura técnica existente"]
        RESTREPO["RestaurantRepository<br/>Spring Data Mongo"]
        RSVREPO["ReservationRepository<br/>Spring Data Mongo"]
        CLIENT["PlannerMSClientMock"]
        RDOC["RestaurantDocument"]
        RSV_DOC["ReservationDocument"]
    end

    REST --> REQ
    REST --> UC
    UC --> SVC
    REQ --> CMD
    CMD --> SVC

    SVC --> RESTAURANT
    SVC --> RESERVATION
    SVC --> STATUS
    SVC --> BEX
    SVC --> NFEX

    SVC --> RPORT
    SVC --> AVPORT
    SVC --> RSVPORT

    RPORT --> RESTMONGO
    AVPORT --> PLANNER
    RSVPORT --> RSVMONGO

    RESTMONGO --> RESTREPO
    RESTMONGO --> RDOC
    RSVMONGO --> RSVREPO
    RSVMONGO --> RSV_DOC
    PLANNER --> CLIENT

    RESERVATION --> RES
```

---

## 2. Flujo de creación de reserva

```mermaid
sequenceDiagram
    autonumber
    actor Client as Cliente HTTP
    participant Controller as ReservationController
    participant UseCase as CreateReservationUseCase
    participant Service as CreateReservationService
    participant RestaurantPort as RestaurantRepositoryPort
    participant RestaurantAdapter as RestaurantMongoAdapter
    participant RestaurantRepo as RestaurantRepository Mongo
    participant AvailabilityPort as AvailabilityPort
    participant PlannerAdapter as PlannerAvailabilityAdapter
    participant Planner as PlannerMSClientMock
    participant ReservationPort as ReservationRepositoryPort
    participant ReservationAdapter as ReservationMongoAdapter
    participant ReservationRepo as ReservationRepository Mongo

    Client->>Controller: POST /reservations
    Controller->>Controller: CreateReservationRequest -> CreateReservationCommand
    Controller->>UseCase: create(command)
    UseCase->>Service: create(command)

    Service->>RestaurantPort: findById(restaurantId)
    RestaurantPort->>RestaurantAdapter: findById(restaurantId)
    RestaurantAdapter->>RestaurantRepo: findById(restaurantId)
    RestaurantRepo-->>RestaurantAdapter: RestaurantDocument
    RestaurantAdapter-->>Service: Restaurant dominio

    Service->>Service: restaurant.isOpenAt(time)

    Service->>AvailabilityPort: isAvailable(...)
    AvailabilityPort->>PlannerAdapter: isAvailable(...)
    PlannerAdapter->>Planner: verifyAvailability(...)
    Planner-->>PlannerAdapter: Boolean
    PlannerAdapter-->>Service: Boolean

    Service->>Service: new Reservation(..., PENDING, ...)

    Service->>ReservationPort: save(reservation)
    ReservationPort->>ReservationAdapter: save(reservation)
    ReservationAdapter->>ReservationAdapter: Reservation -> ReservationDocument
    ReservationAdapter->>ReservationRepo: save(document)
    ReservationRepo-->>ReservationAdapter: ReservationDocument guardado
    ReservationAdapter->>ReservationAdapter: ReservationDocument -> Reservation
    ReservationAdapter-->>Service: Reservation dominio

    Service-->>Controller: Reservation dominio
    Controller->>Controller: Reservation -> CreateReservationResponse
    Controller-->>Client: 201 Created
```

---

## 3. Dependencias permitidas

```mermaid
flowchart LR
    WEB["adapter.in.web<br/>Controller + DTOs HTTP"]
    CONFIG["config<br/>UseCaseConfig"]
    APP["application<br/>Use cases + ports"]
    DOMAIN["domain<br/>Modelos + reglas"]
    OUT["adapter.out<br/>Mongo + Planner adapters"]
    INFRA["infraestructure<br/>Repos Mongo + clients existentes"]

    WEB --> APP
    WEB --> DOMAIN

    CONFIG --> APP
    CONFIG --> OUT

    APP --> DOMAIN

    OUT --> APP
    OUT --> DOMAIN
    OUT --> INFRA
```

Idea clave:

```text
adapter.in.web puede conocer application.
application puede conocer domain.
adapter.out puede conocer application, domain e infraestructura técnica.
domain no conoce a nadie.
```

---

## 4. Dependencias que queremos evitar

```mermaid
flowchart TB
    DOMAIN["domain"]
    APP["application"]
    MONGO["Mongo / ReservationDocument / RestaurantDocument"]
    CLIENT["PlannerMSClientMock"]
    WEB["HTTP DTOs / Controllers"]
    SPRING["Spring Web / Spring Data"]

    DOMAIN -. "NO" .-> MONGO
    DOMAIN -. "NO" .-> CLIENT
    DOMAIN -. "NO" .-> WEB
    DOMAIN -. "NO" .-> SPRING

    APP -. "NO" .-> MONGO
    APP -. "NO" .-> CLIENT
    APP -. "NO" .-> WEB
    APP -. "NO directo" .-> SPRING
```

Si una clase de `domain` o `application` necesita algo externo, no lo importa directamente. Define o usa un puerto.

---

## 5. Mapa de paquetes actual

```mermaid
flowchart TB
    ROOT["com.alejandrovillar.eats_hub_catalog"]

    ROOT --> DOMAIN["domain"]
    ROOT --> APPLICATION["application"]
    ROOT --> ADAPTER["adapter"]
    ROOT --> CONFIG["config"]
    ROOT --> INFRA["infraestructure"]
    ROOT --> LEGACY["interfaces / services legacy"]

    DOMAIN --> DMODEL["domain.model"]
    DOMAIN --> DEX["domain.exception"]
    DOMAIN --> DVALID["domain.validations<br/>legacy temporal"]

    APPLICATION --> PIN["application.port.in"]
    APPLICATION --> POUT["application.port.out"]
    APPLICATION --> ASVC["application.service"]

    ADAPTER --> AIN["adapter.in.web"]
    ADAPTER --> AOUT["adapter.out"]

    AIN --> ADTOS["adapter.in.web.dto"]
    AIN --> ACTRL["adapter.in.web.controllers"]

    AOUT --> AMONGO["adapter.out.mongo"]
    AOUT --> APLANNER["adapter.out.planner"]

    INFRA --> MONGO["infraestructure.persistence.mongo"]
    INFRA --> CLIENTS["infraestructure.clients"]
```

Notas:

- `domain.validations.ReservationValidator` sigue siendo legacy temporal mientras exista el flujo viejo.
- `interfaces.*` también queda como parte del diseño anterior.
- El nuevo flujo hexagonal debería crecer bajo `domain`, `application`, `adapter` y `config`.

---

## 6. Cómo leer el caso de uso nuevo

```mermaid
flowchart LR
    COMMAND["CreateReservationCommand"]
    FIND["Buscar restaurante"]
    OPEN["Validar horario<br/>Restaurant.isOpenAt"]
    AVAILABLE["Consultar disponibilidad<br/>AvailabilityPort"]
    CREATE["Crear Reservation<br/>status PENDING"]
    SAVE["Guardar<br/>ReservationRepositoryPort"]
    RESULT["Reservation"]

    COMMAND --> FIND
    FIND --> OPEN
    OPEN --> AVAILABLE
    AVAILABLE --> CREATE
    CREATE --> SAVE
    SAVE --> RESULT
```

Este flujo vive en:

```text
application.service.CreateReservationService
```

Y representa la historia de negocio:

> Dado un restaurante, una fecha, una hora y un cliente, crear una reserva pendiente si el restaurante existe, está abierto y tiene disponibilidad.

---

## 7. Dónde ocurre cada transformación

```mermaid
flowchart TB
    JSON["JSON HTTP"]
    REQUEST["CreateReservationRequest"]
    COMMAND["CreateReservationCommand"]
    DOMAIN["Reservation dominio"]
    DOCUMENT["ReservationDocument Mongo"]
    RESPONSE["CreateReservationResponse"]

    JSON --> REQUEST
    REQUEST -->|"ReservationController.toCommand"| COMMAND
    COMMAND -->|"CreateReservationService"| DOMAIN
    DOMAIN -->|"ReservationMongoAdapter.toDocument"| DOCUMENT
    DOCUMENT -->|"ReservationMongoAdapter.toDomain"| DOMAIN
    DOMAIN -->|"ReservationController.toResponse"| RESPONSE
```

Regla práctica:

```text
Los DTOs HTTP no entran al caso de uso.
Los documentos Mongo no entran al caso de uso.
El caso de uso trabaja con commands y modelos de dominio.
```

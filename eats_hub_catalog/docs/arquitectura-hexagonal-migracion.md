# Migración a arquitectura hexagonal en `eats_hub_catalog`

Este documento explica cómo estamos migrando el proyecto hacia una arquitectura hexagonal, también conocida como arquitectura de puertos y adaptadores.

La idea principal es simple:

> El dominio y la aplicación no deben depender de Mongo, Spring Web, clientes externos, DTOs de API ni detalles técnicos.  
> Esos detalles viven afuera, en adaptadores.

---

## 1. Objetivo de la migración

El proyecto empezó con una estructura más cercana a arquitectura por capas tradicional:

```text
controller/service/repository/model
```

Ese estilo funciona, pero con el tiempo suele mezclar responsabilidades:

- servicios de negocio usando documentos Mongo directamente;
- validadores de dominio dependiendo de repositorios técnicos;
- lógica de negocio acoplada a mocks o clientes externos;
- DTOs, documentos y modelos de negocio mezclados;
- dificultad para testear casos de uso sin levantar infraestructura.

Con arquitectura hexagonal queremos conseguir esto:

- que el negocio sea fácil de leer;
- que los casos de uso se puedan probar con mocks de puertos;
- que Mongo, WebFlux, clientes externos y frameworks queden en los bordes;
- que cambiar una tecnología no obligue a reescribir reglas de negocio.

---

## 2. Regla de dependencias

La regla más importante es:

```text
Las dependencias siempre apuntan hacia dentro.
```

Visualmente:

```text
Adaptadores externos
        |
        v
Aplicación / Casos de uso
        |
        v
Dominio
```

Eso significa:

- `domain` no conoce Spring, Mongo, WebFlux, repositorios ni clientes externos.
- `application` conoce el dominio y define puertos.
- `adapter` conoce Spring, Mongo, clientes externos y traduce hacia/desde el centro.

---

## 3. Capas del proyecto

### 3.1. `domain`

Representa el negocio puro.

Paquete actual:

```text
com.alejandrovillar.eats_hub_catalog.domain
```

Aquí viven:

- entidades o modelos de negocio;
- value objects;
- enums de negocio;
- excepciones de dominio;
- reglas que pertenecen al negocio.

Ejemplos actuales:

```text
domain.model.Reservation
domain.model.ReservationStatus
domain.model.Restaurant
domain.exception.BusinessException
domain.exception.ResourceNotFoundException
```

Ejemplo importante:

```java
restaurant.isOpenAt(command.time())
```

Esa regla pertenece al dominio porque responder si un restaurante está abierto no depende de Mongo ni de HTTP. Depende del concepto de restaurante.

El dominio no debería importar cosas como:

```java
org.springframework...
org.springframework.data...
reactor...
infraestructure...
adapter...
```

Nota: En este proyecto estamos permitiendo que el caso de uso devuelva `Mono`, porque es una aplicación WebFlux/reactiva. Pero el dominio en sí debería seguir siendo lo más limpio posible.

---

### 3.2. `application`

Representa los casos de uso.

Paquete actual:

```text
com.alejandrovillar.eats_hub_catalog.application
```

Aquí vive la orquestación de negocio:

- recibir una intención;
- buscar datos necesarios mediante puertos;
- ejecutar reglas de negocio;
- pedir disponibilidad a otro sistema mediante un puerto;
- guardar el resultado mediante otro puerto.

Subpaquetes actuales:

```text
application.port.in
application.port.out
application.service
```

#### `application.port.in`

Define cómo entra una acción al sistema.

Ejemplos:

```text
CreateReservationUseCase
CreateReservationCommand
```

`CreateReservationUseCase` es el contrato que un controller, handler, runner o test puede usar.

```java
Mono<Reservation> create(CreateReservationCommand command);
```

`CreateReservationCommand` representa la intención de crear una reserva. No es un DTO HTTP. Es un comando interno del caso de uso.

#### `application.port.out`

Define lo que la aplicación necesita del exterior, sin decir cómo se implementa.

Ejemplos:

```text
RestaurantRepositoryPort
ReservationRepositoryPort
AvailabilityPort
```

Estos son contratos. No son Mongo. No son HTTP. No son clientes reales.

Por ejemplo:

```java
public interface AvailabilityPort {
    Mono<Boolean> isAvailable(
        UUID restaurantId,
        LocalDate date,
        LocalTime time,
        int partySize
    );
}
```

La aplicación dice:

> Necesito saber si hay disponibilidad.

Pero no dice:

> Llama a este mock, a este microservicio, a esta tabla o a esta API.

Eso lo decide un adapter.

#### `application.service`

Contiene la implementación del caso de uso.

Ejemplo:

```text
CreateReservationService
```

Responsabilidad:

```text
Crear una reserva cumpliendo las reglas de negocio.
```

Flujo actual:

```text
1. Buscar restaurante por id.
2. Si no existe, error.
3. Validar si está abierto.
4. Consultar disponibilidad.
5. Si no hay disponibilidad, error.
6. Crear Reservation en estado PENDING.
7. Guardar la reserva.
```

Lo importante es que `CreateReservationService` no debería saber si el restaurante viene de Mongo, PostgreSQL, memoria o una API externa.

---

### 3.3. `adapter`

Representa los bordes técnicos del sistema.

Paquete que estamos introduciendo:

```text
com.alejandrovillar.eats_hub_catalog.adapter
```

Aquí viven las implementaciones concretas de los puertos.

Ejemplos:

```text
adapter.out.planner.PlannerAvailabilityAdapter
adapter.out.mongo.RestaurantMongoAdapter
adapter.out.mongo.ReservationMongoAdapter
```

Un adapter hace traducción.

Por ejemplo:

```text
AvailabilityPort
        ^
        |
PlannerAvailabilityAdapter
        |
        v
PlannerMSClientMock
```

El caso de uso habla con `AvailabilityPort`.

El adapter sabe que, por ahora, la disponibilidad viene de `PlannerMSClientMock`.

---

### 3.4. `infraestructure`

Este paquete ya existía en el proyecto.

Actualmente contiene:

```text
infraestructure.clients
infraestructure.persistence.mongo
```

Durante la migración no estamos moviendo todo de golpe. Estamos usando algunas clases existentes desde nuevos adaptadores.

Por ejemplo:

```text
adapter.out.planner.PlannerAvailabilityAdapter
        usa
infraestructure.clients.PlannerMSClientMock
```

Y:

```text
adapter.out.mongo.RestaurantMongoAdapter
        usa
infraestructure.persistence.mongo.repositories.RestaurantRepository
```

Más adelante podríamos decidir mover o renombrar `infraestructure` a una estructura más consistente, pero no es necesario hacerlo al principio. Primero conviene estabilizar las dependencias.

---

## 4. Flujo completo de creación de reserva

Este es el flujo objetivo del caso de uso `CreateReservation`.

```text
Entrada HTTP, runner o test
        |
        v
CreateReservationUseCase
        |
        v
CreateReservationService
        |
        |-- RestaurantRepositoryPort
        |         |
        |         v
        |   RestaurantMongoAdapter
        |         |
        |         v
        |   RestaurantRepository Mongo
        |
        |-- AvailabilityPort
        |         |
        |         v
        |   PlannerAvailabilityAdapter
        |         |
        |         v
        |   PlannerMSClientMock / microservicio externo
        |
        |-- ReservationRepositoryPort
                  |
                  v
            ReservationMongoAdapter
                  |
                  v
            ReservationRepository Mongo
```

El caso de uso no conoce ningún adapter concreto.

Spring será quien conecte las piezas en runtime.

---

## 5. Qué problema resuelve cada puerto

### `RestaurantRepositoryPort`

El caso de uso necesita consultar un restaurante.

Contrato:

```java
Mono<Restaurant> findById(UUID restaurantId);
```

No devuelve `RestaurantDocument`, porque `RestaurantDocument` es un modelo de persistencia.

Devuelve `Restaurant`, que es modelo de dominio.

---

### `AvailabilityPort`

El caso de uso necesita saber si el restaurante tiene disponibilidad.

Contrato:

```java
Mono<Boolean> isAvailable(
    UUID restaurantId,
    LocalDate date,
    LocalTime time,
    int partySize
);
```

Aunque el mock actual no use `partySize`, el puerto sí lo incluye porque desde el punto de vista de negocio la disponibilidad normalmente depende del tamaño del grupo.

Esto es importante:

- el puerto se diseña según la necesidad del caso de uso;
- el adapter se adapta a la limitación técnica del sistema externo.

---

### `ReservationRepositoryPort`

El caso de uso necesita guardar la reserva.

Contrato:

```java
Mono<Reservation> save(Reservation reservation);
```

No recibe `ReservationDocument`.

El caso de uso crea y maneja `Reservation`, no documentos Mongo.

---

## 6. Diferencia entre modelos

En arquitectura hexagonal conviene separar modelos por intención.

### Modelo de dominio

Ejemplo:

```text
domain.model.Reservation
domain.model.Restaurant
```

Representa negocio.

No debería tener anotaciones de Mongo, HTTP o validaciones de framework.

---

### Documento de persistencia

Ejemplo:

```text
infraestructure.persistence.mongo.models.ReservationDocument
infraestructure.persistence.mongo.models.RestaurantDocument
```

Representa cómo guardamos datos en Mongo.

Puede tener:

```java
@Document
@Id
@Indexed
```

No debería escaparse hacia el caso de uso.

---

### DTO HTTP

Todavía no lo estamos trabajando, pero cuando creemos controllers o handlers, probablemente necesitaremos DTOs como:

```text
CreateReservationRequest
ReservationResponse
```

Estos DTOs pertenecen al adapter de entrada, no al dominio.

Ejemplo futuro:

```text
adapter.in.web.CreateReservationController
adapter.in.web.dto.CreateReservationRequest
adapter.in.web.dto.ReservationResponse
```

El controller traducirá:

```text
CreateReservationRequest -> CreateReservationCommand
Reservation -> ReservationResponse
```

---

## 7. Qué pasa con `ReservationValidator`

La clase antigua `ReservationValidator` no desaparece mágicamente.

Ahora mismo su responsabilidad se está repartiendo:

```text
ReservationValidator.applyValidations()
        pasa a
CreateReservationService.create()
```

```text
ReservationValidator.validateRestaurantNotClosed()
        pasa a
Restaurant.isOpenAt(...)
```

```text
ReservationValidator.validateAvailability()
        pasa a
AvailabilityPort
        implementado por
PlannerAvailabilityAdapter
```

Pero mientras exista el flujo antiguo, por ejemplo `ReservationCrudServiceImpl`, no deberíamos borrar `ReservationValidator`.

Primero terminamos el flujo nuevo. Luego decidimos si:

- migramos el flujo viejo al caso de uso nuevo;
- eliminamos el servicio viejo;
- dejamos ambos temporalmente;
- o hacemos una transición controlada.

---

## 8. Estado actual de la migración

Ya tenemos o estamos creando:

```text
domain.model.Reservation
domain.model.ReservationStatus
domain.model.Restaurant

application.port.in.CreateReservationCommand
application.port.in.CreateReservationUseCase

application.port.out.RestaurantRepositoryPort
application.port.out.ReservationRepositoryPort
application.port.out.AvailabilityPort

application.service.CreateReservationService

adapter.out.planner.PlannerAvailabilityAdapter
adapter.out.mongo.RestaurantMongoAdapter
```

Pendiente:

```text
adapter.out.mongo.ReservationMongoAdapter
configuración Spring para registrar CreateReservationService
adapter de entrada web o runner usando CreateReservationUseCase
limpieza del flujo legacy
tests de adapters si queremos cubrir mapeos
```

---

## 9. Orden recomendado de migración

Estamos siguiendo este orden:

```text
1. Crear modelo de dominio.
2. Crear puertos de salida.
3. Crear puerto de entrada y comando.
4. Crear servicio de aplicación.
5. Probar el caso de uso con mocks.
6. Crear adapter de disponibilidad.
7. Crear adapter Mongo para Restaurant.
8. Crear adapter Mongo para Reservation.
9. Configurar Spring para inyectar el caso de uso.
10. Crear adapter de entrada web.
11. Reemplazar o retirar flujo antiguo.
```

Este orden es intencional: primero protegemos el centro y luego conectamos los bordes.

---

## 10. Cómo saber si una clase está en la capa correcta

Preguntas útiles:

### Si borro Mongo, ¿esta clase debería seguir existiendo?

Si la respuesta es sí, probablemente va en `domain` o `application`.

Si la respuesta es no, probablemente va en `adapter` o `infraestructure`.

---

### ¿Esta clase contiene una regla de negocio?

Ejemplo:

```text
Un restaurante no acepta reservas fuera de su horario.
```

Eso es dominio o aplicación.

---

### ¿Esta clase traduce entre tecnología y negocio?

Ejemplo:

```text
RestaurantDocument -> Restaurant
Reservation -> ReservationDocument
```

Eso es adapter.

---

### ¿Esta clase sabe demasiado?

Si una clase conoce al mismo tiempo:

- Mongo;
- clientes externos;
- DTOs HTTP;
- reglas de negocio;
- validaciones;
- logs;
- transformación de datos;

probablemente está mezclando responsabilidades.

---

## 11. Forma mental de leer el proyecto

Piensa el sistema así:

```text
El dominio sabe qué es verdad para el negocio.

La aplicación sabe qué pasos ejecutar.

Los puertos dicen qué necesita la aplicación del exterior.

Los adaptadores explican cómo se consigue eso técnicamente.
```

En nuestro caso:

```text
Restaurant sabe si está abierto.

CreateReservationService sabe cómo crear una reserva.

RestaurantRepositoryPort dice que necesitamos buscar restaurantes.

RestaurantMongoAdapter sabe cómo buscar ese restaurante en Mongo.
```

Ese es el corazón de la migración.


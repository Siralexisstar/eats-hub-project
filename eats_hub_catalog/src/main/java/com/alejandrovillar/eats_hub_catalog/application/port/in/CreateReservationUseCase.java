package com.alejandrovillar.eats_hub_catalog.application.port.in;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import reactor.core.publisher.Mono;


//Controller(entry) uses this interface (CreateReservationUseCase)--> CreateReservationService --> Exit Port
//CreateReservationUseCase (entry port) --> CreateReservationCommando (transport data) --> CreatrionService
/**
 * Inbound application port for reservation creation.
 * <p>
 * Controllers, tests, schedulers, or other entry points depend on this contract instead of a
 * concrete service implementation.
 */
/**
 * Inbound application port for reservation creation.
 */
public interface CreateReservationUseCase {
    /**
     * Creates a reservation by applying business rules and persistence operations.
     *
     * @param createReservationCommand data required to create the reservation
     * @return a reactive publisher containing the created reservation
     */
    /**
     * Creates a reservation by applying the application use-case flow.
     *
     * @param createReservationCommand data required to create the reservation
     * @return reactive publisher containing the created reservation
     */
    Mono<Reservation> create(CreateReservationCommand createReservationCommand);
}

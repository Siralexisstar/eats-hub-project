package com.alejandrovillar.eats_hub_catalog.application.port.out;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import reactor.core.publisher.Mono;


//Application layer. Application defines the contract and the adapter applies
/**
 * Outbound application port for reservation persistence.
 */
/**
 * Outbound application port for reservation persistence.
 */
public interface ReservationRepositoryPort {

    /**
     * Persists a reservation and returns the stored domain object.
     *
     * @param reservation reservation to store
     * @return persisted reservation
     */
    Mono<Reservation> save(Reservation reservation);
}
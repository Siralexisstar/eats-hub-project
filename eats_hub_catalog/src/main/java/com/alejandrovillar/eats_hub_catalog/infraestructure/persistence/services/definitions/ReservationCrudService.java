package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Persistence-facing contract for reservation CRUD operations.
 */
/**
 * Persistence-facing contract for reservation CRUD operations.
 */
public interface ReservationCrudService {


    /**
     * Creates a reservation using the provided request or document.
     *
     * @return reactive result of the operation
     */
    Mono<ReservationDocument> createReservation(ReservationDocument reservationDocument);

    /**
     * Retrieves a reservation document by its identifier.
     *
     * @return reactive result of the operation
     */
    Mono<ReservationDocument> getReservationById(UUID reservationId);

    /**
     * Retrieves reservation documents for a restaurant and optional status.
     *
     * @return reactive result of the operation
     */
    Flux<ReservationDocument> getByRestaurantId(UUID restaurantId, ReservationStatus status);

    /**
     * Updates an existing reservation.
     *
     * @return reactive result of the operation
     */
    Mono<ReservationDocument> updateReservation(UUID restaurantId, ReservationDocument reservationDocument);

    /**
     * Deletes an existing reservation.
     *
     * @return reactive result of the operation
     */
    Mono<Void> deleteReservation(UUID uuid);

}

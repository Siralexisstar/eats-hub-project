package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Business-facing contract for reservation operations exposed through DTOs.
 */
/**
 * Business-facing contract for reservation operations exposed through DTOs.
 */
public interface ReservationBusinessService {

    /**
     * Creates a reservation using the provided request or document.
     *
     * @return reactive result of the operation
     */
    Mono<String> createReservation(ReservationRequest reservation);

    /**
     * Reads one reservation by its identifier.
     *
     * @return reactive result of the operation
     */
    Mono<ReservationResponse> readByReservationId(UUID id);

    /**
     * Reads reservations associated with a restaurant.
     *
     * @return reactive result of the operation
     */
    Flux<ReservationResponse> readByRestaurantId(UUID restaurantId, ReservationStatus status);

    /**
     * Updates an existing reservation.
     *
     * @return reactive result of the operation
     */
    Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation);

    /**
     * Deletes an existing reservation.
     *
     * @return reactive result of the operation
     */
    Mono<Void> deleteReservation(UUID id);
}

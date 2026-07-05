package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationBusinessService {

    Mono<String> createReservation(ReservationRequest reservationDocument);

    Mono<ReservationResponse> getReservationById(UUID reservationId);

    Flux<ReservationResponse> getRestaurantByIdAndStatus(UUID restaurantId, ReservationStatus status);

    Mono<ReservationResponse> updateReservation(UUID restaurantId, ReservationRequest reservationDocument);

    Mono<Void> deleteReservation(UUID uuid);
}

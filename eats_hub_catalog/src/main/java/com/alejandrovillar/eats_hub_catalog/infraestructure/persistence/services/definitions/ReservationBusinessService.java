package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationBusinessService {

    Mono<String> createReservation(ReservationRequest reservation);

    Mono<ReservationResponse> readByReservationId(UUID id);

    Flux<ReservationResponse> readByRestaurantId(UUID restaurantId, ReservationStatus status);

    Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation);

    Mono<Void> deleteReservation(UUID id);
}

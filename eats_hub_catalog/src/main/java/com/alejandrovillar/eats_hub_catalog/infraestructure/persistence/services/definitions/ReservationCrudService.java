package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationCrudService {


    Mono<ReservationDocument> createReservation(ReservationDocument reservationDocument);

    Mono<ReservationDocument> getReservationById(UUID reservationId);

    //no entiendop que hace este metodo aqui
    Flux<ReservationDocument> getRestaurantByIdAndStatus(UUID restaurantId, ReservationStatus status);

    Mono<ReservationDocument> updateReservation(UUID restaurantId, ReservationDocument reservationDocument);

    Mono<Void> deleteReservation(UUID uuid);

}

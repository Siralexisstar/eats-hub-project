package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.ReservationRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.ReservationBusinessService;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import com.alejandrovillar.eats_hub_catalog.interfaces.mappers.ReservationMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Data
@Slf4j
@RequiredArgsConstructor
public class ReservationBusinessServiceImpl implements ReservationBusinessService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;


    @Override
    public Mono<String> createReservation(ReservationRequest reservationDocument) {
        return null;
    }

    @Override
    public Mono<ReservationResponse> getReservationById(UUID reservationId) {
        return null;
    }

    @Override
    public Flux<ReservationResponse> getRestaurantByIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return null;
    }

    @Override
    public Mono<ReservationResponse> updateReservation(UUID restaurantId, ReservationRequest reservationDocument) {
        return null;
    }

    @Override
    public Mono<Void> deleteReservation(UUID uuid) {
        return null;
    }
}

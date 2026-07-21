package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.ReservationBusinessService;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.ReservationCrudService;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import com.alejandrovillar.eats_hub_catalog.interfaces.mappers.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationBusinessServiceImpl implements ReservationBusinessService {

    private final ReservationCrudService reservationCrudService;
    private final ReservationMapper reservationMapper;
    
    @Override
    public Mono<String> createReservation(ReservationRequest reservation) {
        log.info("Creating reservation: {}", reservation);

        return Mono.just(reservation)
                .transform(this.reservationMapper::toDocumentMono)
                .flatMap(this.reservationCrudService::createReservation)
                .map(savedReservation -> {
                    log.info("Saving reservation: {}", savedReservation);
                    return savedReservation.getId().toString();
                });
    }

    @Override
    public Mono<ReservationResponse> readByReservationId(UUID id) {
        log.info("Reading reservation with id {}", id);

        return this.reservationCrudService.getReservationById(id)
                .transform(this.reservationMapper::toResponseMono)
                .doOnSuccess(reservation -> log.info("Read reservation with id {} successfully", id));
    }


    @Override
    public Flux<ReservationResponse> readByRestaurantId(UUID restaurantId, ReservationStatus status) {
        log.info("Reading reservation with restaurant id {}", restaurantId);

        return this.reservationCrudService.getByRestaurantId(restaurantId, status)
                .transform(this.reservationMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading reservation with restaurant id {} successfully", restaurantId));
    }

    @Override
    public Mono<ReservationResponse> updateReservation(UUID id, ReservationRequest reservation) {
        log.info("Updating reservation with id {}", id);

        return Mono.just(reservation)
                .transform(this.reservationMapper::toDocumentMono)
                .flatMap(reservationCollection -> this.reservationCrudService.updateReservation(id, reservationCollection))
                .transform(this.reservationMapper::toResponseMono)
                .doOnNext(reservationResponse -> log.info("Updating reservation with id {} successfully", id));
    }

    @Override
    public Mono<Void> deleteReservation(UUID id) {
        log.info("Deleting reservation with id {}", id);

        return this.reservationCrudService.deleteReservation(id)
                .doOnSuccess(VOID -> log.info("Deleting reservation with id {} successfully", id));
    }
}

package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.ReservationRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.ReservationCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationCrudServiceImpl implements ReservationCrudService {

    private final ReservationRepository reservationRepository;

    private final RestaurantRepository restaurantRepository;


    @Override
    public Mono<ReservationDocument> createReservation(ReservationDocument reservationDocument) {
        return restaurantRepository
                .findById(UUID.fromString(reservationDocument.getRestaurantId()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Not Restaurant found with this id")))
                .flatMap(restaurant -> {

                            if (Objects.isNull(reservationDocument.getStatus())) {
                                log.info("Setting the status to PENDING, and saving");
                                reservationDocument.setStatus(ReservationStatus.PENDING);
                            }
                            log.info("Saving restaurant with status, {}", reservationDocument.getStatus());
                            return reservationRepository.save(reservationDocument);
                        }
                );


    }

    //Coge la reserva por el id del restaurante por reservation
    @Override
    public Mono<ReservationDocument> getReservationById(UUID reservationId) {
        return reservationRepository
                .findById(reservationId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found with this id")));
    }


    //Encontrar el restaurante por las reservas
    @Override
    public Flux<ReservationDocument> getRestaurantByIdAndStatus(UUID restaurantId, ReservationStatus status) {
        return restaurantRepository
                .findById(restaurantId) //restaurant ID encuentra los restaurantes por ID
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                .flatMapMany(restaurant -> {
                    if (Objects.isNull(status)) {
                        log.info("Reading reservation with id, {}", restaurant.getId());
                    }
                    log.info("Getting the restaurant with id, {} and status {}", restaurant.getId(), status);

                    //encuentra el restaurante por el estado de la reserva
                    return reservationRepository.findByRestaurantIdAndStatus(restaurantId.toString(), status);
                });

    }

    @Override
    public Mono<ReservationDocument> updateReservation(UUID reservationId, ReservationDocument reservationDocument) {
        return reservationRepository.findById(reservationId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found with this id")))
                .flatMap(existingReservation -> {
                    existingReservation.setRestaurantId(reservationDocument.getRestaurantId());
                    existingReservation.setCustomerId(reservationDocument.getCustomerId());
                    existingReservation.setCustomerName(reservationDocument.getCustomerName());
                    existingReservation.setCustomerEmail(reservationDocument.getCustomerEmail());
                    existingReservation.setTime(reservationDocument.getTime());
                    existingReservation.setPartySize(reservationDocument.getPartySize());
                    existingReservation.setStatus(reservationDocument.getStatus());
                    existingReservation.setNotes(reservationDocument.getNotes());

                    return reservationRepository.save(existingReservation);
                });
    }

    @Override
    public Mono<Void> deleteReservation(UUID uuid) {
        return reservationRepository
                .findById(uuid)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found")))
                .flatMap(reservation -> reservationRepository.deleteById(uuid));
    }

}

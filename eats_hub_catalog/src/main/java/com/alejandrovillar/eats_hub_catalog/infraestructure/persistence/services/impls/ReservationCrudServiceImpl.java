package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.ReservationRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.ReservationCrudService;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.validations.ReservationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationCrudServiceImpl implements ReservationCrudService {

    private final ReservationRepository reservationRepository;

    private final RestaurantRepository restaurantRepository;

    private final ReservationValidator reservationValidator;


    @Override
    public Mono<ReservationDocument> createReservation(ReservationDocument reservationDocument) {

        //Taking profit for the type inference
        // the var its the same that --> List<BusinessValidator<ReservationDocument>> validations
        //calculate the type in execution time.
        final var validations = List.of(
                this.reservationValidator.validateRestaurantNotClosed()
        );

        return reservationValidator.applyValidations(reservationDocument, validations)
                .then(Mono.defer(() -> {
                    if (Objects.isNull(reservationDocument.getStatus())) {
                        reservationDocument.setStatus(ReservationStatus.PENDING);
                    }
                    return reservationRepository.save(reservationDocument);
                }));
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

        final var validations = List.of(
                this.reservationValidator.validateRestaurantNotClosed(),
                this.reservationValidator.validateAvailability()
        );

        return reservationRepository.findById(reservationId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reservation not found with this id")))
                //with this validation we can be sure that the reservation it's the same
                //if validations pass we return the samen data
                .flatMap(existingReservation -> {
                    reservationDocument.setRestaurantId(existingReservation.getRestaurantId());

                    return reservationValidator.applyValidations(reservationDocument, validations)
                            .thenReturn(existingReservation);
                })
                .flatMap(existingReservation -> {
                    existingReservation.setStatus(reservationDocument.getStatus());
                    existingReservation.setNotes(reservationDocument.getNotes());
                    existingReservation.setDate(reservationDocument.getDate());
                    existingReservation.setTime(reservationDocument.getTime());
                    existingReservation.setCustomerName(reservationDocument.getCustomerName());
                    existingReservation.setPartySize(reservationDocument.getPartySize());


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

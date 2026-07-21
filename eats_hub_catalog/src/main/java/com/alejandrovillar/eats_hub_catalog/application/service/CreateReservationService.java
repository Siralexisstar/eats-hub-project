package com.alejandrovillar.eats_hub_catalog.application.service;

import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationCommand;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.application.port.out.AvailabilityPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.ReservationRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.RestaurantRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.domain.exception.BusinessException;
import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

/* Service orchestrator of the use case
 - ensure business rules
 - completely abstracted
*  */
/**
 * Application service that implements the reservation creation use case.
 * <p>
 * It orchestrates restaurant lookup, business validation, availability checks, domain object
 * creation, and persistence through output ports.
 */
@RequiredArgsConstructor
@Slf4j
/**
 * Application service that implements the reservation creation use case.
 */
public class CreateReservationService implements CreateReservationUseCase {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final RestaurantRepositoryPort restaurantRepositoryPort;
    private final AvailabilityPort availabilityPort;

    /**
     * Creates a pending reservation when the restaurant exists, is open, and has availability.
     *
     * @param command reservation creation command
     * @return created reservation
     */
    @Override
    public Mono<Reservation> create(CreateReservationCommand command) {

        return restaurantRepositoryPort.findById(command.restaurantId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                .doOnNext(value -> log.info("Creating the reservation"))
                .flatMap(restaurant -> {
                    if (!restaurant.isOpenAt(command.time())) {
                        return Mono.error(new BusinessException("Restaurant closed"));
                    }

                    return availabilityPort
                            .isAvailable(
                                    command.restaurantId(),
                                    command.date(),
                                    command.time(),
                                    command.partySize());
                })
                .flatMap(available -> {
                    if (!Boolean.TRUE.equals(available)) {
                        return Mono.error(new BusinessException("Restaurant is not available"));
                    }

                    Reservation reservation = new Reservation(
                            UUID.randomUUID(),
                            command.restaurantId(),
                            command.customerId(),
                            command.customerName(),
                            command.customerEmail(),
                            command.date(),
                            command.time(),
                            command.partySize(),
                            ReservationStatus.PENDING,
                            command.notes()
                    );

                    return reservationRepositoryPort.save(reservation);
                })
                .doOnSuccess(value -> log.info("Reservation created successfully, {}", value.getId()));
    }
}

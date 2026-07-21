package com.alejandrovillar.eats_hub_catalog.application.port.out;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

//Application layer. Application defines the contract and the adapter applies
/**
 * Outbound application port used to verify restaurant availability.
 * <p>
 * The application layer owns this contract; infrastructure adapters provide concrete
 * implementations against external systems.
 */
/**
 * Outbound application port used to verify restaurant availability.
 */
public interface AvailabilityPort {

    /**
     * Checks whether a restaurant can accept a reservation for the given slot.
     *
     * @param restaurantId restaurant identifier
     * @param date reservation date
     * @param time reservation time
     * @param partySize number of guests
     * @return {@code true} when the slot is available; otherwise {@code false}
     */
    Mono<Boolean> isAvailable(
            UUID restaurantId,
            LocalDate date,
            LocalTime time,
            int partySize
    );

}

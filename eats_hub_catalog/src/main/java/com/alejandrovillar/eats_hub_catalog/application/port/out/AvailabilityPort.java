package com.alejandrovillar.eats_hub_catalog.application.port.out;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

//Application layer. Application defines the contract and the adapter applies
public interface AvailabilityPort {

    Mono<Boolean> isAvailable(
            UUID restaurantId,
            LocalDate date,
            LocalTime time,
            int partySize
    );

}

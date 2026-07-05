package com.alejandrovillar.eats_hub_catalog.application.port.in;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import reactor.core.publisher.Mono;


//Controller(entry) uses this interface--> CreateReservationService --> Exit Port
public interface CreateReservationUseCase {
    Mono<Reservation> create(CreateReservationCommand createReservationCommand);
}

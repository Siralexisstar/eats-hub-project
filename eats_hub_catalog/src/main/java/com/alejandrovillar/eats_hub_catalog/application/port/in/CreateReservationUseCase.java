package com.alejandrovillar.eats_hub_catalog.application.port.in;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import reactor.core.publisher.Mono;


//Controller(entry) uses this interface (CreateReservationUseCase)--> CreateReservationService --> Exit Port
//CreateReservationUseCase (entry port) --> CreateReservationCommando (transport data) --> CreatrionService
public interface CreateReservationUseCase {
    Mono<Reservation> create(CreateReservationCommand createReservationCommand);
}

package com.alejandrovillar.eats_hub_catalog.application.port.out;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import reactor.core.publisher.Mono;


//Application layer. Application defines the contract and the adapter applies
public interface ReservationRepositoryPort {

    Mono<Reservation> save(Reservation reservation);
}
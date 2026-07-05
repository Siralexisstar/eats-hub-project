package com.alejandrovillar.eats_hub_catalog.application.port.out;

import com.alejandrovillar.eats_hub_catalog.domain.model.Restaurant;
import reactor.core.publisher.Mono;

import java.util.UUID;

//Application layer. Application defines the contract and the adapter applies
public interface RestaurantRepositoryPort {

    Mono<Restaurant> findById(UUID restaurantId);
}

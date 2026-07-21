package com.alejandrovillar.eats_hub_catalog.application.port.out;

import com.alejandrovillar.eats_hub_catalog.domain.model.Restaurant;
import reactor.core.publisher.Mono;

import java.util.UUID;

//Application layer. Application defines the contract and the adapter applies
/**
 * Outbound application port for retrieving restaurants from a data source.
 */
/**
 * Outbound application port for retrieving restaurants from a data source.
 */
public interface RestaurantRepositoryPort {

    /**
     * Finds a restaurant by its identifier.
     *
     * @param restaurantId restaurant identifier
     * @return restaurant when it exists, or an empty publisher otherwise
     */
    Mono<Restaurant> findById(UUID restaurantId);
}

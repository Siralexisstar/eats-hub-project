package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Business-facing contract for restaurant catalog queries exposed through DTOs.
 */
/**
 * Business-facing contract for restaurant catalog queries exposed through DTOs.
 */
public interface RestaurantBusinessService {

    /**
     * Reads all restaurants as response DTOs.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantResponse> readAll();

    /**
     * Reads restaurants by cuisine type as response DTOs.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantResponse> readByCuisineType(String cuisineType);

    /**
     * Reads a restaurant by name as a response DTO.
     *
     * @return reactive result of the operation
     */
    Mono<RestaurantResponse> readByName(String name);

    /**
     * Reads restaurants matching the provided price ranges.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantResponse> readByPriceRangeIn(List<PriceRange> priceRanges);

    /**
     * Reads restaurants located in the provided city.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantResponse> readByCity(String city);

}

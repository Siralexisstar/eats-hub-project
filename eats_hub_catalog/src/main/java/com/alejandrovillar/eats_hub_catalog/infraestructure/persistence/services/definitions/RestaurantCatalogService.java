package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Persistence-facing contract for restaurant catalog queries.
 */
/**
 * Persistence-facing contract for restaurant catalog queries.
 */
public interface RestaurantCatalogService {

    /**
     * Retrieves all available records.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> getAll();

    /**
     * Retrieves restaurants that match the provided cuisine type.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> getByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    /**
     * Retrieves a restaurant by name prefix.
     *
     * @return reactive result of the operation
     */
    Mono<RestaurantDocument> getRestaurantByName(String name);

    /**
     * Retrieves restaurants that match any of the provided price ranges.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> getRestaurantByPriceRange(List<PriceRange> priceRanges);

    /**
     * Retrieves restaurants located in the provided city.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> getRestaurantByAddressCity(String addressCity);

}

package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantCatalogService {

    Flux<RestaurantDocument> getAll();

    Flux<RestaurantDocument> getByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    Mono<RestaurantDocument> getRestaurantByName(String name);

    Flux<RestaurantDocument> getRestaurantByPriceRange(List<PriceRange> priceRanges);

    Flux<RestaurantDocument> getRestaurantByAddressCity(String addressCity);

}

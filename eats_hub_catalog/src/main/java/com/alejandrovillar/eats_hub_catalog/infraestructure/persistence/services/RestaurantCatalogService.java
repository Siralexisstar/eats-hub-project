package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface RestaurantCatalogService {

    Flux<RestaurantDocument> getByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    Flux<RestaurantDocument> getRestaurantByName(String name);

    Flux<RestaurantDocument> getRestaurantByPriceRange(Collection<PriceRange> priceRanges);

    Flux<RestaurantDocument> getRestaurantByAddressCity(String addressCity);

}

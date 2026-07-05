package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantBusinessService {


    Flux<RestaurantResponse> getAll();

    Flux<RestaurantResponse> getByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    Mono<RestaurantResponse> getRestaurantByName(String name);

    Flux<RestaurantResponse> getRestaurantByPriceRange(List<PriceRange> priceRanges);

    Flux<RestaurantResponse> getRestaurantByAddressCity(String addressCity);

}

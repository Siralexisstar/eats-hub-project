package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RestaurantBusinessService {

    Flux<RestaurantResponse> readAll();

    Flux<RestaurantResponse> readByCuisineType(String cuisineType);

    Mono<RestaurantResponse> readByName(String name);

    Flux<RestaurantResponse> readByPriceRangeIn(List<PriceRange> priceRanges);

    Flux<RestaurantResponse> readByCity(String city);

}

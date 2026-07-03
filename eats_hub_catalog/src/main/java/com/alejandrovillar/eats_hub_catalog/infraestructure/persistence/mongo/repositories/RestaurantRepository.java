package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

//What is the difference btw clasic Spring and Reactive Spring
//Reactive repos instead of returning a List returns a Flux and instead of return an Object returns a Mono
@Repository
public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantDocument, UUID> {

    Flux<RestaurantDocument> findByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    Mono<RestaurantDocument> findByNameStartingWithIgnoreCase(String name);

    Flux<RestaurantDocument> findByPriceRangeIn(List<PriceRange> priceRanges);

    Flux<RestaurantDocument> findByAddressCity(String addressCity);


}

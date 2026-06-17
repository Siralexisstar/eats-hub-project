package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.UUID;

//What is the difference btw clasic Spring and Reactive Spring
//Reactive repos instead of returning a List returns a Flux and instead of return an Object returns a Mono
@Component
public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantDocument, UUID> {

    Flux<RestaurantDocument> findByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    Flux<RestaurantDocument> finByNameStartingWithIgnoreCase(String name);

    Flux<RestaurantDocument> findByPriceRangeIn(Collection<PriceRange> priceRanges);

    Flux<RestaurantDocument> findByAddressCity(String addressCity);
}

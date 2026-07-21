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
/**
 * Reactive MongoDB repository for restaurant documents.
 */
/**
 * Reactive MongoDB repository for restaurant documents.
 */
public interface RestaurantRepository extends ReactiveMongoRepository<RestaurantDocument, UUID> {

    /**
     * Finds restaurants by cuisine type.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> findByCuisineType(String cuisineType);

    //Remember we can use @Query instead of normalized name
    /**
     * Finds a restaurant whose name starts with the provided value ignoring case.
     *
     * @return reactive result of the operation
     */
    Mono<RestaurantDocument> findByNameStartingWithIgnoreCase(String name);

    /**
     * Finds restaurants whose price range is in the provided list.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> findByPriceRangeIn(List<PriceRange> priceRanges);

    /**
     * Finds restaurants by address city.
     *
     * @return reactive result of the operation
     */
    Flux<RestaurantDocument> findByAddressCity(String addressCity);


}

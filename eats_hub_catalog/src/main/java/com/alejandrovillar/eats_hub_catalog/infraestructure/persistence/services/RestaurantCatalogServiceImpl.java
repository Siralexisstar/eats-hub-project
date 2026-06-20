package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.records.Address;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantCatalogServiceImpl implements RestaurantCatalogService {

    private final RestaurantRepository repo;


    @Override
    public Flux<RestaurantDocument> getByCuisineType(String cuisineType) {

        return repo.findByCuisineType(cuisineType)
                .doOnSubscribe(subs -> log.info("Init search with param, {}", cuisineType))
                .doOnNext(value -> log.info("Getting cuisine, {}", value))
                .doOnComplete(() -> log.info("Search completed with exit")) //no necessary
                .onErrorResume(error -> {
                    log.error(error.getMessage(), error);
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantDocument> getRestaurantByName(String name) {
        return repo.finByNameStartingWithIgnoreCase(name)
                .doOnSubscribe(subs -> log.info("Init search with param, {}", name))
                .doOnNext(value -> log.info("Getting restaurant name, {}", value))
                .doOnComplete(() -> log.info("Search completed with exit"))
                .onErrorResume(error -> {
                    log.error(error.getMessage(), error);
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RestaurantDocument> getRestaurantByPriceRange(Collection<PriceRange> priceRanges) {
        return repo.findByPriceRangeIn(priceRanges)
                //Could be used to return cache data
                .switchIfEmpty(Flux.error(new RuntimeException()))
                .doOnSubscribe(value -> log.info("Restaurant is Empty"));
    }


    //Improving this query
    @Override
    public Flux<RestaurantDocument> getRestaurantByAddressCity(String addressCity) {
        return repo.findAll()
                //search all the addresses
                .map(RestaurantDocument::getAddress)
                //validation
                .filter(Objects::nonNull)
                //get al the cities
                .map(Address::city)
                //validation
                .filter(Objects::nonNull)
                .distinct()
                .collectList()
                .flatMapMany(cities -> {
                    if (cities.isEmpty()) {
                        log.error("No restaurant found in city, {}", addressCity);
                        return Flux.empty();
                    }

                    log.info("Restaurant found in city, {}", addressCity);
                    return repo.findByAddressCity(addressCity)
                            .doOnNext(value -> log.info("Restaurant found in city, {}", value));

                })
                .onErrorResume(error -> {
                            log.error(error.getMessage(), error);
                            return Flux.error(new RuntimeException());
                        }
                );
    }
}

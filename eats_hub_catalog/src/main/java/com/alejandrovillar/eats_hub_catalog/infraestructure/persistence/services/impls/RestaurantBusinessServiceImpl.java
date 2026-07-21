package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantBusinessService;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantCatalogService;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import com.alejandrovillar.eats_hub_catalog.interfaces.mappers.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantBusinessServiceImpl implements RestaurantBusinessService {

    private final RestaurantCatalogService restaurantCatalogService;
    private final RestaurantMapper restaurantMapper;

    @Override
    public Flux<RestaurantResponse> readAll() {
        log.info("Reading all restaurants");

        return this.restaurantCatalogService.getAll()
                .transform(this.restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading all restaurants completed"));
    }

    @Override
    public Flux<RestaurantResponse> readByCuisineType(String cuisineType) {
        log.info("Reading restaurants by cuisine type: {}", cuisineType);

        return this.restaurantCatalogService.getByCuisineType(cuisineType)
                .transform(this.restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading restaurants by cuisine type completed"));
    }

    @Override
    public Mono<RestaurantResponse> readByName(String name) {
        log.info("Reading restaurants by name: {}", name);

        return this.restaurantCatalogService.getRestaurantByName(name)
                .transform(this.restaurantMapper::toResponseMono)
                .doOnSuccess(restaurant -> {
                    if (Objects.isNull(restaurant)) {
                        log.info("Reading restaurants by name completed but not found any restaurants");
                    } else {
                        log.info("Reading restaurants by name completed");
                    }
                });
    }

    @Override
    public Flux<RestaurantResponse> readByPriceRangeIn(List<PriceRange> priceRanges) {
        log.info("Reading restaurants by price ranges: {}", priceRanges);

        return this.restaurantCatalogService.getRestaurantByPriceRange(priceRanges)
                .transform(this.restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading restaurants by price ranges completed"));
    }

    @Override
    public Flux<RestaurantResponse> readByCity(String city) {
        log.info("Reading restaurants by city: {}", city);

        return this.restaurantCatalogService.getRestaurantByAddressCity(city)
                .transform(this.restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading restaurants by city completed"));
    }
}

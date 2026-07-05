package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.impls;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantBusinessService;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantCatalogService;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import com.alejandrovillar.eats_hub_catalog.interfaces.mappers.RestaurantMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Slf4j
@RequiredArgsConstructor
public class RestaurantBusinessServiceImpl implements RestaurantBusinessService {

    private final RestaurantCatalogService restaurantCatalogService;
    private final RestaurantMapper restaurantMapper;

    @Override
    public Flux<RestaurantResponse> getAll() {

        return restaurantCatalogService.getAll()
                .transform(restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Reading all restaurants completed"));
    }

    @Override
    public Flux<RestaurantResponse> getByCuisineType(String cuisineType) {
        return restaurantCatalogService
                .getByCuisineType(cuisineType)
                .transform(restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Getting restaurants By Cuisine Type completed"));
    }

    @Override
    public Mono<RestaurantResponse> getRestaurantByName(String name) {
        return restaurantCatalogService
                .getRestaurantByName(name)
                .transform(restaurantMapper::toResponseMono)
                .doOnSuccess(value ->  log.info("Getting restaurants By Cuisine Type completed"));
    }

    @Override
    public Flux<RestaurantResponse> getRestaurantByPriceRange(List<PriceRange> priceRanges) {
        return restaurantCatalogService
                .getRestaurantByPriceRange(priceRanges)
                .transform(restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Getting restaurants By Price Range Type completed"));
    }

    @Override
    public Flux<RestaurantResponse> getRestaurantByAddressCity(String addressCity) {
        return restaurantCatalogService
                .getRestaurantByAddressCity(addressCity)
                .transform(restaurantMapper::toResponseFlux)
                .doOnComplete(() -> log.info("Getting restaurants By Address completed"));
    }
}

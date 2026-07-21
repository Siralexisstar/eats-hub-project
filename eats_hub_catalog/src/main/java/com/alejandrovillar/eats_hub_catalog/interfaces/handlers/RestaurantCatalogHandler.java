package com.alejandrovillar.eats_hub_catalog.interfaces.handlers;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantBusinessService;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Functional WebFlux handler for restaurant catalog endpoints.
 * <p>
 * This component receives functional routing requests, extracts path variables or query
 * parameters, delegates the catalog lookup to {@link RestaurantBusinessService}, and builds
 * JSON {@link ServerResponse} instances containing {@link RestaurantResponse} payloads.
 */
@Component
@RequiredArgsConstructor
public class RestaurantCatalogHandler {

    private final RestaurantBusinessService restaurantBusinessService;

    /**
     * Handles a request to retrieve the full restaurant catalog.
     * <p>
     * The response body is produced from {@link RestaurantBusinessService#readAll()} and serialized
     * as a JSON stream of {@link RestaurantResponse} objects.
     *
     * @param serverRequest incoming WebFlux server request; currently no request data is required
     * @return {@code 200 OK} with the restaurant catalog as JSON, or {@code 404 Not Found} when no
     * response can be produced
     */
    public Mono<ServerResponse> getAllRestaurants(ServerRequest serverRequest) {
        final var restaurantFlux = this.restaurantBusinessService.readAll();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(restaurantFlux, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Handles a request to retrieve a restaurant by its name.
     * <p>
     * The restaurant name is read from the {@code name} path variable and passed to
     * {@link RestaurantBusinessService#readByName(String)}.
     *
     * @param serverRequest incoming WebFlux server request containing the {@code name} path variable
     * @return {@code 200 OK} with the matching restaurant as JSON, or {@code 404 Not Found} when no
     * restaurant matches the provided name
     */
    public Mono<ServerResponse> getRestaurantByName(ServerRequest serverRequest) {
        //We need to collect the pathVarialble
        final var restaurantName = serverRequest.pathVariable("name");
        final var monoResponse = this.restaurantBusinessService.readByName(restaurantName);

        return monoResponse
                .flatMap(restaurantResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(restaurantResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Handles a request to retrieve restaurants by cuisine type.
     * <p>
     * The cuisine type is read from the {@code cuisineType} query parameter and delegated to
     * {@link RestaurantBusinessService#readByCuisineType(String)}.
     *
     * @param serverRequest incoming WebFlux server request containing the optional
     * {@code cuisineType} query parameter
     * @return {@code 200 OK} with matching restaurants as JSON, or {@code 404 Not Found} when no
     * response can be produced
     */
    public Mono<ServerResponse> getRestaurantsByCuisineType(ServerRequest serverRequest) {
        final var cuisineType = serverRequest.queryParam("cuisineType")
                .orElse(null);
        final var fluxResponse = this.restaurantBusinessService.readByCuisineType(cuisineType);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * Handles a request to retrieve restaurants by one or more price ranges.
     * <p>
     * The method reads the {@code prices} query parameter as a comma-separated list, normalizes each
     * item, converts it to {@link PriceRange}, and delegates the lookup to
     * {@link RestaurantBusinessService#readByPriceRangeIn(List)}.
     *
     * @param serverRequest incoming WebFlux server request containing the {@code prices} query
     * parameter, for example {@code CHEAP,MEDIUM}
     * @return {@code 200 OK} with matching restaurants as JSON, or {@code 404 Not Found} when no
     * response can be produced
     */
    public Mono<ServerResponse> getRestaurantBetweenPrice(ServerRequest serverRequest) {
        final var prices = serverRequest.queryParam("prices")
                .orElse(null);


        //there in the query param can arrive like this CHEAP, EXPENSIVE ETC
        final var priceList = Arrays.stream(prices.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(PriceRange::valueOf)
                .toList();


        final var fluxResponse = this.restaurantBusinessService.readByPriceRangeIn(priceList);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    /**
     * Handles a request to retrieve restaurants by city.
     * <p>
     * The city value is read from the {@code city} query parameter and used to obtain the response
     * stream from the restaurant business service.
     *
     * @param serverRequest incoming WebFlux server request containing the optional {@code city}
     * query parameter
     * @return {@code 200 OK} with matching restaurants as JSON, or {@code 404 Not Found} when no
     * response can be produced
     */
    public Mono<ServerResponse> getRestaurantByCity(ServerRequest serverRequest) {
        final var cuisineType = serverRequest.queryParam("city")
                .orElse(null);
        final var fluxResponse = this.restaurantBusinessService.readByCuisineType(cuisineType);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fluxResponse, RestaurantResponse.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}

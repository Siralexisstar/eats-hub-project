package com.alejandrovillar.eats_hub_catalog.handlers;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Functional WebFlux handler placeholder for restaurant catalog endpoints.
 */
public class RestaurantCatalogHandler {

    /**
     * Handles a request to retrieve all restaurants.
     *
     * @param serverRequest incoming WebFlux server request
     * @return a reactive not-implemented response until the endpoint is wired
     */
    public Mono<ServerResponse> getAllRestaurants(ServerRequest serverRequest) {
        return notImplemented();
    }

    /**
     * Handles a request to retrieve a restaurant by name.
     *
     * @param serverRequest incoming WebFlux server request
     * @return a reactive not-implemented response until the endpoint is wired
     */
    public Mono<ServerResponse> getRestaurantByName(ServerRequest serverRequest) {
        return notImplemented();
    }

    /**
     * Handles a request to retrieve restaurants by cuisine type.
     *
     * @param serverRequest incoming WebFlux server request
     * @return a reactive not-implemented response until the endpoint is wired
     */
    public Mono<ServerResponse> getRestaurantsByCuisineType(ServerRequest serverRequest) {
        return notImplemented();
    }

    /**
     * Handles a request to retrieve restaurants by price range.
     *
     * @param serverRequest incoming WebFlux server request
     * @return a reactive not-implemented response until the endpoint is wired
     */
    public Mono<ServerResponse> getRestaurantBetweenPrice(ServerRequest serverRequest) {
        return notImplemented();
    }

    /**
     * Handles a request to retrieve restaurants by city.
     *
     * @param serverRequest incoming WebFlux server request
     * @return a reactive not-implemented response until the endpoint is wired
     */
    public Mono<ServerResponse> getRestaurantByCity(ServerRequest serverRequest) {
        return notImplemented();
    }

    /**
     * Builds the temporary response used by placeholder handler methods.
     *
     * @return HTTP 501 response publisher
     */
    private Mono<ServerResponse> notImplemented() {
        return ServerResponse.status(501).build();
    }
}

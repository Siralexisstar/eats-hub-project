package com.alejandrovillar.eats_hub_catalog.interfaces.routes;

import com.alejandrovillar.eats_hub_catalog.interfaces.handlers.RestaurantCatalogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RestaurantCatalogRouter {


    //Implementing Webflux functional endpoints
    //advantages responsibility separation

    private final static String BASE_URL = "/catalog/restaurants";
    private final static String BY_NAME_URL = BASE_URL + "/{name}";

    //We manage the dependency injection using variables and constructor
    //This is to build the different URL
    @Bean
    public RouterFunction<ServerResponse> routes(RestaurantCatalogHandler handler) {
        return route()
                .GET(BY_NAME_URL, handler::getRestaurantByName)
                .GET(BASE_URL, request -> {
                    if (request.queryParam("cuisineType").isPresent()) {
                        return handler.getRestaurantsByCuisineType(request);
                    }
                    if (request.queryParam("prices").isPresent()) {
                        return handler.getRestaurantBetweenPrice(request);
                    }
                    if (request.queryParam("city").isPresent()) {
                        return handler.getRestaurantByCity(request);
                    } else return handler.getAllRestaurants(request);
                }).build();
    }
}

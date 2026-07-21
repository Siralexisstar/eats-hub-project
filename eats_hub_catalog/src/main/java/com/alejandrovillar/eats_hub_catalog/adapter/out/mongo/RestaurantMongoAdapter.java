package com.alejandrovillar.eats_hub_catalog.adapter.out.mongo;


/* The idea here it's:
*  CreateReservationService
        |
        v
RestaurantRepositoryPort
        |
        v
RestaurantMongoAdapter
        |
        v
RestaurantRepository Mongo actual
*/

import com.alejandrovillar.eats_hub_catalog.application.port.out.RestaurantRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.domain.model.Restaurant;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantMongoAdapter implements RestaurantRepositoryPort {

    private final RestaurantRepository restaurantRepository;


    @Override
    public Mono<Restaurant> findById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(this::toDomain);

    }

    private Restaurant toDomain(RestaurantDocument restaurantDocument) {

        String[] hours = restaurantDocument.getOpenHours().split("-");
        return new Restaurant(restaurantDocument.getId(), restaurantDocument.getName(), LocalTime.parse(hours[0]),
                LocalTime.parse(hours[1]));
    }
}

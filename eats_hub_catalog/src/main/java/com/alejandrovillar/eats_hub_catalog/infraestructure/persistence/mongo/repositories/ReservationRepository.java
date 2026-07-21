package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

//Same new Repository extends from ReactiveRepo and <T, Identifier>
public interface ReservationRepository extends ReactiveMongoRepository<ReservationDocument, UUID> {

    //find by id
    Mono<ReservationDocument> findById (String restaurantId);

    //find byId and status
    Flux<ReservationDocument> findByRestaurantIdAndStatus(String restaurantId, ReservationStatus status);
}

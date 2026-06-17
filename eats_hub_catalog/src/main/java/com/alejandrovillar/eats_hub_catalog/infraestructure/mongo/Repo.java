package com.alejandrovillar.eats_hub_catalog.infraestructure.mongo;

import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.models.RestaurantDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

//first parameter RestaurantDocument
//Second type parameter UUID
@Component
public interface Repo extends ReactiveMongoRepository<RestaurantDocument, UUID> {

}

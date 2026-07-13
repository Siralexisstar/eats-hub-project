package com.alejandrovillar.eats_hub_catalog.adapter.out.mongo;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantMongoAdapterTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantMongoAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RestaurantMongoAdapter(restaurantRepository);
    }

    @Test
    void shouldFindRestaurantMappingDocumentToDomain() {
        UUID restaurantId = UUID.randomUUID();

        RestaurantDocument document = RestaurantDocument.builder()
                .id(restaurantId)
                .name("La Parrilla Moderna")
                .openHours("12:00-23:00")
                .build();

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Mono.just(document));

        StepVerifier.create(adapter.findById(restaurantId))
                .assertNext(restaurant -> {
                    assertEquals(restaurantId, restaurant.getId());
                    assertEquals("La Parrilla Moderna", restaurant.getName());
                    assertTrue(restaurant.isOpenAt(LocalTime.of(20, 30)));
                })
                .verifyComplete();
    }
}

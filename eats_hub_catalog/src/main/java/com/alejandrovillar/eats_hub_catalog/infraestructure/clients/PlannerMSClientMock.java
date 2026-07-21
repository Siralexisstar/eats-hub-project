package com.alejandrovillar.eats_hub_catalog.infraestructure.clients;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
/**
 * Mock client that simulates calls to an external planner microservice.
 */
/**
 * Mock client that simulates calls to an external planner microservice.
 */
public class PlannerMSClientMock {

    //The final approach for this class is to simulate a always full reservation restaurant
    //Like a new microservice --> In the future will be
    private static final String UNAVAILABLE_RESTAURANT_ID = "dfcbe98d-392b-4b93-9a49-27005223d15d";


    //With this method qwe can check the avaliability of the place
    /**
     * Simulates an availability check against a planner service.
     *
     * @param date reservation date as text
     * @param time reservation time as text
     * @param restaurantId restaurant identifier
     * @return {@code true} when the configured mock restaurant is not blocked
     */
    public Mono<Boolean> verifyAvailability(String date, String time, UUID restaurantId) {

        return Mono.fromCallable(() -> !UNAVAILABLE_RESTAURANT_ID.equals(restaurantId.toString()))
                .delayElement(this.getRandomDuration())
                .doOnNext(reservation -> log.info("Searching avaliabilty fo restaurant {}, date {}, time {}",
                        restaurantId, date, time));

    }

    //Simulating a time lapse response
    /**
     * Generates a random artificial delay used to emulate network latency.
     *
     * @return random delay duration
     */
    public Duration getRandomDuration() {
        final var randomInt = ThreadLocalRandom.current().nextInt(20, 1000);
        return Duration.ofMillis(randomInt);
    }
}

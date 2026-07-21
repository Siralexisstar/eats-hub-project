package com.alejandrovillar.eats_hub_catalog.adapter.out.planner;

import com.alejandrovillar.eats_hub_catalog.application.port.out.AvailabilityPort;
import com.alejandrovillar.eats_hub_catalog.infraestructure.clients.PlannerMSClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
/**
 * Planner outbound adapter that checks restaurant availability through the mock planner client.
 */
/**
 * Planner outbound adapter that checks availability through the mock planner client.
 */
public class PlannerAvailabilityAdapter implements AvailabilityPort {

    private final PlannerMSClientMock plannerMSClientMock;
    @Override
    public Mono<Boolean> isAvailable(UUID restaurantId, LocalDate date, LocalTime time, int partySize) {
        return plannerMSClientMock.verifyAvailability(date.toString(), time.toString(), restaurantId);
    }
}

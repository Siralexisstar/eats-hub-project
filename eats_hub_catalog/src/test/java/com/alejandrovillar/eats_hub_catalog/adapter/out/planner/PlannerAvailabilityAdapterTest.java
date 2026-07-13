package com.alejandrovillar.eats_hub_catalog.adapter.out.planner;

import com.alejandrovillar.eats_hub_catalog.infraestructure.clients.PlannerMSClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerAvailabilityAdapterTest {

    @Mock
    private PlannerMSClientMock plannerMSClientMock;

    private PlannerAvailabilityAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PlannerAvailabilityAdapter(plannerMSClientMock);
    }

    @Test
    void shouldDelegateAvailabilityCheckToPlannerClient() {
        UUID restaurantId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 7, 14);
        LocalTime time = LocalTime.of(20, 30);

        when(plannerMSClientMock.verifyAvailability("2026-07-14", "20:30", restaurantId))
                .thenReturn(Mono.just(true));

        StepVerifier.create(adapter.isAvailable(restaurantId, date, time, 2))
                .expectNext(true)
                .verifyComplete();

        verify(plannerMSClientMock).verifyAvailability("2026-07-14", "20:30", restaurantId);
    }
}

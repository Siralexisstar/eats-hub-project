package com.alejandrovillar.eats_hub_catalog.adapter.in.web.controllers;

import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationCommand;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
/**
 * Unit test for the reactive reservation controller.
 */
/**
 * Unit test for the reactive reservation controller.
 */
class ReservationControllerTest {

    @Mock
    private CreateReservationUseCase createReservationUseCase;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToController(new ReservationController(createReservationUseCase))
                .build();
    }

    @Test
    void shouldCreateReservation() {
        UUID restaurantId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();

        Reservation reservation = new Reservation(
                reservationId,
                restaurantId,
                "customer-1",
                "Alex",
                "alex@example.com",
                LocalDate.of(2026, 7, 14),
                LocalTime.of(20, 30),
                2,
                ReservationStatus.PENDING,
                "Window table"
        );

        when(createReservationUseCase.create(any(CreateReservationCommand.class)))
                .thenReturn(Mono.just(reservation));

        String requestBody = """
                {
                  "restaurantId": "%s",
                  "customerId": "customer-1",
                  "customerName": "Alex",
                  "customerEmail": "alex@example.com",
                  "date": "2026-07-14",
                  "time": "20:30",
                  "partySize": 2,
                  "notes": "Window table"
                }
                """.formatted(restaurantId);

        webTestClient.post()
                .uri("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(reservationId.toString())
                .jsonPath("$.restaurantId").isEqualTo(restaurantId.toString())
                .jsonPath("$.customerName").isEqualTo("Alex")
                .jsonPath("$.date").isEqualTo("2026-07-14")
                .jsonPath("$.time").isEqualTo("20:30:00")
                .jsonPath("$.partySize").isEqualTo(2)
                .jsonPath("$.status").isEqualTo("PENDING")
                .jsonPath("$.notes").isEqualTo("Window table");

        ArgumentCaptor<CreateReservationCommand> commandCaptor =
                ArgumentCaptor.forClass(CreateReservationCommand.class);

        verify(createReservationUseCase).create(commandCaptor.capture());

        CreateReservationCommand command = commandCaptor.getValue();

        assertEquals(restaurantId, command.restaurantId());
        assertEquals("customer-1", command.customerId());
        assertEquals("Alex", command.customerName());
        assertEquals("alex@example.com", command.customerEmail());
        assertEquals(LocalDate.of(2026, 7, 14), command.date());
        assertEquals(LocalTime.of(20, 30), command.time());
        assertEquals(2, command.partySize());
        assertEquals("Window table", command.notes());
    }
}

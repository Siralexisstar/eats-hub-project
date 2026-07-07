package com.alejandrovillar.eats_hub_catalog.application.service;

import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationCommand;
import com.alejandrovillar.eats_hub_catalog.application.port.out.AvailabilityPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.ReservationRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.RestaurantRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.domain.exception.BusinessException;
import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.domain.model.Restaurant;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReservationServiceTest {

    @Mock
    private ReservationRepositoryPort reservationRepository;

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private AvailabilityPort availabilityPort;

    private CreateReservationService service;

    @BeforeEach
    void setUp() {
        service = new CreateReservationService(
                reservationRepository,
                restaurantRepository,
                availabilityPort
        );
    }

    @Test
    void shouldCreatePendingReservationWhenRestaurantIsOpenAndAvailable() {
        UUID restaurantId = UUID.randomUUID();

        CreateReservationCommand command = new CreateReservationCommand(
                restaurantId,
                "customer-1",
                "Alex",
                "alex@example.com",
                LocalDate.of(2026, 7, 10),
                LocalTime.of(20, 30),
                2,
                "Window table"
        );

        Restaurant restaurant = new Restaurant(
                restaurantId,
                "La Parrilla Moderna",
                LocalTime.of(12, 0),
                LocalTime.of(23, 0)
        );

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Mono.just(restaurant));

        when(availabilityPort.isAvailable(
                restaurantId,
                command.date(),
                command.time(),
                command.partySize()
        )).thenReturn(Mono.just(true));

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation ->
                        Mono.just(invocation.getArgument(0))
                );

        StepVerifier.create(service.create(command))
                .assertNext(reservation -> {
                    assertNotNull(reservation.getId());
                    assertEquals(restaurantId, reservation.getRestaurantId());
                    assertEquals(ReservationStatus.PENDING, reservation.getStatus());
                    assertEquals(2, reservation.getPartySize());
                })
                .verifyComplete();

        verify(restaurantRepository).findById(restaurantId);
        verify(availabilityPort).isAvailable(
                restaurantId,
                command.date(),
                command.time(),
                command.partySize()
        );
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldFailWhenRestaurantDoesNotExist() {
        UUID restaurantId = UUID.randomUUID();

        CreateReservationCommand command = createCommand(
                restaurantId,
                LocalTime.of(20, 30)
        );

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.create(command))
                .expectError(ResourceNotFoundException.class)
                .verify();

        verifyNoInteractions(availabilityPort);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    void shouldFailWhenRestaurantIsClosed() {
        UUID restaurantId = UUID.randomUUID();

        CreateReservationCommand command = createCommand(
                restaurantId,
                LocalTime.of(23, 30)
        );

        Restaurant restaurant = new Restaurant(
                restaurantId,
                "La Parrilla Moderna",
                LocalTime.of(12, 0),
                LocalTime.of(23, 0)
        );

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Mono.just(restaurant));

        StepVerifier.create(service.create(command))
                .expectErrorMatches(error ->
                        error instanceof BusinessException
                                && error.getMessage().equals("Restaurant closed")
                )
                .verify();

        verifyNoInteractions(availabilityPort);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    void shouldFailWhenRestaurantHasNoAvailability() {
        UUID restaurantId = UUID.randomUUID();

        CreateReservationCommand command = createCommand(
                restaurantId,
                LocalTime.of(20, 30)
        );

        Restaurant restaurant = new Restaurant(
                restaurantId,
                "La Parrilla Moderna",
                LocalTime.of(12, 0),
                LocalTime.of(23, 0)
        );

        when(restaurantRepository.findById(restaurantId))
                .thenReturn(Mono.just(restaurant));

        when(availabilityPort.isAvailable(
                restaurantId,
                command.date(),
                command.time(),
                command.partySize()
        )).thenReturn(Mono.just(false));

        StepVerifier.create(service.create(command))
                .expectErrorMatches(error ->
                        error instanceof BusinessException
                                && error.getMessage().equals("Restaurant is not available")
                )
                .verify();

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }


    private CreateReservationCommand createCommand(
            UUID restaurantId,
            LocalTime time
    ) {
        return new CreateReservationCommand(
                restaurantId,
                "customer-1",
                "Alex",
                "alex@example.com",
                LocalDate.of(2026, 7, 10),
                time,
                2,
                "Window table"
        );
    }
}
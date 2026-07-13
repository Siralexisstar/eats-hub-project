package com.alejandrovillar.eats_hub_catalog.adapter.out.mongo;

import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationMongoAdapterTest {

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationMongoAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ReservationMongoAdapter(reservationRepository);
    }

    @Test
    void shouldSaveReservationMappingDomainToDocumentAndBackToDomain() {
        UUID reservationId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

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

        when(reservationRepository.save(any(ReservationDocument.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(adapter.save(reservation))
                .assertNext(savedReservation -> {
                    assertEquals(reservationId, savedReservation.getId());
                    assertEquals(restaurantId, savedReservation.getRestaurantId());
                    assertEquals("customer-1", savedReservation.getCustomerId());
                    assertEquals("Alex", savedReservation.getCustomerName());
                    assertEquals("alex@example.com", savedReservation.getCustomerEmail());
                    assertEquals(LocalDate.of(2026, 7, 14), savedReservation.getDate());
                    assertEquals(LocalTime.of(20, 30), savedReservation.getTime());
                    assertEquals(2, savedReservation.getPartySize());
                    assertEquals(ReservationStatus.PENDING, savedReservation.getStatus());
                    assertEquals("Window table", savedReservation.getNotes());
                })
                .verifyComplete();

        ArgumentCaptor<ReservationDocument> documentCaptor =
                ArgumentCaptor.forClass(ReservationDocument.class);

        verify(reservationRepository).save(documentCaptor.capture());

        ReservationDocument document = documentCaptor.getValue();

        assertEquals(reservationId, document.getId());
        assertEquals(restaurantId.toString(), document.getRestaurantId());
        assertEquals("customer-1", document.getCustomerId());
        assertEquals("Alex", document.getCustomerName());
        assertEquals("alex@example.com", document.getCustomerEmail());
        assertEquals("2026-07-14", document.getDate());
        assertEquals("20:30", document.getTime());
        assertEquals(2, document.getPartySize());
        assertEquals(
                com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus.PENDING,
                document.getStatus()
        );
        assertEquals("Window table", document.getNotes());
    }
}

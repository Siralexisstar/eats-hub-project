package com.alejandrovillar.eats_hub_catalog.adapter.out.mongo;

import com.alejandrovillar.eats_hub_catalog.application.port.out.ReservationRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


//adapter implements port. Connect the contract with real technology
//Port contract
//adapter connection
//repo X final tech implementation
@Component
@RequiredArgsConstructor
public class ReservationMongoAdapter implements ReservationRepositoryPort {

    private final ReservationRepository reservationRepository;


    @Override
    public Mono<Reservation> save(Reservation reservation) {
        return reservationRepository
                //para guardar mapeamos al objeto de bbdd
                .save(toDocument(reservation))
                //Volvemos a retornar el objeto de dominio
                .map(this::toDomain);
    }

    private ReservationDocument toDocument(Reservation reservation) {
        return ReservationDocument.builder()
                .id(reservation.getId())
                .restaurantId(reservation.getRestaurantId().toString())
                .customerId(reservation.getCustomerId())
                .customerName(reservation.getCustomerName())
                .customerEmail(reservation.getCustomerEmail())
                .date(reservation.getDate().toString())
                .time(reservation.getTime().toString())
                .partySize(reservation.getPartySize())
                .status(toDocumentStatus(reservation))
                .notes(reservation.getNotes())
                .build();
    }

    private Reservation toDomain(ReservationDocument document) {
        return new Reservation(
                document.getId(),
                UUID.fromString(document.getRestaurantId()),
                document.getCustomerId(),
                document.getCustomerName(),
                document.getCustomerEmail(),
                LocalDate.parse(document.getDate()),
                LocalTime.parse(document.getTime()),
                document.getPartySize(),
                toDomainStatus(document),
                document.getNotes()
        );
    }

    private ReservationStatus toDocumentStatus(
            Reservation reservation
    ) {
        return ReservationStatus.valueOf(
                reservation.getStatus().name()
        );
    }

    private com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus toDomainStatus(
            ReservationDocument document
    ) {
        return com.alejandrovillar.eats_hub_catalog.domain.model.ReservationStatus.valueOf(
                document.getStatus().name()
        );
    }
}

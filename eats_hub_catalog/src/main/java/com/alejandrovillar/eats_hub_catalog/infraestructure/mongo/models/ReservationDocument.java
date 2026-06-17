package com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.models;

import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDocument {

    //We put our Ids with UUID
    @Id
    private UUID id;

    @Indexed
    private String restaurantId;

    private String customerId;

    private String customerName;

    private String customerEmail;

    private LocalDateTime time;

    private Integer partySize;

    @Indexed
    private ReservationStatus status;

    private String notes;



}

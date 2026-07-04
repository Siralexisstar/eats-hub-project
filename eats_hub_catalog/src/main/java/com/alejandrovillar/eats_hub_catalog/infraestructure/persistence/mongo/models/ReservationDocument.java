package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDocument {

    @Id
    private UUID id;
    @Indexed
    private String restaurantId;
    private String customerId;
    //@Field("name")
    private String customerName;
    private String customerEmail;
    private String date;
    private String time;
    private Integer partySize;
    @Indexed
    private ReservationStatus status;

    private String notes;



}

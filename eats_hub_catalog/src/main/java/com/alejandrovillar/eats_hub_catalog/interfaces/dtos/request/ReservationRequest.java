package com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Legacy reservation request DTO used by the persistence-oriented service layer.
 */
public class ReservationRequest {

    private String restaurantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String dateTime; // example 2025-06-16,15:30
    private Integer partySize;
    private String comment;
}

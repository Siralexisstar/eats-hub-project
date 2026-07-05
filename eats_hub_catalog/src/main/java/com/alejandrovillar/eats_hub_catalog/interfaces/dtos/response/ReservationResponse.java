package com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private String restaurantId;
    private String customerName;
    private String dateTime; // example 2025-06-16,15:30
    private Integer partySize;
    private String comment;
}

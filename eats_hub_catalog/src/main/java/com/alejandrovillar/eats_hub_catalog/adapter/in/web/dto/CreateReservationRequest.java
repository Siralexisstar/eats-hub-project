package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateReservationRequest(

        UUID restaurantId,
        String customerId,
        String customerName,
        String customerEmail,
        LocalDate date,
        LocalTime time,
        int partySize,
        String notes) {
}

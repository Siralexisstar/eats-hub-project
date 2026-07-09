package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateReservationResponse(
        UUID id,
        UUID restaurantId,
        String customerName,
        LocalDate date,
        LocalTime time,
        Integer partySize,
        String status,
        String notes
) {
}

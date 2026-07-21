package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateReservationResponse(
        UUID id,
        UUID restaurantId,
        String customerName,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime time,
        Integer partySize,
        String status,
        String notes
) {
}

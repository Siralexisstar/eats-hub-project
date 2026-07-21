package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Response returned after a reservation is created.
 *
 * @param id generated reservation identifier
 * @param restaurantId reserved restaurant identifier
 * @param customerName customer display name
 * @param date reservation date formatted as ISO date
 * @param time reservation time formatted as HH:mm:ss
 * @param partySize number of guests
 * @param status current reservation status
 * @param notes optional reservation notes
 */
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

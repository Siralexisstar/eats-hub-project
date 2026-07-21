package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Request payload used to create a reservation through the REST API.
 *
 * @param restaurantId restaurant identifier to reserve
 * @param customerId external customer identifier
 * @param customerName customer display name
 * @param customerEmail customer email address
 * @param date reservation date
 * @param time reservation time
 * @param partySize number of guests
 * @param notes optional reservation notes
 */
/**
 * Request payload used to create a reservation through the REST API.
 *
 * @param restaurantId restaurant identifier to reserve
 * @param customerId external customer identifier
 * @param customerName customer display name
 * @param customerEmail customer email address
 * @param date reservation date
 * @param time reservation time
 * @param partySize number of guests
 * @param notes optional reservation notes
 */
public record CreateReservationRequest(

        @NotNull(message = "Restaurant id is required")
        UUID restaurantId,

        @NotBlank(message = "Customer id is required")
        String customerId,

        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Customer email must be valid")
        String customerEmail,

        @NotNull(message = "Reservation date is required")
        LocalDate date,

        @NotNull(message = "Reservation time is required")
        LocalTime time,

        @Positive(message = "Party size must be greater than zero")
        int partySize,

        String notes
) {
}
package com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

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
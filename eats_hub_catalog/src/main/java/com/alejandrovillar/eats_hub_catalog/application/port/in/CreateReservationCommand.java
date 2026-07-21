package com.alejandrovillar.eats_hub_catalog.application.port.in;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


//Int port. The entry point of the exterior
public record CreateReservationCommand(
        UUID restaurantId,
        String customerId,
        String customerName,
        String customerEmail,
        LocalDate date,
        LocalTime time,
        int partySize,
        String notes
) {
}

package com.alejandrovillar.eats_hub_catalog.application.port.in;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


//Int port. The entry point of the exterior
/**
 * Application command that carries all data required to create a reservation.
 * <p>
 * Commands decouple inbound adapters from the use-case implementation.
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

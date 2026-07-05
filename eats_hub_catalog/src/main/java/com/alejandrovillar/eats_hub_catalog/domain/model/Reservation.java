package com.alejandrovillar.eats_hub_catalog.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


//Business Class
public class Reservation {

    private final UUID id;
    private final UUID restaurantId;
    private final String customerId;
    private final String customerName;
    private final String customerEmail;
    private final LocalDate date;
    private final LocalTime time;
    private final Integer partySize;
    private final ReservationStatus status;
    private final String notes;

    public Reservation(
            UUID id,
            UUID restaurantId,
            String customerId,
            String customerName,
            String customerEmail,
            LocalDate date,
            LocalTime time,
            Integer partySize,
            ReservationStatus status,
            String notes
    ) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.status = status;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Integer getPartySize() {
        return partySize;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }
}

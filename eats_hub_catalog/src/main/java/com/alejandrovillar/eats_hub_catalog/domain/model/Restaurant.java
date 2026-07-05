package com.alejandrovillar.eats_hub_catalog.domain.model;

import java.time.LocalTime;
import java.util.UUID;

public class Restaurant {

    private final UUID id;
    private final String name;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public Restaurant(
            UUID id,
            String name,
            LocalTime openingTime,
            LocalTime closingTime
    ) {
        this.id = id;
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public boolean isOpenAt(LocalTime time) {
        return !time.isBefore(openingTime)
                && time.isBefore(closingTime);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
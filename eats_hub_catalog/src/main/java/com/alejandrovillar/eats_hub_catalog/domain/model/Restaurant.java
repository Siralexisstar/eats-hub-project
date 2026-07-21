package com.alejandrovillar.eats_hub_catalog.domain.model;

import java.time.LocalTime;
import java.util.UUID;

/**
 * Domain model that represents a restaurant and its opening window.
 */
public class Restaurant {

    private final UUID id;
    private final String name;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    /**
     * Creates a restaurant domain object.
     *
     * @param id restaurant identifier
     * @param name restaurant name
     * @param openingTime opening time
     * @param closingTime closing time
     */
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

    /**
     * Returns whether the restaurant is open at the given time.
     *
     * @param time time to check
     * @return {@code true} when the time is inside the restaurant opening interval
     */
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
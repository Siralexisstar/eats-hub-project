package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.records;

import lombok.Builder;

@Builder
/**
 * Embedded value object that stores restaurant address data.
 */
public record Address(
        String street,
        String city,
        String postalCode) {
}

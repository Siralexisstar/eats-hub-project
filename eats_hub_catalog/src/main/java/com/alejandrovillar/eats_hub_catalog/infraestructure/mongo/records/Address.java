package com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.records;

import lombok.Builder;

@Builder
public record Address(
        String street,
        String city,
        String postalCode) {
}

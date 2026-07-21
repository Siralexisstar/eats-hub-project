package com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.records.Address;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.records.ContactInfo;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
/**
 * Restaurant response DTO containing catalog data and aggregated rating information.
 */
/**
 * Restaurant response DTO containing catalog data and aggregated rating information.
 */
public class RestaurantResponse {

    private String name;
    private Address address;
    private String cuisineType;
    private PriceRange priceRange;
    private String openHours;
    private String logoUrl;
    private String closeAt;
    private ContactInfo contactInfo;
    private Double globalRating;
}

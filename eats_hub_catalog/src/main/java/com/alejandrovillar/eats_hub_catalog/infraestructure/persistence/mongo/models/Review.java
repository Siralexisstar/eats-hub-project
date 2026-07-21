package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


//Class inside the RestaurantDocument
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Embedded MongoDB document that represents a restaurant review.
 */
/**
 * Embedded MongoDB document that represents a restaurant review.
 */
public class Review {

    private Object customerId;
    private String customerName;
    private Integer rating;
    private String comment;
    private Instant timestamp;
}

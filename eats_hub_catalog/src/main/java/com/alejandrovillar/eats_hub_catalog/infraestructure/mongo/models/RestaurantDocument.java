package com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.models;

import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.enums.PriceRange;
import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.records.Address;
import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.records.ContactInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDocument {

    //We put our Ids with UUID
    @Id
    private UUID id;

    @Indexed
    private String name;

    private Address address;

    @Indexed
    private String cuisineType;

    @Indexed
    private PriceRange priceRange;

    private String openHours;

    private String logoUrl;

    private ContactInfo contactInfo;

    private Review review;


}

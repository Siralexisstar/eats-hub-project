package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.records;

import lombok.Builder;

//Why we made a Builder
//We are using java 21 so we have to use the specific caractheristics of the 21 version
//Reason for that we made this in that way
//Embedded object in the JSON
//Practcally are Constants (dificult to change in the future)
//Inspired in Kotlin --> dataclases
//equals, haashcode, setters, getters --> lo tiene embebido
//inmutables --> we can't set anything. Only for read no for write



@Builder
/**
 * Embedded value object that stores restaurant contact information.
 */
/**
 * Embedded value object that stores restaurant contact information.
 *
 * @param iphone restaurant phone number
 * @param email restaurant email
 * @param website restaurant website URL
 */
public record ContactInfo(
        String iphone,
        String email,
        String website
) {
}

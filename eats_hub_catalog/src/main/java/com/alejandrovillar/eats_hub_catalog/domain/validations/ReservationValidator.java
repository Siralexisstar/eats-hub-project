package com.alejandrovillar.eats_hub_catalog.domain.validations;


import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

//Class to configure the diferents validations
//This class ensures logic business validatons
//The @GlobalExcpetionHandler only transforms the ouput
@Component
public class ReservationValidator {

    public <T> Mono<Void> applydaValipdations(T input, List<BusinessValidator<T>> validations) {

        return null;
    }

    public BusinessValidator<ReservationDocument> validateRestaurantNotClosed(ReservationDocument reservation) {
        return null;
    }

    public BusinessValidator<ReservationDocument> validateAvailability(ReservationDocument reservation) {
        return null;

    }

    public BusinessValidator<ReservationDocument> validateRestaurantIdBeforeUpdate(ReservationDocument reservation) {
        return null;
    }

    //method to see if a restaurant is closed or not
    private boolean isRestaurantClosed(RestaurantDocument restaurantDocument, String reservationTime) {

        if (Objects.isNull(restaurantDocument.getCloseAt()) || Objects.isNull(reservationTime)) return true;

        LocalTime closeTimeLocalTime = LocalTime.parse(restaurantDocument.getCloseAt(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime reservationTimeLocalTime = LocalTime.parse(reservationTime, DateTimeFormatter.ofPattern("HH:mm"));

        //If is after it's closed
        return reservationTimeLocalTime.isAfter(closeTimeLocalTime);

    }
}

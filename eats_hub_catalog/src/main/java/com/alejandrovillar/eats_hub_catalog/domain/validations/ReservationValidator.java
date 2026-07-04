package com.alejandrovillar.eats_hub_catalog.domain.validations;


import com.alejandrovillar.eats_hub_catalog.domain.exception.BusinessException;
import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.infraestructure.clients.PlannerMSClientMock;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//Class to configure the diferents validations
//This class ensures logic business validatons
//The @GlobalExcpetionHandler only transforms the ouput
@Component
@AllArgsConstructor
public class ReservationValidator {

    private final RestaurantRepository restaurantRepository;
    private final PlannerMSClientMock plannerMSClientMock;

    public <T> Mono<Void> applyValidations(T input, List<BusinessValidator<T>> validations) {

        return null;
    }

    //Validate the restaurant it's not closed for sure
    public BusinessValidator<ReservationDocument> validateRestaurantNotClosed() {

        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .flatMap(restaurant -> {
                        if (this.isRestaurantClosed(restaurant, reservation.getTime())) {
                            return Mono.error(new BusinessException("Restaurant already closed"));
                        }

                        return Mono.empty();
                    });
        };
    }

    //Validate the viability for the restaurant
    public BusinessValidator<ReservationDocument> validateAvailability() {
        return reservation -> {

            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .then(plannerMSClientMock.verifyAvailability(reservation.getDate(), reservation.getTime(), restaurantId))
                    .flatMap(isAvailability -> {
                        if (isAvailability) {
                            return Mono.error(new ResourceNotFoundException("Restaurant is not avaliable"));
                        }
                        return Mono.empty();
                    });
        };
    }

    //TODO: REVIEW THIS NOT MUCH HAPPY WITH  THIS VALIDATION
    public BusinessValidator<ReservationDocument> validateRestaurantIdBeforeUpdate() {

        return reservation -> {

            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .flatMap(restaurant -> {
                        if (!restaurant.getId().equals(UUID.fromString(reservation.getRestaurantId()))) {
                            return Mono.error(new BusinessException("Restaurant ID must be the same as the original restaurant"));
                        }
                        return Mono.empty();
                    });
        };
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

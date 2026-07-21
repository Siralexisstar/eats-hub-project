package com.alejandrovillar.eats_hub_catalog.domain.validations;


import com.alejandrovillar.eats_hub_catalog.domain.exception.BusinessException;
import com.alejandrovillar.eats_hub_catalog.domain.exception.ResourceNotFoundException;
import com.alejandrovillar.eats_hub_catalog.infraestructure.clients.PlannerMSClientMock;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.repositories.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@AllArgsConstructor
/**
 * Component that groups reactive business validations for reservation documents.
 */
/**
 * Component that groups reactive business validations for reservation documents.
 */
public class ReservationValidator {

    private final RestaurantRepository restaurantRepository;
    private final PlannerMSClientMock plannerMSClientMock;


    //Method to reduce the validations
    //How it works? Read all the validations and detect what it is. Then throws the error.
    /**
     * Applies all validators sequentially to the provided input.
     *
     * @param input object to validate
     * @param validations validators to execute
     * @param <T> validated object type
     * @return empty completion when all validations pass
     */
    public <T> Mono<Void> applyValidations(T input, List<BusinessValidator<T>> validations) {

        if (validations.isEmpty()) {
            return Mono.empty();
        }

        return validations.stream()
                .reduce(
                        Mono.empty(),
                        (chain, validator) -> chain.then(validator.validate(input)),
                        Mono::then
                );
    }

    //Validate the restaurant it's not closed for sure
    /**
     * Builds a validator that verifies that the restaurant is open for the requested time.
     *
     * @return validator for restaurant opening hours
     */
    public BusinessValidator<ReservationDocument> validateRestaurantNotClosed() {

        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .doOnNext(value -> log.info("Validating woorking hours for the Restaurante, {}", value.getName()))
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .flatMap(restaurant -> {
                        if (this.isRestaurantClosed(restaurant, reservation.getTime())) {
                            return Mono.error(new BusinessException("Sorry the Restaurant it's closed for " +
                                    "the scheduled time"));
                        }

                        return Mono.empty();
                    });
        };
    }

    //Validate the viability for the restaurant
    /**
     * Builds a validator that verifies planner availability for the reservation.
     *
     * @return validator for restaurant availability
     */
    public BusinessValidator<ReservationDocument> validateAvailability() {
        return reservation -> {

            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .doOnNext(value -> log.info("Validating the availabilityy of the restaurant"))
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .then(plannerMSClientMock.verifyAvailability(reservation.getDate(), reservation.getTime(), restaurantId))
                    .flatMap(isAvailability -> {
                        if (!isAvailability) {
                            return Mono.error(new ResourceNotFoundException("Restaurant is not avaliable"));
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

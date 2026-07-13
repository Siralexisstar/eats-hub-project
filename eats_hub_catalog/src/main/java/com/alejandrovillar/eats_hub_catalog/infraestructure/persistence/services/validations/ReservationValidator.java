package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.validations;

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

@Component
@Slf4j
@AllArgsConstructor
public class ReservationValidator {

    private final RestaurantRepository restaurantRepository;
    private final PlannerMSClientMock plannerMSClientMock;

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

    public BusinessValidator<ReservationDocument> validateRestaurantNotClosed() {
        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .doOnNext(value -> log.info("Validating working hours for restaurant {}", value.getName()))
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .flatMap(restaurant -> {
                        if (this.isRestaurantClosed(restaurant, reservation.getTime())) {
                            return Mono.error(new BusinessException("Restaurant closed"));
                        }

                        return Mono.empty();
                    });
        };
    }

    public BusinessValidator<ReservationDocument> validateAvailability() {
        return reservation -> {
            final var restaurantId = UUID.fromString(reservation.getRestaurantId());

            return restaurantRepository.findById(restaurantId)
                    .doOnNext(value -> log.info("Validating availability of restaurant {}", value.getName()))
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Restaurant not found")))
                    .then(plannerMSClientMock.verifyAvailability(reservation.getDate(), reservation.getTime(), restaurantId))
                    .flatMap(isAvailable -> {
                        if (!isAvailable) {
                            return Mono.error(new BusinessException("Restaurant is not available"));
                        }
                        return Mono.empty();
                    });
        };
    }

    private boolean isRestaurantClosed(RestaurantDocument restaurantDocument, String reservationTime) {
        if (Objects.isNull(restaurantDocument.getCloseAt()) || Objects.isNull(reservationTime)) {
            return true;
        }

        LocalTime closeTime = LocalTime.parse(
                restaurantDocument.getCloseAt(),
                DateTimeFormatter.ofPattern("HH:mm")
        );
        LocalTime reservationLocalTime = LocalTime.parse(
                reservationTime,
                DateTimeFormatter.ofPattern("HH:mm")
        );

        return reservationLocalTime.isAfter(closeTime);
    }
}

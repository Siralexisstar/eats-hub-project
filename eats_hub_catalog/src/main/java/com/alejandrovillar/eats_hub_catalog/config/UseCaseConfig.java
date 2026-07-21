package com.alejandrovillar.eats_hub_catalog.config;

import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.application.port.out.AvailabilityPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.ReservationRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.RestaurantRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.service.CreateReservationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for application use cases.
 * <p>
 * This class wires input ports to their concrete application-service implementations while keeping
 * the domain and application layers independent from infrastructure details.
 */
@Configuration
/**
 * Spring configuration that wires application use cases with their required ports.
 */
public class UseCaseConfig {

    /**
     * Creates the reservation use-case bean with the required output ports.
     *
     * @param reservationRepositoryPort port used to persist reservations
     * @param restaurantRepositoryPort port used to retrieve restaurants
     * @param availabilityPort port used to check restaurant availability
     * @return configured reservation creation use case
     */
    /**
     * Creates the reservation creation use-case bean.
     *
     * @param reservationRepositoryPort port used to persist reservations
     * @param restaurantRepositoryPort port used to retrieve restaurants
     * @param availabilityPort port used to check availability
     * @return configured reservation creation use case
     */
    @Bean
    /**
     * Creates a reservation using the provided request or document.
     *
     * @return reactive result of the operation
     */
    public CreateReservationUseCase createReservationUseCase(
            ReservationRepositoryPort reservationRepositoryPort,
            RestaurantRepositoryPort restaurantRepositoryPort,
            AvailabilityPort availabilityPort
    ) {
        return new CreateReservationService(
                reservationRepositoryPort,
                restaurantRepositoryPort,
                availabilityPort
        );
    }
}



package com.alejandrovillar.eats_hub_catalog.config;

import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.application.port.out.AvailabilityPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.ReservationRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.port.out.RestaurantRepositoryPort;
import com.alejandrovillar.eats_hub_catalog.application.service.CreateReservationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
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



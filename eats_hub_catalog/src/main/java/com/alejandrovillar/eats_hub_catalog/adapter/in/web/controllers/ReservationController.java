package com.alejandrovillar.eats_hub_catalog.adapter.in.web.controllers;


import com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto.CreateReservationRequest;
import com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto.CreateReservationResponse;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationCommand;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Reactive REST controller that exposes reservation endpoints.
 * <p>
 * The controller acts as an inbound adapter in the hexagonal architecture: it converts HTTP DTOs
 * into application commands and maps domain results back to HTTP responses.
 */
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
/**
 * Reactive REST controller that exposes reservation endpoints.
 */
public class ReservationController {

    /* We should call to this interface to maintain the hexag. architecuture
    *
    * Controller / Runner / Test
                |
                v
        CreateReservationUseCase
                |
                v
        CreateReservationService
                |
                v
              Puertos
                |
                v
        Adapters concretos
    * */
    private final CreateReservationUseCase createReservationUseCase;

    /* Good Practice have the controller wit public instead private */
    /**
     * Creates a reservation from the HTTP request payload.
     *
     * @param request validated reservation request body
     * @return a reactive response containing the created reservation data
     */
    /**
     * Creates a reservation from the HTTP request payload.
     *
     * @param request validated reservation request body
     * @return reactive response containing the created reservation data
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Reservation")
    /**
     * Creates a reservation using the provided request or document.
     *
     * @return reactive result of the operation
     */
    public Mono<CreateReservationResponse> createReservationResponseMono(@Valid @RequestBody CreateReservationRequest request) {

        //first we need to transform to command
        // second transform to response
        // at least x2 mappers
        return Mono.just(request)
                .map(this::toCommand)
                .flatMap(createReservationUseCase::create)
                .map(this::toResponse);
    }

    //Mappers

    /**
     * Maps an HTTP request DTO to the application command consumed by the use case.
     *
     * @param request inbound reservation request
     * @return command with the reservation data required by the application layer
     */
    private CreateReservationCommand toCommand(CreateReservationRequest request) {

        return new CreateReservationCommand(
                request.restaurantId(),
                request.customerId(),
                request.customerName(),
                request.customerEmail(),
                request.date(),
                request.time(),
                request.partySize(),
                request.notes()
        );
    }


    /**
     * Maps a domain reservation into the outbound HTTP response DTO.
     *
     * @param reservation created reservation domain object
     * @return response DTO returned to the client
     */
    private CreateReservationResponse toResponse(Reservation reservation) {

        return new CreateReservationResponse(
                reservation.getId(),
                reservation.getRestaurantId(),
                reservation.getCustomerName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getPartySize(),
                reservation.getStatus().name(),
                reservation.getNotes()
        );
    }

}

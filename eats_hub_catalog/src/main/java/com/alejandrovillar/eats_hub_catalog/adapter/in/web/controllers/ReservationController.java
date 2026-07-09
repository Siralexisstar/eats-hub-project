package com.alejandrovillar.eats_hub_catalog.adapter.in.web.controllers;


import com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto.CreateReservationRequest;
import com.alejandrovillar.eats_hub_catalog.adapter.in.web.dto.CreateReservationResponse;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationCommand;
import com.alejandrovillar.eats_hub_catalog.application.port.in.CreateReservationUseCase;
import com.alejandrovillar.eats_hub_catalog.domain.model.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a Reservation")
    public  Mono<CreateReservationResponse> createReservationResponseMono(@RequestBody Mono<CreateReservationRequest> request) {

        //first we need to transform to command
        // second transform to response
        // at least x2 mappers
        return request.map(this::toCommand)
                .flatMap(createReservationUseCase::create)
                .map(this::toResponse);
    }

    //Mappers

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

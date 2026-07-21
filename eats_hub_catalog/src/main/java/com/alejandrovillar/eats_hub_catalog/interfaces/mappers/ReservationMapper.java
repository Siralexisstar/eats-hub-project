package com.alejandrovillar.eats_hub_catalog.interfaces.mappers;


import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.ReservationDocument;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.request.ReservationRequest;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.ReservationResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org  .mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Maps reservation data between API DTOs and MongoDB persistence documents.
 *
 * <p>MapStruct generates the implementation and registers it as a Spring bean.</p>
 */
@Mapper(componentModel = "spring")
public interface ReservationMapper {

    /**
     * Converts a reservation document into an API response.
     * The separate persistence fields {@code date} and {@code time} are combined
     * into {@code dateTime}, while {@code notes} is exposed as {@code comment}.
     *
     * @param document reservation retrieved from MongoDB
     * @return reservation data prepared for the API response
     */
    @Mapping(target = "comment", source = "notes")
    @Mapping(
            target = "dateTime",
            expression = "java(document.getDate() + \",\" + document.getTime())"
    )
    ReservationResponse toResponse(ReservationDocument document);

    /**
     * Converts an API request into a persistence document.
     * Identifier and status are left unset for the persistence and business layers.
     * Date and time are populated by {@link #splitDateTime(ReservationRequest, ReservationDocument)}.
     *
     * @param request reservation data received by the API
     * @return reservation document ready for further processing
     */
    @Mapping(target = "notes", source = "comment", defaultValue = "")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "status", ignore = true)
    ReservationDocument toDocument(ReservationRequest request);

    /**
     * Converts each reservation document emitted by a {@link Flux}.
     *
     * @param document stream of reservation documents
     * @return stream containing the mapped reservation responses
     */
    default Flux<ReservationResponse> toResponseFlux(Flux<ReservationDocument> document) {
        return document.map(this::toResponse);
    }

    /**
     * Converts the reservation document emitted by a {@link Mono}.
     *
     * @param document reactive container with zero or one reservation document
     * @return reactive container with the mapped reservation response
     */
    default Mono<ReservationResponse> toResponseMono(Mono<ReservationDocument> document) {
        return document.map(this::toResponse);
    }

    /**
     * Converts each reservation request emitted by a {@link Flux}.
     *
     * @param requests stream of reservation requests
     * @return stream containing the mapped persistence documents
     */
    default Flux<ReservationDocument> toDocumentFlux(Flux<ReservationRequest> requests) {
        return requests.map(this::toDocument);
    }

    /**
     * Converts the reservation request emitted by a {@link Mono}.
     *
     * @param requestMono reactive container with zero or one reservation request
     * @return reactive container with the mapped persistence document
     */
    default Mono<ReservationDocument> toDocumentMono(Mono<ReservationRequest> requestMono) {
        return requestMono.map(this::toDocument);
    }


    /**
     * Splits the request's {@code dateTime} value into the persistence fields
     * {@code date} and {@code time} after MapStruct completes the main mapping.
     *
     * @param request source API request
     * @param document mapped document that receives the date and time
     */
    @AfterMapping
    default void splitDateTime(
            ReservationRequest request,
            @MappingTarget ReservationDocument document
    ) {
        if (Objects.nonNull(request.getDateTime())
                && request.getDateTime().contains(",")) {

            var dateTimeParts = request.getDateTime().split(",", 2);

            document.setDate(dateTimeParts[0].trim());
            document.setTime(dateTimeParts[1].trim());
        }
    }
}

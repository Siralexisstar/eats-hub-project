package com.alejandrovillar.eats_hub_catalog.interfaces.mappers;


import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.RestaurantDocument;
import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.mongo.models.Review;
import com.alejandrovillar.eats_hub_catalog.interfaces.dtos.response.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * Maps restaurant persistence documents to the DTOs exposed by the API.
 *
 * <p>MapStruct generates the implementation and registers it as a Spring bean.</p>
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    /**
     * Converts a restaurant document into an API response.
     * The global rating is calculated from the document's reviews.
     *
     * @param document restaurant data retrieved from MongoDB
     * @return restaurant data prepared for the API response
     */
    @Mapping(target = "globalRating", expression = "java(calculateAverageRating(document.getReviews()))")
    RestaurantResponse toResponse(RestaurantDocument document);

    /**
     * Converts each restaurant document emitted by a {@link Flux}.
     *
     * @param document stream of restaurant documents
     * @return stream containing the mapped restaurant responses
     */
    default Flux<RestaurantResponse> toResponseFlux(Flux<RestaurantDocument> document) {
        return document.map(this::toResponse);
    }

    /**
     * Converts the restaurant document emitted by a {@link Mono}.
     *
     * @param document reactive container with zero or one restaurant document
     * @return reactive container with the mapped restaurant response
     */
    default Mono<RestaurantResponse> toResponseMono(Mono<RestaurantDocument> document) {
        return document.map(this::toResponse);
    }

    /**
     * Calculates the average of all non-null review ratings.
     *
     * @param reviews reviews associated with a restaurant
     * @return the average rating, or {@code 0.0} when no valid ratings exist
     */
    default Double calculateAverageRating(List<Review> reviews) {
        if (Objects.isNull(reviews) || reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .map(Review::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }


}

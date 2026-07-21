package com.alejandrovillar.eats_hub_catalog.domain.validations;

import reactor.core.publisher.Mono;

/**
 * Functional contract for asynchronous business validators.
 *
 * @param <T> type of object validated by the implementation
 */
@FunctionalInterface
public interface BusinessValidator<T> {

    /**
     * Validates the provided input.
     *
     * @param input object to validate
     * @return empty completion when valid, or an error publisher when invalid
     */
    Mono<Void> validate(T input);
}

package com.alejandrovillar.eats_hub_catalog.domain.validations;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessValidator<T> {

    Mono<Void> validate(T input);
}

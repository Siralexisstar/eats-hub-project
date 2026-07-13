package com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.validations;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessValidator<T> {

    Mono<Void> validate(T input);
}

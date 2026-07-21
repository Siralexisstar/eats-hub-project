package com.alejandrovillar.eats_hub_catalog.domain.exception;

/**
 * Exception thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends RuntimeException {
  /**
   * Creates a new not-found exception with a human-readable message.
   *
   * @param message description of the missing resource
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}

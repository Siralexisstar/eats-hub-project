package com.alejandrovillar.eats_hub_catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Eats Hub Catalog Spring Boot application.
 * <p>
 * The application exposes reactive catalog and reservation capabilities backed by MongoDB.
 */
@SpringBootApplication
public class EatsHubCatalogApplication {

    /**
     * Starts the Spring Boot application context.
     *
     * @param args command-line arguments provided by the runtime
     */
    public static void main(String[] args) {
        SpringApplication.run(EatsHubCatalogApplication.class, args);
    }
}
package com.alejandrovillar.eats_hub_catalog;

import com.alejandrovillar.eats_hub_catalog.infraestructure.mongo.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class EatsHubCatalogApplication implements CommandLineRunner {

    @Autowired
    private Repo repo;

    public static void main(String[] args) {
        SpringApplication.run(EatsHubCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        this.repo
                .findAll() //findAll restaurants
                .doOnNext(value -> log.info("Printing restaurant, {}", value))
                .doOnError(error -> log.info("Error", error))
                .subscribe();

    }
}

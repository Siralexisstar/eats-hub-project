package com.alejandrovillar.eats_hub_catalog;

import com.alejandrovillar.eats_hub_catalog.infraestructure.persistence.services.definitions.RestaurantCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class EatsHubCatalogApplication implements CommandLineRunner {

    @Autowired
    private RestaurantCatalogService repo;

    public static void main(String[] args) {
        SpringApplication.run(EatsHubCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {


        this.repo
                .getRestaurantByAddressCity("Austin")
                //Always remember to subscribe when get all the results
                .doOnNext(value ->  log.info("Printing values, {}", value))
                .subscribe();

    }
}

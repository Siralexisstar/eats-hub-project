package com.alejandrovillar.eats_hub_catalog.infraestructure.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * OpenAPI configuration for documenting the catalog API.
 */
/**
 * OpenAPI configuration for documenting the catalog API.
 */
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Product Service API")
                .description("API documentation for the Product Service")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("Alejandro Garcia Villar")
                        .email("alexgarciavillar96@gmail.com"))
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
  }
}

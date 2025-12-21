package com.ticketbooking.inventoryservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryServiceAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                .title("Inventory Service API")
                .description("Inventory Service API for ticket-booking-service")
                .version("v1.0.0"));
    }
}

package com.ticketbooking.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class InventoryServiceRoute {

    private final static RestClient restClient = RestClient.create();

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(){
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(request, "venueId",
                                "http://localhost:8080/api/v1/inventory/venue/"))

                .route(RequestPredicates.path("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(request, "eventId",
                                "http://localhost:8080/api/v1/inventory/event/"))
                .build();
    }

    public static ServerResponse forwardWithPathVariable(ServerRequest request,
                                                         String variableName,
                                                         String baseUrl){
        String value = request.pathVariable(variableName);

        // Call the Inventory Service
        String responseBody = restClient.get()
                .uri(baseUrl + value)
                .retrieve()
                .body(String.class);

        return ServerResponse.ok().body(responseBody);
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceApiDocs() {
        return GatewayRouterFunctions.route("inventory-service-api-doc")
                .route(RequestPredicates.path("/docs/inventoryservice/v3/api-docs"),
                        HandlerFunctions.http())
                .filter(FilterFunctions.setPath("/v3/api-docs"))
                .before(BeforeFilterFunctions.uri("http://localhost:8080"))
                .build();
    }
}

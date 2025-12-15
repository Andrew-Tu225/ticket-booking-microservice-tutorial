package com.ticketbooking.inventoryservice.response;

import com.ticketbooking.inventoryservice.model.Venue;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class EventInventoryResponse {
    private Long eventId;
    private String event;
    private Long capacity;
    private Venue venue;
    private BigDecimal ticketPrice;
}

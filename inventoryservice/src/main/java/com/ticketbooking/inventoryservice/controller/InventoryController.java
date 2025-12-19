package com.ticketbooking.inventoryservice.controller;

import com.ticketbooking.inventoryservice.response.EventInventoryResponse;
import com.ticketbooking.inventoryservice.response.VenueInventoryResponse;
import com.ticketbooking.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @GetMapping("inventory/events")
    public @ResponseBody List<EventInventoryResponse> inventoryGetAllEvents(){
        return inventoryService.getAllEvents();
    }

    @GetMapping("inventory/venue/{venueId}")
    public VenueInventoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId){
        return inventoryService.getVenueById(venueId);
    }

    @GetMapping("inventory/event/{eventId}")
    public EventInventoryResponse inventoryByEventId(@PathVariable("eventId") Long eventId){
        return inventoryService.getEventById(eventId);
    }

    @PutMapping("inventory/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(@PathVariable("eventId") Long eventId,
                                                    @PathVariable("capacity") Long ticketBooked){
        inventoryService.updateEventCapacity(eventId, ticketBooked);
        return ResponseEntity.ok().build();
    }
}

package com.ticketbooking.inventoryservice.repository;

import com.ticketbooking.inventoryservice.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue,Long> {
}

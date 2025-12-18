package com.ticketbooking.bookingservice.repository;

import com.ticketbooking.bookingservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

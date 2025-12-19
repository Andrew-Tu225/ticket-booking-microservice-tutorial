package com.ticketbooking.orderservice.repository;

import com.ticketbooking.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

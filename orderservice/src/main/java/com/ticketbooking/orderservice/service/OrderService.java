package com.ticketbooking.orderservice.service;

import com.ticketbooking.bookingservice.event.BookingEvent;
import com.ticketbooking.orderservice.client.InventoryServiceClient;
import com.ticketbooking.orderservice.model.Order;
import com.ticketbooking.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent){
        log.info("Received Order Event: {}", bookingEvent);

        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        inventoryServiceClient.updateEventCapacity(bookingEvent.getEventId(), bookingEvent.getTicketCount());
    }

    private Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .totalPrice(bookingEvent.getTotalPrice())
                .ticketCount(bookingEvent.getTicketCount())
                .build();
    }

}

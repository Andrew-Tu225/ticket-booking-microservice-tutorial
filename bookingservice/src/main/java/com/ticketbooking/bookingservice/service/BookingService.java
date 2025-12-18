package com.ticketbooking.bookingservice.service;

import com.ticketbooking.bookingservice.client.InventoryServiceClient;
import com.ticketbooking.bookingservice.event.BookingEvent;
import com.ticketbooking.bookingservice.model.Customer;
import com.ticketbooking.bookingservice.repository.CustomerRepository;
import com.ticketbooking.bookingservice.request.BookingRequest;
import com.ticketbooking.bookingservice.response.BookingResponse;
import com.ticketbooking.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(CustomerRepository customerRepository,
                          InventoryServiceClient inventoryServiceClient,
                          KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(BookingRequest bookingRequest) {
        Customer customer = customerRepository.findById(bookingRequest.getUserId()).orElse(null);
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }
        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(bookingRequest.getEventId());
        log.info("Inventory Response: {}", inventoryResponse);
        if(inventoryResponse.getCapacity() < bookingRequest.getTicketCount()){
            throw new RuntimeException("not enough tickets");
        }
        BookingEvent bookingEvent = createBookingEvent(customer, bookingRequest, inventoryResponse);
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking event sent to kafka: {}", bookingEvent);
        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(Customer customer,
                                            BookingRequest bookingRequest,
                                            InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(bookingRequest.getEventId())
                .ticketCount(bookingRequest.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(bookingRequest.getTicketCount())))
                .build();
    }
}

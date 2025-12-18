package com.ticketbooking.bookingservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VenueResponse {
    @JsonProperty("id")
    private Long venueId;
    @JsonProperty("name")
    private String venueName;
    @JsonProperty("totalCapacity")
    private Long totalCapacity;
}

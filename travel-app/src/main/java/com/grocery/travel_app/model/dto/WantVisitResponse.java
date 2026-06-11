package com.grocery.travel_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WantVisitResponse {
    private  Long id;
    private UserResponse user;
    private DestinationDto destination;
}

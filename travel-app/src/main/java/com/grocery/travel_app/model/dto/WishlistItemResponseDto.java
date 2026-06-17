package com.grocery.travel_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemResponseDto {
    private  Long id;
    private UserResponseDto user;
    private DestinationDto destination;
}

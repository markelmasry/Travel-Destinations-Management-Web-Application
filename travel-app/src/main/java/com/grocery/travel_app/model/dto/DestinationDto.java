package com.grocery.travel_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationDto {
    private Long id;
    private String country;
    private String capital;
    private String region;
    private Long population;
    private String currency;
    private String flagImageUrl;
}

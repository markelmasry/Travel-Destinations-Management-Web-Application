package com.grocery.travel_app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionsResponseDto {
    private List<DestinationDto> suggestions;
    private String totalResults;
    private String response;
}
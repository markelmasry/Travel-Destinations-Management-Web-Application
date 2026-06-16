package com.grocery.travel_app.mapper;

import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.dto.SuggestionsResponse;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SuggestionsResponseMapper {

    public SuggestionsResponse mapToResponse(List<DestinationDto> suggestionsList) {
        String message = suggestionsList.isEmpty() ? "No matches found." : "Successfully fetched suggestions from REST Countries API";
        return SuggestionsResponse.builder()
                .suggestions(suggestionsList)
                .totalResults(String.valueOf(suggestionsList.size()))
                .response(message)
                .build();
    }
}
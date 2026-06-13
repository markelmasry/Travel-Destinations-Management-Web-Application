package com.grocery.travel_app.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.grocery.travel_app.model.dto.DestinationDto;
import org.springframework.stereotype.Component;

@Component
public class RestCountriesParser {

    /**
     * Parses a raw JSON node from the REST Countries API into a DestinationDto.
     * * @param node A single country JsonNode from the API response array
     * @return A fully built DestinationDto
     */
    public DestinationDto parseJsonNode(JsonNode node) {

        // 1. Parse Name
        String name = node.path("names").path("common").asText("Unknown");

        // 2. Parse Capital (Handles array structure)
        String capital = "N/A";
        JsonNode capitalsNode = node.path("capitals");
        if (capitalsNode.isArray() && !capitalsNode.isEmpty()) {
            capital = capitalsNode.get(0).path("name").asText("N/A");
        }

        // 3. Parse Region & Population
        String region = node.path("region").asText("N/A");
        Long population = node.path("population").asLong(0L);

        // 4. Parse Currency (Handles dynamic keys)
        String currency = "N/A";
        JsonNode currenciesNode = node.path("currencies");
        if (!currenciesNode.isMissingNode() && currenciesNode.fieldNames().hasNext()) {
            String firstCurrencyKey = currenciesNode.fieldNames().next();
            currency = currenciesNode.path(firstCurrencyKey).path("name").asText("N/A");
        }

        // 5. Parse Flag URL
        String flagUrl = node.path("flag").path("url_png").asText("https://via.placeholder.com/150");

        // 6. Build and Return DTO
        return DestinationDto.builder()
                .country(name)
                .capital(capital)
                .region(region)
                .population(population)
                .currency(currency)
                .flagImageUrl(flagUrl)
                .build();
    }
}
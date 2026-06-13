package com.grocery.travel_app.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.travel_app.exception.ExternalApiException;
import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.parser.RestCountriesParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestCountriesClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; // Spring automatically provides this bean
    private final RestCountriesParser restCountriesParser;

    public List<DestinationDto> fetchCountriesByName(String countryName) {
        String apiUrl = "https://api.restcountries.com/countries/v5/name?q=" + countryName;
        List<DestinationDto> suggestionsList = new ArrayList<>();

        try {
            log.info("Client sending request to REST Countries API for: {}", countryName);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer rc_live_demo");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> apiResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
            String rawJson = apiResponse.getBody();

            JsonNode rootNode = objectMapper.readTree(rawJson);
            JsonNode dataArray = rootNode.path("data");

            if (dataArray.isArray()) {
                for (JsonNode node : dataArray) {
                    suggestionsList.add(restCountriesParser.parseJsonNode(node));
                }
            }

            return suggestionsList;

        } catch (Exception e) {
            log.error("REST Countries API communication failed for keyword '{}'", countryName, e);
            throw new ExternalApiException("The external country suggestion service is currently unavailable. Please try again later.", e);
        }
    }
}
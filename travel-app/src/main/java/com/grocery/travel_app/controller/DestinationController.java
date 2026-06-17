package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.dto.SuggestionsResponseDto;
import com.grocery.travel_app.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping("/{id}")
    ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id) {
        log.info(">>> API call: Fetch destination by ID {}", id);
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }

    @GetMapping("/search")
    ResponseEntity<Page<DestinationDto>> searchDestinations(@RequestParam String country, Pageable pageable) {
        log.info(">>> API call: Search destinations by country '{}'", country);
        return ResponseEntity.ok(destinationService.searchDestinations(country, pageable));
    }

    @GetMapping("/suggestions")
    ResponseEntity<SuggestionsResponseDto> fetchSuggestions(@RequestParam String country) {
        log.info(">>> API call: Fetch suggestions for '{}'", country);
        return ResponseEntity.ok(destinationService.fetchSuggestionsFromApi(country));
    }

    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@Valid @RequestBody DestinationDto destinationDto) {
        log.info(">>> API call: Create new destination '{}'", destinationDto.getCountry());
        DestinationDto created = destinationService.createDestination(destinationDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    ResponseEntity<List<DestinationDto>> createDestinationsBulk(@Valid @RequestBody List<DestinationDto> destinationDtos) {
        log.info(">>> API call: Bulk create {} destinations", destinationDtos.size());
        List<DestinationDto> created = destinationService.createDestinationsBulk(destinationDtos);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info(">>> API call: Delete destination ID {}", id);
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        log.info(">>> API call: Fetch all destinations");
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }
}
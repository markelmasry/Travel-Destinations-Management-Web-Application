package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.dto.SuggestionsResponse;
import com.grocery.travel_app.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DestinationController {
    private final DestinationService destinationService;

    @GetMapping("/{id}")
    ResponseEntity<DestinationDto> getDestinationById(@PathVariable Long id){
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }
    @GetMapping("/search")
    ResponseEntity<Page<DestinationDto>> searchDestinations(@RequestParam String country, Pageable pageable){
        return ResponseEntity.ok(destinationService.searchDestinations(country, pageable));
    }
    @GetMapping("/suggestions")
    ResponseEntity<SuggestionsResponse> fetchSuggestions(@RequestParam String country){
        return ResponseEntity.ok(destinationService.fetchSuggestionsFromApi(country));
    }
    @PostMapping
    public ResponseEntity<DestinationDto> createDestination(@RequestBody DestinationDto destinationDto) {
        DestinationDto created = destinationService.createDestination(destinationDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PostMapping("/bulk")
    ResponseEntity<List<DestinationDto>> createDestinationsBulk(@RequestBody List<DestinationDto> destinationDtos){
       List<DestinationDto>  created = destinationService.createDestinationsBulk(destinationDtos);
       return new ResponseEntity<>(created,HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<DestinationDto>> getAllDestinations() {
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }

}

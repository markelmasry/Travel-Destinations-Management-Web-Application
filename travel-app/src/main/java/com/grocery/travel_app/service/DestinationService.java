package com.grocery.travel_app.service;

import com.grocery.travel_app.client.RestCountriesClient;
import com.grocery.travel_app.mapper.SuggestionsResponseMapper;
import com.grocery.travel_app.model.dto.SuggestionsResponseDto;
import com.grocery.travel_app.exception.BadRequestException;
import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.ResourceNotFoundException;
import com.grocery.travel_app.mapper.DestinationMapper;
import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.entity.Destination;
import com.grocery.travel_app.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;
    private final SuggestionsResponseMapper suggestionsResponseMapper;
    private final RestCountriesClient restCountriesClient;

    @Transactional(readOnly = true)
    public DestinationDto getDestinationById(Long id) {
        return destinationRepository.findById(id)
                .map(destinationMapper::toDestinationDto)
                .orElseThrow(() -> {
                    log.warn("Destination not found with id: {}", id);
                    return new ResourceNotFoundException("Destination not found with id: " + id);
                });
    }

    @Transactional
    public DestinationDto createDestination(DestinationDto destinationDto){
        if(destinationDto == null) throw new BadRequestException("Destination data cannot be null");

        if (destinationRepository.existsByCountry(destinationDto.getCountry())) {
            log.warn("Duplicate destination creation attempt: {}", destinationDto.getCountry());
            throw new DuplicateResourceException("Destination with country " + destinationDto.getCountry() + " already exists.");
        }

        Destination destination = destinationMapper.toDestinationEntity(destinationDto);
        Destination savedDestination = destinationRepository.save(destination);
        log.info("Successfully created new destination: {}", savedDestination.getCountry());
        return destinationMapper.toDestinationDto(savedDestination);
    }

    @Transactional
    public void deleteDestination(Long id){
        if(!destinationRepository.existsById(id)){
            log.warn("Delete failed: Destination not found with id: {}", id);
            throw new ResourceNotFoundException("Destination not found with id: " + id);
        }
        destinationRepository.deleteById(id);
        log.info("Successfully deleted destination with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<DestinationDto> searchDestinations(String country, Pageable pageable){
        Page<Destination> destinations = destinationRepository.findByCountryContainingIgnoreCase(country, pageable);
        return destinations.map(destinationMapper::toDestinationDto);
    }

    @Transactional(readOnly = true)
    public SuggestionsResponseDto fetchSuggestionsFromApi(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            throw new BadRequestException("Search query cannot be empty.");
        }
        log.info("Fetching suggestions from external API for: {}", countryName);
        List<DestinationDto> suggestionsList = restCountriesClient.fetchCountriesByName(countryName);
        return suggestionsResponseMapper.mapToResponse(suggestionsList);
    }

    @Transactional(readOnly = true)
    public Destination getDestinationEntityById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));
    }

    @Transactional
    public List<DestinationDto> createDestinationsBulk(List<DestinationDto> destinationDtos) {
        if (destinationDtos == null || destinationDtos.isEmpty()) {
            throw new BadRequestException("List of destinations cannot be null or empty.");
        }
        log.info("Processing bulk creation of {} destinations", destinationDtos.size());
        return destinationDtos.stream()
                .map(this::createDestination)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DestinationDto> getAllDestinations() {
        return destinationRepository.findAll()
                .stream()
                .map(destinationMapper::toDestinationDto)
                .toList();
    }
}
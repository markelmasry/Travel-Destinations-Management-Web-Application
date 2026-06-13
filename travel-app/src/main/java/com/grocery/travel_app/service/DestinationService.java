package com.grocery.travel_app.service;

import com.grocery.travel_app.client.RestCountriesClient;
import com.grocery.travel_app.mapper.SuggestionsResponseMapper;
import com.grocery.travel_app.model.dto.SuggestionsResponse;
import com.grocery.travel_app.exception.BadRequestException;
import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.ResourceNotFoundException;
import com.grocery.travel_app.mapper.DestinationMapper;
import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.entity.Destination;
import com.grocery.travel_app.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;
    private final SuggestionsResponseMapper suggestionsResponseMapper;
    private final RestCountriesClient restCountriesClient;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional(readOnly = true)
    public DestinationDto getDestinationById(Long id) {
         return destinationRepository.findById(id)
                 .map(destinationMapper::toDestinationDto)
                 .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));
    }
    @Transactional
    public DestinationDto createDestination(DestinationDto destinationDto){
        if(destinationDto==null) throw new BadRequestException("Destination data cannot be null");
        if (destinationRepository.existsByCountry(destinationDto.getCountry())) {
            throw new DuplicateResourceException("Destination with country " + destinationDto.getCountry() + " already exists.");
        }
        Destination destination = destinationMapper.toDestinationEntity(destinationDto);
        Destination savedDestination = destinationRepository.save(destination);
        return destinationMapper.toDestinationDto(savedDestination);
    }
    @Transactional
    public void deleteDestination(Long id){
        if(!destinationRepository.existsById(id)){
            throw new ResourceNotFoundException("Destination not found with id: " + id);
        }
        eventPublisher.publishEvent(id);
        destinationRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public Page<DestinationDto> searchDestinations(String country, Pageable pageable){
        Page<Destination> destinations = destinationRepository.findByCountryContainingIgnoreCase(country, pageable);
        return destinations.map(destinationMapper::toDestinationDto);
    }
    @Transactional(readOnly = true)
    public SuggestionsResponse fetchSuggestionsFromApi(String countryName) {
        // 1. Validate Business Rule
        if (countryName == null || countryName.isBlank()) {
            throw new BadRequestException("Search query cannot be empty.");
        }
        // 2. Fetch Data via Client
        List<DestinationDto> suggestionsList = restCountriesClient.fetchCountriesByName(countryName);
        // 3. Map and Return Response
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
        return destinationDtos.stream()
                .map(this::createDestination)
                .toList();
    }
}

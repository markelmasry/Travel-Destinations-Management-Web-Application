package com.grocery.travel_app.mapper;

import com.grocery.travel_app.model.dto.DestinationDto;
import com.grocery.travel_app.model.entity.Destination;
import org.springframework.stereotype.Component;

@Component
public class DestinationMapper {
    public DestinationDto toDestinationDto(Destination destination) {
        if(destination ==null) return null;
        return DestinationDto.builder()
                .id(destination.getId())
                .country(destination.getCountry())
                .capital(destination.getCapital())
                .region(destination.getRegion())
                .population(destination.getPopulation())
                .currency(destination.getCurrency())
                .flagImageUrl(destination.getFlagImageUrl())
                .build();
    }
    public Destination toDestinationEntity(DestinationDto destinationDto) {
        if(destinationDto == null) return null;
        return Destination.builder()
                .id(destinationDto.getId())
                .country(destinationDto.getCountry())
                .capital(destinationDto.getCapital())
                .region(destinationDto.getRegion())
                .population(destinationDto.getPopulation())
                .currency(destinationDto.getCurrency())
                .flagImageUrl(destinationDto.getFlagImageUrl())
                .build();
    }
}

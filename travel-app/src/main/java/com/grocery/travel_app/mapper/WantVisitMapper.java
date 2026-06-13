package com.grocery.travel_app.mapper;

import com.grocery.travel_app.model.dto.WantVisitResponse;
import com.grocery.travel_app.model.entity.WantVisit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WantVisitMapper {
    // Injecting the specific mappers we need!
    private final UserMapper userMapper;
    private final DestinationMapper destinationMapper;

    public WantVisitResponse toWantVisitResponse(WantVisit wantvisit ){
        if (wantvisit == null) return null;
        return WantVisitResponse.builder()
                .id(wantvisit.getId())
                .user(userMapper.toUserResponse(wantvisit.getUser()))
                .destination(destinationMapper.toDestinationDto((wantvisit.getDestination())))
                .build();
    }
    public List<WantVisitResponse> toWantVisitResponseList(List<WantVisit> wantVisits) {
        if (wantVisits == null) {
            return null;
        }
        return wantVisits.stream()
                .map(this::toWantVisitResponse)
                .collect(Collectors.toList());
    }

}

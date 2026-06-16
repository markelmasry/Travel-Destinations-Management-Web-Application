package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.ResourceNotFoundException;
import com.grocery.travel_app.mapper.WantVisitMapper;
import com.grocery.travel_app.model.dto.WantVisitResponse;
import com.grocery.travel_app.model.entity.Destination;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.model.entity.WantVisit;
import com.grocery.travel_app.repository.WantVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WantVisitService {

    private final WantVisitRepository wantVisitRepository;
    private final DestinationService destinationService;
    private final UserService userService;
    private final WantVisitMapper wantVisitMapper;

    @Transactional
    public WantVisitResponse addToVisitList(Long destinationId, Long userId) {

        Destination destination = destinationService.getDestinationEntityById(destinationId);
        User user = userService.getUserEntityById(userId);
        if (wantVisitRepository.existsByDestinationIdAndUserId(destinationId, userId)) {
            throw new DuplicateResourceException("This destination is already in your bucket list.");
        }
        WantVisit wantVisit = WantVisit.builder()
                .destination(destination)
                .user(user)
                .build();
        wantVisitRepository.save(wantVisit);
        return wantVisitMapper.toWantVisitResponse(wantVisit);
    }
    @Transactional(readOnly = true)
    public List<WantVisitResponse> getUserVisitList(Long userId) {
        userService.getUserById(userId);
        List<WantVisit> wantVisits = wantVisitRepository.findByUserId(userId);
        return wantVisitMapper.toWantVisitResponseList(wantVisits);
    }

    @Transactional
    public void removeFromVisitList(Long wantVisitId, Long userId) {
        userService.getUserById(userId);
        if (!wantVisitRepository.existsByIdAndUserId(wantVisitId, userId)) {
            throw new ResourceNotFoundException("Visit list item not found or you do not have permission to delete it.");
        }
        wantVisitRepository.deleteByIdAndUserId(wantVisitId, userId);
    }
    @Transactional
    public void deleteByDestinationId(Long destinationId) {
        wantVisitRepository.deleteByDestinationId(destinationId);
    }
}
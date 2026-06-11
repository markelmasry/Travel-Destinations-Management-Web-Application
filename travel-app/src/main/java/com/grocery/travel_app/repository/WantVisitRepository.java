package com.grocery.travel_app.repository;

import com.grocery.travel_app.model.entity.WantVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WantVisitRepository extends JpaRepository<WantVisit, Long> {
        List<WantVisit> findByUserId(Long userId);
        Optional<WantVisit> findByUserIdAndDestinationId(Long userId, Long destinationId);
}

package com.grocery.travel_app.repository;

import com.grocery.travel_app.model.entity.WantVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface WantVisitRepository extends JpaRepository<WantVisit, Long> {
        List<WantVisit> findByUserId(Long userId);
        void deleteByDestinationId(Long destinationId);
        boolean existsByDestinationIdAndUserId(Long destinationId, Long userId);
        void deleteByIdAndUserId(Long id, Long userId);
        boolean existsByIdAndUserId(Long id, Long userId);

}

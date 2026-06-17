package com.grocery.travel_app.repository;

import com.grocery.travel_app.model.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
        List<WishlistItem> findByUserId(Long userId);
        void deleteByDestinationId(Long destinationId);
        boolean existsByDestinationIdAndUserId(Long destinationId, Long userId);
        void deleteByIdAndUserId(Long id, Long userId);
        boolean existsByIdAndUserId(Long id, Long userId);

}

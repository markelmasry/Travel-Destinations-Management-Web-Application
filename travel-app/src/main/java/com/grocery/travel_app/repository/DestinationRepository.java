package com.grocery.travel_app.repository;

import com.grocery.travel_app.model.entity.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    Page<Destination> findByCountryContainingIgnoreCase(String country, Pageable pageable);
    boolean existsByCountry(String country);
}

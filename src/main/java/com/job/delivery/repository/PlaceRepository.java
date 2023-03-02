package com.job.delivery.repository;

import com.job.delivery.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p WHERE p.placeName = :placeName")
    Optional<Place> findByPlaceName(String placeName);

}

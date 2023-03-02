package com.job.delivery.repository;

import com.job.delivery.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o JOIN o.place p JOIN o.product pd WHERE p.placeName = :placeName AND pd.productName = :productName")
    Optional<Offer> findByPlaceNameAndProductName(@Param("placeName") String placeName, @Param("productName") String productName);

}

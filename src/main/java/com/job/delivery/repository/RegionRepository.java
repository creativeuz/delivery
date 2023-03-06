package com.job.delivery.repository;

import com.job.delivery.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findByRegionName(String regionName);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Region r WHERE :placeName IN elements(r.places)")
    boolean findByPlaceName(@Param("placeName") String placeName);
}
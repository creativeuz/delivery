package com.job.delivery.repository;

import com.job.delivery.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier,Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Region r JOIN Region.places p WHERE :placeName IN elements(p.placeName)")
    boolean findByPlaceName(@Param("placeName") String placeName);

    @Query("SELECT DISTINCT c.carrierName FROM Carrier c JOIN c.regions r WHERE r.regionName = :regionName")
    List<String> findDistinctCarrierNamesByRegionName(@Param("regionName") String regionName);

    @Query("SELECT c FROM Carrier c WHERE c.carrierName IN :carrierNames")
    List<Carrier> findByCarrierNames(@Param("carrierNames") List<String> carrierNames);

    @Query("SELECT c FROM Carrier c WHERE c.carrierName = :carrierName")
    Optional<Carrier> findByCarrierName(@Param("carrierName") String carrierName);

    @Query("SELECT c FROM Carrier c WHERE c.carrierName IN :carrierNames ORDER BY c.carrierName ASC")
    List<Carrier> findAllByNameIn(@Param("carrierNames") List<String> carrierNames);


}

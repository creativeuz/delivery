package com.job.delivery.repository;

import com.job.delivery.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CarrierRepository extends JpaRepository<Carrier,Long> {

    @Query("SELECT DISTINCT c.carrierName FROM Carrier c JOIN c.regions r WHERE r.regionName = :regionName")
    List<String> findDistinctCarrierNamesByRegionName(@Param("regionName") String regionName);

    Optional<Carrier> findByCarrierName(@Param("carrierName") String carrierName);

    @Query("SELECT c FROM Carrier c WHERE c.carrierName IN :carrierNames ORDER BY c.carrierName ASC")
    List<Carrier> findAllByNameIn(@Param("carrierNames") List<String> carrierNames);


}

package com.job.delivery.repository;

import com.job.delivery.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Request r WHERE r.placeName = ?1 AND r.productId = ?2")
    boolean findByPlaceNameAndProductId(String placeName, String productId);

}

package com.job.delivery.service;

import com.job.delivery.entity.Region;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RegionService {
    ResponseEntity<?> addRegion(Region region);

    ResponseEntity<Map<String, List<Map<String, Object>>>> getRegionsWithSameTransactionCount();

}

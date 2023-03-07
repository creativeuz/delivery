package com.job.delivery.service;

import com.job.delivery.entity.Region;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RegionService {
    List<String> addRegion(Region region);

    Map<String, List<Map<String, Object>>> getRegionsWithSameTransactionCount();

}

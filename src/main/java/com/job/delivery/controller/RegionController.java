package com.job.delivery.controller;

import com.job.delivery.entity.Region;
import com.job.delivery.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/regionApi")
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping("/addRegion")
    public ResponseEntity<?> addRegion(@RequestBody Region region) {
        List<String> strings = regionService.addRegion(region);
        return ResponseEntity.ok(strings);
    }

    @GetMapping("/regionsPerNT")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> deliveryRegionsPerNT() {
        Map<String, List<Map<String, Object>>> regionsWithSameTransactionCount = regionService.getRegionsWithSameTransactionCount();
        return ResponseEntity.ok(regionsWithSameTransactionCount);
    }

}

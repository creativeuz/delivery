package com.job.delivery.service;

import com.job.delivery.entity.Carrier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface CarrierService {
    ResponseEntity<?> addCarrier(Carrier carrier);

    List<Optional<Carrier>> getCarriersForRegion(@RequestParam String regionName);

    List<Map<String, Object>> getScorePerCarrier(List<String> carrierNames, int minimumScore);

}

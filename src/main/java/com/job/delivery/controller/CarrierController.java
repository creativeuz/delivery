package com.job.delivery.controller;

import com.job.delivery.entity.Carrier;
import com.job.delivery.exception.CarrierException;
import com.job.delivery.service.CarrierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/carrierApi")
public class CarrierController {
    private final CarrierService carrierService;

    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @PostMapping("/addCarrier")
    public ResponseEntity<?> addCarrier(@RequestBody Carrier carrier) {
        try {
            List<String> strings = carrierService.addCarrier(carrier);
            return ResponseEntity.ok(strings);
        } catch (CarrierException c) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(c.getMessage());
        }
    }

    @GetMapping("/getCarriers")
    public ResponseEntity<List<Optional<Carrier>>> getCarriersForRegion(@RequestParam String regionName) {
        List<Optional<Carrier>> carriersForRegion = carrierService.getCarriersForRegion(regionName);
        return ResponseEntity.ok(carriersForRegion);
    }

    @GetMapping("/score-per-carrier")
    public ResponseEntity<List<Map<String, Object>>> getScorePerCarrier(@RequestParam List<String> carrierNames, @RequestParam int minimumScore) {
        List<Map<String, Object>> scorePerCarrier = carrierService.getScorePerCarrier(carrierNames, minimumScore);
        return ResponseEntity.ok(scorePerCarrier);
    }
}

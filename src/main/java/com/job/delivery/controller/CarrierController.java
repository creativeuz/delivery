package com.job.delivery.controller;

import com.job.delivery.entity.Carrier;
import com.job.delivery.service.CarrierService;
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
        return carrierService.addCarrier(carrier);
    }

    @GetMapping("/getCarriers")
    public List<Optional<Carrier>> getCarriersForRegion(@RequestParam String regionName) {
        return carrierService.getCarriersForRegion(regionName);
    }

    @GetMapping("/score-per-carrier")
    public List<Map<String, Object>> getScorePerCarrier(@RequestParam List<String> carrierNames, @RequestParam int minimumScore) {
        return carrierService.getScorePerCarrier(carrierNames, minimumScore);
    }
}

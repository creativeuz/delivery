package com.job.delivery.service;

import com.job.delivery.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface UserService {
    ResponseEntity<?> signup(SignUpRequest signUpRequest);

    ResponseEntity<?> addRegion(Region region);

    ResponseEntity<?> addCarrier(Carrier carrier);

    List<Optional<Carrier>> getCarriersForRegion(@RequestParam String regionName);

    ResponseEntity<?> addOffer(@RequestBody Offer offer);

    ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction);

    ResponseEntity<Map<String, List<Map<String, Object>>>> getRegionsWithSameTransactionCount();

    ResponseEntity<String> evaluateTransaction(@RequestParam Long transactionId, @RequestParam int score);

    List<Map<String, Object>> getScorePerCarrier(List<String> carrierNames, int minimumScore);

    Map<String, List<Map<String, Object>>> getTransactionCountPerProduct();

    ResponseEntity<?> addRequest(Map<String, String> requestDetails);
}

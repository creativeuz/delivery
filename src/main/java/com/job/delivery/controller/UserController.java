package com.job.delivery.controller;

import com.job.delivery.entity.*;
import com.job.delivery.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.signup(signUpRequest);
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/regionsPerNT")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> deliveryRegionsPerNT() {
        return userService.getRegionsWithSameTransactionCount();
    }

    @PostMapping("/addRegion")
    public ResponseEntity<?> addRegion(@RequestBody Region region) {
        return userService.addRegion(region);
    }

    @PostMapping("/addCarrier")
    public ResponseEntity<?> addCarrier(@RequestBody Carrier carrier) {
        return userService.addCarrier(carrier);
    }

    @GetMapping("/getCarriers")
    public List<Optional<Carrier>> getCarriersForRegion(@RequestParam String regionName) {
        return userService.getCarriersForRegion(regionName);
    }

    @PostMapping("/addRequest")
    public ResponseEntity<?> addRequest(@RequestBody Map<String, String> requestDetails) {

        return userService.addRequest(requestDetails);
    }

    @PostMapping("/addOffer")
    public ResponseEntity<?> addOffer(@RequestBody Offer offer) {
        return userService.addOffer(offer);
    }

    @PostMapping("/addTransaction")
    public ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction) {
        return userService.addTransaction(transaction);
    }

    @PostMapping("/evaluateTransaction")
    public ResponseEntity<String> evaluateTransaction(@RequestParam Long transactionId, @RequestParam int score) {
        return userService.evaluateTransaction(transactionId, score);
    }

    @GetMapping("/score-per-carrier")
    public List<Map<String, Object>> getScorePerCarrier(@RequestParam List<String> carrierNames, @RequestParam int minimumScore) {
        return userService.getScorePerCarrier(carrierNames, minimumScore);
    }

    @GetMapping("/count-transac-per-product")
    public Map<String, List<Map<String, Object>>> getTransactionCountPerProduct() {
        return userService.getTransactionCountPerProduct();
    }

}


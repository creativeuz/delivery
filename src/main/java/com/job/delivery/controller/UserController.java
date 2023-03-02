package com.job.delivery.controller;

import com.job.delivery.entity.*;
import com.job.delivery.security.UserValidator;
import com.job.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserValidator userValidator;

    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/regionsPerNT")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> deliveryRegionsPerNT() {
        return userService.getRegionsWithSameTransactionCount();
    }


    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "redirect:/login";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/registration";
        }

        userService.save(userForm);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).toString();
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


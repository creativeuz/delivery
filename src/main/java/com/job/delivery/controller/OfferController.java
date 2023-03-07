package com.job.delivery.controller;

import com.job.delivery.entity.Offer;
import com.job.delivery.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/offerApi")
public class OfferController {
    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/addOffer")
    public ResponseEntity<?> addOffer(@RequestBody Offer offer) {
        return offerService.addOffer(offer);
    }


}

package com.job.delivery.service;

import com.job.delivery.entity.Offer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface OfferService {
    ResponseEntity<?> addOffer(@RequestBody Offer offer);

}

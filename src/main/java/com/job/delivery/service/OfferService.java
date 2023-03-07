package com.job.delivery.service;

import com.job.delivery.entity.Offer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface OfferService {
    Object addOffer(@RequestBody Offer offer);

}

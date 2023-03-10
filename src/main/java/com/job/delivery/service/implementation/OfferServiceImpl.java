package com.job.delivery.service.implementation;

import com.job.delivery.entity.Offer;
import com.job.delivery.entity.Place;
import com.job.delivery.entity.Product;
import com.job.delivery.repository.OfferRepository;
import com.job.delivery.repository.PlaceRepository;
import com.job.delivery.repository.ProductRepository;
import com.job.delivery.service.OfferService;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class OfferServiceImpl implements OfferService {
    private final PlaceRepository placeRepository;
    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    public OfferServiceImpl(PlaceRepository placeRepository, ProductRepository productRepository, OfferRepository offerRepository) {
        this.placeRepository = placeRepository;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    public Object addOffer(Offer offer) {
        Optional<Place> optionalPlace = placeRepository.findByPlaceName(offer.getPlace().getPlaceName());
        Place place;
        place = optionalPlace.orElseGet(offer::getPlace);

        Set<String> uniqueProductNames = new HashSet<>();
        List<String> duplicateProductNames = new ArrayList<>();
        for (Product product : offer.getProducts()) {
            if (productRepository.findByProductName(product.getProductName()).isPresent()) {
                duplicateProductNames.add(product.getProductName());
            } else {
                uniqueProductNames.add(product.getProductName());
            }
        }

        if (!duplicateProductNames.isEmpty()) {
            return ResponseEntity.badRequest().body("Duplicate product names: " + duplicateProductNames);
        }

        ArrayList<Product> products = new ArrayList<>();
        for (String productName : uniqueProductNames) {
            Product product = new Product();
            product.setProductName(productName);
            productRepository.save(product);
            products.add(product);
        }

        offer.setPlace(place);
        offer.setProducts(products);
        offerRepository.save(offer);

        ArrayList<Offer> placeOffers = place.getOffers();
        ArrayList<Offer> productOffers = new ArrayList<>();
        for (Product product : products) {
            productOffers.addAll(product.getOffers());
        }
        placeOffers.add(offer);
        productOffers.add(offer);
        place.setOffers(placeOffers);
        for (Product product : products) {
            product.setOffers(productOffers);
            productRepository.save(product);
        }
        placeRepository.save(place);

        return offer;
    }
}

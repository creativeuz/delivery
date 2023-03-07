package com.job.delivery.serviceImplementation;

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
    public ResponseEntity<?> addOffer(Offer offer) {
        // Check if place exists in database
        Optional<Place> optionalPlace = placeRepository.findByPlaceName(offer.getPlace().getPlaceName());
        Place place;
        place = optionalPlace.orElseGet(offer::getPlace);

        // Check for duplicates in product names
        Set<String> uniqueProductNames = new HashSet<>();
        List<String> duplicateProductNames = new ArrayList<>();
        for (Product product : offer.getProducts()) {
            if (productRepository.findByProductName(product.getProductName()).isPresent()) {
                duplicateProductNames.add(product.getProductName());
            } else {
                uniqueProductNames.add(product.getProductName());
            }
        }

        // If there are duplicates, return bad request
        if (!duplicateProductNames.isEmpty()) {
            return ResponseEntity.badRequest().body("Duplicate product names: " + duplicateProductNames);
        }

        // Save products with unique names
        ArrayList<Product> products = new ArrayList<>();
        for (String productName : uniqueProductNames) {
            Product product = new Product();
            product.setProductName(productName);
            productRepository.save(product);
            products.add(product);
        }

        // Save offer with place and products
        offer.setPlace(place);
        offer.setProducts(products);
        offerRepository.save(offer);

        // Update offers in place and products
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

        return ResponseEntity.ok().body(offer);
    }
}

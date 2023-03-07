package com.job.delivery.serviceImplementation;

import com.job.delivery.entity.Place;
import com.job.delivery.entity.Product;
import com.job.delivery.entity.Request;
import com.job.delivery.repository.PlaceRepository;
import com.job.delivery.repository.ProductRepository;
import com.job.delivery.repository.RequestRepository;
import com.job.delivery.service.RequestService;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

public class RequestServiceImpl implements RequestService {
    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;
    private final PlaceRepository placeRepository;

    public RequestServiceImpl(ProductRepository productRepository, RequestRepository requestRepository, PlaceRepository placeRepository) {
        this.productRepository = productRepository;
        this.requestRepository = requestRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public Object addRequest(Map<String, String> requestDetails) {
        String requestId = requestDetails.get("requestId");
        String placeName = requestDetails.get("placeName");
        String productId = requestDetails.get("productId");

        // Check if the product exists in the database
        Optional<Product> optionalProduct = productRepository.findById(Long.parseLong(productId));
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Product not found with id: " + productId);
        }
        Product product = optionalProduct.get();

        // Check if the request already exists in the database
        Optional<Request> optionalRequest = requestRepository.findById(Long.parseLong(requestId));
        if (optionalRequest.isPresent()) {
            return ResponseEntity.badRequest().body("Request with id " + requestId + " already exists");
        }

        // Check if the place exists in the database
        Optional<Place> optionalPlace = placeRepository.findByPlaceName(placeName);
        if (optionalPlace.isEmpty()) {
            return ResponseEntity.badRequest().body("Place not found with name: " + placeName);
        }
        Place place = optionalPlace.get();

        // Create the new request and add it to the database
        Request request = new Request();
        request.setRequestId(Long.parseLong(requestId));
        request.setPlace(place);
        request.getProducts().add(product);
        requestRepository.save(request);

        return request;
    }




}

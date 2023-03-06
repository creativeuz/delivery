package com.job.delivery.service;

import com.job.delivery.config.MyUserDetailsService;
import com.job.delivery.entity.*;
import com.job.delivery.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final CarrierRepository carrierRepository;
    private final RequestRepository requestRepository;
    private final OfferRepository offerRepository;
    private final PlaceRepository placeRepository;
    private final RegionRepository regionRepository;
    private final MyUserDetailsService myUserDetailsService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TransactionRepository transactionRepository, ProductRepository productRepository, CarrierRepository carrierRepository, RequestRepository requestRepository, OfferRepository offerRepository, PlaceRepository placeRepository, RegionRepository regionRepository, MyUserDetailsService myUserDetailsService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.carrierRepository = carrierRepository;
        this.requestRepository = requestRepository;
        this.offerRepository = offerRepository;
        this.placeRepository = placeRepository;
        this.regionRepository = regionRepository;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        User user = myUserDetailsService.signUp(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @Override
    public ResponseEntity<?> addRegion(Region region) {
        // Check if region already exists in database
        Optional<Region> optionalRegion = Optional.ofNullable(regionRepository.findByRegionName(region.getRegionName()));
        if (optionalRegion.isPresent()) {
            Region existingRegion = optionalRegion.get();
            Set<String> allPlaceNames = existingRegion.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toSet());
            allPlaceNames.addAll(region.getPlaces().stream().map(Place::getPlaceName).toList());
            existingRegion.setPlaces(new ArrayList<>(region.getPlaces()));
            regionRepository.save(existingRegion);
            return ResponseEntity.ok().body(existingRegion.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toList()));
        } else {
            // Check for duplicates in place names
            Set<String> uniquePlaceNames = region.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toSet());
            List<String> duplicatePlaceNames = new ArrayList<>();
            for (Place place : region.getPlaces()) {
                if (regionRepository.findByPlaceName(place.getPlaceName())) {
                    uniquePlaceNames.remove(place.getPlaceName());
                    duplicatePlaceNames.add(place.getPlaceName());
                }
            }
            // If there are duplicates, remove them and return bad request
            if (!duplicatePlaceNames.isEmpty()) {
                return ResponseEntity.badRequest().body("Duplicate place names: " + duplicatePlaceNames);
            }
            // Otherwise, save region and return added place names
            region.setPlaces(new ArrayList<>(region.getPlaces()));
            regionRepository.save(region);
            return ResponseEntity.ok().body(region.getPlaces().stream().map(Place::getPlaceName).collect(Collectors.toList()));
        }
    }


    @Override
    public ResponseEntity<?> addCarrier(Carrier carrier) {
        Optional<Carrier> optionalCarrier = carrierRepository.findByCarrierName(carrier.getCarrierName());
        if (optionalCarrier.isPresent()) {
            Carrier existingCarrier = optionalCarrier.get();
            List<String> existingRegionNames = getRegionNames(existingCarrier.getRegions());
            Set<String> allRegionNames = new HashSet<>(existingRegionNames);
            allRegionNames.addAll(getRegionNames(carrier.getRegions()));
            existingCarrier.setRegions((ArrayList<Region>) getRegionsByName(new ArrayList<>(allRegionNames)));
            carrierRepository.save(existingCarrier);
            return ResponseEntity.ok().body(getRegionNames(existingCarrier.getRegions()));
        } else {
            // Check for duplicates in region names
            Set<String> uniqueRegionNames = (Set<String>) getRegionNames(carrier.getRegions());
            List<String> duplicateRegionNames = new ArrayList<>();
            for (String regionName : uniqueRegionNames) {
                if (regionRepository.findByRegionName(regionName) != null) {
                    uniqueRegionNames.remove(regionName);
                    duplicateRegionNames.add(regionName);
                }
            }
            // If there are duplicates, remove them and return bad request
            if (!duplicateRegionNames.isEmpty()) {
                return ResponseEntity.badRequest().body("Duplicate region names: " + duplicateRegionNames);
            }
            // Otherwise, save carrier and return added region names
            carrier.setRegions((ArrayList<Region>) getRegionsByName(new ArrayList<>(uniqueRegionNames)));
            carrierRepository.save(carrier);
            return ResponseEntity.ok().body(getRegionNames(carrier.getRegions()));
        }
    }


    @Override
    public List<Optional<Carrier>> getCarriersForRegion(String regionName) {
        Region region = regionRepository.findByRegionName(regionName);
        if (region == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Region not found");
        }
        List<Optional<Carrier>> carriers = new ArrayList<>();
        for (String carrierName : carrierRepository.findDistinctCarrierNamesByRegionName(regionName)) {
            Optional<Carrier> carrier = carrierRepository.findByCarrierName(carrierName);
            if (carrier.isPresent()) {
                carriers.add(carrier);
            }
        }
        return carriers;
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


    @Override
    public ResponseEntity<Object> addTransaction(Transaction transaction) {
        Optional<Request> optionalRequest = requestRepository.findById(transaction.getRequest().getRequestId());
        Optional<Offer> optionalOffer = offerRepository.findById(transaction.getOffer().getOfferId());
        Optional<Carrier> optionalCarrier = carrierRepository.findByCarrierName(transaction.getCarrier().getCarrierName());

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request not found.");
        }

        if (optionalOffer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Offer not found.");
        }

        if (optionalCarrier.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrier not found.");
        }

        Request request = optionalRequest.get();
        Offer offer = optionalOffer.get();
        Carrier carrier = optionalCarrier.get();

        if (request.getTransaction() != null || offer.getTransaction() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request or offer already connected to a transaction.");
        }

        List<String> productNames = request.getProducts().stream().map(Product::getProductName).toList();
        if (!productNames.equals(offer.getProducts().stream())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request and offer not related to the same product.");
        }

        if (!carrier.providesService(request.getPlace().getPlaceName(), offer.getPlace().getPlaceName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrier doesn't provide service to both locations.");
        }

        request.setTransaction(transaction);
        offer.setTransaction(transaction);

        transaction.setProduct(request.getProducts().get(0));
        transactionRepository.save(transaction);

        return ResponseEntity.status(HttpStatus.OK).body("Transaction added successfully.");
    }

    @Override
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getRegionsWithSameTransactionCount() {
        Map<String, List<Map<String, Object>>> responseMap = new HashMap<>();
        List<Map<String, Object>> responseList = new ArrayList<>();

        List<Object[]> resultList = transactionRepository.getRegionsWithSameTransactionCount();

        int transactionNumber = -1;
        List<Map<String, Object>> regions = new ArrayList<>();
        for (Object[] result : resultList) {
            int resultTransactionNumber = ((Number) result[0]).intValue();
            String regionName = (String) result[1];
            // Add other fields as needed

            if (resultTransactionNumber != transactionNumber) {
                // New transaction number encountered, create new entry in response
                if (!regions.isEmpty()) {
                    Map<String, Object> responseEntry = new HashMap<>();
                    responseEntry.put("transactionNumber", transactionNumber);
                    responseEntry.put("regions", regions);
                    responseList.add(responseEntry);
                }

                // Update current transaction number and clear regions list
                transactionNumber = resultTransactionNumber;
                regions = new ArrayList<>();
            }

            // Add region to current transaction number entry
            Map<String, Object> regionEntry = new HashMap<>();
            regionEntry.put("regionName", regionName);
            // Add other fields as needed
            regions.add(regionEntry);
        }

        // Add final transaction number entry to response
        if (!regions.isEmpty()) {
            Map<String, Object> responseEntry = new HashMap<>();
            responseEntry.put("transactionNumber", transactionNumber);
            responseEntry.put("regions", regions);
            responseList.add(responseEntry);
        }

        responseMap.put("response", responseList);
        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<String> evaluateTransaction(Long transactionId, int score) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid transaction ID");
        }
        Transaction transaction = optionalTransaction.get();
        if (score < 1 || score > 10) {
            return ResponseEntity.badRequest().body("Score must be between 1 and 10 (inclusive)");
        }
        transaction.setScore(score);
        transactionRepository.save(transaction);
        return ResponseEntity.ok("Transaction evaluated successfully");
    }

    @Override
    public List<Map<String, Object>> getScorePerCarrier(List<String> carrierNames, int minimumScore) {
        List<Carrier> carriers = carrierRepository.findAllByNameIn(carrierNames);
        List<Object[]> scorePerCarrier = transactionRepository.getScorePerCarrier(carriers, minimumScore);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : scorePerCarrier) {
            Map<String, Object> map = new HashMap<>();
            Carrier carrier = (Carrier) row[0];
            Long totalScore = (Long) row[1];
            map.put("carrierName", carrier.getCarrierName());
            map.put("totalScore", totalScore);
            result.add(map);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("response", result);
        return Collections.singletonList(response);
    }

    @Override
    public Map<String, List<Map<String, Object>>> getTransactionCountPerProduct() {
        List<Product> products = productRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Product product : products) {
            Long transactionCount = transactionRepository.countByProduct(product);
            if (transactionCount > 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", product.getProductId());
                map.put("transactionCount", transactionCount);
                result.add(map);
            }
        }
        // sort the result by product id
        result.sort(Comparator.comparing(m -> (Long) m.get("productId")));
        return Collections.singletonMap("response", result);
    }

    @Override
    public ResponseEntity<?> addRequest(Map<String, String> requestDetails) {
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
        Optional<Request> optionalRequest = Optional.ofNullable(requestRepository.findById(Long.parseLong(requestId)).orElse(null));
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

        return ResponseEntity.ok().body(request);
    }

    private List<String> getRegionNames(List<Region> regions) {
        List<String> regionNames = new ArrayList<>();
        for (Region region : regions) {
            regionNames.add(region.getRegionName());
        }
        return regionNames;
    }

    private List<Region> getRegionsByName(List<String> regionNames) {
        List<Region> regions = new ArrayList<>();
        for (String regionName : regionNames) {
            Optional<Region> optionalRegion = Optional.ofNullable(regionRepository.findByRegionName(regionName));
            optionalRegion.ifPresent(regions::add);
        }
        return regions;
    }

}


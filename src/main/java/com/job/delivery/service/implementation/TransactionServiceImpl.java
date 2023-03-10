package com.job.delivery.service.implementation;

import com.job.delivery.entity.*;
import com.job.delivery.exception.TransactionException;
import com.job.delivery.repository.*;
import com.job.delivery.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final RequestRepository requestRepository;
    private final OfferRepository offerRepository;
    private final CarrierRepository carrierRepository;
    private final ProductRepository productRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, RequestRepository requestRepository, OfferRepository offerRepository, CarrierRepository carrierRepository, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.requestRepository = requestRepository;
        this.offerRepository = offerRepository;
        this.carrierRepository = carrierRepository;
        this.productRepository = productRepository;
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
    public boolean evaluateTransaction(Long transactionId, int score) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            throw new TransactionException("Invalid transaction ID");
        }
        Transaction transaction = optionalTransaction.get();
        if (score < 1 || score > 10) {
            throw new TransactionException("Score must be between 1 and 10 (inclusive)");
        }
        transaction.setScore(score);
        transactionRepository.save(transaction);
        return true;
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
        result.sort(Comparator.comparing(m -> (Long) m.get("productId")));
        return Collections.singletonMap("response", result);
    }

}

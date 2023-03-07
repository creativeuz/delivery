package com.job.delivery.service;

import com.job.delivery.entity.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public interface TransactionService {
    ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction);

    ResponseEntity<String> evaluateTransaction(@RequestParam Long transactionId, @RequestParam int score);

    Map<String, List<Map<String, Object>>> getTransactionCountPerProduct();

}

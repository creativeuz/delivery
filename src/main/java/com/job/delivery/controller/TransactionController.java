package com.job.delivery.controller;

import com.job.delivery.entity.Transaction;
import com.job.delivery.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/transactionApi")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/addTransaction")
    public ResponseEntity<Object> addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    @PostMapping("/evaluateTransaction")
    public ResponseEntity<String> evaluateTransaction(@RequestParam Long transactionId, @RequestParam int score) {
        return transactionService.evaluateTransaction(transactionId, score);
    }

    @GetMapping("/count-transac-per-product")
    public Map<String, List<Map<String, Object>>> getTransactionCountPerProduct() {
        return transactionService.getTransactionCountPerProduct();
    }
}

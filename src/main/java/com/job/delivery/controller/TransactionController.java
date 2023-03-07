package com.job.delivery.controller;

import com.job.delivery.entity.Transaction;
import com.job.delivery.exception.TransactionException;
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
    public ResponseEntity<?> evaluateTransaction(@RequestParam Long transactionId, @RequestParam int score) {
        try {
            boolean b = transactionService.evaluateTransaction(transactionId, score);
            return ResponseEntity.ok(b);
        }catch (TransactionException t){
            if(t.getMessage().equals("Score must be between 1 and 10 (inclusive)")){
                return ResponseEntity.ok(false);
            }
            else return ResponseEntity.badRequest().body(t.getMessage());
        }
    }

    @GetMapping("/count-transac-per-product")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getTransactionCountPerProduct() {
        Map<String, List<Map<String, Object>>> transactionCountPerProduct = transactionService.getTransactionCountPerProduct();
        return ResponseEntity.ok().body(transactionCountPerProduct);
    }
}

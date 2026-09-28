package com.ofss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ofss.entity.TransactionsTable;
import com.ofss.service.TransactionsService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;
   // Purchase
    @PostMapping("/purchase")
    public TransactionsTable purchase(@RequestBody TransactionsTable transaction) {
        return transactionsService.createPurchase(transaction);
    }

   // Get all transactions
    @GetMapping
    public List<TransactionsTable> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }

  // Get transaction by ID
    @GetMapping("/{transactionId}")
    public TransactionsTable getTransaction(@PathVariable Long transactionId) {
        return transactionsService.getTransaction(transactionId);
    }
    // Get merchant transactions
    @GetMapping("/merchant/{merchantId}")
    public List<TransactionsTable> getMerchantTransactions( @PathVariable Long merchantId) {
        return transactionsService.getMerchantTransactions(merchantId);
    }
    // Get customer transactions
    @GetMapping("/card/{cardNumber}")
    public List<TransactionsTable> getCustomerTransactions(@PathVariable Long cardNumber) {
        return transactionsService.getCustomerTransactions(cardNumber);
    }
}
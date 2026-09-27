package com.ofss.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.entity.TransactionsTable;
import com.ofss.repository.TransactionsRepository;

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionRepository;

    // Purchase transaction
    public TransactionsTable createPurchase(TransactionsTable transaction) {
        transaction.setTransactionType("PURCHASE");
        transaction.setStatus("SUCCESS");
        transaction.setDataTime(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }
    // Get all transactions
    public List<TransactionsTable> getAllTransactions() {
        return transactionRepository.findAll();
    }
    // Get transaction by ID
    public TransactionsTable getTransaction(Long transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }


    // Get merchant transactions
    public List<TransactionsTable> getMerchantTransactions(Long merchantId) {
        return transactionRepository.findByMerchantId(merchantId);
    }


    // Get customer transactions
    public List<TransactionsTable> getCustomerTransactions(Long cardNumber) {
        return transactionRepository.findByCardNumber(cardNumber);
    }
}
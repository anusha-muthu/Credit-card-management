package com.ofss.client;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TransactionClient {

    private final RestClient restClient;

    public TransactionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8084")
                .build();
    }

    public void createTransaction(String cardNumber,
                                  Long merchantId,
                                  BigDecimal amount,
                                  String transactionType,
                                  String status) {

        Map<String, Object> transaction = new HashMap<>();

        transaction.put("cardNumber", cardNumber);
        transaction.put("merchantId", merchantId);
        transaction.put("amount", amount);
        transaction.put("transactionType", transactionType);
        transaction.put("status", status);

        restClient.post()
                .uri("/api/transactions")
                .body(transaction)
                .retrieve()
                .toBodilessEntity();
    }
}
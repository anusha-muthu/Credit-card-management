package com.ofss.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.ofss.dto.PurchaseRequest;
import com.ofss.dto.PaymentRequest;

@Component
public class TransactionClient {

    private final RestClient restClient;

    public TransactionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8084")
                .build();
    }

    // --- OVERLOAD FOR PURCHASES ---
    public void createPurchaseTransaction(PurchaseRequest request) {
        restClient.post()
                .uri("/api/transactions/purchase")
                .body(request) // Sends the clean, typed DTO that TransactionController expects
                .retrieve()
                .toBodilessEntity();
    }

    // --- OVERLOAD FOR PAYMENTS ---
    public void createPaymentTransaction(PaymentRequest request) {
    	 Map<String, Object> bodyMap = new HashMap<>();
         bodyMap.put("cardNumber", request.getCardNumber());
         
         // --- FIX: Map 'paymentAmount' from Gateway to 'amount' for Transaction Service ---
         bodyMap.put("amount", request.getPaymentAmount()); 

    	restClient.post()
                .uri("/api/transactions/payment")
                .body(request) // Sends the clean, typed DTO
                .retrieve()
                .toBodilessEntity();
    }
}

package com.ofss.client;

import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.ofss.dto.CardResponse;

@Component
public class CreditCardClient {

    private final RestClient restClient;

    public CreditCardClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    public CardResponse getCard(String cardNumber) {
        ResponseEntity<CardResponse> response = restClient.get()
                .uri("/api/cards/{cardNumber}", cardNumber)
                .retrieve()
                .toEntity(CardResponse.class);

        return response.getBody();
    }

    // --- UPDATE TO BodiLessEntity TO AVOID JSON MAPPING ERRORS ---
    public void applyPurchase(String cardNumber, BigDecimal amount) {
        restClient.put()
                .uri("/api/cards/{cardNumber}/purchase/{amount}", cardNumber, amount)
                .retrieve()
                .toBodilessEntity(); // Safely accepts the 200 OK and ignores the body text
    }

    // --- UPDATE TO BodiLessEntity TO AVOID JSON MAPPING ERRORS ---
    public void applyPayment(String cardNumber, BigDecimal amount) {
        restClient.put()
                .uri("/api/cards/{cardNumber}/payment/{amount}", cardNumber, amount)
                .retrieve()
                .toBodilessEntity(); // Safely accepts the 200 OK and ignores the body text
    }



    // Kept for backward compatibility
    public void updateAvailableCredit(String cardNumber, BigDecimal availableCredit) {
        restClient.put()
                .uri("/api/cards/{cardNumber}/available-credit", cardNumber)
                .body(availableCredit)
                .retrieve()
                .toBodilessEntity();
    }

    // Kept for backward compatibility
    public void updateOutstanding(String cardNumber, BigDecimal outstandingAmount) {
        restClient.put()
                .uri("/api/cards/{cardNumber}/outstanding", cardNumber)
                .body(outstandingAmount)
                .retrieve()
                .toBodilessEntity();
    }
}

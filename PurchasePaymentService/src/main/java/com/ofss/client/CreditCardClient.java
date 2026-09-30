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

    public CardResponse getCard(Long cardNumber) {

        ResponseEntity<CardResponse> response = restClient.get()
                .uri("/api/cards/{cardNumber}", cardNumber)
                .retrieve()
                .toEntity(CardResponse.class);

        return response.getBody();
    }

    public void updateAvailableCredit(Long cardNumber,
                                      BigDecimal availableCredit) {

        restClient.put()
                .uri("/api/cards/{cardNumber}/available-credit", cardNumber)
                .body(availableCredit)
                .retrieve()
                .toBodilessEntity();
    }

    public void updateOutstanding(Long cardNumber,
                                  BigDecimal outstandingAmount) {

        restClient.put()
                .uri("/api/cards/{cardNumber}/outstanding", cardNumber)
                .body(outstandingAmount)
                .retrieve()
                .toBodilessEntity();
    }
}
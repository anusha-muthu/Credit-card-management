package com.ofss.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.ofss.dto.MerchantResponse;

@Component
public class MerchantClient {

    private final RestClient restClient;

    public MerchantClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public MerchantResponse getMerchant(Long merchantId) {

        ResponseEntity<MerchantResponse> response = restClient.get()
                .uri("/api/merchants/{merchantId}", merchantId)
                .retrieve()
                .toEntity(MerchantResponse.class);

        return response.getBody();
    }
}
package com.ofss.transaction.client;

import com.ofss.transaction.dto.MerchantResponse;
import com.ofss.transaction.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MerchantServiceClient {

    private final RestClient restClient;
    private final String merchantServiceUrl;

    public MerchantServiceClient(
            RestClient restClient,
            @Value("${services.merchant.url}") String merchantServiceUrl
    ) {
        this.restClient = restClient;
        this.merchantServiceUrl = merchantServiceUrl;
    }

    public MerchantResponse getMerchant(Long merchantId) {

        return restClient.get()
                .uri(merchantServiceUrl + "/api/merchants/{merchantId}",
                        merchantId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new BusinessException(
                            "Merchant not found or merchant service unavailable"
                    );
                })
                .body(MerchantResponse.class);
    }
}
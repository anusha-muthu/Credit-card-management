package com.ofss.transaction.client;

import com.ofss.transaction.dto.CardBalanceResponse;
import com.ofss.transaction.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class CardServiceClient {

    private final RestClient restClient;
    private final String cardServiceUrl;

    public CardServiceClient(
            RestClient restClient,
            @Value("${services.card.url}") String cardServiceUrl
    ) {
        this.restClient = restClient;
        this.cardServiceUrl = cardServiceUrl;
    }

    public CardBalanceResponse applyPurchase(
            String cardNumber,
            BigDecimal amount
    ) {

        return restClient.post()
                .uri(
                        cardServiceUrl + "/api/cards/{cardNumber}/purchase",
                        cardNumber
                )
                .body(new AmountRequest(amount))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new BusinessException(
                                    "Card purchase failed: HTTP "
                                            + response.getStatusCode()
                            );
                        }
                )
                .body(CardBalanceResponse.class);
    }

    public CardBalanceResponse applyPayment(
            String cardNumber,
            BigDecimal amount
    ) {

        return restClient.post()
                .uri(
                        cardServiceUrl + "/api/cards/{cardNumber}/payment",
                        cardNumber
                )
                .body(new AmountRequest(amount))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new BusinessException(
                                    "Card payment failed: HTTP "
                                            + response.getStatusCode()
                            );
                        }
                )
                .body(CardBalanceResponse.class);
    }

    private record AmountRequest(BigDecimal amount) {
    }
}
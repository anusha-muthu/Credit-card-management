package com.ofss.transaction.dto;

public record MerchantResponse(

        Long merchantId,
        String merchantName,
        String category,
        String location
) {
}
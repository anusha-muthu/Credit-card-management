package com.ofss.transaction.dto;

import java.math.BigDecimal;

public record CardBalanceResponse(

        String cardNumber,

        Long customerId,

        String status,

        BigDecimal creditLimit,

        BigDecimal availableCredit,

        BigDecimal outstandingAmount
) {
}
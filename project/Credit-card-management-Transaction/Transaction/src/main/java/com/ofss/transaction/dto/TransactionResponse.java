package com.ofss.transaction.dto;

import com.ofss.transaction.entity.TransactionEntity;
import com.ofss.transaction.enums.TransactionStatus;
import com.ofss.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long transactionId,
        String cardNumber,
        Long customerId,
        TransactionType transactionType,
        BigDecimal amount,
        Long merchantId,
        LocalDateTime transactionDateTime,
        TransactionStatus status
) {

    public static TransactionResponse fromEntity(TransactionEntity entity) {
        return new TransactionResponse(
                entity.getTransactionId(),
                entity.getCardNumber(),
                entity.getCustomerId(),
                entity.getTransactionType(),
                entity.getAmount(),
                entity.getMerchantId(),
                entity.getTransactionDateTime(),
                entity.getStatus()
        );
    }
}

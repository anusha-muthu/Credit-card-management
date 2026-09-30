package com.ofss.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PurchaseRequest(
		 @NotBlank(message = "Card number is required")
	        String cardNumber,

	        @NotNull(message = "Merchant ID is required")
	        @Positive(message = "Merchant ID must be positive")
	        Long merchantId,

	        @NotNull(message = "Purchase amount is required")
	        @DecimalMin(value = "0.01", message = "Purchase amount must be greater than 0")
	        BigDecimal amount
	) {
	}

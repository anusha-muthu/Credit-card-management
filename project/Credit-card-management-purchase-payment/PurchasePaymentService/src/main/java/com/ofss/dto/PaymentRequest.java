package com.ofss.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {
    
    // --- FIX: Changed from Long to String to preserve precision ---
    private String cardNumber;

    @JsonProperty("amount")
    @JsonAlias("paymentAmount")
    private BigDecimal paymentAmount;

    public PaymentRequest() {
        super();
    }

    // --- FIX: Updated Constructor ---
    public PaymentRequest(String cardNumber, BigDecimal paymentAmount) {
        super();
        this.cardNumber = cardNumber;
        this.paymentAmount = paymentAmount;
    }

    // --- FIX: Updated Getter ---
    public String getCardNumber() {
        return cardNumber;
    }

    // --- FIX: Updated Setter ---
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public String toString() {
        return "PaymentRequest [cardNumber=" + cardNumber + ", paymentAmount=" + paymentAmount + "]";
    }
}

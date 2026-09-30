package com.creditcard.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "CREDIT_CARD")
public class CreditCard {

    @Id
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    @Column(name = "CARD_NUMBER", length = 20)
    private String cardNumber;

    @NotNull(message = "Customer ID is required")
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @NotBlank(message = "Card type is required")
    @Column(name = "CARD_TYPE", nullable = false, length = 30)
    private String cardType;

    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Credit limit must be greater than 0")
    @Column(name = "CREDIT_LIMIT", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @NotNull(message = "Available credit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Available credit cannot be negative")
    @Column(name = "AVAILABLE_CREDIT", nullable = false, precision = 12, scale = 2)
    private BigDecimal availableCredit;

    @NotNull(message = "Outstanding amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Outstanding amount cannot be negative")
    @Column(name = "OUTSTANDING_AMOUNT", nullable = false, precision = 12, scale = 2)
    private BigDecimal outstandingAmount;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDate expiryDate;

    @NotBlank(message = "Card status is required")
    @Pattern(regexp = "ACTIVE|BLOCKED", message = "Card status must be ACTIVE or BLOCKED")
    @Column(name = "CARD_STATUS", nullable = false, length = 20)
    private String cardStatus;

    public CreditCard() {
    }

    public CreditCard(String cardNumber, Long customerId, String cardType, BigDecimal creditLimit,
                      BigDecimal availableCredit, BigDecimal outstandingAmount, LocalDate expiryDate,
                      String cardStatus) {
        this.cardNumber = cardNumber;
        this.customerId = customerId;
        this.cardType = cardType;
        this.creditLimit = creditLimit;
        this.availableCredit = availableCredit;
        this.outstandingAmount = outstandingAmount;
        this.expiryDate = expiryDate;
        this.cardStatus = cardStatus;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }
}
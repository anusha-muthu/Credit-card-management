package com.ofss.transaction.entity;

import com.ofss.transaction.enums.TransactionStatus;
import com.ofss.transaction.enums.TransactionType;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "CC_TRANSACTION",
        indexes = {
                @Index(name = "IDX_TX_CARD", columnList = "CARD_NUMBER"),
                @Index(name = "IDX_TX_CUSTOMER", columnList = "CUSTOMER_ID"),
                @Index(name = "IDX_TX_MERCHANT", columnList = "MERCHANT_ID"),
                @Index(name = "IDX_TX_DATE", columnList = "TRANSACTION_DATE_TIME"),
                @Index(name = "IDX_TX_TYPE_STATUS", columnList = "TRANSACTION_TYPE, STATUS")
        }
)

public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq_gen")
    @SequenceGenerator(
            name = "transaction_seq_gen",
            sequenceName = "CC_TRANSACTION_SEQ",
            allocationSize = 1
    )
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "CARD_NUMBER", nullable = false, length = 20)
    private String cardNumber;

    /*
     * Denormalized reporting field.
     * The customer is actually owned by Customer/Card services.
     */
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSACTION_TYPE", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(name = "AMOUNT", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    /*
     * NULL for PAYMENT
     */
    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    @Column(name = "TRANSACTION_DATE_TIME", nullable = false)
    private LocalDateTime transactionDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private TransactionStatus status;

    public TransactionEntity() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
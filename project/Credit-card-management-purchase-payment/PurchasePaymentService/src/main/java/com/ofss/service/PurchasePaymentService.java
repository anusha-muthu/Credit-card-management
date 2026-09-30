package com.ofss.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.ofss.client.CreditCardClient;
import com.ofss.client.MerchantClient;
import com.ofss.client.TransactionClient;
import com.ofss.dto.CardResponse;
import com.ofss.dto.MerchantResponse;
import com.ofss.dto.PaymentRequest;
import com.ofss.dto.PurchaseRequest;

@Service
public class PurchasePaymentService {
    private final CreditCardClient creditCardClient;
    private final MerchantClient merchantClient;
    private final TransactionClient transactionClient;

    public PurchasePaymentService(
            CreditCardClient creditCardClient,
            MerchantClient merchantClient,
            TransactionClient transactionClient) {
        this.creditCardClient = creditCardClient;
        this.merchantClient = merchantClient;
        this.transactionClient = transactionClient;
    }

    // PURCHASE
    public String processPurchase(PurchaseRequest request) {
        if (request == null) return "Purchase failed: Request cannot be null";
        if (request.getCardNumber() == null) return "Purchase failed: Card number is required";
        if (request.getMerchantId() == null) return "Purchase failed: Merchant ID is required";
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Purchase failed: Invalid purchase amount";
        }

        try {
            CardResponse card = creditCardClient.getCard(request.getCardNumber());
            if (card == null) { 
                createFailedPurchaseTransaction(request);
                return "Purchase failed: Card not found";
            }
            if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
                createFailedPurchaseTransaction(request);
                return "Purchase failed: Card is not active";
            }
            
            BigDecimal availableCredit = card.getAvailableCredit();
            if (availableCredit == null || request.getAmount().compareTo(availableCredit) > 0) {
                createFailedPurchaseTransaction(request);
                return "Purchase failed: Insufficient available credit";
            }

            creditCardClient.applyPurchase(request.getCardNumber(), request.getAmount());

            try {
                transactionClient.createPurchaseTransaction(request);
            } catch (Exception txEx) {
                System.err.println("Transaction log failed: " + txEx.getMessage());
            }
            
            return "Purchase successful";
        } catch (Exception e) {
            return "Purchase failed: " + e.getMessage();
        }
    }

    // PAYMENT
    public String processPayment(PaymentRequest request) {
        if (request == null) return "Payment failed: Request cannot be null";
        if (request.getCardNumber() == null) return "Payment failed: Card number is required";
        if (request.getPaymentAmount() == null || request.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Payment failed: Invalid payment amount";
        }

        try {
            CardResponse card = creditCardClient.getCard(String.valueOf(request.getCardNumber()));
            if (card == null) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Card not found";
            }

            if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Card is not active";
            }

            BigDecimal outstanding = card.getOutstandingAmount() != null ? card.getOutstandingAmount() : BigDecimal.ZERO;
            if (request.getPaymentAmount().compareTo(outstanding) > 0) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Payment amount exceeds outstanding amount";
            }

            creditCardClient.applyPayment(String.valueOf(request.getCardNumber()), request.getPaymentAmount());

            // --- FIX: Using the correct, typed client method for payments ---
            try {
                transactionClient.createPaymentTransaction(request);
            } catch (Exception txEx) {
                System.err.println("Payment transaction log failed: " + txEx.getMessage());
                return "Payment successful (Transaction log failed: " + txEx.getMessage() + ")";
            }
            
            return "Payment successful";
        } catch (Exception e) {
            return "Payment failed: " + e.getMessage();
        }
    }

    private void createFailedPurchaseTransaction(PurchaseRequest request) {
        try {
            transactionClient.createPurchaseTransaction(request);
        } catch (Exception e) {
            System.out.println("Could not create failed purchase transaction: " + e.getMessage());
        }
    }

    private void createFailedPaymentTransaction(PaymentRequest request) {
        try {
            transactionClient.createPaymentTransaction(request);
        } catch (Exception e) { 
            System.out.println("Could not create failed payment transaction: " + e.getMessage());
        }
    }
}

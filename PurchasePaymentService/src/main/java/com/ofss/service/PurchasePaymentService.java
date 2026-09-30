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
        // 1. Validate request
        if (request == null) {
            return "Purchase failed: Request cannot be null";
        }
        if (request.getCardNumber() == null) {
            return "Purchase failed: Card number is required";
        }
        if (request.getMerchantId() == null) {
            return "Purchase failed: Merchant ID is required";
        }
        if (request.getAmount() == null ||request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Purchase failed: Invalid purchase amount";
        }
        try {
            // 2. Find card
            CardResponse card =creditCardClient.getCard(request.getCardNumber());
            if (card == null) { createFailedPurchaseTransaction(request, "FAILED");
                return "Purchase failed: Card not found";
            }
            // 3. Check card status
            if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
                createFailedPurchaseTransaction(request,"FAILED");
                return "Purchase failed: Card is not active";
            }
            // 4. Find merchant
//            MerchantResponse merchant = merchantClient.getMerchant(String.valueOf(request.getMerchantId()));
//            if (merchant == null) {
//                createFailedPurchaseTransaction(request,"FAILED");
//                return "Purchase failed: Merchant not found";
//            }
            
            MerchantResponse merchant = new MerchantResponse();
            
            // 5. Get available credit
            BigDecimal availableCredit =card.getAvailableCredit();
            if (availableCredit == null) {
                createFailedPurchaseTransaction( request,"FAILED");
                return "Purchase failed: Available credit not found";
            }
            // 6. Check available credit
            if (request.getAmount().compareTo(availableCredit) > 0) {
                createFailedPurchaseTransaction(request,"FAILED");
                return "Purchase failed: Insufficient available credit";
            }
            // 7. Calculate new available credit
            BigDecimal newAvailableCredit =availableCredit.subtract(request.getAmount());
            // 8. Calculate new outstanding amount
            BigDecimal currentOutstanding =card.getOutstandingAmount();
            if (currentOutstanding == null) {
                currentOutstanding = BigDecimal.ZERO;
            }
            BigDecimal newOutstanding = currentOutstanding.add(request.getAmount());
            // 9. Update available credit
            creditCardClient.updateAvailableCredit(
                    request.getCardNumber(),newAvailableCredit);
            // 10. Update outstanding amount
            creditCardClient.updateOutstanding(
                    request.getCardNumber(),newOutstanding);
            // 11. Create successful transaction
            transactionClient.createTransaction(
                    request.getCardNumber(),
                    request.getMerchantId(),request.getAmount(),"PURCHASE", "SUCCESS");
            return "Purchase successful";
        } catch (Exception e) {
            return "Purchase failed: " + e.getMessage();
        }
    }
    // PAYMENT
    public String processPayment(PaymentRequest request) {
        // 1. Validate request
        if (request == null) {
            return "Payment failed: Request cannot be null";
        }
        if (request.getCardNumber() == null) {
            return "Payment failed: Card number is required";
        }

        if (request.getPaymentAmount() == null ||
                request.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Payment failed: Invalid payment amount";
        }

        try {
            // 2. Find card
            CardResponse card =creditCardClient.getCard(String.valueOf(request.getCardNumber()));

            if (card == null) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Card not found";
            }

            // 3. Check card status
            if (!"ACTIVE".equalsIgnoreCase(card.getStatus())) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Card is not active";
            }

            // 4. Get outstanding amount
            BigDecimal outstanding =card.getOutstandingAmount();

            if (outstanding == null) {
                outstanding = BigDecimal.ZERO;
            }

            // 5. Check payment against outstanding
            if (request.getPaymentAmount().compareTo(outstanding) > 0) {
                createFailedPaymentTransaction(request);
                return "Payment failed: Payment amount exceeds outstanding amount";
            }

            // 6. Calculate new outstanding
            BigDecimal newOutstanding =outstanding.subtract(request.getPaymentAmount());

            // 7. Get available credit
            BigDecimal availableCredit =card.getAvailableCredit();

            if (availableCredit == null) {
                availableCredit = BigDecimal.ZERO;
            }
            // 8. Calculate new available credit
            BigDecimal newAvailableCredit = availableCredit.add(request.getPaymentAmount());
            // 9. Update outstanding
            creditCardClient.updateOutstanding(String.valueOf(request.getCardNumber()), newOutstanding);
            // 10. Update available credit
            creditCardClient.updateAvailableCredit(String.valueOf(request.getCardNumber()),newAvailableCredit);
            // 11. Create successful transaction
            transactionClient.createTransaction(String.valueOf(request.getCardNumber()),
                    null, request.getPaymentAmount(),
                    "PAYMENT","SUCCESS");
            return "Payment successful";

        } catch (Exception e) {
            return "Payment failed: " + e.getMessage();
        }
    }
    // FAILED PURCHASE
    private void createFailedPurchaseTransaction(
            PurchaseRequest request,String status) {

        try {
            transactionClient.createTransaction(
                    request.getCardNumber(),request.getMerchantId(),
                    request.getAmount(),"PURCHASE",status);

        } catch (Exception e) {
        		System.out.println(
                    "Could not create failed purchase transaction: "
                    + e.getMessage());
        }
    }
    // FAILED PAYMENT
    private void createFailedPaymentTransaction(PaymentRequest request) {

        try {
        	transactionClient.createTransaction(String.valueOf(request.getCardNumber()),
                    null, request.getPaymentAmount(),
                    "PAYMENT","FAILED");

        } catch (Exception e) { System.out.println("Could not create failed payment transaction: "
                    + e.getMessage());
        }
    }
}
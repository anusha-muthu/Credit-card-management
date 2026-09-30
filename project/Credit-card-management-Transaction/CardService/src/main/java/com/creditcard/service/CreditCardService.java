package com.creditcard.service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.creditcard.entity.CreditCard;
import com.creditcard.repository.CreditCardRepository;
import java.math.BigDecimal;
@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final RestTemplate restTemplate;

    public CreditCardService(CreditCardRepository creditCardRepository, RestTemplate restTemplate) {
        this.creditCardRepository = creditCardRepository;
        this.restTemplate = restTemplate;
    }

    public CreditCard addCard(CreditCard creditCard) {
//        validateCustomerExists(creditCard.getCustomerId());

        if (creditCardRepository.existsById(creditCard.getCardNumber())) {
            throw new RuntimeException("Card already exists with number: " + creditCard.getCardNumber());
        }

        if (creditCard.getAvailableCredit() == null) {
            creditCard.setAvailableCredit(creditCard.getCreditLimit());
        }

        if (creditCard.getOutstandingAmount() == null) {
            creditCard.setOutstandingAmount(BigDecimal.ZERO);
        }

        if (creditCard.getCardStatus() == null || creditCard.getCardStatus().isBlank()) {
            creditCard.setCardStatus("ACTIVE");
        }

        return creditCardRepository.save(creditCard);
    }
    public List<CreditCard> getAllCards() {
        return creditCardRepository.findAll();
    }
    public BigDecimal getAvailableCredit(String cardNumber) {
        CreditCard card = getCardByNumber(cardNumber);
        return card.getAvailableCredit();
    }

    public BigDecimal getOutstandingAmount(String cardNumber) {
        CreditCard card = getCardByNumber(cardNumber);
        return card.getOutstandingAmount();
    }

    public CreditCard getCardByNumber(String cardNumber) {
        return creditCardRepository.findById(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found with number: " + cardNumber));
    }

    public List<CreditCard> getCardsByCustomerId(Long customerId) {
        return creditCardRepository.findByCustomerId(customerId);
    }

    public CreditCard updateCard(String cardNumber, CreditCard updatedCard) {
        CreditCard existingCard = getCardByNumber(cardNumber);

        existingCard.setCustomerId(updatedCard.getCustomerId());
        existingCard.setCardType(updatedCard.getCardType());
        existingCard.setCreditLimit(updatedCard.getCreditLimit());
        existingCard.setAvailableCredit(updatedCard.getAvailableCredit());
        existingCard.setOutstandingAmount(updatedCard.getOutstandingAmount());
        existingCard.setExpiryDate(updatedCard.getExpiryDate());
        existingCard.setCardStatus(updatedCard.getCardStatus());

        return creditCardRepository.save(existingCard);
    }

    public void deleteCard(String cardNumber) {
        CreditCard existingCard = getCardByNumber(cardNumber);
        creditCardRepository.delete(existingCard);
    }

    public CreditCard blockCard(String cardNumber) {
        CreditCard card = getCardByNumber(cardNumber);
        card.setCardStatus("BLOCKED");
        return creditCardRepository.save(card);
    }

    public CreditCard unblockCard(String cardNumber) {
        CreditCard card = getCardByNumber(cardNumber);
        card.setCardStatus("ACTIVE");
        return creditCardRepository.save(card);
    }
    private void validateCustomerExists(Long customerId) {
        try {
            restTemplate.getForObject("http://localhost:8081/api/customers/" + customerId, Object.class);
        } catch (Exception ex) {
            throw new RuntimeException("Customer does not exist with id: " + customerId);
        }
    }
    // 1. Add this annotation to ensure database persistence commits properly
    @org.springframework.transaction.annotation.Transactional
    public CreditCard applyPurchase(String cardNumber, BigDecimal amount) {
        CreditCard card = getCardByNumber(cardNumber);

        // Safe status check
        if (card.getCardStatus() == null || !"ACTIVE".equalsIgnoreCase(card.getCardStatus())) {
            throw new RuntimeException("Card is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Purchase amount must be greater than 0");
        }

        // --- FIX: Safe Null Checks for numeric values ---
        BigDecimal currentAvailable = card.getAvailableCredit() != null ? card.getAvailableCredit() : card.getCreditLimit();
        BigDecimal currentOutstanding = card.getOutstandingAmount() != null ? card.getOutstandingAmount() : BigDecimal.ZERO;

        if (currentAvailable == null) {
            throw new RuntimeException("Card has no credit limit defined");
        }

        if (currentAvailable.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient available credit");
        }

        card.setAvailableCredit(currentAvailable.subtract(amount));
        card.setOutstandingAmount(currentOutstanding.add(amount));

        return creditCardRepository.save(card);
    }

    // 2. Add this annotation here as well
    @org.springframework.transaction.annotation.Transactional
    public CreditCard applyPayment(String cardNumber, BigDecimal amount) {
        CreditCard card = getCardByNumber(cardNumber);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be greater than 0");
        }

        // --- FIX: Safe Null Checks for numeric values ---
        BigDecimal currentAvailable = card.getAvailableCredit() != null ? card.getAvailableCredit() : card.getCreditLimit();
        BigDecimal currentOutstanding = card.getOutstandingAmount() != null ? card.getOutstandingAmount() : BigDecimal.ZERO;

        if (currentOutstanding.compareTo(amount) < 0) {
            throw new RuntimeException("Payment amount cannot be greater than outstanding amount");
        }

        card.setOutstandingAmount(currentOutstanding.subtract(amount));
        card.setAvailableCredit(currentAvailable.add(amount));

        return creditCardRepository.save(card);
    }

}
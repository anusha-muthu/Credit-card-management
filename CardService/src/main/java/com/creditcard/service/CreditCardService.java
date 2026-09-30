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
    public CreditCard applyPurchase(String cardNumber, BigDecimal amount) {
        CreditCard card = getCardByNumber(cardNumber);

        if (!"ACTIVE".equalsIgnoreCase(card.getCardStatus())) {
            throw new RuntimeException("Card is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Purchase amount must be greater than 0");
        }

        if (card.getAvailableCredit().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient available credit");
        }

        card.setAvailableCredit(card.getAvailableCredit().subtract(amount));
        card.setOutstandingAmount(card.getOutstandingAmount().add(amount));

        return creditCardRepository.save(card);
    }

    public CreditCard applyPayment(String cardNumber, BigDecimal amount) {
        CreditCard card = getCardByNumber(cardNumber);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be greater than 0");
        }

        if (card.getOutstandingAmount().compareTo(amount) < 0) {
            throw new RuntimeException("Payment amount cannot be greater than outstanding amount");
        }

        card.setOutstandingAmount(card.getOutstandingAmount().subtract(amount));
        card.setAvailableCredit(card.getAvailableCredit().add(amount));

        return creditCardRepository.save(card);
    }
}
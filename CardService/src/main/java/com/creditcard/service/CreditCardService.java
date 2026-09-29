package com.creditcard.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.creditcard.entity.CreditCard;
import com.creditcard.repository.CreditCardRepository;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCard addCard(CreditCard creditCard) {
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
}
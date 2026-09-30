package com.creditcard.controller;
import java.math.BigDecimal;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creditcard.entity.CreditCard;
import com.creditcard.service.CreditCardService;
@RestController
@RequestMapping("/api/cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping
    public ResponseEntity<CreditCard> addCard(@Valid @RequestBody CreditCard creditCard) {
        CreditCard savedCard = creditCardService.addCard(creditCard);
        return new ResponseEntity<>(savedCard, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCards() {
        return ResponseEntity.ok(creditCardService.getAllCards());
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<CreditCard> getCardByNumber(@PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.getCardByNumber(cardNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CreditCard>> getCardsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(creditCardService.getCardsByCustomerId(customerId));
    }

    @PutMapping("/{cardNumber}")
    public ResponseEntity<CreditCard> updateCard(
            @PathVariable String cardNumber,
            @RequestBody CreditCard creditCard) {

        return ResponseEntity.ok(creditCardService.updateCard(cardNumber, creditCard));
    }

    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<String> deleteCard(@Valid @PathVariable String cardNumber) {
        creditCardService.deleteCard(cardNumber);
        return ResponseEntity.ok("Card deleted successfully");
    }

    @PutMapping("/{cardNumber}/block")
    public ResponseEntity<CreditCard> blockCard(@Valid @PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.blockCard(cardNumber));
    }

    @PutMapping("/{cardNumber}/unblock")
    public ResponseEntity<CreditCard> unblockCard(@Valid @PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.unblockCard(cardNumber));
    }
    @GetMapping("/{cardNumber}/available-credit")
    public ResponseEntity<BigDecimal> getAvailableCredit(@PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.getAvailableCredit(cardNumber));
    }

    @GetMapping("/{cardNumber}/outstanding")
    public ResponseEntity<BigDecimal> getOutstandingAmount(@PathVariable String cardNumber) {
        return ResponseEntity.ok(creditCardService.getOutstandingAmount(cardNumber));
    }
}
package com.ofss.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ofss.dto.PaymentRequest;
import com.ofss.dto.PurchaseRequest;
import com.ofss.service.PurchasePaymentService;

@RestController
@RequestMapping("/api")
public class PaymentPurchaseController {

    private final PurchasePaymentService purchasePaymentService;

    public PaymentPurchaseController(PurchasePaymentService purchasePaymentService) {

        this.purchasePaymentService = purchasePaymentService;
    }

    @PostMapping("/purchases")
    public ResponseEntity<String> purchase( @RequestBody PurchaseRequest request) {

        String result =purchasePaymentService.processPurchase(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/payments")
    public ResponseEntity<String> payment( @RequestBody PaymentRequest request) {

        String result = purchasePaymentService.processPayment(request);

        return ResponseEntity.ok(result);
    }
}
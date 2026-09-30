package com.ofss.transaction.controller;

import com.ofss.transaction.dto.PaymentRequest;
import com.ofss.transaction.dto.PurchaseRequest;
import com.ofss.transaction.dto.TransactionResponse;
import com.ofss.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService
    ) {
        this.transactionService = transactionService;
    }

    /*
     * PURCHASE
     */
    @PostMapping("/purchase")
    public ResponseEntity<TransactionResponse> purchase(
            @Valid @RequestBody PurchaseRequest request
    ) {

        TransactionResponse response =
                transactionService.makePurchase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * PAYMENT
     */
    @PostMapping("/payment")
    public ResponseEntity<TransactionResponse> payment(
            @Valid @RequestBody PaymentRequest request
    ) {

        TransactionResponse response =
                transactionService.makePayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * GET TRANSACTION BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                transactionService.getTransaction(id)
        );
    }

    /*
     * GET ALL
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }

    /*
     * GET BY CARD
     */
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<List<TransactionResponse>> getByCard(
            @PathVariable String cardNumber
    ) {

        return ResponseEntity.ok(
                transactionService.getByCard(cardNumber)
        );
    }

    /*
     * GET BY MERCHANT
     */
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TransactionResponse>> getByMerchant(
            @PathVariable Long merchantId
    ) {

        return ResponseEntity.ok(
                transactionService.getByMerchant(merchantId)
        );
    }

    /*
     * GET BY DATE RANGE
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getByDateRange(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to

    ) {

        return ResponseEntity.ok(
                transactionService.getByDateRange(from, to)
        );
    }

    /*
     * REPORT 11
     */
    @GetMapping("/reports/today/purchases")
    public ResponseEntity<BigDecimal> todayPurchases() {

        return ResponseEntity.ok(
                transactionService.getTodayPurchaseAmount()
        );
    }

    /*
     * REPORT 12
     */
    @GetMapping("/reports/today/payments")
    public ResponseEntity<BigDecimal> todayPayments() {

        return ResponseEntity.ok(
                transactionService.getTodayPaymentAmount()
        );
    }

    /*
     * REPORT 7
     */
    @GetMapping("/reports/merchant/highest-sales")
    public ResponseEntity<Map<String, Object>> highestSalesMerchant() {

        return ResponseEntity.ok(
                transactionService.getHighestSalesMerchant()
        );
    }

    /*
     * REPORT 8
     */
    @GetMapping("/reports/merchant/highest-transactions")
    public ResponseEntity<Map<String, Object>> highestTransactionMerchant() {

        return ResponseEntity.ok(
                transactionService.getHighestTransactionMerchant()
        );
    }

    /*
     * REPORT 9
     */
    @GetMapping("/reports/card/most-used")
    public ResponseEntity<Map<String, Object>> mostUsedCard() {

        return ResponseEntity.ok(
                transactionService.getMostUsedCard()
        );
    }

    /*
     * REPORT 10
     */
    @GetMapping("/reports/card/least-used")
    public ResponseEntity<Map<String, Object>> leastUsedCard() {

        return ResponseEntity.ok(
                transactionService.getLeastUsedCard()
        );
    }

    /*
     * REPORT 18
     */
    @GetMapping("/reports/purchase/average")
    public ResponseEntity<BigDecimal> averagePurchase() {

        return ResponseEntity.ok(
                transactionService.getAveragePurchaseAmount()
        );
    }

    /*
     * REPORT 19
     */
    @GetMapping("/reports/purchase/largest")
    public ResponseEntity<TransactionResponse> largestPurchase() {

        return ResponseEntity.ok(
                transactionService.getLargestPurchase()
        );
    }

    /*
     * REPORT 20
     */
    @GetMapping("/reports/customer/monthly-spending")
    public ResponseEntity<List<Map<String, Object>>> monthlyCustomerSpending() {

        return ResponseEntity.ok(
                transactionService.getMonthlyCustomerSpending()
        );
    }
}
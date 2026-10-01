package com.ofss.transaction.service;

import com.ofss.transaction.client.CardServiceClient;
import com.ofss.transaction.client.MerchantServiceClient;
import com.ofss.transaction.dto.*;
import com.ofss.transaction.entity.TransactionEntity;
import com.ofss.transaction.enums.TransactionStatus;
import com.ofss.transaction.enums.TransactionType;
import com.ofss.transaction.exception.ResourceNotFoundException;
import com.ofss.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardServiceClient cardServiceClient;
    private final MerchantServiceClient merchantServiceClient;

    public TransactionService(
            TransactionRepository transactionRepository,
            CardServiceClient cardServiceClient,
            MerchantServiceClient merchantServiceClient
    ) {
        this.transactionRepository = transactionRepository;
        this.cardServiceClient = cardServiceClient;
        this.merchantServiceClient = merchantServiceClient;
    }

    @Transactional
    public TransactionResponse makePurchase(PurchaseRequest request) {

        LocalDateTime now = LocalDateTime.now();

        try {

            // 1. Verify merchant exists
            merchantServiceClient.getMerchant(request.merchantId());

            // 2. Ask Credit Card Service to process the purchase.
            // Card service is responsible for:
            // - card exists
            // - card is ACTIVE
            // - sufficient available credit
            // - deduct available credit
            // - increase outstanding amount
            CardBalanceResponse cardResponse =
                    cardServiceClient.applyPurchase(
                            request.cardNumber(),
                            request.amount()
                    );

            // 3. Save SUCCESS transaction
            TransactionEntity transaction =
                    new TransactionEntity();

            transaction.setCardNumber(request.cardNumber());
            transaction.setCustomerId(cardResponse.customerId());
            transaction.setTransactionType(TransactionType.PURCHASE);
            transaction.setAmount(request.amount());
            transaction.setMerchantId(request.merchantId());
            transaction.setTransactionDateTime(now);
            transaction.setStatus(TransactionStatus.SUCCESS);

            TransactionEntity saved =
                    transactionRepository.save(transaction);

            return TransactionResponse.fromEntity(saved);

        } catch (RuntimeException ex) {

            /*
             * Every failed purchase must also be logged.
             *
             * We may not know customerId if card validation itself failed,
             * so use null when the remote card service could not provide it.
             */
            TransactionEntity failedTransaction =
                    new TransactionEntity();

            failedTransaction.setCardNumber(request.cardNumber());
            failedTransaction.setCustomerId(resolveCustomerId(request.cardNumber()));
            failedTransaction.setTransactionType(TransactionType.PURCHASE);
            failedTransaction.setAmount(request.amount());
            failedTransaction.setMerchantId(request.merchantId());
            failedTransaction.setTransactionDateTime(now);
            failedTransaction.setStatus(TransactionStatus.FAILED);

            transactionRepository.save(failedTransaction);

            throw ex;
        }
    }

    @Transactional
    public TransactionResponse makePayment(PaymentRequest request) {

        LocalDateTime now = LocalDateTime.now();

        try {

            // Credit Card Service checks:
            // - card exists
            // - card is ACTIVE
            // - payment amount <= outstanding amount
            // - reduce outstanding
            // - increase available credit
            CardBalanceResponse cardResponse =
                    cardServiceClient.applyPayment(
                            request.cardNumber(),
                            request.amount()
                    );

            TransactionEntity transaction =
                    new TransactionEntity();

            transaction.setCardNumber(request.cardNumber());
            transaction.setCustomerId(cardResponse.customerId());
            transaction.setTransactionType(TransactionType.PAYMENT);
            transaction.setAmount(request.amount());
            transaction.setMerchantId(null);
            transaction.setTransactionDateTime(now);
            transaction.setStatus(TransactionStatus.SUCCESS);

            TransactionEntity saved =
                    transactionRepository.save(transaction);

            return TransactionResponse.fromEntity(saved);

        } catch (RuntimeException ex) {

            TransactionEntity failedTransaction =
                    new TransactionEntity();

            failedTransaction.setCardNumber(request.cardNumber());
            failedTransaction.setCustomerId(resolveCustomerId(request.cardNumber()));
            failedTransaction.setTransactionType(TransactionType.PAYMENT);
            failedTransaction.setAmount(request.amount());
            failedTransaction.setMerchantId(null);
            failedTransaction.setTransactionDateTime(now);
            failedTransaction.setStatus(TransactionStatus.FAILED);

            transactionRepository.save(failedTransaction);

            throw ex;
        }
    }

    public TransactionResponse getTransaction(Long id) {

        TransactionEntity transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found: " + id
                                )
                        );

        return TransactionResponse.fromEntity(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public List<TransactionResponse> getByCard(
            String cardNumber
    ) {

        return transactionRepository
                .findByCardNumberOrderByTransactionDateTimeDesc(cardNumber)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public List<TransactionResponse> getByMerchant(
            Long merchantId
    ) {

        return transactionRepository
                .findByMerchantIdOrderByTransactionDateTimeDesc(merchantId)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public List<TransactionResponse> getByDateRange(
            LocalDateTime from,
            LocalDateTime to
    ) {

        return transactionRepository
                .findByTransactionDateTimeBetweenOrderByTransactionDateTimeDesc(
                        from,
                        to
                )
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public BigDecimal getTodayPurchaseAmount() {

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return transactionRepository.totalByTypeAndDateRange(
                TransactionType.PURCHASE,
                TransactionStatus.SUCCESS,
                start,
                end
        );
    }

    public BigDecimal getTodayPaymentAmount() {

        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        return transactionRepository.totalByTypeAndDateRange(
                TransactionType.PAYMENT,
                TransactionStatus.SUCCESS,
                start,
                end
        );
    }

    public Map<String, Object> getHighestSalesMerchant() {

        List<Object[]> result =
                transactionRepository.findMerchantWithHighestSales();

        if (result.isEmpty()) {
            return Map.of("message", "No successful purchase transactions found");
        }

        Object[] row = result.getFirst();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("merchantId", ((Number) row[0]).longValue());
        response.put("totalSales", row[1]);

        return response;
    }

    public Map<String, Object> getHighestTransactionMerchant() {

        List<Object[]> result =
                transactionRepository.findMerchantWithHighestTransactions();

        if (result.isEmpty()) {
            return Map.of("message", "No successful purchase transactions found");
        }

        Object[] row = result.getFirst();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("merchantId", ((Number) row[0]).longValue());
        response.put("transactionCount", row[1]);

        return response;
    }

    public Map<String, Object> getMostUsedCard() {

        List<Object[]> result =
                transactionRepository.findMostFrequentlyUsedCard();

        if (result.isEmpty()) {
            return Map.of("message", "No successful transactions found");
        }

        Object[] row = result.getFirst();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("cardNumber", row[0]);
        response.put("transactionCount", row[1]);

        return response;
    }

    public Map<String, Object> getLeastUsedCard() {

        List<Object[]> result =
                transactionRepository.findLeastFrequentlyUsedCard();

        if (result.isEmpty()) {
            return Map.of("message", "No successful transactions found");
        }

        Object[] row = result.getFirst();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("cardNumber", row[0]);
        response.put("transactionCount", row[1]);

        return response;
    }

    public BigDecimal getAveragePurchaseAmount() {

        return transactionRepository.averageAmount(
                TransactionType.PURCHASE,
                TransactionStatus.SUCCESS
        );
    }

    public TransactionResponse getLargestPurchase() {

        List<TransactionEntity> result =
                transactionRepository.findLargestTransactions(
                        TransactionType.PURCHASE,
                        TransactionStatus.SUCCESS
                );

        if (result.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No successful purchase transactions found"
            );
        }

        return TransactionResponse.fromEntity(
                result.getFirst()
        );
    }

    public List<Map<String, Object>> getMonthlyCustomerSpending() {

        List<Object[]> rows =
                transactionRepository.findMonthlyCustomerSpending();

        return rows.stream()
                .map(row -> {

                    Map<String, Object> result =
                            new LinkedHashMap<>();

                    result.put(
                            "customerId",
                            ((Number) row[0]).longValue()
                    );

                    result.put(
                            "year",
                            ((Number) row[1]).intValue()
                    );

                    result.put(
                            "month",
                            ((Number) row[2]).intValue()
                    );

                    result.put(
                            "totalSpending",
                            row[3]
                    );

                    return result;
                })
                .toList();
    }

    /*
     * This fallback is intentionally simple for the starter implementation.
     *
     * Ideally the Card Service exposes:
     * GET /api/cards/{cardNumber}/summary
     *
     * and returns customerId even when the card is blocked.
     */
    private Long resolveCustomerId(String cardNumber) {

        try {
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
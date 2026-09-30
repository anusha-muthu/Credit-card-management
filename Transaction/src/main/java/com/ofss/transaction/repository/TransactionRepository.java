package com.ofss.transaction.repository;

import com.ofss.transaction.entity.TransactionEntity;
import com.ofss.transaction.enums.TransactionStatus;
import com.ofss.transaction.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByCardNumberOrderByTransactionDateTimeDesc(
            String cardNumber
    );

    List<TransactionEntity> findByMerchantIdOrderByTransactionDateTimeDesc(
            Long merchantId
    );

    List<TransactionEntity> findByTransactionDateTimeBetweenOrderByTransactionDateTimeDesc(
            LocalDateTime from,
            LocalDateTime to
    );

    Optional<TransactionEntity> findTopByCardNumberAndTransactionTypeAndStatusOrderByTransactionDateTimeDesc(
            String cardNumber,
            TransactionType transactionType,
            TransactionStatus status
    );

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM TransactionEntity t
        WHERE t.transactionType = :type
          AND t.status = :status
          AND t.transactionDateTime >= :start
          AND t.transactionDateTime < :end
    """)
    BigDecimal totalByTypeAndDateRange(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT COUNT(t)
        FROM TransactionEntity t
        WHERE t.transactionType = :type
          AND t.status = :status
    """)
    long countByTypeAndStatus(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status
    );

    @Query("""
        SELECT COALESCE(AVG(t.amount), 0)
        FROM TransactionEntity t
        WHERE t.transactionType = :type
          AND t.status = :status
    """)
    BigDecimal averageAmount(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status
    );

    @Query("""
        SELECT t
        FROM TransactionEntity t
        WHERE t.transactionType = :type
          AND t.status = :status
        ORDER BY t.amount DESC
    """)
    List<TransactionEntity> findLargestTransactions(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status
    );

    @Query(value = """
        SELECT merchant_id, SUM(amount) AS total_sales
        FROM cc_transaction
        WHERE transaction_type = 'PURCHASE'
          AND status = 'SUCCESS'
          AND merchant_id IS NOT NULL
        GROUP BY merchant_id
        ORDER BY total_sales DESC
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    List<Object[]> findMerchantWithHighestSales();

    @Query(value = """
        SELECT merchant_id, COUNT(*) AS transaction_count
        FROM cc_transaction
        WHERE transaction_type = 'PURCHASE'
          AND status = 'SUCCESS'
          AND merchant_id IS NOT NULL
        GROUP BY merchant_id
        ORDER BY transaction_count DESC
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    List<Object[]> findMerchantWithHighestTransactions();

    @Query(value = """
        SELECT card_number, COUNT(*) AS transaction_count
        FROM cc_transaction
        WHERE status = 'SUCCESS'
        GROUP BY card_number
        ORDER BY transaction_count DESC
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    List<Object[]> findMostFrequentlyUsedCard();

    @Query(value = """
        SELECT card_number, COUNT(*) AS transaction_count
        FROM cc_transaction
        WHERE status = 'SUCCESS'
        GROUP BY card_number
        ORDER BY transaction_count ASC
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    List<Object[]> findLeastFrequentlyUsedCard();

    @Query(value = """
        SELECT customer_id,
               EXTRACT(YEAR FROM transaction_date_time) AS transaction_year,
               EXTRACT(MONTH FROM transaction_date_time) AS transaction_month,
               SUM(amount) AS total_spending
        FROM cc_transaction
        WHERE transaction_type = 'PURCHASE'
          AND status = 'SUCCESS'
        GROUP BY customer_id,
                 EXTRACT(YEAR FROM transaction_date_time),
                 EXTRACT(MONTH FROM transaction_date_time)
        ORDER BY customer_id,
                 transaction_year,
                 transaction_month
        """, nativeQuery = true)
    List<Object[]> findMonthlyCustomerSpending();
}
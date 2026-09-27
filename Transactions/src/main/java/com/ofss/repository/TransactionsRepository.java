package com.ofss.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.entity.TransactionsTable;
public interface TransactionsRepository extends JpaRepository <TransactionsTable, Long>{
	List<TransactionsTable> findByMerchantId(Long merchandId);
	List<TransactionsTable> findByCardNumber(Long cardNumber);
}

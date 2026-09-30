package com.ofss.dto;

import java.math.BigDecimal;


public class PurchaseRequest {
	

	private String cardNumber;
	private Long merchantId;
	private BigDecimal amount;
	public PurchaseRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PurchaseRequest(String cardNumber, Long merchantId, BigDecimal amount) {
		super();
		this.cardNumber = cardNumber;
		this.merchantId = merchantId;
		this.amount = amount;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "PurchaseRequest [cardNumber=" + cardNumber + ", merchantId=" + merchantId + ", amount=" + amount + "]";
	}
	

}

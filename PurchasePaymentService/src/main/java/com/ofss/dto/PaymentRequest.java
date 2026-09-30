package com.ofss.dto;

import java.math.BigDecimal;

public class PaymentRequest {
	private Long cardNumber;
	private BigDecimal paymentAmount;
	public PaymentRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PaymentRequest(Long cardNumber, BigDecimal paymentAmount) {
		super();
		this.cardNumber = cardNumber;
		this.paymentAmount = paymentAmount;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	@Override
	public String toString() {
		return "PaymentRequest [cardNumber=" + cardNumber + ", paymentAmount=" + paymentAmount + "]";
	}
	
	
	
}

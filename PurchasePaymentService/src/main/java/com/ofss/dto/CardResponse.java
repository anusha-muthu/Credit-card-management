package com.ofss.dto;

import java.math.BigDecimal;

public class CardResponse {
	
	private Long cardNumber;
	private String status;
	private BigDecimal availableCredit;
	private BigDecimal outstandingAmount;
	public CardResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardResponse(Long cardNumber, String status, BigDecimal availableCredit, BigDecimal outstandingAmount) {
		super();
		this.cardNumber = cardNumber;
		this.status = status;
		this.availableCredit = availableCredit;
		this.outstandingAmount = outstandingAmount;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getAvailableCredit() {
		return availableCredit;
	}
	public void setAvailableCredit(BigDecimal availableCredit) {
		this.availableCredit = availableCredit;
	}
	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(BigDecimal outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	@Override
	public String toString() {
		return "CardResponse [cardNumber=" + cardNumber + ", status=" + status + ", availableCredit=" + availableCredit
				+ ", outstandingAmount=" + outstandingAmount + "]";
	}
	
	
}

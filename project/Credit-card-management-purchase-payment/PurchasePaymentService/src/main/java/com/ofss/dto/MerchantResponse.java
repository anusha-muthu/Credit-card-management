package com.ofss.dto;

public class MerchantResponse {
	
	private Long merchantId;
	private String merchantName;
	public MerchantResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MerchantResponse(Long merchantId, String merchantName) {
		super();
		this.merchantId = merchantId;
		this.merchantName = merchantName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	@Override
	public String toString() {
		return "MerchantResponse [merchantId=" + merchantId + ", merchantName=" + merchantName + "]";
	}
	
}

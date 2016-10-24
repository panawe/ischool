package com.esoft.ischool.util;

public enum AlertType {
	TUITION_PAYMENT_OVER_DUE("1"),  			
	PRODUCT_CONSUMER_OVER_DUE("3"),			
	PRODUCT_MINIMUM_STOCK_HIT("2");		
	
	private final String value;
	
	private AlertType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}

}


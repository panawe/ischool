package com.esoft.ischool.util;

public enum PurchaseOrderStatus {
	IN_PROCESS(1),			// Order en cours
	COMPLETED(3),			// Order completed
	SHIPPED(2),//shipped
	CANCELLED(4);			// Order cancelled.
	
	
	private final Integer value;
	
	private PurchaseOrderStatus(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}

}


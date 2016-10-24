package com.esoft.ischool.util;

public enum ProductConsumerStatus {
	PENDING_APPROVAL(1),  			// New order
	APPROVED(2),			// Order en cours
	REJECTED(3),			// Order completed
	DELIVERED(4),			// Order delivered.
	RETURNED(5);				// Order closed.

	private final Integer value;
	
	private ProductConsumerStatus(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}

}


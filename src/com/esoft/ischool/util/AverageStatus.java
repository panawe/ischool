package com.esoft.ischool.util;

public enum AverageStatus {
	
	CREATED_UNLOCKED((short)0),
	CREATED_LOCKED((short)1),
	PUBLISHED_UNLOCKED((short)2),
	PUBLISHED_LOCKED((short)3);
	
	private final Short value;
	
	private AverageStatus(Short value) {
		this.value = value;
	}
	
	public Short getValue() {
		return this.value;
	}

}

package com.esoft.ischool.util;

public enum MenuIdEnum {
	INFIRMERIE_CONFIG(new Long(45)),
	INFIRMERIE_CONSULTATION(new Long(44)),
	STUDENT(new Long(9)),  			
	TEACHER(new Long(10)),
	CORRESPONDENCE(new Long(11)),
	INSCRIPTIONS(new Long(12)),
	QUESTIONS(new Long(13)),
	ONLINE_TEST(new Long(14)),
	EXAM(new Long(15)),
	CALCUL_MOYENNE(new Long(16)),
	ONLINE_RANKING(new Long(17)),
	PURCHASE_ORDER(new Long(18)),
	PRODUCT(new Long(19)),
	PRODUCT_REQUEST(new Long(20)),
	STOCK_CONFIGURATION(new Long(21)),
	PAYMENT_DUE(new Long(22)),
	ALERTS(new Long(23)),
	TUITION(new Long(24)),
	SCHOOL(new Long(25)),
	TERM(new Long(26)),
	COURSE(new Long(27)),
	SECURITY(new Long(28)),
	AUTRE(new Long(29)),
	CREATE_SCHOOL(new Long(30)),
	IMPRIMER_BULLETIN(new Long(46)),
	ANNUAL_RESULTS(new Long(46)), 
	PARENT(new Long(50));
	private final Long value;
	
	private MenuIdEnum(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return this.value;
	}
	
	private String getIt() { return "";}

}


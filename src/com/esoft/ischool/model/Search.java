package com.esoft.ischool.model;

public class Search extends BaseEntity {

	private String firstName;
	private String lastName;
	private String userName;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
}

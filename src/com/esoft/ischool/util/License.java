package com.esoft.ischool.util;
import java.io.*;
import java.util.*;

public class License implements Serializable {
    private static final long serialVersionUID = 1L;
    private String macAddress;
    private Date expiration;
    private String nbrOfStudent;
    
/*	public License( String macAddress,
			Date expiration) {
		super();
		this.macAddress = macAddress;
		this.expiration = expiration;
	}*/
    
    
	public License() {
		// TODO Auto-generated constructor stub
	}
	public License(String macAddress, Date expiration, String nbrOfStudent) {
		super();
		this.macAddress = macAddress;
		this.expiration = expiration;
		this.nbrOfStudent = nbrOfStudent;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
 
	public String getNbrOfStudent() {
		return nbrOfStudent;
	}
	public void setNbrOfStudent(String nbrOfStudent) {
		this.nbrOfStudent = nbrOfStudent;
	}
	@Override
	public String toString() {
		return "License [macAddress=" + macAddress + ", expiration="
				+ expiration + ", nbrOfStudent=" + nbrOfStudent + "]";
	}


}

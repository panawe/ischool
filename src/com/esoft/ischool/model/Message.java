package com.esoft.ischool.model;

import java.util.Date;

public class Message implements Comparable{
	
	private String message;
	private String newsType;
	private Date newsDate;
	public int compareTo(Object arg0) {
		Message aNews= (Message)arg0;
		return this.newsDate.compareTo(aNews.getNewsDate());
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	public Date getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(Date newsDate) {
		this.newsDate = newsDate;
	}

}

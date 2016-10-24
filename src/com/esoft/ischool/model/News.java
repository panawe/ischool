package com.esoft.ischool.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.esoft.ischool.util.Utils;

@Table(name = "NEWS")
@Entity
public class News extends BaseEntity implements Comparable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NEWS_ID")
	private Long id;

	// @NotNull
	@Column(name = "TITLE")
	private String title;

	// @NotNull
	@Column(name = "MESSAGE")
	private String message;

	// @NotNull
	@Column(name = "NEWS_DATE")
	private Date newsDate;

	@Column(name = "AUTHOR")
	private String author;

	public int compareTo(Object arg0) {
		News aNews = (News) arg0;
		return this.newsDate.compareTo(aNews.getNewsDate());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(Date newsDate) {
		this.newsDate = newsDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Transient
	public String getShortMessage() {
		return message != null && message.length() > 170 ? Utils.truncateHTML(message, 170, null) : message;
	}

}

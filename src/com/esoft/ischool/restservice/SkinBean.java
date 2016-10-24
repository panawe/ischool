package com.esoft.ischool.restservice;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.util.Constants;

@Component("skinBean")
@Scope("session")
public class SkinBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String skin =Constants.DEFAULT_PAGE_SKIN;

	private String banner="images/classic.jpg";
	
	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
		this.banner="images/"+skin+"jpg";
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}
	
}
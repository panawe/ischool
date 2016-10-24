package com.esoft.ischool.restservice;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.esoft.ischool.model.News;
import com.esoft.ischool.model.News;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("newsBean")
@Scope("session")
public class NewsBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<News> newss;
	private News news = new News();
	private News news1;
	private News news2;
	private String selectedTab = "newsList";

	public String getSelectedTab() {
		return selectedTab;
	}

	public News getNews1() {
		return news1;
	}

	public void setNews1(News news1) {
		this.news1 = news1;
	}

	public News getNews2() {
		return news2;
	}

	public void setNews2(News news2) {
		this.news2 = news2;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public String validate() {

		return "succes";
	}

	public String selectNews() {
		news = (News) baseService.getById(News.class, getIdParameter());
		return "success";
	}

	@Override
	public String clear() {
		news = new News();
		setPicture(null);
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), News.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString(
					"DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString(
					"DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = news.getId();
		try {
			// get picture
			if (getPicture() != null) {
				news.setPicture(getPicture());
				if (news.getPicture().length > MAX_FILE_SIZE) {
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}
			if (id == null || id == 0)
				baseService.save(news, getCurrentUser());
			else
				baseService.update(news, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString(
					"SAVED_SUCCESSFULLY"));
			clear();
		} catch (Exception ex) {
			news.setId(id);
			setErrorMessage(ex, "Cette news existe deja");
		}
		selectedTab = "newsList";
		clear();
		getAll();
		return "Success";
	}

	public String edit() {
		clearMessages();
		news = (News) baseService.getById(News.class, getIdParameter());
		setPicture(news.getPicture());
		selectedTab = "newsDetail";
		return "Success";
	}

	@PostConstruct
	public void getAll() {
		newss = baseService
				.getAllSortedNews(getCurrentUser() == null ? baseService
						.getDefaultSchool() : getCurrentUser().getSchool());
		if (newss != null && newss.size() >= 1) {
			news1 = newss.get(0);
		}
		if (newss != null && newss.size() >= 2) {
			news2 = newss.get(1);
		}
		setRowCount(new Long(newss.size()));
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	public List<News> getNewss() {
		return newss;
	}

	public void setNewss(List<News> newss) {
		this.newss = newss;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public void paintNewsPic(OutputStream stream, Object object)
			throws IOException {
		Long id = (Long) object;
		if (id != null && id > 0) {
			News t = (News) baseService.getById(News.class, id);
			if (t != null && t.getPicture() != null) {
				stream.write(t.getPicture());
			} else {
				stream.write(new byte[] {});
			}
		}
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}

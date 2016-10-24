package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Rating;
import com.esoft.ischool.service.BaseService;

@Component("ratingBean")
@Scope("session")
public class RatingBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> ratings;
	private List<String> allRatings = new ArrayList<String>();
	
	private Rating rating = new Rating();

	public String validate() {
		return "succes";
	}
	
	public List<String> getAllRatings() {
		return allRatings;
	}

	public void setAllRatings(List<String> allRatings) {
		this.allRatings = allRatings;
	}

	@Override
	public String clear() {
		rating = new Rating();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Rating.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}


		return "Success";
	}
	
	public String insert() {
		clearMessages();
		Long id = rating.getId();
		try {
			if (id == null || id == 0)
				baseService.save(rating,getCurrentUser());
			else
				baseService.update(rating,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			rating.setId(id);
			setErrorMessage(ex,getRessourceProperty("insertSuccess"));
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		rating = (Rating) baseService.getById(Rating.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		ratings = baseService.loadAll(Rating.class,getCurrentUser().getSchool());
		setRowCount(new Long(ratings.size()));
		for(BaseEntity entity:ratings){
			Rating rating = (Rating) entity;
			allRatings.add(rating.getName());
		}
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

	public List<BaseEntity> getRatings() {
		return ratings;
	}

	public void setRatings(List<BaseEntity> ratings) {
		this.ratings = ratings;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}	
}

package com.esoft.ischool.restservice;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Position;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("positionBean")
@Scope("session")
public class PositionBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> positions;
	
	private Position position = new Position();

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		position = new Position();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Position.class);
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
		Long id = position.getId();
		try {
			if (id == null || id == 0)
				baseService.save(position,getCurrentUser());
			else
				baseService.update(position,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			position.setId(id);
			setErrorMessage(ex,"Cette position existe deja");
		}

		
		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		position = (Position) baseService.getById(Position.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		positions = baseService.loadAll(Position.class,baseService.getDefaultSchool());
		setRowCount(new Long(positions.size()));

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

	public List<BaseEntity> getPositions() {
		return positions;
	}

	public void setPositions(List<BaseEntity> positions) {
		this.positions = positions;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}

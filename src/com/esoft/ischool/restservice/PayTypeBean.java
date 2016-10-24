package com.esoft.ischool.restservice;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.PayType;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.util.MenuIdEnum;

@Component("payTypeBean")
@Scope("session")
public class PayTypeBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> payTypes = new ArrayList<BaseEntity>();
	private List<String> allPayTypes = new ArrayList<String>();
	
	private PayType payType = new PayType();

	public List<PayType> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<PayType> result = new ArrayList<PayType>();
		allPayTypes.clear();
		
		for (BaseEntity entity : payTypes) {
			PayType pt = (PayType) entity;
			if ((pt.getName() != null && pt.getName().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(pt);
			
			allPayTypes.add(pt.getName());
		}

		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		payType = new PayType();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), PayType.class);
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
		Long id = payType.getId();
		try {
			if (id == null || id == 0)
				baseService.save(payType,getCurrentUser());
			else
				baseService.update(payType,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			payType.setId(id);
			setErrorMessage(ex,"Ce type de payment existe deja");
		}

		clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		payType = (PayType) baseService.getById(PayType.class, getIdParameter());
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		payTypes = baseService.loadAll(PayType.class,baseService.getDefaultSchool());
		setRowCount(new Long(payTypes.size()));

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

	public List<BaseEntity> getPayTypes() {
		return payTypes;
	}

	public void setPayTypes(List<BaseEntity> payTypes) {
		this.payTypes = payTypes;
	}

	public List<String> getAllPayTypes() {
		autoComplete("");
		return allPayTypes;
	}

	public void setAllPayTypes(List<String> allPayTypes) {
		this.allPayTypes = allPayTypes;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}
	
	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.AUTRE.getValue());
	}

}

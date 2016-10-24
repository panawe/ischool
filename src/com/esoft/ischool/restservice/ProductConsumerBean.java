package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.bean.CourseBean;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.security.model.User;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Product;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.ProductConsumerStatus;

@Component("productConsumerBean")
@Scope("session")
public class ProductConsumerBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;

	@Autowired
	@Qualifier("config")
	private Config config;

	private Long rowCount;
	private List<BaseEntity> productConsumers = new ArrayList<BaseEntity>();
	private List<String> allProductConsumers = new ArrayList<String>();

	private ProductConsumer productConsumer = new ProductConsumer();
	private String consumerName = "";
	private String productName = "";
	private String status = "";

	private Integer numberOfDays;
	private String action;
	private String selectedTab = "productConsumerListTab";

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<ProductConsumer> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<ProductConsumer> result = new ArrayList<ProductConsumer>();
		return result;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		setConsumerName("");
		setProductName("");
		productConsumer = new ProductConsumer();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), ProductConsumer.class);
			getAll();
			clear();
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL") + " :\n" + ex.getMessage());
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = productConsumer.getId();
		try {
			// Product product = (Product) baseService.findByName(Product.class,
			// productName);
			// productConsumer.setProduct(product);

			if (productConsumer.getRequestDate()!= null && productConsumer.getPickupDate() != null
					&& productConsumer.getRequestDate().after(productConsumer.getPickupDate()))	{
				setErrorMessage(getResourceBundle().getString("REQUEST_DATE_INVALID"));
				throw new Exception();
			}
			
			if (id == null || id == 0) {
				if (getSessionParameter("user") != null && productConsumer.getConsumer() == null) {
					User consumer = (User) getSessionParameter("user");
					productConsumer.setConsumer(consumer);
				}
				productConsumer.setStatus(ProductConsumerStatus.PENDING_APPROVAL.getValue());
				productConsumer.setPickupDate(new Date());
				baseService.save(productConsumer, getCurrentUser());
			}
			/*
			 * else if
			 * (ProductConsumerStatus.COMPLETED.getValue().equals(productConsumer
			 * .getStatus())) baseService.saveProductConsumer(productConsumer,
			 * productConsumer.getQuantityAccepted(),getCurrentUser());
			 */
			else {
				ProductConsumer pc = (ProductConsumer) baseService.getById(ProductConsumer.class, id);
				productConsumer.setStatus(pc.getStatus());
				baseService.update(productConsumer, getCurrentUser());
			}
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();

		} catch (Exception ex) {
			productConsumer.setId(id);
			if (StringUtils.isEmpty(getErrorMessage()) )
				setErrorMessage(ex, "Ce productConsumer existe deja");
		}

		return "Success";
	}

	public String edit() {
		setAction("CREATE");
		clearMessages();
		productConsumer = (ProductConsumer) baseService.getById(
				ProductConsumer.class, getIdParameter());
		setProductName(productConsumer.getProduct().getName());
		productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey(
				"CONSUMER_STATUS", productConsumer.getStatus().toString()));
		selectedTab = "productConsumerDetailsTab";
		return "Success";
	}

	public String approve() {
		try {
			setAction("APPROVE");
			clearMessages();
			productConsumer = (ProductConsumer) baseService.getById(
					ProductConsumer.class, getIdParameter());
			productConsumer.setStatus(ProductConsumerStatus.APPROVED.getValue());
			baseService.saveProductConsumer(productConsumer,
					 productConsumer.getQuantityRequested(),getCurrentUser());
			getAll();
			setSuccessMessage(getResourceBundle().getString("APPROVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("APPROVAL_FAILED"));
		}
		return "Success";
	}

	public String returnDemande() {
		try {
			setAction("RETURN");
			clearMessages();
			productConsumer = (ProductConsumer) baseService.getById(
					ProductConsumer.class, getIdParameter());
			productConsumer.setStatus(ProductConsumerStatus.RETURNED.getValue());
			baseService.saveProductConsumer(productConsumer,
					 -1 * productConsumer.getQuantityRequested(),getCurrentUser());
			getAll();
			setSuccessMessage(getResourceBundle().getString("RETURNED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("RETURNED_FAILED"));
		}
		return "Success";
	}
	
	public String reject() {
		try {
			clearMessages();
			productConsumer = (ProductConsumer) baseService.getById(
					ProductConsumer.class, getIdParameter());
			productConsumer.setStatus(ProductConsumerStatus.REJECTED.getValue());
			baseService.saveProductConsumer(productConsumer,
					 -1 * productConsumer.getQuantityRequested(),getCurrentUser());
			getAll();
			setSuccessMessage(getResourceBundle().getString("REJECTED_SUCCESSFULLY"));
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, getResourceBundle().getString("REJECT_FAILED"));
		}
		return "Success";
	}
	@PostConstruct
	public String getAll() {

		productConsumers = baseService.loadAll(ProductConsumer.class,
				getCurrentUser().getSchool());

		for (BaseEntity pc : productConsumers) {
			ProductConsumer productConsumer = (ProductConsumer) pc;
			productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey(
					"CONSUMER_STATUS", productConsumer.getStatus().toString()));
		}
		setRowCount(new Long(productConsumers.size()));

		return "Success";
	}

	public String getOverDueProductConsumers() {

		if (numberOfDays != null
				&& StringUtils.isEmpty(numberOfDays.toString()))
			numberOfDays = 0;

		productConsumers = baseService.getAllProductConsumersDueInDays(
				ProductConsumer.class, numberOfDays);
		setRowCount(productConsumers.size());
		return "";
	}

	public boolean isUserHasWriteAccess() {
		if (((String) getSessionParameter("link")).equals("student")) {
			return isUserHasWriteAccess(MenuIdEnum.STUDENT.getValue());
		} else if (((String) getSessionParameter("link")).equals("teacher")) {
			return isUserHasWriteAccess(MenuIdEnum.TEACHER.getValue());
		} else if (((String) getSessionParameter("link")).equals("demandes")) {
			return isUserHasWriteAccess(MenuIdEnum.PRODUCT_REQUEST.getValue());
		}
		return false;
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

	public List<BaseEntity> getProductConsumers() {
		return productConsumers;
	}

	public void setProductConsumers(List<BaseEntity> productConsumers) {
		this.productConsumers = productConsumers;
	}

	public List<String> getAllProductConsumers() {
		return allProductConsumers;
	}

	public void setAllProductConsumers(List<String> allProductConsumers) {
		this.allProductConsumers = allProductConsumers;
	}

	public ProductConsumer getProductConsumer() {
		return productConsumer;
	}

	public void setProductConsumer(ProductConsumer productConsumer) {
		this.productConsumer = productConsumer;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String addProducts() {
		setSessionAttribute("pageProvenence", "PRODUCT_CONSUMER");
		return "";
	}

	public String addProductToConsumer() {
		Product product = (Product) baseService.getById(Product.class,
				getIdParameter());
		productConsumer.setProduct(product);
		return "";
	}

	public String assignUserToConsumer() {
		User consumer = (User) baseService
				.getById(User.class, getIdParameter());
		productConsumer.setConsumer(consumer);
		return "";
	}

	public Integer getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}

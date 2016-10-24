package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.model.Student;
import com.esoft.ischool.model.Teacher;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Brand;
import com.esoft.ischool.stocks.model.Category;
import com.esoft.ischool.stocks.model.Product;
import com.esoft.ischool.stocks.model.ProductConsumer;
import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.ProductConsumerStatus;

@Component("productBean")
@Scope("session")
public class ProductBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	
	@Autowired
	@Qualifier("config")
	private Config config;
	
	private Long rowCount;
	private List<BaseEntity> products;
	private List<String> allProducts;
	private List<BaseEntity> studentDemands = new ArrayList<BaseEntity>();
	private List<BaseEntity> teacherDemands = new ArrayList<BaseEntity>();
	private Product product = new Product();
	private String brandName;
	private String categoryName;
	private boolean returnable;
	private String pageProvenence;
	private Integer requestedQty;
	private String comment;
	private Date returnDate;
	
	

	public List<BaseEntity> getTeacherDemands() {
		return teacherDemands;
	}

	public void setTeacherDemands(List<BaseEntity> teacherDemands) {
		this.teacherDemands = teacherDemands;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getComment() {
		return comment;
	}

	public List<BaseEntity> getStudentDemands() {
		return studentDemands;
	}

	public void setStudentDemands(List<BaseEntity> studentDemands) {
		this.studentDemands = studentDemands;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(Integer requestedQty) {
		this.requestedQty = requestedQty;
	}

	public String getPageProvenence() {
		return pageProvenence;
	}

	public void setPageProvenence(String pageProvenence) {
		this.pageProvenence = pageProvenence;
	}

	private String selectedTab = "productList";

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<String> autoComplete(Object suggest) {
		allProducts = new ArrayList<String>();
		for (BaseEntity entity : products) {
			Product p = (Product) entity;
			if (p.getName().equalsIgnoreCase(suggest.toString()))
				allProducts.add(p.getName());
		}
		return allProducts;
	}

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		product = new Product();
		setBrandName("");
		setCategoryName("");
		setPicture(null);
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Product.class);
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
		Long id = product.getId();
		try {
			Brand b = (Brand) baseService.findByName(Brand.class, brandName,
					getCurrentUser().getSchool());
			Category c = (Category) baseService.findByName(Category.class,
					categoryName, getCurrentUser().getSchool());
			product.setBrand(b);
			product.setCategory(c);

			// get picture
			if (getPicture() != null) {
				product.setPicture(getPicture());
				if(product.getPicture().length>MAX_FILE_SIZE){
					setErrorMessage(MAX_SIZE_EXCEEDED);
					return "ERROR";
				}
			}

			if (returnable == true)
				product.setReturnable("1");
			else
				product.setReturnable("0");

			if (id == null || id == 0)
				baseService.save(product, getCurrentUser());
			else
				baseService.update(product, getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();
		} catch (Exception ex) {
			product.setId(id);
			setErrorMessage(ex, "Ce produit existe deja");
		}

		return "Success";
	}

	public String edit() {
		clearMessages();
		product = (Product) baseService
				.getById(Product.class, getIdParameter());
		setBrandName(product.getBrand().getName());
		setCategoryName(product.getCategory().getName());
		selectedTab = "productDetails";
		setPicture(product.getPicture());
		return "Success";
	}

	public String selectProduct() {
		clearMessages();
		product = (Product) baseService
				.getById(Product.class, getIdParameter());
		setBrandName(product.getBrand().getName());
		setCategoryName(product.getCategory().getName());
		setPicture(product.getPicture());
		return "Success";
	}


	public String deleteStudentDemande() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), ProductConsumer.class);
			Student st = (Student) baseService.getById(Student.class,
					new Long(getSessionParameter("currentStudentId")
							.toString()));
			studentDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
			for (BaseEntity apc : studentDemands) {
				ProductConsumer productConsumer = (ProductConsumer) apc;
				productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
			}
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String deleteTeacherDemande() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), ProductConsumer.class);
			Teacher st = (Teacher) baseService.getById(Teacher.class,
					new Long(getSessionParameter("currentTeacherId")
							.toString()));
			teacherDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
			for (BaseEntity apc : studentDemands) {
				ProductConsumer productConsumer = (ProductConsumer) apc;
				productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
			}
			setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
		} catch (Exception ex) {
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL"));
		}

		return "Success";
	}
	
	public String getAllStudentDemands(){
		clearMessages();
		try {
			Student st = (Student) baseService.getById(Student.class,
					new Long(getSessionParameter("currentStudentId")
							.toString()));
			studentDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
			for (BaseEntity apc : studentDemands) {
				ProductConsumer productConsumer = (ProductConsumer) apc;
				productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "success";
	}
	
	public String getAllTeacherDemands(){
		clearMessages();
		try {
			Teacher st = (Teacher) baseService.getById(Teacher.class,
					new Long(getSessionParameter("currentTeacherId")
							.toString()));
			teacherDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
			for (BaseEntity apc : studentDemands) {
				ProductConsumer productConsumer = (ProductConsumer) apc;
				productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "success";
	}
	
	public String demandForStudent() {
		try {
			if (requestedQty != null && requestedQty > 0) {
				ProductConsumer pc = new ProductConsumer();
				pc.setComment(comment);
				pc.setProduct(product);
				pc.setRequestDate(new Date());
				pc.setQuantityRequested(requestedQty);
				pc.setStatus(ProductConsumerStatus.PENDING_APPROVAL.getValue());
				pc.setPossibleReturnedDate(returnDate);
				Student st = (Student) baseService.getById(Student.class,
						new Long(getSessionParameter("currentStudentId")
								.toString()));
				pc.setConsumer(st.getUser());
				baseService.save(pc, getCurrentUser());
				studentDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
				for (BaseEntity apc : studentDemands) {
					ProductConsumer productConsumer = (ProductConsumer) apc;
					productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
				}
			} else {
				throw new Exception(getResourceBundle().getString("INVALID_QTY"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, ex.getMessage());
		}
		return "success";
	}
	
	public String demandForTeacher() {
		try {
			if (requestedQty != null && requestedQty > 0) {
				ProductConsumer pc = new ProductConsumer();
				pc.setComment(comment);
				pc.setProduct(product);
				pc.setRequestDate(new Date());
				pc.setQuantityRequested(requestedQty);
				pc.setStatus(ProductConsumerStatus.PENDING_APPROVAL.getValue());
				pc.setPossibleReturnedDate(returnDate);
				Teacher st = (Teacher) baseService.getById(Teacher.class,
						new Long(getSessionParameter("currentTeacherId")
								.toString()));
				pc.setConsumer(st.getUser());
				baseService.save(pc, getCurrentUser());
				teacherDemands = baseService.loadByParentsIds(ProductConsumer.class, "consumer",st.getUser().getId(),"school" ,getCurrentUser().getSchool().getId());
				for (BaseEntity apc : teacherDemands) {
					ProductConsumer productConsumer = (ProductConsumer) apc;
					productConsumer.setStatusDesc(config.getConfigurationByGroupAndKey("CONSUMER_STATUS", productConsumer.getStatus().toString()));
				}
			} else {
				throw new Exception(getResourceBundle().getString("INVALID_QTY"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(ex, ex.getMessage());
		}
		return "success";
	}
	

	@PostConstruct
	private void getAll() {
		products = baseService.loadAll(Product.class, getCurrentUser()
				.getSchool());
		setRowCount(new Long(products.size()));

	}
	
	public String addProducts() {
		setSessionAttribute("pageProvenence", "PURCHASE_ORDER");
		getAll();
		return "";
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

	public List<BaseEntity> getProducts() {
		return products;
	}

	public void setProducts(List<BaseEntity> products) {
		this.products = products;
	}

	public List<String> getAllProducts() {
		return allProducts;
	}

	public void setAllProducts(List<String> allProducts) {
		this.allProducts = allProducts;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean isPageComingFromPurchaseOrder() {
		return "PURCHASE_ORDER".equals(getSessionParameter("pageProvenence"));
	}

	public boolean isPageComingFromProductConsumer() {
		return "PRODUCT_CONSUMER".equals(getSessionParameter("pageProvenence"));
	}

	public boolean isPageComingFromProduct() {
		return (String) getSessionParameter("pageProvenence") == null
				|| "PRODUCT".equals(getSessionParameter("pageProvenence"));
	}

	public boolean getReturnable() {
		return returnable;
	}

	public void setReturnable(boolean returnable) {
		this.returnable = returnable;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.PRODUCT.getValue());
	}
}

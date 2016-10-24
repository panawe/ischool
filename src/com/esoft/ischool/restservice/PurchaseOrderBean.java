package com.esoft.ischool.restservice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.util.MenuIdEnum;
import com.esoft.ischool.util.PurchaseOrderStatus;
import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.job.Config;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.OrderProduct;
import com.esoft.ischool.stocks.model.Product;
import com.esoft.ischool.stocks.model.PurchaseOrder;
import com.esoft.ischool.stocks.model.Shipment;
import com.esoft.ischool.stocks.model.Supplier;

@Component("purchaseOrderBean")
@Scope("session")
public class PurchaseOrderBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;

	@Autowired
	@Qualifier("config")
	private Config config;

	private Long rowCount;
	private Long productRowCount;
	private List<BaseEntity> purchaseOrders;

	private PurchaseOrder purchaseOrder = new PurchaseOrder();
	private String supplierName;
	private String shipmentName;

	private OrderProduct orderProduct = new OrderProduct();
	private List<BaseEntity> orderProducts;

	private Product product = new Product();

	public String validate() {
		return "succes";
	}

	@Override
	public String clear() {
		purchaseOrder = new PurchaseOrder();
		setSupplierName("");
		setShipmentName("");
		setOrderProducts(null);
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {

			List<BaseEntity> orderProds = baseService.loadAllByParentId(
					OrderProduct.class, "order", "id", getIdParameter());

			if (orderProds != null && orderProds.size() <= 0) {
				baseService.delete(getIdParameter(), PurchaseOrder.class);
				getAll();
				clear();
				setSuccessMessage(getResourceBundle().getString("DELETE_SUCCESSFULLY"));
			} else {
				setErrorMessage(getResourceBundle().getString("PRODUCTS_ASSIGNED_TO_ORDER"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			setErrorMessage(getResourceBundle().getString("DELETE_UNSUCCESSFULL") + " : \n" + ex.getMessage());
		}

		return "Success";
	}

	public String insert() {
		clearMessages();
		Long id = purchaseOrder.getId();
		try {
			if (purchaseOrder.getOrderDate()!= null && purchaseOrder.getPossibleDeliveryDate() != null
					&& purchaseOrder.getOrderDate().after(purchaseOrder.getPossibleDeliveryDate()))	{
				setErrorMessage(getResourceBundle().getString("ORDER_DATE_INVALID"));
				throw new Exception();
			}
			Supplier s = (Supplier) baseService.findByName(Supplier.class,
					supplierName, getCurrentUser().getSchool());
			Shipment shipment = (Shipment) baseService.findByColumn(
					Shipment.class, "shipmentNumber", shipmentName,
					getCurrentUser().getSchool());

			purchaseOrder.setSupplier(s);
			purchaseOrder.setShipment(shipment);

			if (id == null || id == 0)
				baseService.save(purchaseOrder, getCurrentUser());
			else {

				PurchaseOrder readOrder = (PurchaseOrder) baseService.getById(
						PurchaseOrder.class, purchaseOrder.getId());

				if (PurchaseOrderStatus.COMPLETED.getValue().equals(
						purchaseOrder.getStatus())
						&& !readOrder.getStatus().equals(
								PurchaseOrderStatus.COMPLETED.getValue()))
					baseService.saveDeliveredOrder(purchaseOrder,
							getCurrentUser());
				else
					baseService.update(purchaseOrder, getCurrentUser());
			}
			
			//setSessionAttribute("id", purchaseOrder.getId());
			//edit( purchaseOrder.getId());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
			clear();
			getAll();
		} catch (Exception ex) {
			purchaseOrder.setId(id);
			if (StringUtils.isEmpty(getErrorMessage()) )
				setErrorMessage(ex, "Cette demande existe deja");
		}

		return "Success";
	}

	public String edit(Long id) {
		try {
			clearMessages();
			try{
			purchaseOrder = (PurchaseOrder) baseService.getById(
					PurchaseOrder.class, id);
			}catch(Exception e){
				e.printStackTrace();
			}
			setSupplierName(purchaseOrder.getSupplier().getName());
			if (purchaseOrder.getShipment() != null)
				setShipmentName(purchaseOrder.getShipment().getShipmentNumber());
			setSessionAttribute("currentPurchaseOrderId", id);
			setSelectedTab("purchaseOrderDetailsTab");
			getOrderProductsForOrder();
			purchaseOrder.setTotalAmount(new Double(0));
			purchaseOrder.setDiscountAmount(new Double(0));
			for (BaseEntity op : orderProducts) {
				OrderProduct ordPro = (OrderProduct) op;
				purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() + ordPro.getTotalAmount());
				purchaseOrder.setDiscountAmount(purchaseOrder.getDiscountAmount() + ordPro.getDiscountAmount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Success";
	}
	public String edit() {
		try {
			clearMessages();
			try{
			purchaseOrder = (PurchaseOrder) baseService.getById(
					PurchaseOrder.class, getIdParameter());
			}catch(Exception e){
				e.printStackTrace();
			}
			setSupplierName(purchaseOrder.getSupplier().getName());
			if (purchaseOrder.getShipment() != null)
				setShipmentName(purchaseOrder.getShipment().getShipmentNumber());
			setSessionAttribute("currentPurchaseOrderId", getIdParameter());
			setSelectedTab("purchaseOrderDetailsTab");
			getOrderProductsForOrder();
			purchaseOrder.setTotalAmount(new Double(0));
			purchaseOrder.setDiscountAmount(new Double(0));
			for (BaseEntity op : orderProducts) {
				OrderProduct ordPro = (OrderProduct) op;
				purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() + ordPro.getTotalAmount());
				purchaseOrder.setDiscountAmount(purchaseOrder.getDiscountAmount() + ordPro.getDiscountAmount());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Success";
	}

	@PostConstruct
	private void getAll() {
		purchaseOrders = baseService.loadAll(PurchaseOrder.class,
				getCurrentUser().getSchool());

		for (BaseEntity po : purchaseOrders) {
			PurchaseOrder purchaseOrder = (PurchaseOrder) po;
			purchaseOrder.setStatusDesc(config.getConfigurationByGroupAndKey(
					"PURCHASE_ORDER_STATUS", purchaseOrder.getStatus()
							.toString()));
			getOrderProductsForOrder(purchaseOrder.getId());
			purchaseOrder.setTotalAmount(new Double(0));
			purchaseOrder.setDiscountAmount(new Double(0));
			for (BaseEntity op : orderProducts) {
				OrderProduct ordPro = (OrderProduct) op;
				purchaseOrder.setTotalAmount(purchaseOrder.getTotalAmount() + ordPro.getTotalAmount());
				purchaseOrder.setDiscountAmount(purchaseOrder.getDiscountAmount() + ordPro.getDiscountAmount());
			}
			
		}
		setRowCount(new Long(purchaseOrders.size()));

	}
	
	public String getOrderProductsForOrder(Long purchaseOrderId) {
		try {
			if (purchaseOrderId != null)
				orderProducts = baseService.loadAllByParentId(
						OrderProduct.class, "order", "id", purchaseOrderId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String addProducts() {
		setSessionAttribute("pageProvenence", "PURCHASE_ORDER");

		product = new Product();
		orderProduct = new OrderProduct();

		return "";
	}

	public String selectProductToAddToCart() {
		product = (Product) baseService
				.getById(Product.class, getIdParameter());
		return "";
	}

	public String addProductToCart() {
		if (getSessionParameter("currentPurchaseOrderId") != null) {
			try {
				OrderProduct orderProduct = (OrderProduct) baseService
						.findByParents(OrderProduct.class, "order", new Long(
								getSessionParameter("currentPurchaseOrderId")
										.toString()), "product", product
								.getId());

				if (orderProduct != null) {
					orderProduct.setQuantityOrdered(orderProduct
							.getQuantityOrdered()
							+ product.getQuantityOrdered());
					orderProduct.setQuantityReceived(orderProduct
							.getQuantityReceived()
							+ product.getQuantityOrdered());
					orderProduct.setDiscountAmount(product.getDiscountAmount());
					orderProduct.setTotalAmount(product.getTotalAmount());
					baseService.update(orderProduct, getCurrentUser());
				} else {
					PurchaseOrder purchaseOrder = (PurchaseOrder) baseService
							.getById(
									PurchaseOrder.class,
									new Long(getSessionParameter(
											"currentPurchaseOrderId")
											.toString()));
					orderProduct = new OrderProduct();
					orderProduct.setOrder(purchaseOrder);
					orderProduct.setProduct(product);
					orderProduct.setQuantityOrdered(product
							.getQuantityOrdered());
					orderProduct.setQuantityReceived(product
							.getQuantityOrdered());
					orderProduct.setQuantityDamaged(0);
					orderProduct.setDiscountAmount(product.getDiscountAmount());
					orderProduct.setTotalAmount(product.getTotalAmount());
					baseService.save(orderProduct, getCurrentUser());
				}

				product = new Product();
			} catch (Exception ex) {
				setErrorMessage(ex,
						"Cette order product combination existe deja");
			}
		}
		getOrderProductsForOrder();
		return "";
	}

	public String getOrderProductsForOrder() {
		try {
			if (purchaseOrder.getId() != null)
				orderProducts = baseService.loadAllByParentId(
						OrderProduct.class, "order", "id", new Long(
								getSessionParameter("currentPurchaseOrderId")
										.toString()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public List<BaseEntity> getOrderProducts() {
		if (purchaseOrder.getId() != null && orderProducts != null)
			setProductRowCount(new Long(orderProducts.size()));
		else
			orderProducts = null;
		return orderProducts;
	}

	public void setOrderProducts(List<BaseEntity> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public boolean getHasOrder() {
		return purchaseOrder != null && purchaseOrder.getId() != null;
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

	public List<BaseEntity> getPurchaseOrders() {
		getAll();
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<BaseEntity> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	HtmlInputText uiQtyOrdered;
	private UIDataTable repeater;
	private Set<Integer> keys = new HashSet<Integer>();

	/**
	 * @return the keys
	 */
	public Set getKeys() {
		return keys;
	}

	/**
	 * @param keys
	 *            the keys to set
	 */
	public void setKeys(Set keys) {
		this.keys = keys;
	}

	public UIDataTable getRepeater() {
		return repeater;
	}

	public void setRepeater(UIDataTable repeater) {
		this.repeater = repeater;
	}

	public HtmlInputText getUiQtyOrdered() {
		return uiQtyOrdered;
	}

	public void setUiQtyOrdered(HtmlInputText uiQtyOrdered) {
		this.uiQtyOrdered = uiQtyOrdered;
	}

	public String updateQuantity() {

		HashSet keys = new HashSet<Integer>();
		int rowKey = getRepeater().getRowIndex();
		keys.add(rowKey);
		setKeys(keys);
		uiQtyOrdered.processValidators(FacesContext.getCurrentInstance());
		uiQtyOrdered.processUpdates(FacesContext.getCurrentInstance());

		return null;
	}

	public String updateCart() {
		if (getSessionParameter("currentPurchaseOrderId") != null) {
			Integer quantityOrdered = orderProduct.getQuantityOrdered();
			Integer quantityReceived = orderProduct.getQuantityReceived();
			Integer quantityDamaged = orderProduct.getQuantityDamaged();
			orderProduct = (OrderProduct) baseService.findByParents(
					OrderProduct.class, "order",
					new Long(getSessionParameter("currentPurchaseOrderId")
							.toString()), "product", orderProduct.getProduct()
							.getId());

			if (orderProduct != null) {
				if (PurchaseOrderStatus.IN_PROCESS.getValue().equals(
						purchaseOrder.getStatus())) {
					orderProduct.setQuantityOrdered(quantityOrdered);
					orderProduct.setQuantityReceived(quantityOrdered);
					orderProduct.setQuantityDamaged(0);
				} else if (PurchaseOrderStatus.COMPLETED.getValue().equals(
						purchaseOrder.getStatus())) {
					orderProduct.setQuantityReceived(quantityReceived);
					orderProduct.setQuantityDamaged(quantityDamaged);
				}

				baseService.update(orderProduct, getCurrentUser());
			}
			getOrderProductsForOrder();
			orderProduct = new OrderProduct();
		}
		return "";
	}

	public String editOrderProduct() {
		orderProduct = (OrderProduct) baseService.getById(OrderProduct.class,
				getIdParameter());
		return "Success";
	}

	public String deleteOrderProduct() {
		orderProduct = (OrderProduct) baseService.getById(OrderProduct.class,
				getIdParameter());
		baseService.delete(orderProduct.getId(), OrderProduct.class);
		getOrderProductsForOrder();
		return "Success";
	}

	public OrderProduct getOrderProduct() {
		return orderProduct;
	}

	public void setOrderProduct(OrderProduct orderProduct) {
		this.orderProduct = orderProduct;
	}

	private Integer quantityOrdered;

	public Integer getQuantityOrdered() {
		return quantityOrdered;
	}

	public void setQuantityOrdered(Integer quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	public Long getProductRowCount() {
		return productRowCount;
	}

	public void setProductRowCount(Long productRowCount) {
		this.productRowCount = productRowCount;
	}

	public String getShipmentName() {
		return shipmentName;
	}

	public void setShipmentName(String shipmentName) {
		this.shipmentName = shipmentName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public void setConfig(Config config) {
		this.config = config;
	}

	private String selectedTab;

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isUserHasWriteAccess() {
		return isUserHasWriteAccess(MenuIdEnum.PURCHASE_ORDER.getValue());
	}

}

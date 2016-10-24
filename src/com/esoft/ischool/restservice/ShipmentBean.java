package com.esoft.ischool.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.ischool.bean.BaseBean;
import com.esoft.ischool.model.BaseEntity;
import com.esoft.ischool.service.BaseService;
import com.esoft.ischool.stocks.model.Carrier;
import com.esoft.ischool.stocks.model.Shipment;

@Component("shipmentBean")
@Scope("session")
public class ShipmentBean extends BaseBean {

	@Autowired
	@Qualifier("baseService")
	private BaseService baseService;
	private Long rowCount;
	private List<BaseEntity> shipments = new ArrayList<BaseEntity>();
	private List<String> allShipments = new ArrayList<String>();
	
	private Shipment shipment = new Shipment();
	private String carrierName = "";
	private String status = "";
	private String selectedTab="ShipmentTabList";

	@Override
	public String getSelectedTab() {
		return selectedTab;
	}

	@Override
	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public List<Shipment> autoComplete(Object suggest) {
		String pref = (String) suggest;
		List<Shipment> result = new ArrayList<Shipment>();
		allShipments.clear();
		
		for (BaseEntity entity : shipments) {
			Shipment b = (Shipment) entity;
			if ((b.getShipmentNumber() != null && b.getShipmentNumber().toLowerCase().indexOf(
					pref.toLowerCase()) == 0)
					|| "".equals(pref))
				result.add(b);
			
			allShipments.add(b.getShipmentNumber());
		}
		return result;
	}

	public String validate() {
		return "succes";
	}
	
	@Override
	public String clear() {
		setCarrierName("");
		shipment = new Shipment();
		return "Success";
	}

	public String delete() {
		clearMessages();
		try {
			baseService.delete(getIdParameter(), Shipment.class);
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
		Long id = shipment.getId();
		try {
			Carrier c = (Carrier) baseService.findByName(Carrier.class, carrierName,getCurrentUser().getSchool());
			shipment.setCarrier(c);
			
			if (id == null || id == 0)
				baseService.save(shipment,getCurrentUser());
			else
				baseService.update(shipment,getCurrentUser());
			setSuccessMessage(getResourceBundle().getString("SAVED_SUCCESSFULLY"));
		} catch (Exception ex) {
			shipment.setId(id);
			setErrorMessage(ex,"Ce shipment existe deja");
		}

		
		//clear();
		getAll();
		return "Success";
	}


	public String edit() {
		clearMessages();
		shipment = (Shipment) baseService.getById(Shipment.class, getIdParameter());
		setCarrierName(shipment.getCarrier().getName());
		selectedTab="ShipmentTabDetails";
		return "Success";
	}
	
	@PostConstruct
	private void getAll() {
		shipments = baseService.loadAll(Shipment.class, getCurrentUser().getSchool());
		setRowCount(new Long(shipments.size()));
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

	public List<BaseEntity> getShipments() {
		return shipments;
	}

	public void setShipments(List<BaseEntity> shipments) {
		this.shipments = shipments;
	}

	public List<String> getAllShipments() {
		autoComplete("");
		return allShipments;
	}

	public void setAllShipments(List<String> allShipments) {
		this.allShipments = allShipments;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

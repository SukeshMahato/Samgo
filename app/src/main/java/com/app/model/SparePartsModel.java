package com.app.model;

public class SparePartsModel {

	private String sparePartsId, description, quantity, unitSales, machineId, spareId, jobId, id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getSpareId() {
		return spareId;
	}

	public void setSpareId(String spareId) {
		this.spareId = spareId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getSparePartsId() {
		return sparePartsId;
	}

	public void setSparePartsId(String sparePartsId) {
		this.sparePartsId = sparePartsId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnitSales() {
		return unitSales;
	}

	public void setUnitSales(String unitSales) {
		this.unitSales = unitSales;
	}

}

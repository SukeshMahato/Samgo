package com.app.model;

public class MachineDetails {

	private String machine_si_no, machine_name, machine_type, manufacturer, machine_model, machine_added_by, voltage,
			suction, traction, water, mfg_year,work_order,visual_inspect,spare_parts,marks_avail;

	public MachineDetails(String manufacturer, String type, String model, String name, String si_no, String added_by,
			String voltage, String suction, String traction, String water, String mfg_year) {
		this.machine_added_by = added_by;
		this.machine_model = model;
		this.machine_name = name;
		this.machine_si_no = si_no;
		this.machine_type = type;
		this.manufacturer = manufacturer;

		this.voltage = voltage;
		this.suction = suction;
		this.traction = traction;
		this.water = water;
		this.mfg_year = mfg_year;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getSuction() {
		return suction;
	}

	public void setSuction(String suction) {
		this.suction = suction;
	}

	public String getTraction() {
		return traction;
	}

	public void setTraction(String traction) {
		this.traction = traction;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getMfg_year() {
		return mfg_year;
	}

	public void setMfg_year(String mfg_year) {
		this.mfg_year = mfg_year;
	}

	public String getMachine_si_no() {
		return machine_si_no;
	}

	public void setMachine_si_no(String machine_si_no) {
		this.machine_si_no = machine_si_no;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public String getMachine_type() {
		return machine_type;
	}

	public void setMachine_type(String machine_type) {
		this.machine_type = machine_type;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getMachine_model() {
		return machine_model;
	}

	public void setMachine_model(String machine_model) {
		this.machine_model = machine_model;
	}

	public String getMachine_added_by() {
		return machine_added_by;
	}

	public void setMachine_added_by(String machine_added_by) {
		this.machine_added_by = machine_added_by;
	}

	public String getWork_order() {
		return work_order;
	}

	public String getVisual_inspect() {
		return visual_inspect;
	}

	public String getSpare_parts() {
		return spare_parts;
	}

	public String getMarks_avail() {
		return marks_avail;
	}

	public void setWork_order(String work_order) {
		this.work_order = work_order;
	}

	public void setVisual_inspect(String visual_inspect) {
		this.visual_inspect = visual_inspect;
	}

	public void setSpare_parts(String spare_parts) {
		this.spare_parts = spare_parts;
	}

	public void setMarks_avail(String marks_avail) {
		this.marks_avail = marks_avail;
	}
	

}

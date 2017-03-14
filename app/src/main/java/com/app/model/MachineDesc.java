package com.app.model;

public class MachineDesc {

	private String machineSr, machineDesc;

	public MachineDesc(String machineSr, String machineDesc) {
		this.machineDesc = machineDesc;
		this.machineSr = machineSr;
	}

	public String getMachineSr() {
		return machineSr;
	}

	public void setMachineSr(String machineSr) {
		this.machineSr = machineSr;
	}

	public String getMachineDesc() {
		return machineDesc;
	}

	public void setMachineDesc(String machineDesc) {
		this.machineDesc = machineDesc;
	}

}

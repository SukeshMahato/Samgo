package com.app.model;

public class MachineServiceHistory {

	private String errorCode, problemcomments, engineerName, dateTime, engineerComments, workCarriedOut, machine_id;

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getProblemcomments() {
		return problemcomments;
	}

	public void setProblemcomments(String problemcomments) {
		this.problemcomments = problemcomments;
	}

	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getEngineerComments() {
		return engineerComments;
	}

	public void setEngineerComments(String engineerComments) {
		this.engineerComments = engineerComments;
	}

	public String getWorkCarriedOut() {
		return workCarriedOut;
	}

	public void setWorkCarriedOut(String workCarriedOut) {
		this.workCarriedOut = workCarriedOut;
	}

}

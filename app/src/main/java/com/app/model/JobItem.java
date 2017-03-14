package com.app.model;

public class JobItem {

	public String jobNo, jobTitle, jobDate, clientName, siteName, siteAddress, adminComments, jobDetailsString;

	public String completeStringForOneRow;

	public String getCompleteStringForOneRow() {
		return completeStringForOneRow;
	}

	public void setCompleteStringForOneRow(String completeStringForOneRow) {
		this.completeStringForOneRow = completeStringForOneRow;
	}

	public String getJobDetailsString() {
		return jobDetailsString;
	}

	public void setJobDetailsString(String jobDetailsString) {
		this.jobDetailsString = jobDetailsString;
	}

	public String getJobNo() {
		return jobNo;
	}

	public void setJobNo(String jobNo) {
		this.jobNo = jobNo;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDate() {
		return jobDate;
	}

	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getAdminComments() {
		return adminComments;
	}

	public void setAdminComments(String adminComments) {
		this.adminComments = adminComments;
	}

}

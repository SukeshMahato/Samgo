package com.app.model;

public class DocketDetail {

	private String jobdetail_id, job_id, machine_id, work_carried_out, parts_unable_to_find, eol;

	public String getJobdetail_id() {
		return jobdetail_id;
	}

	public void setJobdetail_id(String jobdetail_id) {
		this.jobdetail_id = jobdetail_id;
	}

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getWork_carried_out() {
		return work_carried_out;
	}

	public void setWork_carried_out(String work_carried_out) {
		this.work_carried_out = work_carried_out;
	}

	public String getParts_unable_to_find() {
		return parts_unable_to_find;
	}

	public void setParts_unable_to_find(String parts_unable_to_find) {
		this.parts_unable_to_find = parts_unable_to_find;
	}

	public String getEol() {
		return eol;
	}

	public void setEol(String eol) {
		this.eol = eol;
	}

}

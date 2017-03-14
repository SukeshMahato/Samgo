package com.app.model;

public class StartJobModel {

	private String job_id, engineer_id, site_id, distance, distance_unit, start_time, end_time;

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public String getEngineer_id() {
		return engineer_id;
	}

	public void setEngineer_id(String engineer_id) {
		this.engineer_id = engineer_id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistance_unit() {
		return distance_unit;
	}

	public void setDistance_unit(String distance_unit) {
		this.distance_unit = distance_unit;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}

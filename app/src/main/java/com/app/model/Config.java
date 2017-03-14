package com.app.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Config {

	private Config() {
	}

	public static final String MYFREFS = "my_pref_file";
	
	public static final int REQUEST_SCAN = 112;

	public static final String YOUTUBE_API_KEY = "AIzaSyC2IoKUYxfxlXPmeFX2HSSIgFwNMz1OQMc";
	
	//public static final String base_url = "http://192.168.7.128/msts/"; //Local url
	public static final String base_url = "http://testserver1.eceltichosting.com/msts/"; //Test server url
	//public static final String base_url = "http://sam.chemicaldirect.ie/"; //Live server url
	

	public static String machineSlNo = "";

	public static String docket_id = "";

	public static String site_id = "";

	public static String job_id = "";

	public static String client_id = "";

	public static String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	// live url

	// public static final String base_url = " http://sam.chemicaldirect.ie/";

}

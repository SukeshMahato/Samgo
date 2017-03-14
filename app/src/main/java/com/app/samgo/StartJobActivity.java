package com.app.samgo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.Job;
import com.app.model.MachineView;
import com.app.model.StartJobModel;
import com.app.model.TaskItem;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartJobActivity extends Activity {

	private LinearLayout mLinearListView;
	private ArrayList<TaskItem> mArrayListData = new ArrayList<TaskItem>();;

	private ImageView backButton;
	private TextView clientName, siteName, contractorName, siteAddress, siteManagerList, declaration, admin_comment;
	private Button proceedButton, cancelButton;
	private String extraString, fromtoday;

	private String job_id, site_id, distance, distance_unit, docket_no;

	private String start_address = "Unit 14a Stadium Business Park,Ballycoolin Road,Dublin 11";
	private String latitude = "53.40011";
	private String longitude = "-6.3373315";

	private String latitudeSite = "";
	private String longitudeSite = "";
	private String addressSite = "";

	private SamgoSQLOpenHelper db;

	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_job_activity);
		db = new SamgoSQLOpenHelper(getApplicationContext());
		parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);
		SharedPreferences settings = getSharedPreferences(Config.MYFREFS, 0);
		String background_image = settings.getString("background_image", "test");
		Log.e("TAG", "background_image >> " + background_image);
		if (background_image != null) {

			if (background_image.equalsIgnoreCase("background_1")) {
				parentLayout.setBackgroundResource(R.drawable.background_1);
			} else if (background_image.equalsIgnoreCase("background_2")) {
				parentLayout.setBackgroundResource(R.drawable.background_2);
			} else if (background_image.equalsIgnoreCase("background_3")) {
				parentLayout.setBackgroundResource(R.drawable.background_3);
			} else if (background_image.equalsIgnoreCase("background_4")) {
				parentLayout.setBackgroundResource(R.drawable.background_4);
			} else if (background_image.equalsIgnoreCase("background_5")) {
				parentLayout.setBackgroundColor(color.white);
			}

		}

		mLinearListView = (LinearLayout) findViewById(R.id.task_details_wrapper);
		backButton = (ImageView) findViewById(R.id.back_button);
		clientName = (TextView) findViewById(R.id.client_name_text);
		siteName = (TextView) findViewById(R.id.site_name_test);
		contractorName = (TextView) findViewById(R.id.contractor_name_test);
		siteAddress = (TextView) findViewById(R.id.site_address_test);
		siteManagerList = (TextView) findViewById(R.id.site_manager_list_text);
		declaration = (TextView) findViewById(R.id.declration);
		proceedButton = (Button) findViewById(R.id.proceed_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);
		admin_comment = (TextView) findViewById(R.id.admin_comment_text);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			fromtoday = extras.getString("JOB");
			if (fromtoday.equalsIgnoreCase("todaysjob")) {

				extraString = extras.getString("JOBITEM");
				try {
					JSONObject jObj = new JSONObject(extraString);
					String job = jObj.getString("Job");
					JSONObject jobObj = new JSONObject(job);
					job_id = jobObj.getString("id");
					docket_no = jobObj.getString("docket_no");
					String Site = jObj.getString("Site");
					JSONObject jObj3 = new JSONObject(Site);
					site_id = jObj3.getString("id");
					String sitename = jObj3.getString("name");
					siteName.setText(sitename);
					String siteadd = jObj3.getString("address");
					Log.e("TAG", "Address Value::" + siteadd);

					if (siteadd != null && !siteadd.equalsIgnoreCase(""))
						siteAddress.setText(siteadd);

					String latitude = jObj3.getString("latitude");
					String longitude = jObj3.getString("longitude");
					latitudeSite = latitude;
					longitudeSite = longitude;
					addressSite = siteadd;

					String comment = jObj3.getString("comments");
					Log.e("TAG", "comment Value::" + comment);
					admin_comment.setText(comment);
					distance = String.valueOf(getDistance(latitude, longitude));
					distance_unit = "meter";
					String Client = jObj.getString("Client");
					JSONObject jObj4 = new JSONObject(Client);
					String first_name = jObj4.getString("first_name");
					clientName.setText(first_name);
					String contractor_name = "";
					String contractor = jObj3.getString("Contractor");
					Log.e("TAG", "contractor >>> " + contractor);
					Object json = new JSONTokener(contractor).nextValue();

					if (json instanceof JSONObject) {
						Log.e("TAG", "contractor >>> " + contractor);
						JSONObject contractorObj = new JSONObject(contractor);
						contractor_name = contractorObj.getString("name");
					} else if (json instanceof JSONArray) {
						Log.e("TAG", "contractor >>> " + contractor);
						contractor_name = "N/A";
					}

					contractorName.setText(contractor_name);
					String siteManager_list = jObj3.getString("SiteManager");
					JSONArray siteManagerjArr = new JSONArray(siteManager_list);

					if (siteManagerjArr.length() > 0) {

						String siteManagerName = "";
						for (int j = 0; j < siteManagerjArr.length(); j++) {
							String siteManagerListString = siteManagerjArr.getString(j);
							JSONObject siteManagerListObj = new JSONObject(siteManagerListString);
							siteManagerName += siteManagerListObj.getString("site_manager_name") + "\n";
						}

						siteManagerList.setText(siteManagerName);

					} else {
						siteManagerList.setText("N/A");
					}

					declaration.setText("Are you sure you want to start the Job for " + first_name + " in "
							+ contractor_name
							+ "? To proceed simply click confirm below to start the job, or cancel and return to the dashboard.");

					String jobDetails = jObj.getString("JobDetail");
					JSONArray jArr = new JSONArray(jobDetails);

					for (int i = 0; i < jArr.length(); i++) {

						String jsonMachineDetails = jArr.getString(i);
						JSONObject jObjMachine = new JSONObject(jsonMachineDetails);
						String machineId = jObjMachine.getString("machine_id");
						String machineSR = jObjMachine.getString("id");
						String desc = jObjMachine.getString("problem");
						String errorcode = jObjMachine.getString("error_code");
						String mode_machine = db.getModelMachineFromMaster(machineId);
						Log.e("TAG", "mode_machine>>" + mode_machine);
						Log.e("TAG", "machine id>>" + machineId);
						String[] mode_machineArr = mode_machine.split(",");
						String model = "";
						String machine = "";
						if (mode_machineArr.length > 0) {
							model = mode_machineArr[0];
							machine = mode_machineArr[1];
						}
						mArrayListData.add(new TaskItem(machineSR, errorcode, desc, model, machine));
						// mArrayListData.add(new TaskItem(machineSR, errorcode,
						// desc));
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else if (fromtoday.equalsIgnoreCase("joblist")) {

				docket_no = extras.getString("DocketNo");

				Job jobItem = db.getJobItemByDocketId(docket_no);

				job_id = jobItem.getId();
				site_id = jobItem.getSiteid();

				String latitude = jobItem.getLatitude();
				String longitude = jobItem.getLongitude();

				latitudeSite = latitude;
				longitudeSite = longitude;
				addressSite = jobItem.getSiteAddress();

				distance = String.valueOf(getDistance(latitude, longitude));

				distance_unit = "meter";

				siteName.setText(jobItem.getSiteName());
				// changes for static address
				siteAddress.setText(jobItem.getSiteAddress());
				// siteAddress.setText(start_address);
				clientName.setText(jobItem.getClient_name());
				contractorName.setText(jobItem.getContractor_name());
				siteManagerList.setText(jobItem.getSiteManagerList());
				admin_comment.setText(jobItem.getComment());

				declaration.setText("Are you sure you want to start the Job for " + jobItem.getClient_name() + " in "
						+ jobItem.getContractor_name()
						+ "? To proceed simply click confirm below to start the job, or cancel and return to the dashboard.");

				try {
					JSONArray jArr = new JSONArray(jobItem.getTask_details());

					for (int i = 0; i < jArr.length(); i++) {

						String jsonMachineDetails = jArr.getString(i);

						JSONObject jObjMachine = new JSONObject(jsonMachineDetails);

						String machineId = jObjMachine.getString("machine_id");
						// String machineSR = jObjMachine.getString("id");
						MachineView machineView = db.getMachineViewDataByMachineId(machineId);
						String machineSR = machineView.getMachine_sl_no();
						String desc = jObjMachine.getString("problem");
						String errorcode = jObjMachine.getString("error_code");

						String mode_machine = db.getModelMachineFromMaster(machineId);
						Log.e("TAG", "mode_machine>>" + mode_machine);
						Log.e("TAG", "machine id>>" + machineId);
						String[] mode_machineArr = mode_machine.split(",");

						String model = "";
						String machine = "";
						if (mode_machineArr.length > 0) {
							model = mode_machineArr[0];
							machine = mode_machineArr[1];
						}

						mArrayListData.add(new TaskItem(machineSR, errorcode, desc, model, machine));
						// mArrayListData.add(new TaskItem(machineSR, errorcode,
						// desc));

					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}

		}

		if (mArrayListData.size() > 0) {
			/***
			 * adding item into listview
			 */

			for (int i = 0; i < mArrayListData.size(); i++) {
				/**
				 * inflate items/ add items in linear layout instead of listview
				 */

				Log.e("TAG", "size of i >> " + i);

				LayoutInflater inflater1 = null;
				inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View mLinearView = inflater1.inflate(R.layout.start_job_one_row_item, null);
				/**
				 * getting id of row.xml
				 */
				TextView mSerialNo = (TextView) mLinearView.findViewById(R.id.serial_no_text);
				TextView mErrorCode = (TextView) mLinearView.findViewById(R.id.error_code_text);
				TextView mDescription = (TextView) mLinearView.findViewById(R.id.description_text);
				TextView model_id = (TextView) mLinearView.findViewById(R.id.model_id);
				TextView machine_name = (TextView) mLinearView.findViewById(R.id.machine_name);

				/**
				 * set item into row
				 */
				String serialNo = mArrayListData.get(i).getSerialNo();
				String errorCode = mArrayListData.get(i).getErrorCode();
				String description = mArrayListData.get(i).getDescription();
				mSerialNo.setText(serialNo);
				mErrorCode.setText(errorCode);
				mDescription.setText(description);

				machine_name.setText(mArrayListData.get(i).getMachine_name());
				model_id.setText(mArrayListData.get(i).getModel_id());

				Log.e("TAG", "serial Number >> " + serialNo);
				Log.e("TAG", "errorCode >> " + errorCode);
				Log.e("TAG", "description >> " + description);

				if (i % 2 == 1) {
					mLinearView.setBackgroundColor(Color.parseColor("#f6f6f6"));
				} else {
					mLinearView.setBackgroundColor(Color.parseColor("#ffffff"));
				}

				/**
				 * add view in top linear
				 */

				mLinearListView.addView(mLinearView);

			}
		} else {

		}

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});

		proceedButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (db.getCountForStartJob(AppManager.getSinleton().getUser().getId(), site_id, job_id) == 0) {

					StartJobModel startJobModel = new StartJobModel();

					startJobModel.setJob_id(job_id);
					startJobModel.setSite_id(site_id);
					startJobModel.setEngineer_id(AppManager.getSinleton().getUser().getId());
					startJobModel.setDistance(distance);
					startJobModel.setDistance_unit(distance_unit);
					startJobModel.setStart_time(getDateTime());
					startJobModel.setEnd_time("");

					Job jobItem = new Job();

					jobItem.setJobId(docket_no);
					jobItem.setJob_start_place(start_address);
					jobItem.setJob_start_time(getDateTime());
					jobItem.setUpdatedOn(getDateTime());

					db.addDataToStartJob(startJobModel);
					db.updateJobTable(jobItem);

					Intent startJob = new Intent(getApplicationContext(), MainActivity.class);
					startJob.putExtra("FROMSTARTACT", "startActivity");
					startActivity(startJob);
				} else {
					Toast.makeText(getApplicationContext(), "You cannot start multiple jobs under same site.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		siteAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent startSite = new Intent(StartJobActivity.this, ViewMapActivity.class);

				startSite.putExtra("longitude", latitudeSite);
				startSite.putExtra("latitude", longitudeSite);
				startSite.putExtra("address", addressSite);

				Log.e("TAG", "longitudeSite>>" + longitudeSite);
				Log.e("TAG", "latitudeSite>>" + latitudeSite);
				startActivity(startSite);
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}

	public double getDistance(String lat, String lon) {

		double dis = 0.0;

		Location locationA = new Location("point A");
		locationA.setLatitude(Double.parseDouble(latitude));
		locationA.setLongitude(Double.parseDouble(longitude));
		Location locationB = new Location("point B");
		locationB.setLatitude(Double.parseDouble(lat));
		locationB.setLongitude(Double.parseDouble(lon));
		dis = locationA.distanceTo(locationB);

		return dis;

	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}

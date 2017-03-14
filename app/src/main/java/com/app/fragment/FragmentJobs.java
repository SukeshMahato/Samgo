package com.app.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.JobListsAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.JobsListListener;
import com.app.model.Config;
import com.app.model.Job;
import com.app.model.Machine;
import com.app.model.MachineDetails;
import com.app.model.MachineManufacturer;
import com.app.model.MachineMaster;
import com.app.model.MachineModel;
import com.app.model.MachineView;
import com.app.model.Machinetype;
import com.app.samgo.CreateDocketActivity;
import com.app.samgo.R;
import com.app.samgo.StartJobActivity;
import com.app.samgo.ViewJobActivity;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FragmentJobs extends Fragment implements JobsListListener {

	View v;
	private ListView jobLists;
	private JobListsAdapter jobListsAdapter;
	private ArrayList<Job> jobDataList = new ArrayList<Job>();
	private ArrayList<Job> jobDataList2 = new ArrayList<Job>();
	// private TextView searchBy;
	private EditText searchHere;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	private ConnectionDetector cd;
	private SamgoSQLOpenHelper db;
	private RelativeLayout parentLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.fragment_job_lists, container, false);
		this.initialisation();
		this.listner();
		parentLayout = (RelativeLayout) v.findViewById(R.id.parent_layout);
		SharedPreferences settings = getActivity().getSharedPreferences(Config.MYFREFS, 0);
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

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());
		db = new SamgoSQLOpenHelper(getContext());
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		jobDataList.clear();
		jobDataList2.clear();
		jobDataList = db.getAllDataFromJob();
		jobDataList2 = db.getAllDataFromJob();

		Log.e("TAG", "size >> " + jobDataList.size());

		jobListsAdapter = new JobListsAdapter(getActivity(), jobDataList, FragmentJobs.this);
		jobLists.setAdapter(jobListsAdapter);
		jobListsAdapter.notifyDataSetChanged();

//		/*
//		 * if (isInternetPresent) {
//		 *
//		 * Log.e("TAG", "engineer id >> " +
//		 * AppManager.getSinleton().getUser().getId());
//		 *
//		 * new JobListServices(this, AppManager.getSinleton().getUser().getId(),
//		 * getActivity()).execute();
//		 *
//		 * } else { jobDataList.clear(); jobDataList2.clear();
//		 *
//		 * jobDataList = db.getAllDataFromJob(); jobDataList2 =
//		 * db.getAllDataFromJob();
//		 *
//		 * Log.e("TAG", "size >> " + jobDataList.size());
//		 *
//		 * jobListsAdapter = new JobListsAdapter(getActivity(), jobDataList,
//		 * FragmentJobs.this); jobLists.setAdapter(jobListsAdapter);
//		 * jobListsAdapter.notifyDataSetChanged(); }
//		 */

		return v;
	}

	private void listner() {
		jobLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}
		});
//		/*
//		 * searchBy.setOnClickListener(new OnClickListener() {
//		 *
//		 * @Override public void onClick(View arg0) { // TODO Auto-generated
//		 * method stub
//		 *
//		 * Log.e("TAG", "clicked on");
//		 *
//		 * final Dialog dialog = new Dialog(getContext(),
//		 * R.style.TopToBottomDialog);
//		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		 * dialog.setContentView(R.layout.custom_search_dialog_for_job);
//		 *
//		 * TextView site_name = (TextView) dialog.findViewById(R.id.site_name);
//		 * TextView job_id = (TextView) dialog.findViewById(R.id.job_id);
//		 * TextView job_title = (TextView) dialog.findViewById(R.id.job_title);
//		 *
//		 * site_name.setOnClickListener(new OnClickListener() {
//		 *
//		 * @Override public void onClick(View v) { // TODO Auto-generated method
//		 * stub searchBy.setText("Site Name"); dialog.dismiss(); } });
//		 *
//		 * job_id.setOnClickListener(new OnClickListener() {
//		 *
//		 * @Override public void onClick(View v) { // TODO Auto-generated method
//		 * stub searchBy.setText("Job Id"); dialog.dismiss(); } });
//		 *
//		 * job_title.setOnClickListener(new OnClickListener() {
//		 *
//		 * @Override public void onClick(View v) { // TODO Auto-generated method
//		 * stub searchBy.setText("Job Title"); dialog.dismiss(); } });
//		 *
//		 * dialog.show(); } });
//		 */

		searchHere.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String searchString = searchHere.getText().toString().toLowerCase();
				int textLength = searchString.length();

				// if (searchBy.getText().toString().contains("Search
				// Everything")) {

				if (textLength > 0) {
					jobDataList.clear();
				}

				for (int i = 0; i < jobDataList2.size(); i++) {
					Job jobItem = new Job();

					String jobId = jobDataList2.get(i).getJobId();
					String jobTitle = jobDataList2.get(i).getJobTitle();
					String sectorName = jobDataList2.get(i).getSectorName();
					String siteName = jobDataList2.get(i).getSiteName();
					String dueDate = jobDataList2.get(i).getDueDate();
					String status = jobDataList2.get(i).getJobStatus();

					if (textLength > 0) {

						if (siteName.toLowerCase().contains(searchString) || jobId.toLowerCase().contains(searchString)
								|| jobTitle.toLowerCase().contains(searchString)
								|| status.toLowerCase().contains(searchString)
								|| sectorName.toLowerCase().contains(searchString)
								|| dueDate.toLowerCase().contains(searchString)) {

							jobItem.setJobId(jobId);
							jobItem.setJobTitle(jobTitle);
							jobItem.setSectorName(sectorName);
							jobItem.setDueDate(dueDate);
							jobItem.setSiteName(siteName);
							jobItem.setJobStatus(status);

							jobDataList.add(jobItem);

						}
					}

					// }

				}
//				/*
//					 * else { jobDataList.clear();
//					 *
//					 * for (int i = 0; i < jobDataList2.size(); i++) {
//					 *
//					 * Job jobItem = new Job();
//					 *
//					 * String jobId = jobDataList2.get(i).getJobId(); String
//					 * jobTitle = jobDataList2.get(i).getJobTitle(); String
//					 * sectorName = jobDataList2.get(i).getSectorName(); String
//					 * siteName = jobDataList2.get(i).getSiteName(); String
//					 * dueDate = jobDataList2.get(i).getDueDate(); String status
//					 * = jobDataList2.get(i).getJobStatus();
//					 *
//					 * if (searchBy.getText().toString().contains("Site Name"))
//					 * {
//					 *
//					 * if (textLength <= siteName.length()) {
//					 *
//					 * if (siteName.toLowerCase().contains(searchString)) {
//					 *
//					 * jobItem.setJobId(jobId); jobItem.setJobTitle(jobTitle);
//					 * jobItem.setSectorName(sectorName);
//					 * jobItem.setDueDate(dueDate);
//					 * jobItem.setSiteName(siteName);
//					 * jobItem.setJobStatus(status);
//					 *
//					 * jobDataList.add(jobItem); }
//					 *
//					 * }
//					 *
//					 * } else if (searchBy.getText().toString().contains(
//					 * "Job Id")) {
//					 *
//					 * if (textLength <= jobId.length()) {
//					 *
//					 * if (jobId.toLowerCase().contains(searchString)) {
//					 *
//					 * jobItem.setJobId(jobId); jobItem.setJobTitle(jobTitle);
//					 * jobItem.setSectorName(sectorName);
//					 * jobItem.setDueDate(dueDate);
//					 * jobItem.setSiteName(siteName);
//					 * jobItem.setJobStatus(status);
//					 *
//					 * jobDataList.add(jobItem);
//					 *
//					 * }
//					 *
//					 * }
//					 *
//					 * } else if (searchBy.getText().toString().contains(
//					 * "Job Title")) {
//					 *
//					 * if (textLength <= jobTitle.length()) {
//					 *
//					 * if (jobTitle.toLowerCase().contains(searchString)) {
//					 *
//					 * jobItem.setJobId(jobId); jobItem.setJobTitle(jobTitle);
//					 * jobItem.setSectorName(sectorName);
//					 * jobItem.setDueDate(dueDate);
//					 * jobItem.setSiteName(siteName);
//					 * jobItem.setJobStatus(status);
//					 *
//					 * jobDataList.add(jobItem);
//					 *
//					 * }
//					 *
//					 * }
//					 *
//					 * }
//					 *
//					 * } }
//					 */

				jobListsAdapter = new JobListsAdapter(getActivity(), jobDataList, FragmentJobs.this);
				jobLists.setAdapter(jobListsAdapter);
				jobListsAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		jobLists = (ListView) v.findViewById(R.id.job_list);
		// searchBy = (TextView) v.findViewById(R.id.search_by);
		searchHere = (EditText) v.findViewById(R.id.search_here);
	}

	@Override
	public void getAllJobListResponse(String response) {
		// TODO Auto-generated method stub

		Log.e("TAG", "response >> " + response);

		if (response != null) {
			try {

				JSONArray jArr = new JSONArray(response);
				jobDataList.clear();
				jobDataList2.clear();

				db.deleteJobListAllJobRecords();
				// db.truncateTableForMachineLIst();

				// db.deleteAllRecordsForMachineView();

				for (int i = 0; i < jArr.length(); i++) {
					String jsonObject = jArr.getString(i);

					JSONObject jObj = new JSONObject(jsonObject);

					String jsonJobObject = jObj.getString("Job");

					JSONObject jobObj = new JSONObject(jsonJobObject);

					String title = jobObj.getString("title");
					String docket_no = jobObj.getString("docket_no");
					String id = jobObj.getString("id");
					String due_date = jobObj.getString("due_date");
					String job_date = jobObj.getString("docket_date");

					String jsonSiteObject = jObj.getString("Site");

					JSONObject siteObj = new JSONObject(jsonSiteObject);

					String sitename = siteObj.getString("name");
					String siteAdd = siteObj.getString("address");

					String site_id = siteObj.getString("id");
					String latitude = siteObj.getString("latitude");
					String longitude = siteObj.getString("longitude");

					String sectorObj = siteObj.getString("Sector");
					String contractor = siteObj.getString("Contractor");

					String siteManager_list = siteObj.getString("SiteManager");

					JSONArray siteManagerjArr = new JSONArray(siteManager_list);

					String siteManagerName = "";

					if (siteManagerjArr.length() > 0) {

						for (int j = 0; j < siteManagerjArr.length(); j++) {
							String siteManagerListString = siteManagerjArr.getString(j);
							JSONObject siteManagerListObj = new JSONObject(siteManagerListString);
							siteManagerName += siteManagerListObj.getString("site_manager_name") + "\n";
						}

					} else {
						siteManagerName = "N/A";
					}
					Log.e("TAG", "contractor >>> " + contractor);
					String contractor_name = "";
					Object contractorjson = new JSONTokener(contractor).nextValue();
					if (contractorjson instanceof JSONObject) {
						Log.e("TAG", "contractor >>> " + contractor);
						JSONObject contractorObj = new JSONObject(contractor);

						contractor_name = contractorObj.getString("name");
					} else if (contractorjson instanceof JSONArray) {
						Log.e("TAG", "contractor >>> " + contractor);

						contractor_name = "N/A";
					}

					String sectorName = "";

					Object json = new JSONTokener(sectorObj).nextValue();
					if (json instanceof JSONObject) {
						Log.e("TAG", "Sector >>> " + sectorObj);
						JSONObject sectorjObj = new JSONObject(sectorObj);

						sectorName = sectorjObj.getString("name");
					} else if (json instanceof JSONArray) {
						Log.e("TAG", "Sector >>> " + sectorObj);

						sectorName = "Awaiting Sector from Admin";
					}

					String jsonJobStatusObject = jObj.getString("JobStatus");

					JSONObject jobStatusObj = new JSONObject(jsonJobStatusObject);

					String status = jobStatusObj.getString("status");
					String startPlace = jobStatusObj.getString("start_place");
					String finishPlace = jobStatusObj.getString("finish_place");

					String task_details = jObj.getString("JobDetail");

					String clientString = jObj.getString("Client");
					JSONObject clientObj = new JSONObject(clientString);

					String clientName = clientObj.getString("first_name");

					/**
					 * All Machine details operation will be done below
					 */

					JSONArray taskjArr = new JSONArray(task_details);

					Log.e("TAG", "TASK DETAILS Length>> " + taskjArr.length());

					for (int j = 0; j < taskjArr.length(); j++) {

						String jsonMachineDetails = taskjArr.getString(j);

						JSONObject jObjMachine = new JSONObject(jsonMachineDetails);

						String machineId = jObjMachine.getString("machine_id");
						/*
						 * String jobId = jObjMachine.getString("job_id");
						 * String errorCode =
						 * jObjMachine.getString("error_code"); String problem =
						 * jObjMachine.getString("problem"); String comments =
						 * jObjMachine.getString("comments");
						 */

						String machineDetailsView = jObjMachine.getString("Machine");

						Object jsonMachine = new JSONTokener(machineDetailsView).nextValue();
						if (jsonMachine instanceof JSONArray) {
							Log.e("TAG", "Blank");
						} else if (jsonMachine instanceof JSONObject) {
							JSONObject jObjMachineDetails = new JSONObject(machineDetailsView);

							String master_id = jObjMachineDetails.getString("master_id");
							String machine_serial_no = jObjMachineDetails.getString("machine_serial_no");
							String machine_name = jObjMachineDetails.getString("machine_name");
							String machine_type_name = jObjMachineDetails.getString("machine_type_name");
							String machine_manufacturer_name = jObjMachineDetails
									.getString("machine_manufacturer_name");
							String machine_model_name = jObjMachineDetails.getString("machine_model_name");
							String added_by = jObjMachineDetails.getString("added_by");

							String voltage = jObjMachineDetails.getString("voltage").equalsIgnoreCase("null") ? "0"
									: jObjMachineDetails.getString("voltage");
							String suction = jObjMachineDetails.getString("suction").equalsIgnoreCase("null") ? "0"
									: jObjMachineDetails.getString("suction");
							String traction = jObjMachineDetails.getString("traction").equalsIgnoreCase("null") ? "0"
									: jObjMachineDetails.getString("traction");
							String water = jObjMachineDetails.getString("water").equalsIgnoreCase("null") ? "0"
									: jObjMachineDetails.getString("water");
							String visual_inspection = jObjMachineDetails.getString("visual_inspection")
									.equalsIgnoreCase("null") ? "0" : jObjMachineDetails.getString("visual_inspection");
							String good_working_order = jObjMachineDetails.getString("good_working_order")
									.equalsIgnoreCase("null") ? "0"
											: jObjMachineDetails.getString("good_working_order");
							String spare_parts_available = jObjMachineDetails.getString("spare_parts_available")
									.equalsIgnoreCase("null") ? "0"
											: jObjMachineDetails.getString("spare_parts_available");
							String machineMarks = jObjMachineDetails.getString("marks_avail").equalsIgnoreCase("null")
									? "0" : jObjMachineDetails.getString("marks_avail");

							String mfg_year = jObjMachineDetails.getString("mfg_year");
							String machine_qr_code = jObjMachineDetails.getString("machine_qr_code");
							String description = jObjMachineDetails.getString("description");
							String youtube_link_1 = jObjMachineDetails.getString("youtube_link_1");
							String youtube_link_2 = jObjMachineDetails.getString("youtube_link_2");
							String machine_exp_date = jObjMachineDetails.getString("machine_exp_date");
							String parts_drawling = jObjMachineDetails.getString("parts_drawling");
							String user_manual = jObjMachineDetails.getString("user_manual");
							String data_sheet = jObjMachineDetails.getString("data_sheet");
							String machine_type_id = jObjMachineDetails.getString("machine_type_id");
							String machine_manufacturer_id = jObjMachineDetails.getString("machine_manufacturer_id");
							String model_id = jObjMachineDetails.getString("model_id");

							/**
							 * Calculate the total marks
							 */

							double[] staticMarksArray = { 50, 43.75, 37.5, 31.25, 25, 18.75, 12.50, 6.25, 0 };

							Log.e("TAG", "Marks >> " + voltage + " >>" + suction + " >>" + traction + " >>" + water
									+ " >>" + visual_inspection + " >>" + good_working_order + " >>"
									+ spare_parts_available + " >>" + machineMarks + " >> gh" + voltage + " >>");

							int machine_total = Integer.parseInt(voltage) + Integer.parseInt(suction)
									+ Integer.parseInt(traction) + Integer.parseInt(water)
									+ Integer.parseInt(visual_inspection) + Integer.parseInt(good_working_order)
									+ Integer.parseInt(spare_parts_available) + Integer.parseInt(machineMarks);

							int year = Calendar.getInstance().get(Calendar.YEAR);

							if (machine_exp_date.equalsIgnoreCase("0000-00-00")) {

								machine_exp_date = String.valueOf((year - 8));

							} else {
								machine_exp_date = machine_exp_date.substring(0, 4);
							}

							Log.e("TAG", "machine_exp_date >> " + machine_exp_date);

							int year_diff = (year - Integer.parseInt(machine_exp_date));

							if (year_diff > 8) {
								year_diff = 8;
							}

							Log.e("TAG", "Year diff >> " + year_diff);

							double total_marks = machine_total + staticMarksArray[year_diff];

							Log.e("TAG", "Total Marks >> " + total_marks);

							/**
							 * Add data into database for TABLE_MACHINE_LIST
							 */

							Machine machineItem = new Machine();

							machineItem.setMachine_id(machineId);
							machineItem.setMachine_si_no(machine_serial_no);
							machineItem.setMachine_name(machine_name);
							machineItem.setMachine_type(machine_type_name);
							machineItem.setManufacturer(machine_manufacturer_name);
							machineItem.setMarks(String.valueOf(total_marks));
							machineItem.setSite_name(sitename);

							int countMachineList = db.getMachineListCountByMachineId(machineId);
							if (countMachineList > 0) {

							} else {
								db.addMachine(machineItem);
							}

							/**
							 * Add data into database for TABLE_MACHINE_VIEW
							 */

							MachineView machineView = new MachineView();

							machineView.setMachine_id(machineId);
							machineView.setMachine_master_name(machine_name);
							machineView.setMachine_sl_no(machine_serial_no);
							machineView.setMachine_qr_code(machine_qr_code);
							machineView.setSite_name(sitename);
							machineView.setMachine_manufacturer(machine_manufacturer_name);
							machineView.setMachine_type(machine_type_name);
							machineView.setMachine_model(machine_model_name);
							machineView.setMachine_desc(description);
							machineView.setMachine_marks(String.valueOf(total_marks));
							machineView.setYoutube_link1(youtube_link_1);
							machineView.setYoutube_link2(youtube_link_2);
							machineView.setParts_drawling(parts_drawling);
							machineView.setUser_manual(user_manual);
							machineView.setData_sheet(data_sheet);

							int countMachineView = db.getMachineViewCountByMachineId(machineId);

							if (countMachineView > 0) {

							} else {
								db.addMachineViewData(machineView);
							}

							/**
							 * Add data into database for TABLE_MACHINE_DETAILS
							 */

							MachineDetails machineDetails = new MachineDetails(machine_manufacturer_id, machine_type_id,
									model_id, machine_name, machine_serial_no, added_by, voltage, suction, traction,
									water, mfg_year);

							int count = db.getMachineCountBySerialNo(machine_serial_no);
							if (count > 0) {

							} else {
								db.addMachineDetails(machineDetails, site_id);
							}

							/**
							 * Add data into database TABLE_MACHINEMASTER_LIST
							 */

							MachineMaster machineMaster = new MachineMaster();

							// setting data in MasterMachine
							machineMaster.setMachine_id(master_id);
							machineMaster.setMachine_desc(description);
							machineMaster.setMachine_name(machine_name);
							machineMaster.setManufacturer_id(machine_manufacturer_id);
							machineMaster.setModel_id(model_id);
							machineMaster.setType_id(machine_type_id);

							int count2 = db.getMachineCountByMachineId(master_id);
							if (count2 > 0) {

							} else {
								db.addmachineMaster(machineMaster);
							}

							/**
							 * Add data into database
							 * TABLE_MACHINEMANUFACTURER_LIST
							 */

							MachineManufacturer machineManufacturer = new MachineManufacturer();

							// setting data in MasterManufacturer
							machineManufacturer.setManufacture_id(machine_manufacturer_id);
							machineManufacturer.setManufacturer_name(machine_manufacturer_name);

							ArrayList<String> getManufactureIds = db.getMachineManufaturesId();

							if (getManufactureIds.contains(machine_manufacturer_id)) {

							} else {
								db.addmachineManufactureMaster(machineManufacturer);
							}

							/**
							 * Add data into database TABLE_MACHINETYPE_LIST
							 */

							Machinetype machinetype = new Machinetype();

							// setting data in MasterType
							machinetype.setType_id(machine_type_id);
							machinetype.setType_name(machine_type_name);
							machinetype.setType_desc("");

							int countType = db.getMachineTypeCountByTypeId(machine_type_id);

							if (countType > 0) {

							} else {
								db.addMachineTypeMaster(machinetype);
							}

							/**
							 * Add data into database TABLE_MACHINEMODEL_LIST
							 */

							MachineModel machineModel = new MachineModel();

							// setting data in MasterMopdel
							machineModel.setModel_id(model_id);
							machineModel.setMachine_id(machineId);
							machineModel.setModel_name(machine_model_name);

							int countModel = db.getModelCountByModelId(model_id);

							if (countModel > 0) {

							} else {
								db.addmachineModelMaster(machineModel);
							}
						}

					}

					Job jobItem = new Job();

					jobItem.setId(id);
					jobItem.setJobId(docket_no);
					jobItem.setJobTitle(title);
					jobItem.setSectorName(sectorName);
					jobItem.setSiteName(sitename);
					jobItem.setDueDate(getFormattedString(due_date));
					jobItem.setJobStatus(status);

					jobItem.setTask_details(task_details);
					jobItem.setSiteAddress(siteAdd);
					jobItem.setClient_name(clientName);
					jobItem.setContractor_name(contractor_name);
					jobItem.setSiteManagerList(siteManagerName);

					jobItem.setLatitude(latitude);
					jobItem.setLongitude(longitude);
					jobItem.setSiteid(site_id);
					jobItem.setJob_date(getFormattedString(job_date));

					jobItem.setStartPlace(startPlace);
					jobItem.setFinishPlace(finishPlace);

					db.addDataToJobList(jobItem);

					jobDataList.add(jobItem);
					jobDataList2.add(jobItem);
				}

				jobListsAdapter = new JobListsAdapter(getActivity(), jobDataList, FragmentJobs.this);
				jobLists.setAdapter(jobListsAdapter);
				jobListsAdapter.notifyDataSetChanged();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	private String getFormattedString(String strCurrentDate) {
		String date = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date newDate = format.parse(strCurrentDate);

			format = new SimpleDateFormat("dd/MM/yy");
			date = format.format(newDate);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return date;
	}

	public void openStartJobActivity(int position) {

		String docket_no = jobDataList.get(position).getJobId();
		Intent startJob = new Intent(getContext(), StartJobActivity.class);
		startJob.putExtra("DocketNo", docket_no);
		startJob.putExtra("JOB", "joblist");
		startActivity(startJob);
		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

	}

	public void openViewJobActivity(int position) {

		String docket_no = jobDataList.get(position).getJobId();
		Intent startJob = new Intent(getContext(), ViewJobActivity.class);
		startJob.putExtra("DocketNo", docket_no);
		startActivity(startJob);
		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

	}

	public void openCreateDocketActivity(int position) {

		/*
		 * Toast.makeText(getContext(), "JobLists Position:" + position +
		 * "and docket no:" + jobDataList.get(position).getJobId(),
		 * Toast.LENGTH_LONG).show();
		 */
		// Toast.makeText(getContext(), "Clicked Laugh Vote/::" +
		// jobDataList.get(position).getJobId(), Toast.LENGTH_SHORT).show();
		String docket_no = jobDataList.get(position).getJobId();
		String site_id = jobDataList.get(position).getSiteid();
		String client_id = jobDataList.get(position).getClient_id();

		Config.site_id = site_id;
		Config.client_id = client_id;
		Config.docket_id = docket_no;

		Log.e("TAG", "site id passes>> " + Config.site_id);
		Log.e("TAG", "client id passes>> " + Config.client_id);
		Log.e("TAG", "docket id passes>> " + Config.docket_id);

		Intent startJob = new Intent(getContext(), CreateDocketActivity.class);
		startJob.putExtra("DocketNo", docket_no);
		startActivity(startJob);

		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

	}

	public void completeJobStatusPopUp(final int position) {

		final Dialog dialog = new Dialog(getContext(), R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_complete_job);

		Button cancelCompletion = (Button) dialog.findViewById(R.id.cancel_completion);
		Button ok_completion = (Button) dialog.findViewById(R.id.ok_job_completion);

		final String docket_id = jobDataList.get(position).getJobId();

		cancelCompletion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		ok_completion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				Job jobItem = db.getJobItemByDocketId(docket_id);

				String job_id = jobItem.getId();

				db.updateJobStatus(job_id);

				db.updateStartJobTable(job_id, getDateTime());

				jobDataList.clear();

				jobDataList = db.getAllDataFromJob();

				jobListsAdapter = new JobListsAdapter(getActivity(), jobDataList, FragmentJobs.this);
				jobLists.setAdapter(jobListsAdapter);
				jobListsAdapter.notifyDataSetChanged();

				jobLists.smoothScrollToPosition(position);
			}
		});
		dialog.show();
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}

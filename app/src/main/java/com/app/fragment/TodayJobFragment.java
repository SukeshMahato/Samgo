package com.app.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.JobListAdapter;
import com.app.adapter.MachineJobDetailsAdapter;
import com.app.asyncs.ErrorCodeListServices;
import com.app.asyncs.MasterSparePartsServices;
import com.app.asyncs.TodaysJobServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.ErrorCodeListListener;
import com.app.listners.MasterSparePartsListener;
import com.app.listners.TodayJobsListener;
import com.app.model.AppManager;
import com.app.model.ErrorCodeModel;
import com.app.model.Job;
import com.app.model.JobDetails;
import com.app.model.Machine;
import com.app.model.MachineDesc;
import com.app.model.MachineDetails;
import com.app.model.MachineManufacturer;
import com.app.model.MachineMaster;
import com.app.model.MachineModel;
import com.app.model.MachineSiteMain;
import com.app.model.MachineView;
import com.app.model.Machinetype;
import com.app.model.MasterSpareParts;
import com.app.model.Site;
import com.app.samgo.R;
import com.app.samgo.StartJobActivity;
import com.app.services.AddSparePartsService;
import com.app.services.ConnectionDetector;
import com.app.services.GetSparePartsService;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class TodayJobFragment extends Fragment
		implements TodayJobsListener, MasterSparePartsListener, ErrorCodeListListener {

	View v;
	private ListView jobList;
	String response="";
	ProgressDialog progressDialog;
	JSONArray jsonArray;

	private JobListAdapter jobListAdapter;
	private ProgressDialog progressBar;
	private ArrayList<Job> jobDataList = new ArrayList<Job>();
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	private SamgoSQLOpenHelper db;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_todays_job, container, false);
		this.initialisation();
		this.listner();
		progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());
		db = new SamgoSQLOpenHelper(getContext());
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {

			SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
			String todaysjobDate = settings.getString("TodayJobDate", "test");
			if (todaysjobDate.equalsIgnoreCase(getDateTime())) {
				Log.e("TAG", "TESTING >>> <<<");
				//new ErrorCodeListServices(TodayJobFragment.this, getActivity()).execute();
				jobDataList.clear();
				jobDataList = db.getAllDataFromJobForToday(getDateTime());
				jobListAdapter = new JobListAdapter(TodayJobFragment.this, getActivity(), jobDataList);
				jobList.setAdapter(jobListAdapter);
				jobListAdapter.notifyDataSetChanged();
				new TodaysJobServices(this, AppManager.getSinleton().getUser().getId(), getActivity()).execute();
			} else {
				SharedPreferences settings1 = getActivity().getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings1.edit();
				// Set "hasLoggedIn" to true
				editor.putString("TodayJobDate", getDateTime());
				// Commit the edits!
				editor.commit();
				new TodaysJobServices(this, AppManager.getSinleton().getUser().getId(), getActivity()).execute();
			}
		} else {

			jobDataList.clear();
			jobDataList = db.getAllDataFromJobForToday(getDateTime());
			jobListAdapter = new JobListAdapter(TodayJobFragment.this, getActivity(), jobDataList);
			jobList.setAdapter(jobListAdapter);
			jobListAdapter.notifyDataSetChanged();
		}

		try {
			String lastrecord=db.countdata();
			Log.e("lastrecord: ",lastrecord);
			if (lastrecord == ""){
				//new MasterSparePartsServices(getActivity(), TodayJobFragment.this).execute();
			}
		}catch (Exception e){
			Log.e("Exception: ",e.toString());
		}
		return v;
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void listner() {
		jobList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		jobList = (ListView) v.findViewById(R.id.today_job_list);
	}

	public void openPopupForTaskDetails(int position) {
		// Toast.makeText(getContext(), "Position >> " + position,
		// Toast.LENGTH_LONG).show();

		ArrayList<MachineDesc> machineJobDetails = new ArrayList<MachineDesc>();
		final Dialog dialog = new Dialog(getContext(), R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_task_details);
		// dialog.setTitle("Login Failed");

		Button closeDialog = (Button) dialog.findViewById(R.id.close_dialog);
		ListView machineDetails = (ListView) dialog.findViewById(R.id.task_details);

		try {

			ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();

			jobDetailsArray.clear();

			jobDetailsArray = db.getAllJobDetails(jobDataList.get(position).getId());

			for (int i = 0; i < jobDetailsArray.size(); i++) {

				String machineId = jobDetailsArray.get(i).getMachineId();

				String problem = jobDetailsArray.get(i).getProblem();

				MachineView machineView = db.getMachineViewDataByMachineId(machineId);

				String machineSR = machineView.getMachine_sl_no();

				if (machineView.getMachine_master_name() != null) {
					machineJobDetails.add(new MachineDesc(machineSR, problem));
				}

			}

			MachineJobDetailsAdapter machineJobDetailsAdapter = new MachineJobDetailsAdapter(getActivity(),
					machineJobDetails);
			machineDetails.setAdapter(machineJobDetailsAdapter);
			machineJobDetailsAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		closeDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public void openStartJobActivity(int position, String status) {

		if (!status.equalsIgnoreCase("Start Job")) {
			Toast.makeText(getContext(), "Status:" + status, Toast.LENGTH_LONG).show();
		} else {

			String docket_no = jobDataList.get(position).getJobId();
			Intent startJob = new Intent(getContext(), StartJobActivity.class);
			startJob.putExtra("DocketNo", docket_no);
			startJob.putExtra("JOB", "joblist");
			startActivity(startJob);
		}
	}

	@Override
	public void getTodaysJobResponse(String RequestMessage) {
		// TODO Auto-generated method stub
		Log.e("TAG", "JOBResult ??? >> " + RequestMessage);

		if (RequestMessage != null) {
			try {
				JSONArray jArr = new JSONArray(RequestMessage);
				jobDataList.clear();
				db.deleteTodaysAllJobRecords();
				if (jArr != null && jArr.length() > 0) {
					for (int i = 0; i < jArr.length(); i++) {
						String jsonObject = jArr.getString(i);
						JSONObject jObj = new JSONObject(jsonObject);
						String jsonJobObject = jObj.getString("Job");
						JSONObject jobObj = new JSONObject(jsonJobObject);
						/**
						 * Get the details from the Job object
						 **/

						String title = jobObj.getString("title");
						String docket_no = jobObj.getString("docket_no");
						String id = jobObj.getString("id");
						String client_id = jobObj.getString("client_id");
						String due_date = jobObj.getString("due_date");
						String job_date = jobObj.getString("docket_date");
						String site_id = jobObj.getString("site_id");
						String job_comment = jobObj.getString("comments");
						/**
						 * Get the details from the Site object
						 */

						String jsonSiteObject = jObj.getString("Site");
						JSONObject siteObj = new JSONObject(jsonSiteObject);
						String sitename = siteObj.getString("name");
						String siteAdd = siteObj.getString("address");
						String latitude = siteObj.getString("latitude");
						String longitude = siteObj.getString("longitude");
						String county = siteObj.getString("county");
						String city = siteObj.getString("city");

						/**
						 * Get the machine details under sites also all
						 * operations under machine will take place here
						 */

						JSONArray jArrMachine = new JSONArray(siteObj.getString("Machine"));
						if (jArrMachine != null && jArrMachine.length() > 0) {
							for (int j = 0; j < jArrMachine.length(); j++) {

								JSONObject jObjMachine = new JSONObject(jArrMachine.getString(j));
								String siteid = jObjMachine.getString("site_id");

								/**
								 * Add Machine Details view
								 */

								String machine_si_no = jObjMachine.getString("machine_serial_no");
								String machine_name = jObjMachine.getString("machine_name");
								String machine_type = jObjMachine.getString("machine_type_id");
								String manufacturer = jObjMachine.getString("machine_manufacturer_id");
								String machine_model = jObjMachine.getString("model_id");
								String machine_added_by = jObjMachine.getString("added_by");
								String voltage = jObjMachine.getString("voltage").equalsIgnoreCase("null") ? "0"
										: jObjMachine.getString("voltage");
								String suction = jObjMachine.getString("suction").equalsIgnoreCase("null") ? "0"
										: jObjMachine.getString("suction");
								String traction = jObjMachine.getString("traction").equalsIgnoreCase("null") ? "0"
										: jObjMachine.getString("traction");
								String water = jObjMachine.getString("water").equalsIgnoreCase("null") ? "0"
										: jObjMachine.getString("water");
								String mfg_year = jObjMachine.getString("mfg_year");
								String machine_photo = jObjMachine.getString("image");

								String visual_inspection = jObjMachine.getString("visual_inspection")
										.equalsIgnoreCase("null") ? "0" : jObjMachine.getString("visual_inspection");
								String good_working_order = jObjMachine.getString("good_working_order")
										.equalsIgnoreCase("null") ? "0" : jObjMachine.getString("good_working_order");
								String spare_parts_available = jObjMachine.getString("spare_parts_available")
										.equalsIgnoreCase("null") ? "0"
												: jObjMachine.getString("spare_parts_available");
								String machineMarks = jObjMachine.getString("marks_avail").equalsIgnoreCase("null")
										? "0" : jObjMachine.getString("marks_avail");

								MachineDetails machineDetails = new MachineDetails(manufacturer, machine_type,
										machine_model, machine_name, machine_si_no, machine_added_by, voltage, suction,
										traction, water, mfg_year);
								machineDetails.setWork_order(good_working_order);
								machineDetails.setVisual_inspect(visual_inspection);
								machineDetails.setSpare_parts(spare_parts_available);
								machineDetails.setMarks_avail(machineMarks);

								int count = db.getMachineCountBySerialNo(machine_si_no);

								if (count > 0) {

								} else {
									db.addMachineDetails(machineDetails, siteid);
								}

								/**
								 * Add data into database for TABLE_MACHINE_LIST
								 */

								String machineId = jObjMachine.getString("id");

								String master_id = jObjMachine.getString("master_id");

								String machine_type_name = jObjMachine.getString("machine_type_name");
								String machine_manufacturer_name = jObjMachine.getString("machine_manufacturer_name");
								// String machine_model_name = "";
								String machine_model_name = jObjMachine.getString("machine_model_name");
								String machine_photo2 = jObjMachine.getString("image");
								String machine_qr_code = jObjMachine.getString("machine_qr_code");
								String description = jObjMachine.getString("description");
								String youtube_link_1 = jObjMachine.getString("youtube_link_1");
								String youtube_link_2 = jObjMachine.getString("youtube_link_2");
								String machine_exp_date = jObjMachine.getString("machine_exp_date");
								String parts_drawling = jObjMachine.getString("parts_drawling");
								String user_manual = jObjMachine.getString("user_manual");
								String data_sheet = jObjMachine.getString("data_sheet");

								/**
								 * Calculate the total marks
								 */

								double[] staticMarksArray = { 50, 43.75, 37.5, 31.25, 25, 18.75, 12.50, 6.25, 0 };

								int machine_total = Integer.parseInt(voltage) + Integer.parseInt(suction)
										+ Integer.parseInt(traction) + Integer.parseInt(water)
										+ Integer.parseInt(visual_inspection) + Integer.parseInt(good_working_order)
										+ Integer.parseInt(spare_parts_available) + Integer.parseInt(machineMarks);

								int year = Calendar.getInstance().get(Calendar.YEAR);

								if (machine_exp_date != null) {
									if (machine_exp_date.equalsIgnoreCase("0000-00-00")) {

										machine_exp_date = String.valueOf((year - 8));

									} else {
										machine_exp_date = machine_exp_date.substring(0, 4);
									}
								} else {
									machine_exp_date = "0000";
								}
								int year_diff = year;
								if (machine_exp_date.matches("-?\\d+")) {
									year_diff = (year - Integer.parseInt(machine_exp_date));
								}
								if (year_diff > 8) {
									year_diff = 8;
								}

								double total_marks = machine_total + staticMarksArray[year_diff];

								Machine machineItem = new Machine();

								machineItem.setMachine_id(machineId);
								machineItem.setMachine_si_no(machine_si_no);
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
								machineView.setMachine_sl_no(machine_si_no);
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
								machineView.setMachine_photo(machine_photo2);
								machineView.setMachinePhoto(machine_photo);

								int countMachineView = db.getMachineViewCountByMachineId(machineId);

								Log.e("TAG", "Machine id will be what by me >>> " + machineId);

								if (countMachineView > 0) {

								} else {
									db.addMachineViewData(machineView);
								}

								/**
								 * Add data into database
								 * TABLE_MACHINEMASTER_LIST
								 */

								MachineMaster machineMaster = new MachineMaster();

								// setting data in MasterMachine
								machineMaster.setMachine_id(master_id);
								machineMaster.setMachine_desc(description);
								machineMaster.setMachine_name(machine_name);
								machineMaster.setManufacturer_id(manufacturer);
								machineMaster.setModel_id(machine_model);
								machineMaster.setType_id(machine_type);

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
								machineManufacturer.setManufacture_id(manufacturer);
								machineManufacturer.setManufacturer_name(machine_manufacturer_name);

								ArrayList<String> getManufactureIds = db.getMachineManufaturesId();

								if (getManufactureIds.contains(manufacturer)) {

								} else {
									db.addmachineManufactureMaster(machineManufacturer);
								}

								/**
								 * Add data into database TABLE_MACHINETYPE_LIST
								 */

								Machinetype machinetype = new Machinetype();

								// setting data in MasterType
								machinetype.setType_id(machine_type);
								machinetype.setType_name(machine_type_name);
								machinetype.setType_desc("");

								int countType = db.getMachineTypeCountByTypeId(machine_type);

								if (countType > 0) {

								} else {
									db.addMachineTypeMaster(machinetype);
								}

								/**
								 * Add data into database
								 * TABLE_MACHINEMODEL_LIST
								 */

								MachineModel machineModel = new MachineModel();

								// setting data in MasterMopdel
								machineModel.setModel_id(machine_model);
								machineModel.setMachine_id(machineId);
								machineModel.setModel_name(machine_model_name);

								int countModel = db.getModelCountByModelId(machine_model);

								if (countModel > 0) {

								} else {
									db.addmachineModelMaster(machineModel);
								}

								/**
								 * Add Machine to site main
								 */

								MachineSiteMain result = new MachineSiteMain();
								result.setMachineSuction("Y");
								result.setMachineTraction("Y");
								result.setMachineWater("Y");
								result.setMachineSpareParts("Y");
								result.setMachineVoltage("Y");
								result.setMachineWorkOrder(good_working_order);
								result.setMachineMAnufacturer(machine_manufacturer_name);
								result.setMachineMAnufacturer_id(manufacturer);
								result.setMachineType(machine_type_name);
								result.setMachineType_id(machine_type);
								result.setMachineName(machine_name);
								result.setMachineName_id(machineId);
								result.setMachineModel(machine_model_name);
								result.setMachineModel_id(machine_model);
								result.setMachineSINo(machine_si_no);
								result.setMachineManuYear(mfg_year);
								result.setMachineMarksAvail(machineMarks);
								result.setMachineVisualInspection(visual_inspection);
								result.setSite_id(site_id);

								Log.e("TAG", "Site Id  >> " + site_id);

								int countMachine = db.getCountAddMachineList(machine_si_no);

								Log.e("TAG", "Count machine >> " + countMachine);

								if (countMachine > 0) {

								} else {
									db.addMachineToSiteMain(result);

								}
							}

						}

						/**
						 * Get the sub details under site, sector, contractor,
						 * site manager
						 */

						/**
						 * Get the sector details
						 */

						String sectorObj = siteObj.getString("Sector");

						String sectorName = "";

						Object json = new JSONTokener(sectorObj).nextValue();
						if (json instanceof JSONObject) {
							JSONObject sectorjObj = new JSONObject(sectorObj);

							sectorName = sectorjObj.getString("name");
						} else if (json instanceof JSONArray) {

							sectorName = "Awaiting Sector from Admin";
						}

						/**
						 * Get the contractor details
						 */

						String contractor = siteObj.getString("Contractor");

						String contractor_name = "";

						Object contractorjson = new JSONTokener(contractor).nextValue();
						if (contractorjson instanceof JSONObject) {
							JSONObject contractorObj = new JSONObject(contractor);

							contractor_name = contractorObj.getString("name");
						} else if (contractorjson instanceof JSONArray) {

							contractor_name = "N/A";
						}

						/**
						 * Get the site manager details
						 */

						String siteManager_list = siteObj.getString("SiteManager");
						JSONArray siteManagerjArr = new JSONArray(siteManager_list);

						String siteManagerName = "";

						if (siteManagerjArr != null && siteManagerjArr.length() > 0) {

							for (int j = 0; j < siteManagerjArr.length(); j++) {
								String siteManagerListString = siteManagerjArr.getString(j);

								JSONObject siteManagerListObj = new JSONObject(siteManagerListString);

								siteManagerName += siteManagerListObj.getString("site_manager_name") + "\n";
							}

						} else {
							siteManagerName = "N/A";
						}

						/**
						 * Get the job status
						 */

						String jsonJobStatusObject = jObj.getString("JobStatus");

						JSONObject jobStatusObj = new JSONObject(jsonJobStatusObject);

						String status = jobStatusObj.getString("status");
						String startPlace = jobStatusObj.getString("start_place");
						String finishPlace = jobStatusObj.getString("finish_place");

						/**
						 * Get the job details
						 */

						String task_details = jObj.getString("JobDetail");

						Log.e("TAG", "TASK DETAILS by Rohit >><<>> " + task_details);

						/**
						 * Add data into local jobdetail table
						 */

						try {

							JSONArray taskDetailArr = new JSONArray(task_details);

							if (taskDetailArr != null && taskDetailArr.length() > 0) {
								for (int p = 0; p < taskDetailArr.length(); p++) {

									String jsonMachineDetails = taskDetailArr.getString(p);

									Log.e("TAG", "jsonMachineDetails by Rohit >> " + jsonMachineDetails);

									JSONObject jObjMachine = new JSONObject(jsonMachineDetails);

									String machineId = jObjMachine.getString("machine_id");
									String jobId = jObjMachine.getString("job_id");

									String errorCode = jObjMachine.getString("error_code");
									String problem = jObjMachine.getString("problem");

									Log.e("TAG", "machineId >>> " + machineId);
									Log.e("TAG", "jobId >>> " + jobId);
									Log.e("TAG", "errorCode >>> " + errorCode);
									Log.e("TAG", "problem >>> " + problem);

									JobDetails jobDetails = new JobDetails();

									jobDetails.setErrorCode(errorCode);
									jobDetails.setJobId(jobId);
									jobDetails.setMachineId(machineId);
									jobDetails.setProblem(problem);

									int countJobDetail = db.getCountForJobDetails(jobId, machineId, errorCode);

									if (countJobDetail > 0) {

									} else {
										db.addJobDetails(jobDetails);
									}

								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						/**
						 * Get the client details
						 */

						String clientString = jObj.getString("Client");
						JSONObject clientObj = new JSONObject(clientString);

						String clientName = clientObj.getString("first_name");

						/**
						 * Add data to its model
						 */

						/**
						 * For Job Model
						 */

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
						jobItem.setClient_id(client_id);
						jobItem.setContractor_name(contractor_name);
						jobItem.setSiteManagerList(siteManagerName);

						jobItem.setLatitude(latitude);
						jobItem.setLongitude(longitude);
						jobItem.setSiteid(site_id);
						jobItem.setJob_date(getFormattedString(job_date));

						jobItem.setStartPlace(startPlace);
						jobItem.setFinishPlace(finishPlace);
						jobItem.setComment(job_comment);

						int countJob = db.getCountForJobList(id);

						if (countJob > 0) {

						} else {
							db.addDataToJobList(jobItem);
						}

						/**
						 * For Site Model
						 */
						Site site = new Site();
						site.setId(site_id);
						site.setName(sitename);
						site.setCounty(county);
						site.setCity(city);
						site.setClientName(clientName);
						site.setLatitude(latitude);
						site.setLongitude(longitude);
						site.setPhotoPath("");
						site.setSiteAddress(siteAdd);
						site.setContractorName(contractor_name);
						db.addSite(site);
						jobDataList.add(jobItem);
					}
				}
				jobListAdapter = new JobListAdapter(TodayJobFragment.this, getActivity(), jobDataList);
				jobList.setAdapter(jobListAdapter);
				jobListAdapter.notifyDataSetChanged();
				SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
				boolean loadSpareParts = settings.getBoolean("LoadSpareParts", false);
				if (loadSpareParts) {

				} else {

					SharedPreferences settings1 = getActivity().getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings1.edit();

					// Set "hasLoggedIn" to true
					editor.putBoolean("LoadSpareParts", true);

					// Commit the edits!
					editor.commit();

					new MasterSparePartsServices(getActivity(), TodayJobFragment.this).execute();
				}

			} catch (Exception e) {
				e.printStackTrace();
                Log.e("Exception get@dayJOb",e.toString());
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

	@Override
	public void getMasterSparePartsResponse(String responseMessage) throws JSONException {
		// TODO Auto-generated method stub
        try {
            if (responseMessage != null) {
                db.truncateSpareParts();
                JSONArray response = new JSONArray(responseMessage);
                new UpdateSparePartsToDatabase().execute(response);
            }
        }catch(NullPointerException e){
            Log.e("Exception SpareParts",e.toString());
        }
//			Intent intent = new Intent(getActivity(), AddSparePartsService.class);
//			intent.putExtra("response", responseMessage);
//			getActivity().startService(intent);
//			response = responseMessage;
//			Log.e("response: ", responseMessage);
//			try {
//				jsonArray = new JSONArray(response);
//			} catch (JSONException e) {
//				Log.e("jsonexception: ", e.toString());
//			}
//
//
//			new parsingtask().execute(responseMessage);

//			Intent intent = new Intent(getActivity(), AddSparePartsService.class);
//			intent.putExtra("response", responseMessage);
//			getActivity().startService(intent);


			//getActivity().startService(new Intent(getActivity(), GetSparePartsService.class));
			//new UpdateSparePartsToDatabase().execute(response);
//			InputStream stream = new ByteArrayInputStream(responseMessage.getBytes(StandardCharsets.UTF_8));
//			try {
//				readJsonStream(stream);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

	}

	public void readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			readJSONArray(reader);
		} finally {
			reader.close();
		}
	}

	public void readJSONArray(JsonReader reader) throws IOException {
		reader.beginArray();

		while (reader.hasNext()) {

			readParentJSONObject(reader);

		}
		reader.endArray();
	}

	public void readParentJSONObject(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {

			String name = reader.nextName();

			if (name.equals("SpareMaster") && reader.peek() != JsonToken.NULL) {
				readJSONObject(reader);
			} else {
				reader.skipValue();
			}
		}

		reader.endObject();
	}

	public void readJSONObject(JsonReader reader) throws IOException {
		reader.beginObject();

		MasterSpareParts masterSpareParts = new MasterSpareParts();

		String id = "";
		String product_id = "";
		String desc = "";
		String sales_price = "";
		String qty = "";
		String bcode = "";

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (name.equals("id") && reader.peek() != JsonToken.NULL) {
				id = reader.nextString();

				masterSpareParts.setId(id);
			} else if (name.equals("product_id") && reader.peek() != JsonToken.NULL) {
				product_id = reader.nextString();

				masterSpareParts.setProduct_id(product_id);
			} else if (name.equals("description") && reader.peek() != JsonToken.NULL) {
				desc = reader.nextString();
				masterSpareParts.setDescription(desc);
			} else if (name.equals("sales_price") && reader.peek() != JsonToken.NULL) {
				sales_price = reader.nextString();
				masterSpareParts.setUnit_sales(sales_price);
			} else if (name.equals("qty") && reader.peek() != JsonToken.NULL) {
				qty = reader.nextString();

				masterSpareParts.setQuantity(qty);
			}  else if (name.equals("barcode") && reader.peek() != JsonToken.NULL) {
				bcode = reader.nextString();

				masterSpareParts.setBarcode(bcode);
			}else {
				reader.skipValue();
			}

		}

		int count = db.getCountSparePartsById(id);

		if (count > 0) {

		} else {
			db.addSpareParts(masterSpareParts);
		}

		reader.endObject();
	}

	@Override
	public void getAllErrorCode(String response) {
		// TODO Auto-generated method stub
		Log.e("ERROR CODE >>", "RESPONSE >>> ");
		if (response != null) {
			try {

				Log.e("ERROR CODE >>", "RESPONSE >>> " + response);
				JSONArray machineErrorCodeArray = new JSONArray(response);
				db.truncateErrorCodeTable();
				for (int i = 0; i < machineErrorCodeArray.length(); i++) {

					JSONObject machineErrorObj = new JSONObject(machineErrorCodeArray.getString(i));
					String machineErrorCode = machineErrorObj.getString("MachineErrorCode");
					JSONObject machineErrorDesc = new JSONObject(machineErrorCode);
					String id = machineErrorDesc.getString("id");
					String name = machineErrorDesc.getString("name");
					String description = machineErrorDesc.getString("description");
					ErrorCodeModel errorCodeModel = new ErrorCodeModel();
					errorCodeModel.setId(id);
					errorCodeModel.setErrorCode(name);
					errorCodeModel.setProblem(description);
					db.addErrorCodeToTable(errorCodeModel);
				}
                //new MasterSparePartsServices(getActivity(), TodayJobFragment.this).execute();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

    String sqlStatement="";
    int k=0;

    public class UpdateSparePartsToDatabase extends AsyncTask<JSONArray,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Synchronizing data....");
            progressDialog.show();
            db.truncateSpareParts();
        }

        @Override
        protected String doInBackground(JSONArray... jsonArrays) {
            try {
                final JSONArray jsonArray = jsonArrays[0];
                int j = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    k = i;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            // some code #3 (Write your code here to run in UI thread)
                            progressDialog.setMessage("Synchronizing data...." + k + "/" + jsonArray.length());
                        }
                    });
                    // progressDialog.setMessage("Synchronizing data...."+i+"/1000");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("SpareMaster");
                    String pid = jsonObject1.getString("product_id");
                    String id = jsonObject1.getString("id");

                    String desc = jsonObject1.getString("description");
                    desc = desc.replace("'","");
                    String sales_price = jsonObject1.getString("sales_price");
                    String qty = jsonObject1.getString("qty");
                    String barcode = jsonObject1.getString("barcode");

                    j++;
                    try {
                        if (i == (jsonArray.length() - 1)) {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "')";
                            db.insertStatement(sqlStatement);
                        } else if (j == 500) {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "')";
                            db.insertStatement(sqlStatement);
                            sqlStatement = "";
                            j = 0;
                        } else {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "'),";
                        }
                    }catch (SQLException sqlexception){
                        Log.e("sqlexcep: ", sqlexception.toString());
                        Log.e("Id: ",id);
                    }

                }
                Log.e("values", sqlStatement);

            }catch (JSONException e){
                return e.toString();
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            k=0;
            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
        }
    }

}

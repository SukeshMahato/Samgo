package com.app.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.JobListAdapter;
import com.app.asyncs.AddMachine;
import com.app.asyncs.DocketSyncServices;
import com.app.asyncs.ErrorCodeListServices;
import com.app.asyncs.MachineMasterServices;
import com.app.asyncs.MachineSyncServices;
import com.app.asyncs.MasterSparePartsServices;
import com.app.asyncs.ParkJobSyncServices;
import com.app.asyncs.TodaysJobServices;
import com.app.asyncs.TrainingSyncServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.AddMachineMasterListener;
import com.app.listners.DialogDissmiss;
import com.app.listners.DocketSyncListner;
import com.app.listners.ErrorCodeListListener;
import com.app.listners.MachineSyncListner;
import com.app.listners.MasterSparePartsListener;
import com.app.listners.ParkJobSyncListner;
import com.app.listners.TodayJobsListener;
import com.app.listners.TrainingSyncListner;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.DocketDetail;
import com.app.model.DocketList;
import com.app.model.ErrorCodeModel;
import com.app.model.Job;
import com.app.model.JobDetails;
import com.app.model.Machine;
import com.app.model.MachineDetails;
import com.app.model.MachineManufacturer;
import com.app.model.MachineMaster;
import com.app.model.MachineModel;
import com.app.model.MachineSiteMain;
import com.app.model.MachineView;
import com.app.model.Machinetype;
import com.app.model.Site;
import com.app.model.SparePartsModel;
import com.app.model.StartJobModel;
import com.app.model.TrainingAddedToJob;
import com.app.model.TrainingSyncPojo;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SyncClass extends Fragment
		implements DialogDissmiss,DocketSyncListner, ParkJobSyncListner, MachineSyncListner, TrainingSyncListner,TodayJobsListener, MasterSparePartsListener, ErrorCodeListListener, AddMachineMasterListener {

    private ListView jobList;
    ProgressDialog progressDialog;


    private JobListAdapter jobListAdapter;

    private ArrayList<Job> jobDataList = new ArrayList<Job>();
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;
    private SamgoSQLOpenHelper db;
    public static final String PREFS_NAME = "MyPrefsFile";

	private ArrayList<Job> jobArraylist = new ArrayList<Job>();

	private ArrayList<Job> parkjobArraylist = new ArrayList<Job>();

	private ArrayList<JobDetails> jobDetailArray = new ArrayList<JobDetails>();

	private ArrayList<DocketDetail> jobDocketDetailArray = new ArrayList<DocketDetail>();

	private ArrayList<SparePartsModel> sparePartsArray = new ArrayList<SparePartsModel>();

	private ArrayList<TrainingAddedToJob> trainingJobArray = new ArrayList<TrainingAddedToJob>();

	private ArrayList<StartJobModel> startJobArray = new ArrayList<StartJobModel>();

	ArrayList<MachineSiteMain> machineSiteMainList = new ArrayList<MachineSiteMain>();

	ArrayList<TrainingSyncPojo> trainingSyncPojoList = new ArrayList<TrainingSyncPojo>();

	private Button sync,download;
	
	private LinearLayout parentLayout;

	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.sync_activity, container, false);

		db = new SamgoSQLOpenHelper(getContext());
		sync = (Button) v.findViewById(R.id.upload);
		download = (Button) v.findViewById(R.id.download);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
		jobArraylist = db.getAllDataFromJobforSync("Closed");
		parkjobArraylist = db.getAllDataFromJobforSync("Call Back");
		machineSiteMainList = db.getMachineDataListForSync();
		trainingSyncPojoList = db.getTrainingSyncDetails();
		parentLayout = (LinearLayout) v.findViewById(R.id.parent_layout);
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
				parentLayout.setBackgroundResource(R.drawable.background_5);
			}

		}

		Log.e("TAG", "Size value for machine::" + machineSiteMainList.size());

		sync.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String data = syncDocketData(jobArraylist);

				String jobDetailData = syncJobDetailData(jobArraylist);

				String docketDetailData = syncDocketDetailData(jobArraylist);

				String SparePartsData = syncSparePartsData(jobArraylist);
				String TrainingData = syncTrainingData(jobArraylist);

				String jobStatusData = syncJobStatusData(jobArraylist);

				String machineSyncData = syncMachineData(machineSiteMainList);

				String trainingSyncData = syncTrainingDataIndividual(trainingSyncPojoList);

				new DocketSyncServices(getActivity(), data, jobDetailData, docketDetailData, SparePartsData,
						TrainingData, jobStatusData, SyncClass.this).execute();

				new MachineSyncServices(getActivity(), machineSyncData, SyncClass.this).execute();

				new TrainingSyncServices(getActivity(), trainingSyncData, SyncClass.this).execute();

			}
		});
        
        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait it will take few minutes...");
                progressDialog.show();
                new ErrorCodeListServices(SyncClass.this, getActivity()).execute();
                new TodaysJobServices(SyncClass.this, AppManager.getSinleton().getUser().getId(), getActivity()).execute();
            }
        });

		return v;
	}


    @Override
    public void getTodaysJobResponse(String RequestMessage) {
        // TODO Auto-generated method stub
        Log.e("TAG", "JOBResult ??? >> " + RequestMessage);
        new MasterSparePartsServices(getActivity(), SyncClass.this).execute();
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

//                SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
//                boolean loadSpareParts = settings.getBoolean("LoadSpareParts", false);
////				if (loadSpareParts) {
////
////				} else {
//
//                SharedPreferences settings1 = getActivity().getSharedPreferences(PREFS_NAME, 0);
//                SharedPreferences.Editor editor = settings1.edit();
//
//                // Set "hasLoggedIn" to true
//                editor.putBoolean("LoadSpareParts", true);
//
//                // Commit the edits!
//                editor.commit();

                //new MasterSparePartsServices(getActivity(), SyncClass.this).execute();
                //}

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception get@dayJOb",e.toString());
                //new MasterSparePartsServices(getActivity(), SyncClass.this).execute();
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


    private String syncDocketData(ArrayList<Job> jobarrayList) {

		String data = "";

		try {

			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonObjectForDocket = new JSONObject();

			for (int i = 0; i < jobarrayList.size(); i++) {

				String jobId = jobarrayList.get(i).getId();
				String siteId = jobarrayList.get(i).getSiteid();
				String clientId = jobarrayList.get(i).getClient_id();

				int count = db.getCountForDocket(jobId);

				JSONObject jsonDocketString;

				if (count > 0) {
					jsonDocketString = new JSONObject();
					DocketList docketList = new DocketList();

					docketList = db.getDocketByJobId(jobId);

					jsonDocketString.put("DocketDate", docketList.getDocket_date());
					jsonDocketString.put("CheckIn", docketList.getCheck_in());
					jsonDocketString.put("CheckOut", docketList.getCheck_out());
					jsonDocketString.put("PatTest", docketList.getPad_test());
					jsonDocketString.put("Comment", docketList.getComment());
					jsonDocketString.put("Status", docketList.getStatus());
					jsonDocketString.put("JobId", jobId);
					jsonDocketString.put("SiteId", siteId);
					jsonDocketString.put("ClientId", clientId);
					jsonDocketString.put("Signature", docketList.getSignDecode());
					jsonDocketString.put("signator_name", docketList.getSignName());
					jsonDocketString.put("Engineer_id", AppManager.getSinleton().getUser().getId());

					jsonObjectForDocket.put(String.valueOf(i), jsonDocketString);

				}

			}

			jsonAllData.put("Docket", jsonObjectForDocket);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();
			return data;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}

	}

	private String syncJobDetailData(ArrayList<Job> jobArrayList) {
		String data = "";
		try {
			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonJobDetailString = new JSONObject();

			int arrayCount = 0;

			for (int i = 0; i < jobArrayList.size(); i++) {

				String jobId = jobArrayList.get(i).getId();
				String siteId = jobArrayList.get(i).getSiteid();
				String clientId = jobArrayList.get(i).getClient_id();

				// int count = db.getCountForDocket(jobId);
				// jobDetailArray.clear();
				jobDetailArray = db.getAllJobDetails(jobId);
				Log.e("TAG", "Size of Task Details:" + jobDetailArray.size());

				// if (count > 0) {

				for (int j = 0; j < jobDetailArray.size(); j++) {

					JSONObject jsonJobDetailIndividualString = new JSONObject();

					String machine_id = jobDetailArray.get(j).getMachineId();
					String error_code = jobDetailArray.get(j).getErrorCode();
					String problem = jobDetailArray.get(j).getProblem();
					String job_id = jobDetailArray.get(j).getJobId();
					String id = jobDetailArray.get(j).getId();

					jsonJobDetailIndividualString.put("id", id);
					jsonJobDetailIndividualString.put("machine_id", machine_id);
					jsonJobDetailIndividualString.put("error_code", error_code);
					jsonJobDetailIndividualString.put("problem", problem);
					jsonJobDetailIndividualString.put("jobId", job_id);
					jsonJobDetailIndividualString.put("SiteId", siteId);
					jsonJobDetailIndividualString.put("ClientId", clientId);
					jsonJobDetailIndividualString.put("Engineer_id", AppManager.getSinleton().getUser().getId());

					ArrayList<String> machineImageArray = new ArrayList<String>();

					machineImageArray = db.getMachineImageName(id);

					JSONObject jsonMachineImageTotalString = new JSONObject();

					for (int o = 0; o < machineImageArray.size(); o++) {

						JSONObject jsonMachineImageString = new JSONObject();

						jsonMachineImageString.put("JobDetailId", id);
						jsonMachineImageString.put("ImageName", machineImageArray.get(o));

						jsonMachineImageTotalString.put(String.valueOf(o), jsonMachineImageString);

					}

					jsonJobDetailIndividualString.put("Image", jsonMachineImageTotalString);

					jsonJobDetailString.put(String.valueOf(arrayCount), jsonJobDetailIndividualString);

					arrayCount++;

				}

				// }

			}

			jsonAllData.put("JobDetail", jsonJobDetailString);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}

	}

	private String syncDocketDetailData(ArrayList<Job> jobArrayList) {
		String data = "";
		try {
			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonDocketDetailString = new JSONObject();

			int docketDetailArray = 0;

			for (int i = 0; i < jobArrayList.size(); i++) {

				String jobId = jobArrayList.get(i).getId();
				String siteId = jobArrayList.get(i).getSiteid();
				String clientId = jobArrayList.get(i).getClient_id();

				// int count = db.getCountForDocket(jobId);

				jobDocketDetailArray = db.getDocketDetailByJobId(jobId);

				// JSONArray jsonDocketDetailArray = new JSONArray();
				// if (count > 0) {

				for (int k = 0; k < jobDocketDetailArray.size(); k++) {

					JSONObject jsonDocketDetailIndividualString = new JSONObject();

					String job_id = jobDocketDetailArray.get(k).getJob_id();
					String machine_id = jobDocketDetailArray.get(k).getMachine_id();
					String jobdetail_id = jobDocketDetailArray.get(k).getJobdetail_id();
					String work_carried_out = jobDocketDetailArray.get(k).getWork_carried_out();
					String parts_unable_to_find = jobDocketDetailArray.get(k).getParts_unable_to_find();
					String eol_status = jobDocketDetailArray.get(k).getEol();

					Log.e("TAG", "Job detail id in docket detail >>> " + jobdetail_id);

					jsonDocketDetailIndividualString.put("job_id", job_id);
					jsonDocketDetailIndividualString.put("machine_id", machine_id);
					jsonDocketDetailIndividualString.put("jobdetail_id", jobdetail_id);
					jsonDocketDetailIndividualString.put("work_carried_out", work_carried_out);
					jsonDocketDetailIndividualString.put("parts_unable_to_find", parts_unable_to_find);
					jsonDocketDetailIndividualString.put("eol_status", eol_status);
					jsonDocketDetailIndividualString.put("JobId", jobId);
					jsonDocketDetailIndividualString.put("SiteId", siteId);
					jsonDocketDetailIndividualString.put("ClientId", clientId);
					jsonDocketDetailIndividualString.put("Engineer_id", AppManager.getSinleton().getUser().getId());

					jsonDocketDetailString.put(String.valueOf(docketDetailArray), jsonDocketDetailIndividualString);
					docketDetailArray++;
				}

				// }

			}

			jsonAllData.put("DocketDetail", jsonDocketDetailString);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}

	}

	private String syncSparePartsData(ArrayList<Job> jobArrayList) {
		String data = "";
		try {

			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonSparePartsString = new JSONObject();

			for (int i = 0; i < jobArrayList.size(); i++) {

				String jobId = jobArrayList.get(i).getId();
				String siteId = jobArrayList.get(i).getSiteid();
				String clientId = jobArrayList.get(i).getClient_id();

				// int count = db.getCountForDocket(jobId);

				sparePartsArray = db.getSparePartsByJob(jobId);

				// JSONArray jsonSparePartsArray = new JSONArray();

				// if (count > 0) {
				for (int l = 0; l < sparePartsArray.size(); l++) {
					JSONObject jsonSparePartsIndividualString = new JSONObject();

					String spare_id = sparePartsArray.get(l).getSpareId();
					String job_id = sparePartsArray.get(l).getJobId();
					String description = sparePartsArray.get(l).getDescription();
					String quantity = sparePartsArray.get(l).getQuantity();
					String machine_id = sparePartsArray.get(l).getMachineId();

					jsonSparePartsIndividualString.put("spare_id", spare_id);
					jsonSparePartsIndividualString.put("job_id", job_id);
					jsonSparePartsIndividualString.put("description", description);
					jsonSparePartsIndividualString.put("quantity", quantity);
					jsonSparePartsIndividualString.put("machine_id", machine_id);
					jsonSparePartsIndividualString.put("JobId", jobId);
					jsonSparePartsIndividualString.put("SiteId", siteId);
					jsonSparePartsIndividualString.put("ClientId", clientId);
					jsonSparePartsIndividualString.put("Engineer_id", AppManager.getSinleton().getUser().getId());

					jsonSparePartsString.put(String.valueOf(l), jsonSparePartsIndividualString);
				}

				// }

			}

			jsonAllData.put("SpareParts", jsonSparePartsString);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}

	}

	private String syncTrainingData(ArrayList<Job> jobArrayList) {
		String data = "";
		try {

			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonTrainingString = new JSONObject();

			for (int i = 0; i < jobArrayList.size(); i++) {

				String siteId = jobArrayList.get(i).getSiteid();
				String clientId = jobArrayList.get(i).getClient_id();

				// int count = db.getCountForDocket(jobId);

				trainingJobArray = db.getTrainingDetails();

				// JSONArray jsonTrainingArray = new JSONArray();

				// if (count > 0) {

				for (int m = 0; m < trainingJobArray.size(); m++) {
					JSONObject jsonTrainingIndividualString = new JSONObject();

					String job_title = trainingJobArray.get(m).getJob_title();
					String assign_date = trainingJobArray.get(m).getAssign_date();
					String created_date = trainingJobArray.get(m).getCreated_date();
					String job_id = trainingJobArray.get(m).getJob_id();
					String training_id = trainingJobArray.get(m).getTraining_id();

					jsonTrainingIndividualString.put("training_id", training_id);
					jsonTrainingIndividualString.put("job_title", job_title);
					jsonTrainingIndividualString.put("assign_date", assign_date);
					jsonTrainingIndividualString.put("created_date", created_date);
					jsonTrainingIndividualString.put("SiteId", siteId);
					jsonTrainingIndividualString.put("JobId", job_id);
					jsonTrainingIndividualString.put("ClientId", clientId);
					jsonTrainingIndividualString.put("Engineer_id", AppManager.getSinleton().getUser().getId());

					jsonTrainingString.put(String.valueOf(m), jsonTrainingIndividualString);
				}

				// }

			}
			jsonAllData.put("Training", jsonTrainingString);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}
	}

	private String syncJobStatusData(ArrayList<Job> jobArrayList) {
		String data = "";
		try {

			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonStartJobString = new JSONObject();

			int startJobIndex = 0;

			for (int i = 0; i < jobArrayList.size(); i++) {

				String jobId = jobArrayList.get(i).getId();

				// int count = db.getCountForDocket(jobId);

				startJobArray = db.getAllStartData(jobId);
				// JSONArray jsonTrainingArray = new JSONArray();

				// if (count > 0) {

				for (int m = 0; m < startJobArray.size(); m++) {
					JSONObject jsonStartJobIndividualString = new JSONObject();

					String distance = startJobArray.get(m).getDistance();
					String unit = startJobArray.get(m).getDistance_unit();
					String site_id = startJobArray.get(m).getSite_id();
					String job_id = jobId;
					String engineer_id = startJobArray.get(m).getEngineer_id();
					String start_time = startJobArray.get(m).getStart_time();
					String start_address = "Unit 14a Stadium Business Park,Ballycoolin Road,Dublin 11";
					String finish_time = startJobArray.get(m).getEnd_time();
					String status = "Completed by Eng";

					jsonStartJobIndividualString.put("distance", distance);
					jsonStartJobIndividualString.put("unit", unit);
					jsonStartJobIndividualString.put("site_id", site_id);
					jsonStartJobIndividualString.put("job_id", job_id);
					jsonStartJobIndividualString.put("engineer_id", engineer_id);
					jsonStartJobIndividualString.put("start_time", start_time);
					jsonStartJobIndividualString.put("start_address", start_address);
					jsonStartJobIndividualString.put("finish_time", finish_time);
					jsonStartJobIndividualString.put("status", status);

					jsonStartJobString.put(String.valueOf(startJobIndex), jsonStartJobIndividualString);
					startJobIndex++;
				}

				// }

			}
			jsonAllData.put("StartJob", jsonStartJobString);

			Log.e("TAG", "Total JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}
	}

	private String syncParkJobData(ArrayList<Job> jobarrayList) {

		String data = "";

		try {

			JSONObject jsonAllData = new JSONObject();

			JSONObject jsonObjectForDocket = new JSONObject();

			for (int i = 0; i < jobarrayList.size(); i++) {

				String jobId = jobarrayList.get(i).getId();
				String siteId = jobarrayList.get(i).getSiteid();
				String clientId = jobarrayList.get(i).getClient_id();

				String parkJobComment = db.getCountForParkJob(jobId);

				String[] stringSplit = parkJobComment.split(",");

				JSONObject jsonParkJobComment = new JSONObject();

				jsonParkJobComment.put("Comment", stringSplit[0]);
				jsonParkJobComment.put("added_date", stringSplit[1]);
				jsonParkJobComment.put("JobId", jobId);
				jsonParkJobComment.put("SiteId", siteId);
				jsonParkJobComment.put("ClientId", clientId);
				jsonParkJobComment.put("Engineer_id", AppManager.getSinleton().getUser().getId());

				jsonObjectForDocket.put(String.valueOf(i), jsonParkJobComment);

			}

			jsonAllData.put("ParkJob", jsonObjectForDocket);

			Log.e("TAG", "Total Park JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}

	}

	@Override
	public void getSyncSuccess(String successString) {
		// TODO Auto-generated method stub
		Log.e("TAG", "sync success >> " + successString);

		if (successString != null) {
			try {

				JSONObject successJson = new JSONObject(successString);

				int status = successJson.getInt("status");

				if (status > 0) {

				} else {

				}

				String parkJobData = syncParkJobData(parkjobArraylist);

				String jobDetailData = syncJobDetailData(parkjobArraylist);

				String docketDetailData = syncDocketDetailData(parkjobArraylist);

				String SparePartsData = syncSparePartsData(parkjobArraylist);
				String TrainingData = syncTrainingData(parkjobArraylist);

				String jobStatusData = syncJobStatusData(parkjobArraylist);

				new ParkJobSyncServices(getActivity(), parkJobData, jobDetailData, docketDetailData, SparePartsData,
						TrainingData, jobStatusData, SyncClass.this).execute();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	private String syncMachineData(ArrayList<MachineSiteMain> machineArrayList) {
		String data = "";
		try {

			JSONObject jsonAllData = new JSONObject();
			JSONObject jsonAllMachineString = new JSONObject();

			int startJobIndex = 0;

			// JSONObject jsonMachineString;

			for (int i = 0; i < machineArrayList.size(); i++) {

				JSONObject jsonMachineString = new JSONObject();

				/*
				 * String distance = startJobArray.get(m).getDistance(); String
				 * unit = startJobArray.get(m).getDistance_unit(); String
				 * site_id = startJobArray.get(m).getSite_id(); String job_id =
				 * jobId; String engineer_id =
				 * startJobArray.get(m).getEngineer_id(); String start_time =
				 * startJobArray.get(m).getStart_time(); String start_address =
				 * "Unit 14a Stadium Business Park,Ballycoolin Road,Dublin 11";
				 * String finish_time = startJobArray.get(m).getEnd_time();
				 * String status = "Completed by Eng";
				 */

				jsonMachineString.put("manufacturer", machineArrayList.get(i).getMachineMAnufacturer());
				jsonMachineString.put("manufacturer_id", machineArrayList.get(i).getMachineMAnufacturer_id());
				jsonMachineString.put("machine_type", machineArrayList.get(i).getMachineType());
				jsonMachineString.put("type_id", machineArrayList.get(i).getMachineType_id());
				jsonMachineString.put("machine", machineArrayList.get(i).getMachineName());
				jsonMachineString.put("machine_id", machineArrayList.get(i).getMachineName_id());
				jsonMachineString.put("machine_model", machineArrayList.get(i).getMachineModel());
				jsonMachineString.put("model_id", machineArrayList.get(i).getMachineModel_id());
				jsonMachineString.put("machine_voltage", machineArrayList.get(i).getMachineVoltage());
				jsonMachineString.put("machine_suction", machineArrayList.get(i).getMachineSuction());
				jsonMachineString.put("machine_traction", machineArrayList.get(i).getMachineTraction());
				jsonMachineString.put("machine_water", machineArrayList.get(i).getMachineWater());
				jsonMachineString.put("machine_work_order", machineArrayList.get(i).getMachineWorkOrder());
				jsonMachineString.put("machine_spare_parts", machineArrayList.get(i).getMachineSpareParts());
				jsonMachineString.put("machine_marks_avail", machineArrayList.get(i).getMachineMarksAvail());
				jsonMachineString.put("Engineer_id", AppManager.getSinleton().getUser().getId());
				jsonMachineString.put("machine_visual_inspection",
						machineArrayList.get(i).getMachineVisualInspection());
				jsonMachineString.put("machine_year", machineArrayList.get(i).getMachineManuYear());
				if (machineArrayList.get(i).getMachineWarranty() == null
						|| machineArrayList.get(i).getMachineWarranty().equalsIgnoreCase(""))
					jsonMachineString.put("machine_warranty", "");
				else
					jsonMachineString.put("machine_warranty", machineArrayList.get(i).getMachineWarranty());
				jsonMachineString.put("machine_si_no", machineArrayList.get(i).getMachineSINo());
				if (machineArrayList.get(i).getPurchase_date() == null)
					jsonMachineString.put("purchase_date", "");
				else
					jsonMachineString.put("purchase_date", machineArrayList.get(i).getPurchase_date());
				jsonMachineString.put("site_id", machineArrayList.get(i).getSite_id());

				jsonAllMachineString.put(String.valueOf(startJobIndex), jsonMachineString);
				startJobIndex++;

				// }

			}
			jsonAllData.put("MachineData", jsonAllMachineString);

			Log.e("TAG", "Total Machine JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}
	}

	private String syncTrainingDataIndividual(ArrayList<TrainingSyncPojo> trainingSyncPojoList) {
		String data = "";
		try {

			JSONObject jsonAllData = new JSONObject();
			JSONObject jsonAllTrainingString = new JSONObject();

			int startJobIndex = 0;

			// JSONObject jsonMachineString;

			for (int i = 0; i < trainingSyncPojoList.size(); i++) {

				JSONObject jsonTraineeString = new JSONObject();

				jsonTraineeString.put("training_id", trainingSyncPojoList.get(i).getTraining_id());
				jsonTraineeString.put("trainee_names", trainingSyncPojoList.get(i).getTrainee_names());
				jsonTraineeString.put("date_time", trainingSyncPojoList.get(i).getTrainee_date());
				jsonTraineeString.put("engineer_id", AppManager.getSinleton().getUser().getId());

				jsonAllTrainingString.put(String.valueOf(startJobIndex), jsonTraineeString);
				startJobIndex++;

				// }

			}
			jsonAllData.put("TraineeData", jsonAllTrainingString);

			Log.e("TAG", "Total TraineeIndividual JSON data >> " + jsonAllData.toString());
			data = jsonAllData.toString();

			return data;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return data;
		}
	}

	@Override
	public void getParkSyncSuccess(String successString) {
		// TODO Auto-generated method stub
		Log.e("TAG", "sync success >> " + successString);
	}

	@Override
	public void getMachineSyncSuccess(String successString) {
		// TODO Auto-generated method stub
		Log.e("TAG", "sync success for Machine >> " + successString);
	}

	@Override
	public void getTrainingSyncSuccess(String successString) {
		// TODO Auto-generated method stub
		Log.e("TAG", "sync success for Trainee >> " + successString);
	}

    @Override
    public void getAllMachineMasterListResponse(String response) {
        if (response != null) {
            try {
                // Log.e("TAG", "response >> " + response);

                JSONArray jArr = new JSONArray(response);
                db.deletemachineMasterAllJobRecords();
                db.deletemachineManufacturerMasterrAllJobRecords();
                db.deletemachineModelMasterAllJobRecords();
                db.deletemachineTypeMasterAllJobRecords();
                //Added by Sukesh

                new AddMachine(getActivity(),SyncClass.this).execute(jArr);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getAllErrorCode(String response) {
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

    @Override
    public void getMasterSparePartsResponse(String responseMessage) throws JSONException {
        try {
            if (responseMessage != null) {
                db.truncateSpareParts();
                JSONArray response = new JSONArray(responseMessage);
                JSONArray jsonArray = response;
                Log.e("Message",jsonArray.toString());
                new UpdateSparePartsToDatabase().execute(jsonArray);
            }
        }catch(NullPointerException e){
            Log.e("Exception SpareParts",e.toString());
        }
    }



    String sqlStatement="";
    int k=0;

    @Override
    public void getDismissCommand() {
        progressDialog.dismiss();
    }

    public class UpdateSparePartsToDatabase extends AsyncTask<JSONArray,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog.setMessage("Please Wait it will take few minutes to sync spare parts ...");
//            progressDialog.show();
            db.truncateSpareParts();
        }

        @Override
        protected String doInBackground(JSONArray... jsonArrays) {
            try {
                final JSONArray jsonArray = jsonArrays[0];
                int j = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    k = i;
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
                Log.e("Exception cought",e.toString());
                return e.toString();
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // progressDialog.dismiss();
            k=0;
            //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            new MachineMasterServices("0", "-1", SyncClass.this,getActivity()).execute();
        }
    }
}

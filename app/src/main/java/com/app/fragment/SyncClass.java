package com.app.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import com.app.asyncs.DocketSyncServices;
import com.app.asyncs.MachineSyncServices;
import com.app.asyncs.ParkJobSyncServices;
import com.app.asyncs.TrainingSyncServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.DocketSyncListner;
import com.app.listners.MachineSyncListner;
import com.app.listners.ParkJobSyncListner;
import com.app.listners.TrainingSyncListner;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.DocketDetail;
import com.app.model.DocketList;
import com.app.model.Job;
import com.app.model.JobDetails;
import com.app.model.MachineSiteMain;
import com.app.model.SparePartsModel;
import com.app.model.StartJobModel;
import com.app.model.TrainingAddedToJob;
import com.app.model.TrainingSyncPojo;
import com.app.samgo.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class SyncClass extends Fragment
		implements DocketSyncListner, ParkJobSyncListner, MachineSyncListner, TrainingSyncListner {

	private SamgoSQLOpenHelper db;

	private ArrayList<Job> jobArraylist = new ArrayList<Job>();

	private ArrayList<Job> parkjobArraylist = new ArrayList<Job>();

	private ArrayList<JobDetails> jobDetailArray = new ArrayList<JobDetails>();

	private ArrayList<DocketDetail> jobDocketDetailArray = new ArrayList<DocketDetail>();

	private ArrayList<SparePartsModel> sparePartsArray = new ArrayList<SparePartsModel>();

	private ArrayList<TrainingAddedToJob> trainingJobArray = new ArrayList<TrainingAddedToJob>();

	private ArrayList<StartJobModel> startJobArray = new ArrayList<StartJobModel>();

	ArrayList<MachineSiteMain> machineSiteMainList = new ArrayList<MachineSiteMain>();

	ArrayList<TrainingSyncPojo> trainingSyncPojoList = new ArrayList<TrainingSyncPojo>();

	private Button sync;
	
	private LinearLayout parentLayout;

	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.sync_activity, container, false);

		db = new SamgoSQLOpenHelper(getContext());

		sync = (Button) v.findViewById(R.id.sync);

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

		return v;
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

}

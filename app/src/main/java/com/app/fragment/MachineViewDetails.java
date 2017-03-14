package com.app.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.asyncs.MachineHistoryServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.MachineHistoryListener;
import com.app.model.Config;
import com.app.model.MachineHistory;
import com.app.model.MachineServiceHistory;
import com.app.model.MachineView;
import com.app.samgo.R;
import com.app.utility.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MachineViewDetails extends Fragment implements MachineHistoryListener {

	View v;

	int loader = R.drawable.no_image;
	String picturePath;

	private LinearLayout mLinearListMachineView, mLinearListServiceView;

	private ArrayList<MachineHistory> mArrayListMachineHistory = new ArrayList<MachineHistory>();

	private ArrayList<MachineServiceHistory> mArrayListServiceHistory = new ArrayList<MachineServiceHistory>();

	private TextView machine_name, serial_no, qr_code, associated_sites, manufacturer, machine_type, machineModel,
			description, nodataServiceHistory, nodataMachineHistory;

	private ImageView machine_image;

	private SamgoSQLOpenHelper db;

	private String machine_id = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.machine_details_view, container, false);

		this.initialisation();

		mArrayListMachineHistory.clear();
		mArrayListServiceHistory.clear();

		db = new SamgoSQLOpenHelper(getContext());

		Log.e("TAG", "Machine view SI NO>> " + Config.machineSlNo);

		MachineView machineView = db.getMachineViewData(Config.machineSlNo);

		new MachineHistoryServices(MachineViewDetails.this, machineView.getMachine_id(), getActivity()).execute();

		machine_name.setText(machineView.getMachine_master_name());
		serial_no.setText(machineView.getMachine_sl_no());
		qr_code.setText(machineView.getMachine_qr_code());
		associated_sites.setText(machineView.getSite_name());
		manufacturer.setText(machineView.getMachine_manufacturer());
		machine_type.setText(machineView.getMachine_type());
		machineModel.setText(machineView.getMachine_model());
		description.setText(machineView.getMachine_desc());

		machine_id = machineView.getMachine_id();

		ImageLoader imgLoader = new ImageLoader(getActivity());

		if (machineView.getMachine_photo() != null && !machineView.getMachine_photo().equalsIgnoreCase("")) {
			String[] separated = machineView.getMachine_photo().split("\\;;");

			Log.e("TAG", "Value of Image Path:::" + machineView.getMachine_photo());
			imgLoader.DisplayImage(separated[0].toString(), loader, machine_image);
		}

		return v;
	}

	private void initialisation() {
		// TODO Auto-generated method stub
		mLinearListMachineView = (LinearLayout) v.findViewById(R.id.machine_history_detail);
		mLinearListServiceView = (LinearLayout) v.findViewById(R.id.service_history_detail);

		machine_name = (TextView) v.findViewById(R.id.machine_name);
		serial_no = (TextView) v.findViewById(R.id.serial_no_text);
		qr_code = (TextView) v.findViewById(R.id.qr_code_text);
		associated_sites = (TextView) v.findViewById(R.id.associated_sites_text);
		manufacturer = (TextView) v.findViewById(R.id.manufacturer_text);
		machine_type = (TextView) v.findViewById(R.id.machine_type_text);
		machineModel = (TextView) v.findViewById(R.id.model_text);
		description = (TextView) v.findViewById(R.id.description_text);
		machine_image = (ImageView) v.findViewById(R.id.machine_image);
		nodataServiceHistory = (TextView) v.findViewById(R.id.nodataServiceHistory);
		nodataMachineHistory = (TextView) v.findViewById(R.id.nodataMachineHistory);
	}

	@Override
	public void getAllMachineHistoryResponse(String response) {
		// TODO Auto-generated method stub
		try {

			Log.e("TAG", "History Response>>>" + response);
			JSONObject jObj = new JSONObject(response);
			String jObjStringSer = jObj.getString("service_history_list");
			String jObjStringMc = jObj.getString("site_machine_list");

			JSONArray serviceArr = new JSONArray(jObjStringSer);
			for (int i = 0; i < serviceArr.length(); i++) {

				MachineServiceHistory machineServiceHistory = new MachineServiceHistory();

				JSONObject serviceArrayIndividual = new JSONObject(serviceArr.getString(i));

				JSONObject docketDetail = new JSONObject(serviceArrayIndividual.getString("DocketDetail"));
				/*
				 * machineHistoryModal.setAssignedOn(docketDetail.getString(
				 * "caried_out_date"));
				 * machineHistoryModal.setMachine_id(docketDetail.getString(
				 * "machine_id"));
				 */
				machineServiceHistory.setWorkCarriedOut(docketDetail.getString("work_carried_out"));

				JSONObject JobDetail = new JSONObject(serviceArrayIndividual.getString("JobDetail"));
				machineServiceHistory.setErrorCode(JobDetail.getString("error_code"));
				machineServiceHistory.setEngineerComments(JobDetail.getString("comments"));
				machineServiceHistory.setDateTime(docketDetail.getString("caried_out_date"));
				machineServiceHistory.setProblemcomments(JobDetail.getString("problem"));
				machineServiceHistory.setMachine_id(JobDetail.getString("machine_id"));

				JSONObject engineer = new JSONObject(serviceArrayIndividual.getString("Engineer"));
				machineServiceHistory
						.setEngineerName(engineer.getString("first_name") + " " + engineer.getString("last_name"));
				int count = db.getCountMachineServiceHistory(JobDetail.getString("machine_id"));
				if (count == 0) {
					db.addMachineServiceHistory(machineServiceHistory);
				}

			}
			JSONArray machineArr = new JSONArray(jObjStringMc);
			db.deleteAllRecordsForMachineHistory();
			for (int j = 0; j < machineArr.length(); j++) {

				MachineHistory machineHistoryModal = new MachineHistory();
				JSONObject machineArrayIndividual = new JSONObject(machineArr.getString(j));
				JSONObject Site = new JSONObject(machineArrayIndividual.getString("Site"));
				machineHistoryModal.setSiteName(Site.getString("name"));

				JSONArray machineArrHist = new JSONArray(jObjStringMc);

				JSONObject machineArrayIndividualHist = new JSONObject(machineArrHist.getString(j));
				JSONObject SiteHist = new JSONObject(machineArrayIndividualHist.getString("SiteMachine"));
				machineHistoryModal.setAssignedOn(SiteHist.getString("assign_date"));
				machineHistoryModal.setMachine_id(SiteHist.getString("machine_id"));

				db.addMachineHistoryData(machineHistoryModal);

				Log.e("TAG", "Value for Site:::" + Site.getString("name"));
				// db.addMachineHistoryData(machineHistoryModal);
			}
			/**
			 * Add item into arraylist
			 */

			Log.e("TAG", "machine_id >> " + machine_id);

			mArrayListMachineHistory = db.getMachineHistoryDataById(machine_id);

			Log.d("TAG", "machine history array size >> " + mArrayListMachineHistory.size());

			mArrayListServiceHistory = db.getMachineServiceHistoryById(machine_id);

			Log.d("TAG", "Service history array size >> " + mArrayListServiceHistory.size());

			/***
			 * adding item into listview
			 */
			if (mArrayListMachineHistory.size() > 0) {
				for (

				int j = 0; j < mArrayListMachineHistory.size(); j++)

				{
					nodataMachineHistory.setVisibility(View.GONE);
					mLinearListMachineView.setVisibility(View.VISIBLE);

					/**
					 * inflate items/ add items in linear layout instead of
					 * listview
					 */

					Log.e("TAG", "size of i >> " + j);

					LayoutInflater inflater1 = null;
					inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View mLinearView = inflater1.inflate(R.layout.machine_history_one_row_item, null);

					/**
					 * getting id of row.xml
					 */
					TextView siteName = (TextView) mLinearView.findViewById(R.id.site_name_text);
					TextView assignedOn = (TextView) mLinearView.findViewById(R.id.assigned_on);

					/**
					 * set item into row
					 */
					String siteNameText = mArrayListMachineHistory.get(j).getSiteName();
					String assignedOnText = mArrayListMachineHistory.get(j).getAssignedOn();

					siteName.setText(siteNameText);
					assignedOn.setText(assignedOnText);

					/**
					 * add view in top linear
					 */

					mLinearListMachineView.addView(mLinearView);

				}
			} else {
				mLinearListMachineView.setVisibility(View.GONE);
				nodataMachineHistory.setVisibility(View.VISIBLE);
				nodataMachineHistory.setText("No machine history available.");
			}

			if (mArrayListServiceHistory.size() > 0) {

				for (int k = 0; k < mArrayListServiceHistory.size(); k++)

				{

					mLinearListServiceView.setVisibility(View.VISIBLE);
					nodataServiceHistory.setVisibility(View.GONE);

					/**
					 * inflate items/ add items in linear layout instead of
					 * listview
					 */

					Log.e("TAG", "size of k >> " + k);

					LayoutInflater inflater2 = null;
					inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View mLinearView1 = inflater2.inflate(R.layout.service_history_one_item_row, null);

					/**
					 * getting id of row.xml
					 */
					TextView noOfVisit = (TextView) mLinearView1.findViewById(R.id.no_of_visit);
					TextView errorCode = (TextView) mLinearView1.findViewById(R.id.error_code_text);
					TextView problemComments = (TextView) mLinearView1.findViewById(R.id.comments_text);
					TextView engineerSolution = (TextView) mLinearView1.findViewById(R.id.problem_solution);
					TextView engineerDateTime = (TextView) mLinearView1.findViewById(R.id.engineer_date_time_text);
					TextView engineerComments = (TextView) mLinearView1.findViewById(R.id.engineer_comments_text);
					TextView workCarriedOut = (TextView) mLinearView1.findViewById(R.id.work_carried_out_text);
					LinearLayout engineerWrapper = (LinearLayout) mLinearView1
							.findViewById(R.id.engineer_solution_notes_wrapper);

					/**
					 * set item into row
					 */

					String errorCodeText = mArrayListServiceHistory.get(k).getErrorCode();
					String problemCommentsText = mArrayListServiceHistory.get(k).getProblemcomments();
					String engineerName = mArrayListServiceHistory.get(k).getEngineerName();
					String engineerDateTimeText = mArrayListServiceHistory.get(k).getDateTime();
					String engineerCommentsText = mArrayListServiceHistory.get(k).getEngineerComments();
					String workCarriedOutText = mArrayListServiceHistory.get(k).getWorkCarriedOut();

					noOfVisit.setText("Visit " + (k + 1));
					errorCode.setText(errorCodeText);
					problemComments.setText(problemCommentsText);

					Log.e("TAG", "engineer name >> " + engineerName);

					if (engineerName == "") {
						engineerSolution.setVisibility(View.GONE);
						engineerWrapper.setVisibility(View.GONE);
					} else {
						engineerDateTime.setText(engineerName + " " + engineerDateTimeText);
						if (engineerCommentsText.equalsIgnoreCase("null")) {
							engineerComments.setText("");
						} else {
							engineerComments.setText(engineerCommentsText);
						}

						workCarriedOut.setText(workCarriedOutText);
					}

					/**
					 * add view in top linear
					 */

					mLinearListServiceView.addView(mLinearView1);

				}
			} else {
				mLinearListServiceView.setVisibility(View.GONE);
				nodataServiceHistory.setVisibility(View.VISIBLE);
				nodataServiceHistory.setText("No service history available.");
			}
			Log.e("TAG", "Data Inserted Successfully....");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

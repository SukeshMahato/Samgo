package com.app.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.adapter.MachineListsAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.MachineListListener;
import com.app.model.Config;
import com.app.model.Machine;
import com.app.model.MachineHistory;
import com.app.model.MachineServiceHistory;
import com.app.model.MachineView;
import com.app.samgo.MachineDetails;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentMachines extends Fragment implements MachineListListener {

	View v;

	private ListView machineLists;

	private MachineListsAdapter machineListsAdapter;

	private ArrayList<Machine> machineDataList = new ArrayList<Machine>();
	private ArrayList<Machine> machineDataList2 = new ArrayList<Machine>();

	boolean isLoading = false;

	int next;

	protected TextView searchBy;

	private EditText searchHere;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private SamgoSQLOpenHelper db;

	private RelativeLayout parentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.fragment_machine_lists, container, false);

		this.initialisation();

		this.listner();

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		db = new SamgoSQLOpenHelper(getContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

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

		/*
		 * if (isInternetPresent) { new MachineListServices(this, "100",
		 * getActivity()).execute(); } else {
		 */
		machineDataList.clear();
		machineDataList2.clear();

		machineDataList = db.getMachineLIstDetails();
		machineDataList2 = db.getMachineLIstDetails();

		Log.e("TAG", "size >> " + machineDataList.size());

		machineListsAdapter = new MachineListsAdapter(FragmentMachines.this, getActivity(), machineDataList);
		machineLists.setAdapter(machineListsAdapter);
		machineListsAdapter.notifyDataSetChanged();
		// }

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void listner() {
		machineLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * searchBy.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub
		 * 
		 * Log.e("TAG", "clicked on");
		 * 
		 * final Dialog dialog = new Dialog(getContext(),
		 * R.style.TopToBottomDialog);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.custom_search_dialog_for_machine);
		 * 
		 * TextView serial_no = (TextView) dialog.findViewById(R.id.serial_no);
		 * TextView machine_name = (TextView)
		 * dialog.findViewById(R.id.machine_name); TextView machine_type =
		 * (TextView) dialog.findViewById(R.id.machine_type); TextView
		 * manufacturer = (TextView) dialog.findViewById(R.id.manufacturer);
		 * 
		 * serial_no.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Serial No"); dialog.dismiss(); } });
		 * 
		 * machine_name.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Machine Name"); dialog.dismiss(); } });
		 * 
		 * machine_type.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Machine Type"); dialog.dismiss(); } });
		 * 
		 * manufacturer.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Manufacturer"); dialog.dismiss(); } });
		 * 
		 * dialog.show(); } });
		 */

		searchHere.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String searchString = searchHere.getText().toString().toLowerCase();
				int textLength = searchString.length();

				// if (searchBy.getText().toString().contains("Search By")) {

				// } else {
				machineDataList.clear();

				for (int i = 0; i < machineDataList2.size(); i++) {

					Machine machineItem = new Machine();

					String machineSlNo = machineDataList2.get(i).getMachine_si_no();
					String machineType = machineDataList2.get(i).getMachine_type();
					String machineName = machineDataList2.get(i).getMachine_name();
					String machineMarks = machineDataList2.get(i).getMarks();
					String manufactureName = machineDataList2.get(i).getManufacturer();
					String siteName = machineDataList2.get(i).getSite_name();

					// if (searchBy.getText().toString().contains("Serial No"))
					// {

					if (textLength <= machineSlNo.length()) {

						if (machineSlNo.toLowerCase().contains(searchString)
								|| machineName.toLowerCase().contains(searchString)
								|| machineType.toLowerCase().contains(searchString)
								|| manufactureName.toLowerCase().contains(searchString)
								|| machineMarks.toLowerCase().contains(searchString)
								|| siteName.toLowerCase().contains(searchString)) {

							machineItem.setMachine_si_no(machineSlNo);
							machineItem.setMachine_name(machineName);
							machineItem.setMachine_type(machineType);
							machineItem.setManufacturer(manufactureName);
							machineItem.setMarks(machineMarks);
							machineItem.setSite_name(siteName);

							machineDataList.add(machineItem);
							// }

							// }

						} /*
							 * else if (searchBy.getText().toString().contains(
							 * "Machine Name")) {
							 * 
							 * if (textLength <= machineName.length()) {
							 * 
							 * if
							 * (machineName.toLowerCase().contains(searchString)
							 * ) {
							 * 
							 * machineItem.setMachine_si_no(machineSlNo);
							 * machineItem.setMachine_name(machineName);
							 * machineItem.setMachine_type(machineType);
							 * machineItem.setManufacturer(manufactureName);
							 * machineItem.setMarks(machineMarks);
							 * machineItem.setSite_name(siteName);
							 * 
							 * machineDataList.add(machineItem); }
							 * 
							 * }
							 * 
							 * } else if
							 * (searchBy.getText().toString().contains(
							 * "Machine Type")) {
							 * 
							 * if (textLength <= machineType.length()) {
							 * 
							 * if
							 * (machineType.toLowerCase().contains(searchString)
							 * ) {
							 * 
							 * machineItem.setMachine_si_no(machineSlNo);
							 * machineItem.setMachine_name(machineName);
							 * machineItem.setMachine_type(machineType);
							 * machineItem.setManufacturer(manufactureName);
							 * machineItem.setMarks(machineMarks);
							 * machineItem.setSite_name(siteName);
							 * 
							 * machineDataList.add(machineItem); }
							 * 
							 * }
							 * 
							 * } else if
							 * (searchBy.getText().toString().contains(
							 * "Manufacturer")) {
							 * 
							 * if (textLength <= manufactureName.length()) {
							 * 
							 * if (manufactureName.toLowerCase().contains(
							 * searchString)) {
							 * 
							 * machineItem.setMachine_si_no(machineSlNo);
							 * machineItem.setMachine_name(machineName);
							 * machineItem.setMachine_type(machineType);
							 * machineItem.setManufacturer(manufactureName);
							 * machineItem.setMarks(machineMarks);
							 * machineItem.setSite_name(siteName);
							 * 
							 * machineDataList.add(machineItem); }
							 * 
							 * } }
							 */

					}
				}

				machineListsAdapter = new MachineListsAdapter(FragmentMachines.this, getActivity(), machineDataList);
				machineLists.setAdapter(machineListsAdapter);
				machineListsAdapter.notifyDataSetChanged();
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
		machineLists = (ListView) v.findViewById(R.id.machine_list);
		// searchBy = (TextView) v.findViewById(R.id.search_by);
		searchHere = (EditText) v.findViewById(R.id.search_here);
	}

	public void loadMore() {

		int i;

		if (machineDataList.size() <= 1000) { // Limit the number of items to
												// 100 (stop
			// loading when reaching 100 items)

			for (i = next; i <= next + 9; i++) {
				Machine machineItem = new Machine();

				machineItem.setMachine_si_no("1");
				machineItem.setMachine_name("Lenovo Vibe");
				machineItem.setMachine_type("Mobile");
				machineItem.setManufacturer("Lenovo");
				machineItem.setMarks("10");
				machineItem.setSite_name("lenovo.com");
				// machineItem.setAction("In Boom");

				machineDataList.add(machineItem);
			}

			// Notify the ListView of data changed

			machineListsAdapter.notifyDataSetChanged();

			isLoading = false;

			// Update next

			next = i;

		}

	}

	@Override
	public void getAllMachineListResponse(String response) {
		// TODO Auto-generated method stub
		if (response != null) {
			try {

				Log.e("TAG", "response Machine Details >> " + response);

				JSONArray jArr = new JSONArray(response);

				machineDataList.clear();
				machineDataList2.clear();

				// db.truncateTableForMachineLIst();

				// db.deleteAllRecordsForMachineView();
				db.deleteAllRecordsForMachineHistory();
				db.deleteAllRecordsForServiceHistory();

				for (int i = 0; i < jArr.length(); i++) {

					Machine machineItem = new Machine();

					MachineView machineView = new MachineView();

					String machineObj = jArr.getString(i);

					JSONObject machinObj = new JSONObject(machineObj);

					String machineData = machinObj.getString("Machine");

					JSONObject machineObj2 = new JSONObject(machineData);

					String machineSlNo = machineObj2.getString("machine_serial_no");
					String machineType = machineObj2.getString("machine_type");
					String machineName = machineObj2.getString("machine_name");
					String id = machineObj2.getString("id");
					String qrCode = machineObj2.getString("machine_qr_code");
					String machine_manufacturer = machineObj2.getString("machine_manufacturer");
					String machine_type = machineObj2.getString("machine_type");

					String machineModelData = machinObj.getString("MachineModel");
					JSONObject machineModelDataObj = new JSONObject(machineModelData);

					String machine_model = machineModelDataObj.getString("name");

					/**
					 * Calculate the total marks
					 */

					double[] staticMarksArray = { 50, 43.75, 37.5, 31.25, 25, 18.75, 12.50, 6.25, 0 };

					String voltage = machineObj2.getString("voltage").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("voltage");
					String suction = machineObj2.getString("suction").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("suction");
					String traction = machineObj2.getString("traction").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("traction");
					String water = machineObj2.getString("water").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("water");
					String visual_inspection = machineObj2.getString("visual_inspection").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("visual_inspection");
					String good_working_order = machineObj2.getString("good_working_order").equalsIgnoreCase("null")
							? "0" : machineObj2.getString("good_working_order");
					String spare_parts_available = machineObj2.getString("spare_parts_available")
							.equalsIgnoreCase("null") ? "0" : machineObj2.getString("spare_parts_available");
					String machineMarks = machineObj2.getString("marks_avail").equalsIgnoreCase("null") ? "0"
							: machineObj2.getString("marks_avail");

					Log.e("TAG",
							"Marks >> " + voltage + " >>" + suction + " >>" + traction + " >>" + water + " >>"
									+ visual_inspection + " >>" + good_working_order + " >>" + spare_parts_available
									+ " >>" + machineMarks + " >> gh" + voltage + " >>");

					int machine_total = Integer.parseInt(voltage) + Integer.parseInt(suction)
							+ Integer.parseInt(traction) + Integer.parseInt(water) + Integer.parseInt(visual_inspection)
							+ Integer.parseInt(good_working_order) + Integer.parseInt(spare_parts_available)
							+ Integer.parseInt(machineMarks);

					String machine_exp_date = machineObj2.getString("machine_exp_date");

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

					String manufactureData = machinObj.getString("MachineMaster");

					JSONObject manufactureObj = new JSONObject(manufactureData);

					String manufactureName = manufactureObj.getString("name");
					String description = manufactureObj.getString("description");

					String parts_drawling = manufactureObj.getString("parts_drawling");
					String user_manual = manufactureObj.getString("user_manual");
					String data_sheet = manufactureObj.getString("data_sheet");
					String youtube_link_1 = manufactureObj.getString("youtube_link_1");
					String youtube_link_2 = manufactureObj.getString("youtube_link_2");

					String siteData = machinObj.getString("Site");

					JSONObject siteObj = new JSONObject(siteData);

					String siteName = siteObj.getString("name");

					machineItem.setMachine_id(id);
					machineItem.setMachine_si_no(machineSlNo);
					machineItem.setMachine_name(machineName);
					machineItem.setMachine_type(machineType);
					machineItem.setManufacturer(manufactureName);
					machineItem.setMarks(String.valueOf(total_marks));
					machineItem.setSite_name(siteName);

					int countMachineList = db.getMachineListCountByMachineId(id);
					if (countMachineList > 0) {

					} else {
						db.addMachine(machineItem);
					}

					/**
					 * Add Machine view data to modal and insert into database
					 */

					machineView.setMachine_id(id);
					machineView.setMachine_master_name(manufactureName);
					machineView.setMachine_sl_no(machineSlNo);
					machineView.setMachine_qr_code(qrCode);
					machineView.setSite_name(siteName);
					machineView.setMachine_manufacturer(machine_manufacturer);
					machineView.setMachine_type(machine_type);
					machineView.setMachine_model(machine_model);
					machineView.setMachine_desc(description);
					machineView.setMachine_marks(String.valueOf(total_marks));
					machineView.setYoutube_link1(youtube_link_1);
					machineView.setYoutube_link2(youtube_link_2);
					machineView.setParts_drawling(parts_drawling);
					machineView.setUser_manual(user_manual);
					machineView.setData_sheet(data_sheet);

					int countMachineView = db.getMachineViewCountByMachineId(id);

					if (countMachineView > 0) {

					} else {
						db.addMachineViewData(machineView);
					}

					/**
					 * Add Machine History to its modal and insert into database
					 */

					String machineHistory = machinObj.getString("SiteMachine");

					JSONArray machineHistoryArr = new JSONArray(machineHistory);

					if (machineHistoryArr.length() > 0) {
						for (int j = 0; j < machineHistoryArr.length(); j++) {

							String machineHistoryRowWise = machineHistoryArr.getString(j);

							JSONObject machineHistoryObj = new JSONObject(machineHistoryRowWise);

							String machine_id = machineHistoryObj.getString("machine_id");
							String site_name = machineHistoryObj.getString("site_name");
							String assigned_on = machineHistoryObj.getString("assign_date");

							MachineHistory machineHistoryModal = new MachineHistory();

							machineHistoryModal.setMachine_id(machine_id);
							machineHistoryModal.setSiteName(site_name);
							machineHistoryModal.setAssignedOn(assigned_on);

							db.addMachineHistoryData(machineHistoryModal);
						}
					}

					/**
					 * Add Service History to its modal and insert into database
					 */

					String jobDetail = machinObj.getString("JobDetail");

					JSONArray jobDetailArr = new JSONArray(jobDetail);

					String docketDetail = machinObj.getString("DocketDetail");

					JSONArray docketDetailArr = new JSONArray(docketDetail);

					if (jobDetailArr.length() > 0) {
						for (int k = 0; k < jobDetailArr.length(); k++) {
							String jobDetailRowWise = jobDetailArr.getString(k);

							JSONObject jobDetailObj = new JSONObject(jobDetailRowWise);

							String machine_id = jobDetailObj.getString("machine_id");
							String errorCode = jobDetailObj.getString("error_code");
							String problem = jobDetailObj.getString("problem");
							String engName = jobDetailObj.getString("engineer_name");

							String dateTime = "";
							String engComments = "";
							String workCarriedOut = "";

							if (docketDetailArr.length() > k) {
								JSONObject docketDetailObj = new JSONObject(docketDetailArr.getString(k));

								dateTime = docketDetailObj.getString("caried_out_date");
								engComments = docketDetailObj.getString("comment");
								workCarriedOut = docketDetailObj.getString("work_carried_out");
							}

							MachineServiceHistory machineServiceHistory = new MachineServiceHistory();

							machineServiceHistory.setMachine_id(machine_id);
							machineServiceHistory.setErrorCode(errorCode);
							machineServiceHistory.setProblemcomments(problem);
							machineServiceHistory.setEngineerName(engName);
							machineServiceHistory.setDateTime(dateTime);
							machineServiceHistory.setEngineerComments(engComments);
							machineServiceHistory.setWorkCarriedOut(workCarriedOut);

							db.addMachineServiceHistory(machineServiceHistory);
						}
					}

					machineDataList.add(machineItem);
					machineDataList2.add(machineItem);

				}

				machineListsAdapter = new MachineListsAdapter(FragmentMachines.this, getActivity(), machineDataList);
				machineLists.setAdapter(machineListsAdapter);
				machineListsAdapter.notifyDataSetChanged();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public void openMachineDetailsActivity(int position) {
		// TODO Auto-generated method stub
		String machineSlNo = machineDataList.get(position).getMachine_si_no();

		Config.machineSlNo = machineSlNo;
		Intent gotoVideoActivity = new Intent(getContext(), MachineDetails.class);
		Log.e("TAG", "machineSlNo>>" + machineSlNo);
		
		startActivity(gotoVideoActivity);
		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

	}

}

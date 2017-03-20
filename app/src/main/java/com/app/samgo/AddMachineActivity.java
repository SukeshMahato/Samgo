package com.app.samgo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.adapter.AddCommonListAdapter;
import com.app.adapter.AddMachineListAdapter;
import com.app.adapter.AddMachineModelListAdapter;
import com.app.adapter.AddMachineNameListAdapter;
import com.app.adapter.AddMachineTypeListAdapter;
import com.app.asyncs.AddMachine;
import com.app.asyncs.MachineMasterServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.fragment.TodayJobFragment;
import com.app.listners.AddMachineMasterListener;
import com.app.model.Config;
import com.app.model.MachineManufacturer;
import com.app.model.MachineMaster;
import com.app.model.MachineModel;
import com.app.model.MachineSiteMain;
import com.app.model.Machinetype;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddMachineActivity extends Activity implements AddMachineMasterListener {

	private ArrayList<MachineMaster> machineMasterList = new ArrayList<MachineMaster>();
	private ArrayList<Machinetype> typeMasterList = new ArrayList<Machinetype>();
	private ArrayList<MachineModel> modelMasterList = new ArrayList<MachineModel>();
	private ArrayList<MachineManufacturer> manufacturerMasterList = new ArrayList<MachineManufacturer>();
	ArrayList<String> warranty_length_list = new ArrayList<String>();
	private ArrayList<String> commonList = new ArrayList<String>();
	// private AddMachineListAdapter addMachineListAdapter;

	private TextView dropManufacturer;
	private TextView dropMachineType;
	private TextView dropMachineName;
	private TextView dropModelName;
	private RadioGroup voltageRadio;
	private RadioGroup suctionRadio;
	private RadioGroup tractionRadio;
	private RadioGroup waterRadio;
	private RadioGroup sparePartsRadio;
	private RadioButton voltageButton;
	private RadioButton suctionButton;
	private RadioButton tractionButton;
	private RadioButton waterButton;
	private RadioButton sparePartsButton;
	private TextView dropWorkingOrder;
	private TextView dropVisualInspect;
	private TextView dropMarksAvails;
	private TextView dropManufacturerYear;
	private CheckBox warrantyBox;
	private EditText machineSINo;
	private Button saveButton;
	private LinearLayout hiddenLayout;
	StringBuilder result1;
	private DatePickerDialog fromDatePickerDialog;
	protected TextView purchase_date_readonly, warranty_length_readonly;
	private ImageView backButton;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	String manufacturer_id, model_id, type_id, machine_id;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	private SamgoSQLOpenHelper db;
	private String site_id = "";
	private LinearLayout parentLayout;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_machine_site_activity);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		initialisation();
		listner();


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			site_id = extras.getString("site_id");
			// Log.e("TAG", "VALUE PASSES::" + site_id);
		}

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		db = new SamgoSQLOpenHelper(getApplicationContext());
		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);
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

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent) {

			//new MachineMasterServices("0", "-1", this).execute();
		}
	}

	private void listner() {
		// TODO Auto-generated method stub
		dropManufacturer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_manufacturer_name);

				ListView manufactureListsView = (ListView) dialog.findViewById(R.id.manufacturer_name_list);

				manufacturerMasterList = db.getManufactureIdLIstDetails();

				// manufacturerList =
				// Log.e("TAG", "Size isd >>>" + manufacturerMasterList.size());

				AddMachineListAdapter addMachineListAdapter = new AddMachineListAdapter(AddMachineActivity.this,
						manufacturerMasterList);
				manufactureListsView.setAdapter(addMachineListAdapter);
				addMachineListAdapter.notifyDataSetChanged();

				manufactureListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						MachineManufacturer machineManufacturer = manufacturerMasterList.get(position);
						// Log.e("TAG", "Value for Id:" +
						// machineManufacturer.getManufacture_id());
						manufacturer_id = machineManufacturer.getManufacture_id();
						dropManufacturer.setText(machineManufacturer.getManufacturer_name());
						dropMachineType.setText("Select Machine Type");
						typeMasterList = db.getListForMachineType(machineManufacturer.getManufacture_id());
						/*
						 * for(int i=0;i<typeMasterList.size();i++){ Machinetype
						 * MT = typeMasterList.get(i); //Log.e("TAG",
						 * "Value for type Master>>"+MT.getType_id());
						 * //Log.e("TAG", "Value for type Master>>"
						 * +MT.getType_name()); }
						 */
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropMachineType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_machine_type);

				ListView machineTypeListsView = (ListView) dialog.findViewById(R.id.machine_type_list);

				// typeMasterList = db.getListForMachineType();

				// manufacturerList =
				// Log.e("TAG", "Size isd >>>" + manufacturerMasterList.size());

				AddMachineTypeListAdapter addMachineListAdapter = new AddMachineTypeListAdapter(AddMachineActivity.this,
						typeMasterList);
				machineTypeListsView.setAdapter(addMachineListAdapter);
				addMachineListAdapter.notifyDataSetChanged();

				machineTypeListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						Machinetype machinetype = typeMasterList.get(position);
						// Log.e("TAG", "Value for Id:" +
						// machinetype.getType_id());
						type_id = machinetype.getType_id();
						dropMachineType.setText(machinetype.getType_name());
						dropMachineName.setText("Select Machine");
						machineMasterList = db.getListForMachineMaster(machinetype.getType_id(), manufacturer_id);
						/*
						 * for (int i = 0; i < typeMasterList.size(); i++) {
						 * MachineMaster MT = machineMasterList.get(i);
						 * //Log.e("TAG", "Value for machine Master>>" +
						 * MT.getMachine_id()); //Log.e("TAG",
						 * "Value for machine Master>>" + MT.getMachine_name());
						 * }
						 */
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropMachineName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_machine_name);

				ListView machineNameListsView = (ListView) dialog.findViewById(R.id.machine_name_list);

				AddMachineNameListAdapter addMachineNameListAdapter = new AddMachineNameListAdapter(
						AddMachineActivity.this, machineMasterList);
				machineNameListsView.setAdapter(addMachineNameListAdapter);
				addMachineNameListAdapter.notifyDataSetChanged();

				machineNameListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						MachineMaster machineMaster = machineMasterList.get(position);
						// Log.e("TAG", "Value for Machine Id:" +
						// machineMaster.getMachine_id());
						machine_id = machineMaster.getMachine_id();
						// Log.e("TAG", "Value for Machine Name:" +
						// machineMaster.getMachine_name());
						dropMachineName.setText(machineMaster.getMachine_name());
						modelMasterList = db.getModelForMachine(machineMaster.getMachine_id());
						/*
						 * for (int i = 0; i < modelMasterList.size(); i++) {
						 * MachineModel MT = modelMasterList.get(i);
						 * //Log.e("TAG", "Value for machine model id>>" +
						 * MT.getModel_id()); //Log.e("TAG",
						 * "Value for machine model name>>" +
						 * MT.getModel_name()); }
						 */
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropModelName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_machine_model);

				ListView machineMOdelListsView = (ListView) dialog.findViewById(R.id.machine_model_list);

				AddMachineModelListAdapter addMachineModelListAdapter = new AddMachineModelListAdapter(
						AddMachineActivity.this, modelMasterList);
				machineMOdelListsView.setAdapter(addMachineModelListAdapter);
				addMachineModelListAdapter.notifyDataSetChanged();

				machineMOdelListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						if(modelMasterList.size() > 0){
							MachineModel machineModel = modelMasterList.get(position);
							// Log.e("TAG", "Value for Machine Id:" +
							// machineModel.getModel_id());
							model_id = machineModel.getModel_id();
							// Log.e("TAG", "Value for Machine Name:" +
							// machineModel.getModel_name());
							dropModelName.setText(machineModel.getModel_name());
							/*
							 * modelMasterList =
							 * db.getModelForMachine(machineMaster.getMachine_id());
							 * for (int i = 0; i < modelMasterList.size(); i++) {
							 * MachineModel MT = modelMasterList.get(i);
							 * //Log.e("TAG", "Value for machine model id>>" +
							 * MT.getModel_id()); //Log.e("TAG",
							 * "Value for machine model name>>" +
							 * MT.getModel_name()); }
							 */
							dialog.dismiss();
						}
					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		voltageRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// get selected radio button from radioGroup
				int selectedId = voltageRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				voltageButton = (RadioButton) findViewById(selectedId);

				// Toast.makeText(AddMachineActivity.this,
				// voltageButton.getText(), Toast.LENGTH_SHORT).show();

			}

		});

		suctionRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// get selected radio button from radioGroup
				int selectedId = suctionRadio.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				suctionButton = (RadioButton) findViewById(selectedId);
				// Toast.makeText(AddMachineActivity.this,
				// suctionButton.getText(), Toast.LENGTH_SHORT).show();

			}

		});

		tractionRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// get selected radio button from radioGroup
				int selectedId = tractionRadio.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				tractionButton = (RadioButton) findViewById(selectedId);
				// Toast.makeText(AddMachineActivity.this,
				// tractionButton.getText(), Toast.LENGTH_SHORT).show();

			}

		});

		waterRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// get selected radio button from radioGroup
				int selectedId = waterRadio.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				waterButton = (RadioButton) findViewById(selectedId);
				// Toast.makeText(AddMachineActivity.this,
				// waterButton.getText(), Toast.LENGTH_SHORT).show();

			}

		});

		sparePartsRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// get selected radio button from radioGroup
				int selectedId = sparePartsRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				sparePartsButton = (RadioButton) findViewById(selectedId);

				// Toast.makeText(AddMachineActivity.this,
				// sparePartsButton.getText(), Toast.LENGTH_SHORT).show();

			}

		});

		dropWorkingOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_working_order);

				ListView commonListsView = (ListView) dialog.findViewById(R.id.work_order_list);
				String[] workArray = getResources().getStringArray(R.array.working_order);
				commonList.clear();

				for (int i = 0; i < workArray.length; i++) {
					commonList.add(workArray[i]);
				}

				AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(AddMachineActivity.this,
						commonList);
				commonListsView.setAdapter(addCommonListAdapter);
				addCommonListAdapter.notifyDataSetChanged();

				commonListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String workOrder = commonList.get(position);
						// Log.e("TAG", "Value for workOrder:" + workOrder);
						dropWorkingOrder.setText(workOrder);
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropVisualInspect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_visual_inspection);

				ListView commonListsView = (ListView) dialog.findViewById(R.id.visual_inspection_list);
				String[] visualArray = getResources().getStringArray(R.array.visual_inspection);
				commonList.clear();

				for (int i = 0; i < visualArray.length; i++) {
					commonList.add(visualArray[i]);
				}

				AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(AddMachineActivity.this,
						commonList);
				commonListsView.setAdapter(addCommonListAdapter);
				addCommonListAdapter.notifyDataSetChanged();

				commonListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String visualInspection = commonList.get(position);
						// Log.e("TAG", "Value for visualInspection:" +
						// visualInspection);
						dropVisualInspect.setText(visualInspection);
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropMarksAvails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_marks_avails);

				ListView commonListsView = (ListView) dialog.findViewById(R.id.marks_avail_list);
				String[] marksArray = getResources().getStringArray(R.array.marks_avails);
				commonList.clear();

				for (int i = 0; i < marksArray.length; i++) {
					commonList.add(marksArray[i]);
				}

				AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(AddMachineActivity.this,
						commonList);
				commonListsView.setAdapter(addCommonListAdapter);
				addCommonListAdapter.notifyDataSetChanged();

				commonListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String marksAvail = commonList.get(position);
						// Log.e("TAG", "Value for marksAvail:" + marksAvail);
						dropMarksAvails.setText(marksAvail);
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		dropManufacturerYear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_manufacture_year);

				ListView commonListsView = (ListView) dialog.findViewById(R.id.manufacture_year_list);
				commonList.clear();
				int year = Calendar.getInstance().get(Calendar.YEAR);

				for (int i = year; i > 1980; i--) {
					commonList.add("" + i);
				}

				AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(AddMachineActivity.this,
						commonList);
				commonListsView.setAdapter(addCommonListAdapter);
				addCommonListAdapter.notifyDataSetChanged();

				commonListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String years = commonList.get(position);
						// Log.e("TAG", "Value for Years:" + years);
						dropManufacturerYear.setText(years);
						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		warrantyBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				result1 = new StringBuilder();
				// result1.append("Warranty Checked:");
				if (warrantyBox.isChecked()) {
					result1.append("Y");
					hiddenLayout.setVisibility(View.VISIBLE);
				} else {
					result1.append("N");
					hiddenLayout.setVisibility(View.GONE);
				}
				// Displaying the message on the toast
				// Toast.makeText(getApplicationContext(), result1.toString(),
				// Toast.LENGTH_LONG).show();
			}

		});

		saveButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			StringBuffer errorText;

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onClick(View view) {
				errorText = new StringBuffer();
				MachineSiteMain result = new MachineSiteMain();

				int selectedId1 = sparePartsRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				sparePartsButton = (RadioButton) findViewById(selectedId1);
				// Log.e("TAG", "Value for sparePartsButton:" +
				// sparePartsButton.getText());

				int selectedId2 = suctionRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				suctionButton = (RadioButton) findViewById(selectedId2);
				// Log.e("TAG", "Value for suctionButton:" +
				// suctionButton.getText());

				int selectedId3 = tractionRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				tractionButton = (RadioButton) findViewById(selectedId3);
				// Log.e("TAG", "Value for tractionButton:" +
				// tractionButton.getText());

				int selectedId4 = waterRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				waterButton = (RadioButton) findViewById(selectedId4);
				// Log.e("TAG", "Value for waterButton:" +
				// waterButton.getText());

				int selectedId5 = voltageRadio.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				voltageButton = (RadioButton) findViewById(selectedId5);
				// Log.e("TAG", "Value for voltageButton:" +
				// voltageButton.getText());

				/*
				 * result.setMachineMAnufacturer(dropManufacturer.getText().
				 * toString());
				 * result.setMachineMAnufacturer_id(manufacturer_id);
				 * result.setMachineModel(dropModelName.getText().toString());
				 * result.setMachineModel_id(model_id);
				 * result.setMachineType(dropMachineType.getText().toString());
				 * result.setMachineType_id(type_id);
				 * result.setMachineName(dropMachineName.getText().toString());
				 * result.setMachineName_id(machine_id);
				 */
				if (suctionButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineSuction("5");
				} else {
					result.setMachineSuction("1");
				}
				if (tractionButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineTraction("5");
				} else {
					result.setMachineTraction("1");
				}
				if (waterButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineWater("5");
				} else {
					result.setMachineWater("1");
				}
				if (sparePartsButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineSpareParts("5");
				} else {
					result.setMachineSpareParts("1");
				}
				if (voltageButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineWater("5");
				} else {
					result.setMachineWater("1");
				}
				/*
				 * result.setMachineManuYear(dropManufacturerYear.getText().
				 * toString());
				 * result.setMachineMarksAvail(dropMarksAvails.getText().
				 * toString());
				 */
				if (dropWorkingOrder.getText().toString() != null) {
					result.setMachineWorkOrder(dropWorkingOrder.getText().toString());
				} else {
					result.setMachineWorkOrder("");
				}
				/*
				 * result.setMachineSINo(machineSINo.getText().toString());
				 * result.setMachineVisualInspection(dropVisualInspect.getText()
				 * .toString());
				 */

				if (voltageButton.getText().toString().equalsIgnoreCase("Yes")) {
					result.setMachineVoltage("5");
				} else {
					result.setMachineVoltage("0");
				}
				// result.setMachineVoltage(voltageButton.getText().toString());
				/*
				 * if (warrantyBox.isChecked()) { if
				 * (warranty_length_readonly.getText().toString().
				 * equalsIgnoreCase("Select Length") ||
				 * purchase_date_readonly.getText().toString().equalsIgnoreCase(
				 * "Purchase Date:")) { errorText.append(
				 * "Select Warranty Property"); errorText.append("\n"); } } else
				 * { result.setMachineWarranty(""); }
				 */

				final int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

					if (dropManufacturer.getText().toString().equalsIgnoreCase("Select Machine Manufacturer")) {
						dropManufacturer.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Manufacture ");
						errorText.append("\n");
					} else {
						result.setMachineMAnufacturer(dropManufacturer.getText().toString());
						result.setMachineMAnufacturer_id(manufacturer_id);
						dropManufacturer.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMachineType.getText().toString().equalsIgnoreCase("Select Machine Type")) {
						dropMachineType.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Type ");
						errorText.append("\n");
					} else {
						result.setMachineType(dropMachineType.getText().toString());
						result.setMachineType_id(type_id);
						dropMachineType.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMachineName.getText().toString().equalsIgnoreCase("Select Machine")) {
						dropMachineName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine ");
						errorText.append("\n");
					} else {
						result.setMachineName(dropMachineName.getText().toString());
						result.setMachineName_id(machine_id);
						dropMachineName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropModelName.getText().toString().equalsIgnoreCase("Select Machine Model")) {
						dropModelName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Model ");
						errorText.append("\n");
					} else {
						result.setMachineModel(dropModelName.getText().toString());
						result.setMachineModel_id(model_id);
						dropModelName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (machineSINo.getText().toString().equalsIgnoreCase("")
							|| machineSINo.getText().toString() == null) {
						machineSINo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Enter Machine SI No ");
						errorText.append("\n");
					} else {
						result.setMachineSINo(machineSINo.getText().toString());
						machineSINo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropManufacturerYear.getText().toString().equalsIgnoreCase("Select Manufacturer Year")) {
						dropManufacturerYear
								.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Manufacturer Year ");
						errorText.append("\n");
					} else {
						result.setMachineManuYear(dropManufacturerYear.getText().toString());
						dropManufacturerYear.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMarksAvails.getText().toString().equalsIgnoreCase("Select Marks Avail")) {
						dropMarksAvails.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Marks Avail ");
						errorText.append("\n");
					} else {
						result.setMachineMarksAvail(dropMarksAvails.getText().toString());
						dropMarksAvails.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropVisualInspect.getText().toString().equalsIgnoreCase("Select Visual Inspection")) {
						dropVisualInspect
								.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Visual Inspection ");
						errorText.append("\n");
					} else {
						result.setMachineVisualInspection(dropVisualInspect.getText().toString());
						dropVisualInspect.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}

					if (warrantyBox.isChecked()) {

						if (warranty_length_readonly.getText().toString().equalsIgnoreCase("Select Length")) {
							warranty_length_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
							errorText.append("Select Warranty Length ");
							errorText.append("\n");
						} else {
							result.setMachineWarranty(warranty_length_readonly.getText().toString());
							warranty_length_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
						}
						if (purchase_date_readonly.getText().toString().equalsIgnoreCase("Purchase Date:")) {
							purchase_date_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
							errorText.append("Select Purchase Date ");
							errorText.append("\n");
						} else {
							result.setMachineVisualInspection(purchase_date_readonly.getText().toString());
							purchase_date_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
						}

					} else {
						result.setMachineWarranty("");
					}

				} else {
					if (dropManufacturer.getText().toString().equalsIgnoreCase("Select Machine Manufacturer")) {
						dropManufacturer.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Manufacture ");
						errorText.append("\n");
					} else {
						result.setMachineMAnufacturer(dropManufacturer.getText().toString());
						result.setMachineMAnufacturer_id(manufacturer_id);
						dropManufacturer.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMachineType.getText().toString().equalsIgnoreCase("Select Machine Type")) {
						dropMachineType.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Type ");
						errorText.append("\n");
					} else {
						result.setMachineType(dropMachineType.getText().toString());
						result.setMachineType_id(type_id);
						dropMachineType.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMachineName.getText().toString().equalsIgnoreCase("Select Machine")) {
						dropMachineName.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine ");
						errorText.append("\n");
					} else {
						result.setMachineName(dropMachineName.getText().toString());
						result.setMachineName_id(machine_id);
						dropMachineName.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropModelName.getText().toString().equalsIgnoreCase("Select Machine Model")) {
						dropModelName.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Machine Model ");
						errorText.append("\n");
					} else {
						result.setMachineModel(dropModelName.getText().toString());
						result.setMachineModel_id(model_id);
						dropModelName.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (machineSINo.getText().toString().equalsIgnoreCase("")
							|| machineSINo.getText().toString() == null) {
						machineSINo.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Enter Machine SI No ");
						errorText.append("\n");
					} else {
						result.setMachineSINo(machineSINo.getText().toString());
						machineSINo.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropManufacturerYear.getText().toString().equalsIgnoreCase("Select Manufacturer Year")) {
						dropManufacturerYear.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Manufacturer Year ");
						errorText.append("\n");
					} else {
						result.setMachineManuYear(dropManufacturerYear.getText().toString());
						dropManufacturerYear.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropMarksAvails.getText().toString().equalsIgnoreCase("Select Marks Avail")) {
						dropMarksAvails.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Marks Avail ");
						errorText.append("\n");
					} else {
						result.setMachineMarksAvail(dropMarksAvails.getText().toString());
						dropMarksAvails.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}
					if (dropVisualInspect.getText().toString().equalsIgnoreCase("Select Visual Inspection")) {
						dropVisualInspect.setBackground(getResources().getDrawable(R.drawable.radio_red_border));
						errorText.append("Select Visual Inspection ");
						errorText.append("\n");
					} else {
						result.setMachineVisualInspection(dropVisualInspect.getText().toString());
						dropVisualInspect.setBackground(getResources().getDrawable(R.drawable.radio_border));
					}

					if (warrantyBox.isChecked()) {

						if (warranty_length_readonly.getText().toString().equalsIgnoreCase("Select Length")) {
							warranty_length_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
							errorText.append("Select Warranty Length ");
							errorText.append("\n");
						} else {
							result.setMachineWarranty(warranty_length_readonly.getText().toString());
							warranty_length_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
						}
						if (purchase_date_readonly.getText().toString().equalsIgnoreCase("Purchase Date:")) {
							purchase_date_readonly
									.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
							errorText.append("Select Purchase Date ");
							errorText.append("\n");
						} else {
							if (checkSysDate(purchase_date_readonly.getText().toString())) {
								result.setPurchase_date(purchase_date_readonly.getText().toString());
								purchase_date_readonly
										.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
							} else {
								purchase_date_readonly
										.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
								errorText.append("Please Select Valid Date");
								errorText.append("\n");
							}
						}

					} else {
						result.setMachineWarranty("");
					}
				}
				ArrayList<String> siList = db.getSIMachineList();
				for (int k = 0; k < siList.size(); k++) {
					// Log.e("TAG", "SIMACHINE>>>>value:" + siList.get(k));
				}
				// Log.e("TAG", "SIMACHINE>>>>Size:" + siList.size());

				if (errorText.toString().equalsIgnoreCase("")) {
					int count = db.getCountAddMachineList(machineSINo.getText().toString().trim());
					// Log.e("TAG", "Count>>>>value:" + count);
					if (count > 0) {
						Toast.makeText(getApplicationContext(), "Machine Serial number is already added.",
								Toast.LENGTH_LONG).show();
					} else {
						result.setSite_id(site_id);
						result.setLocally("local");
						db.addMachineToSiteMain(result);
						// Log.e("TAG", "Purchase Date::" +
						// result.getPurchase_date());
						com.app.model.MachineDetails mcDetails = new com.app.model.MachineDetails(
								result.getMachineMAnufacturer_id(), result.getMachineType_id(),
								result.getMachineModel_id(), result.getMachineName(), result.getMachineSINo(), "---",
								result.getMachineVoltage(), result.getMachineSuction(), result.getMachineTraction(),
								result.getMachineWater(), result.getMachineManuYear());

						db.addMachineDetails(mcDetails, site_id);
						// Log.e("TAG", "Manufacturer::" +
						// mcDetails.getManufacturer());
						// Log.e("TAG", "Type::" + mcDetails.getMachine_type());
						// Log.e("TAG", "Model::" +
						// mcDetails.getMachine_model());
						Toast.makeText(getApplicationContext(), "Machine Added Successfully", Toast.LENGTH_LONG).show();

						AddMachineActivity.this.finish();
					}
				} else {
					Toast.makeText(AddMachineActivity.this, errorText.toString(), Toast.LENGTH_SHORT).show();
				}

			}

		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		purchase_date_readonly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar newCalendar = Calendar.getInstance();
				fromDatePickerDialog = new DatePickerDialog(AddMachineActivity.this, R.style.DialogTheme,
						new OnDateSetListener() {

							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								// fromDatePickerDialog.getDatePicker().setMaxDate(new
								// Date().getTime());
								Calendar newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								purchase_date_readonly.setText(dateFormatter.format(newDate.getTime()));
							}

						}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
						newCalendar.get(Calendar.DAY_OF_MONTH));
				fromDatePickerDialog.show();
			}

		});
		warranty_length_readonly.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(AddMachineActivity.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.common_one_row_shift_item);

				ListView timingListsView = (ListView) dialog.findViewById(R.id.list_timing);
				String[] items = getResources().getStringArray(R.array.warranty_length);
				warranty_length_list.clear();
				for (int i = 0; i < items.length; i++) {
					String s = items[i];
					warranty_length_list.add(s);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddMachineActivity.this,
						android.R.layout.simple_list_item_1, warranty_length_list);
				timingListsView.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				timingListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String length = warranty_length_list.get(position);
						// //Log.e("TAG", "Value for Years:" + hours);
						warranty_length_readonly.setText(length);
						dialog.dismiss();

					}

				});

				dialog.show();
			}
		});

	}

	private void initialisation() {
		// TODO Auto-generated method stub

		dropManufacturer = (TextView) findViewById(R.id.machine_manufacturer_dropdown);
		dropMachineType = (TextView) findViewById(R.id.machine_type);
		dropMachineName = (TextView) findViewById(R.id.select_machine);
		dropModelName = (TextView) findViewById(R.id.machine_model);
		voltageRadio = (RadioGroup) findViewById(R.id.radioVoltage);
		suctionRadio = (RadioGroup) findViewById(R.id.radioSuction);
		tractionRadio = (RadioGroup) findViewById(R.id.radioTraction);
		waterRadio = (RadioGroup) findViewById(R.id.radioWater);
		sparePartsRadio = (RadioGroup) findViewById(R.id.radioSpareParts);
		dropWorkingOrder = (TextView) findViewById(R.id.good_working_order);
		dropVisualInspect = (TextView) findViewById(R.id.visual_inspection);
		dropMarksAvails = (TextView) findViewById(R.id.marks_avail);
		dropManufacturerYear = (TextView) findViewById(R.id.manufacturer_year);
		warrantyBox = (CheckBox) findViewById(R.id.warranty);
		saveButton = (Button) findViewById(R.id.save);
		machineSINo = (EditText) findViewById(R.id.serial_no);
		backButton = (ImageView) findViewById(R.id.back_button);
		hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
		purchase_date_readonly = (TextView) findViewById(R.id.purchase_date_readonly);
		warranty_length_readonly = (TextView) findViewById(R.id.warranty_length_readonly);
	}

	@Override
	public void getAllMachineMasterListResponse(String response) {
		// TODO Auto-generated method stub
		if (response != null) {
			try {
				// Log.e("TAG", "response >> " + response);

				JSONArray jArr = new JSONArray(response);
				db.deletemachineMasterAllJobRecords();
				db.deletemachineManufacturerMasterrAllJobRecords();
				db.deletemachineModelMasterAllJobRecords();
				db.deletemachineTypeMasterAllJobRecords();
                //Added by Sukesh

                //new AddMachine(this).execute(jArr);

//				for (int i = 0; i < jArr.length(); i++) {
//
//					MachineMaster machineMaster = new MachineMaster();
//					MachineManufacturer machineManufacturer = new MachineManufacturer();
//					Machinetype machinetype = new Machinetype();
//					MachineModel machineModel = null;
//
//					String masterStr = jArr.getString(i);
//
//					JSONObject machinObj = new JSONObject(masterStr);
//
//					String machineMasterStr = machinObj.getString("MachineMaster");
//
//					JSONObject machinMasterObj = new JSONObject(machineMasterStr);
//					String machineMasterId = machinMasterObj.getString("id");
//					String type_id = machinMasterObj.getString("type_id");
//					String manufacturer_id = machinMasterObj.getString("manufacturer_id");
//					String model_id = machinMasterObj.getString("model_id");
//					String machine_name = machinMasterObj.getString("name");
//					String machine_desc = machinMasterObj.getString("description");
//					String MachineTypeMaster = machinObj.getString("MachineTy" +
//							"pe");
//					JSONObject machineTypeObj = new JSONObject(MachineTypeMaster);
//					String typeMaster_id = machineTypeObj.getString("id");
//					String type_name = machineTypeObj.getString("name");
//					String type_desc = machineTypeObj.getString("description");
//
//					String machineArray = machinObj.getString("MachineModel");
//					JSONArray MachineModelArray = new JSONArray(machineArray);
//
//					for (int j = 0; j < MachineModelArray.length(); j++) {
//						machineModel = new MachineModel();
//						String masterModelStr = MachineModelArray.getString(j);
//						JSONObject ModelObj = new JSONObject(masterModelStr);
//						String modelMaster_id = ModelObj.getString("id");
//						String machine_id = ModelObj.getString("master_id");
//						String model_name = ModelObj.getString("name");
//						// setting data in MasterMopdel
//						machineModel.setModel_id(modelMaster_id);
//						machineModel.setMachine_id(machine_id);
//						machineModel.setModel_name(model_name);
//						db.addmachineModelMaster(machineModel);
//					}
//
//					// setting data in MasterMachine
//					machineMaster.setMachine_id(machineMasterId);
//					machineMaster.setMachine_desc(machine_desc);
//					machineMaster.setMachine_name(machine_name);
//					machineMaster.setManufacturer_id(manufacturer_id);
//					machineMaster.setModel_id(model_id);
//					machineMaster.setType_id(type_id);
//					// machineMasterList.add(machineMaster);
//					db.addmachineMaster(machineMaster);
//
//					String MachineManufacturerMaster = machinObj.getString("MachineManufacturer");
//					JSONObject machineManufacturerObj = new JSONObject(MachineManufacturerMaster);
//					String manufactureMaster_id = machineManufacturerObj.getString("id");
//					String manufacturer_name = machineManufacturerObj.getString("name");
//					// setting data in MasterManufacturer
//					machineManufacturer.setManufacture_id(manufactureMaster_id);
//					machineManufacturer.setManufacturer_name(manufacturer_name);
//					ArrayList<String> getManufactureIds = db.getMachineManufaturesId();
//
//					if (getManufactureIds.contains(manufactureMaster_id)) {
//
//					} else {
//
//						// Log.e("TAG", "During Insert>>");
//						// Log.e("TAG", "Value>>" + manufactureMaster_id);
//						// Log.e("TAG", "Value>>" + manufacturer_name);
//
//						db.addmachineManufactureMaster(machineManufacturer);
//					}
//					// setting data in MasterType
//					machinetype.setType_id(typeMaster_id);
//					machinetype.setType_name(type_name);
//					machinetype.setType_desc(type_desc);
//					// typeMasterList.add(machinetype);
//					db.addMachineTypeMaster(machinetype);
//
//				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	public boolean checkSysDate(String userDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = sdf.parse(userDate);
			Date date = new Date();

			if (date1.compareTo(date) > 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}




}

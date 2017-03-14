package com.app.samgo;

import java.util.ArrayList;
import java.util.Calendar;

import com.app.adapter.AddCommonListAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.MachineDetails;
import com.app.model.MachineSiteMain;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSiteActivity extends Activity {

	private LinearLayout mLinearListView;
	private ArrayList<MachineDetails> mArrayListData = new ArrayList<MachineDetails>();
	private ArrayList<String> commonList = new ArrayList<String>();

	private ImageView backButton;

	private String clientName;
	private String contractorName;
	private String siteName;
	private String siteAddress;

	private TextView clientName_text;
	private TextView siteName_text;
	private TextView siteAddress_text;
	private TextView contractorName_text;
	private TextView attach_machine_top;
	private String site_id = "";

	private SamgoSQLOpenHelper db;

	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.view_sites_activity);

		mLinearListView = (LinearLayout) findViewById(R.id.machine_details_wrapper);
		backButton = (ImageView) findViewById(R.id.back_button);
		attach_machine_top = (TextView) findViewById(R.id.attach_machine_top);

		clientName_text = (TextView) findViewById(R.id.client_name_text);
		siteName_text = (TextView) findViewById(R.id.site_name_text);
		siteAddress_text = (TextView) findViewById(R.id.site_address_text);
		contractorName_text = (TextView) findViewById(R.id.contractor_name_text);

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

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			clientName = extras.getString("clientName");
			contractorName = extras.getString("contractorName");
			siteName = extras.getString("siteName");
			siteAddress = extras.getString("siteAddress");
			site_id = extras.getString("site_id");

			clientName_text.setText(clientName);
			siteName_text.setText(siteName);
			siteAddress_text.setText(siteAddress);
			contractorName_text.setText(contractorName);

			mArrayListData.clear();

			mArrayListData = db.getMachineDetailsBySiteId(site_id);

			Log.e("TAG", "size >>> " + mArrayListData.size());

			/***
			 * adding item into lattach_machine_topistview
			 */
			for (int i = 0; i < mArrayListData.size(); i++) {
				/**
				 * inflate items/ add items in linear layout instead of listview
				 */

				Log.e("TAG", "size of i >> " + i);

				LayoutInflater inflater1 = null;
				inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View mLinearView = inflater1.inflate(R.layout.machine_details_one_row_item, null);

				
				/**
				 * getting id of row.xml
				 */
				TextView manufacturer = (TextView) mLinearView.findViewById(R.id.manufacturer_name);
				TextView type = (TextView) mLinearView.findViewById(R.id.type_name);
				TextView model = (TextView) mLinearView.findViewById(R.id.model);
				TextView name = (TextView) mLinearView.findViewById(R.id.name);
				TextView serialNo = (TextView) mLinearView.findViewById(R.id.serial_number);
				TextView addedBy = (TextView) mLinearView.findViewById(R.id.added_by);
				ImageView view_more = (ImageView) mLinearView.findViewById(R.id.view_more);
				ImageView edit = (ImageView) mLinearView.findViewById(R.id.edit);
				final LinearLayout machineDetails = (LinearLayout) mLinearView
						.findViewById(R.id.remaining_machine_details_wrapper);

				TextView voltage = (TextView) mLinearView.findViewById(R.id.voltage);
				TextView suction = (TextView) mLinearView.findViewById(R.id.suction);
				TextView traction = (TextView) mLinearView.findViewById(R.id.traction);
				TextView water = (TextView) mLinearView.findViewById(R.id.water);
				TextView manufacture_year = (TextView) mLinearView.findViewById(R.id.manufacture_year);

				/**
				 * set item into row
				 */
				final String manufacturerName = mArrayListData.get(i).getManufacturer();
				final String machineType = mArrayListData.get(i).getMachine_type();
				final String machineModel = mArrayListData.get(i).getMachine_model();
				final String machineName = mArrayListData.get(i).getMachine_name();
				final String machineSNo = mArrayListData.get(i).getMachine_si_no();
				final String machineAddedBy = mArrayListData.get(i).getMachine_added_by();

				final String voltageText = mArrayListData.get(i).getVoltage();
				final String suctionText = mArrayListData.get(i).getSuction();
				final String tractionText = mArrayListData.get(i).getTraction();
				final String waterText = mArrayListData.get(i).getWater();
				final String mfg_year = mArrayListData.get(i).getMfg_year();

				final String dropWorkingOrder1 = mArrayListData.get(i).getWork_order();
				final String dropVisualInspect1 = mArrayListData.get(i).getVisual_inspect();
				final String dropMarksAvails1 = mArrayListData.get(i).getMarks_avail();
				final String spareParts1 = mArrayListData.get(i).getSpare_parts();

				String manufacture = db.getMachineManufactureFromManufactureId(manufacturerName);
				String modelName = db.getMachineModelFromModelId(machineModel);
				String typeName = db.getMachineTypeFromTypeId(machineType);

				manufacturer.setText(manufacture);
				type.setText(typeName);
				model.setText(modelName);
				name.setText(machineName);
				serialNo.setText(machineSNo);
				if (machineAddedBy.equalsIgnoreCase("null")) {
					addedBy.setText("-----");
				} else {
					addedBy.setText(machineAddedBy);
				}

				voltage.setText(voltageText);
				suction.setText(suctionText);
				traction.setText(tractionText);
				water.setText(waterText);
				manufacture_year.setText(mfg_year);

				view_more.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (machineDetails.getVisibility() == View.GONE) {
							machineDetails.setVisibility(View.VISIBLE);
						} else {
							machineDetails.setVisibility(View.GONE);
						}
					}
				});

				edit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						final Dialog dialog = new Dialog(ViewSiteActivity.this, R.style.PauseDialog);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.edit_machine_site);

						final RadioButton voltage_yes = (RadioButton) dialog.findViewById(R.id.voltage_yes);
						final RadioButton voltage_no = (RadioButton) dialog.findViewById(R.id.voltage_no);
						final RadioButton suction_yes = (RadioButton) dialog.findViewById(R.id.suction_yes);
						final RadioButton suction_no = (RadioButton) dialog.findViewById(R.id.suction_no);
						final RadioButton traction_yes = (RadioButton) dialog.findViewById(R.id.traction_yes);
						final RadioButton traction_no = (RadioButton) dialog.findViewById(R.id.traction_no);
						final RadioButton water_yes = (RadioButton) dialog.findViewById(R.id.water_yes);
						final RadioButton water_no = (RadioButton) dialog.findViewById(R.id.water_no);
						final RadioButton spare_yes = (RadioButton) dialog.findViewById(R.id.spare_yes);
						final RadioButton spare_no = (RadioButton) dialog.findViewById(R.id.spare_no);
						final TextView dropWorkingOrder = (TextView) dialog.findViewById(R.id.good_working_order);
						final TextView dropVisualInspect = (TextView) dialog.findViewById(R.id.visual_inspection);
						final TextView dropMarksAvails = (TextView) dialog.findViewById(R.id.marks_avail);
						final TextView dropManufacturerYear = (TextView) dialog.findViewById(R.id.manufacturer_year);
						Button saveButton = (Button) dialog.findViewById(R.id.save);

						dropWorkingOrder.setText(dropWorkingOrder1);
						dropVisualInspect.setText(dropVisualInspect1);
						dropMarksAvails.setText(dropMarksAvails1);
						dropManufacturerYear.setText(mfg_year);
						//String visualInspection;
						/*
						 * String visualInspectionText; String workOrderText =
						 * ""; String marksAvailText = ""; String yearsText =
						 * "";
						 */

						if (voltageText.equalsIgnoreCase("5")) {
							voltage_yes.setChecked(true);
						} else {
							voltage_no.setChecked(true);
						}

						if (suctionText.equalsIgnoreCase("5")) {
							suction_yes.setChecked(true);
						} else {
							suction_no.setChecked(true);
						}

						if (tractionText.equalsIgnoreCase("5")) {
							traction_yes.setChecked(true);
						} else {
							traction_no.setChecked(true);
						}

						if (waterText.equalsIgnoreCase("5")) {
							water_yes.setChecked(true);
						} else {
							water_no.setChecked(true);
						}

						if (spareParts1.equalsIgnoreCase("5")) {
							spare_yes.setChecked(true);
						} else {
							spare_no.setChecked(true);
						}

						dropWorkingOrder.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								Log.e("TAG", "clicked on");

								final Dialog dialog = new Dialog(ViewSiteActivity.this, R.style.TopToBottomDialog);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dropdown_working_order);

								ListView commonListsView = (ListView) dialog.findViewById(R.id.work_order_list);
								String[] workArray = getResources().getStringArray(R.array.working_order);
								commonList.clear();

								for (int i = 0; i < workArray.length; i++) {
									commonList.add(workArray[i]);
								}

								AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(
										ViewSiteActivity.this, commonList);
								commonListsView.setAdapter(addCommonListAdapter);
								addCommonListAdapter.notifyDataSetChanged();

								commonListsView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO Auto-generated method stub
										String workOrder = commonList.get(position);
										Log.e("TAG", "Value for workOrder:" + workOrder);
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

								Log.e("TAG", "clicked on");

								final Dialog dialog = new Dialog(ViewSiteActivity.this, R.style.TopToBottomDialog);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dropdown_visual_inspection);

								ListView commonListsView = (ListView) dialog.findViewById(R.id.visual_inspection_list);
								String[] visualArray = getResources().getStringArray(R.array.visual_inspection);
								commonList.clear();

								for (int i = 0; i < visualArray.length; i++) {
									commonList.add(visualArray[i]);
								}

								AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(
										ViewSiteActivity.this, commonList);
								commonListsView.setAdapter(addCommonListAdapter);
								addCommonListAdapter.notifyDataSetChanged();

								commonListsView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO Auto-generated method stub
										String visualInspection = commonList.get(position);
										Log.e("TAG", "Value for visualInspection:" + visualInspection);
										dropVisualInspect.setText(visualInspection);
										// visualInspectionText =
										// visualInspection;
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

								Log.e("TAG", "clicked on");

								final Dialog dialog = new Dialog(ViewSiteActivity.this, R.style.TopToBottomDialog);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dropdown_marks_avails);

								ListView commonListsView = (ListView) dialog.findViewById(R.id.marks_avail_list);
								String[] marksArray = getResources().getStringArray(R.array.marks_avails);
								commonList.clear();

								for (int i = 0; i < marksArray.length; i++) {
									commonList.add(marksArray[i]);
								}

								AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(
										ViewSiteActivity.this, commonList);
								commonListsView.setAdapter(addCommonListAdapter);
								addCommonListAdapter.notifyDataSetChanged();

								commonListsView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO Auto-generated method stub
										String marksAvail = commonList.get(position);
										Log.e("TAG", "Value for marksAvail:" + marksAvail);
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

								Log.e("TAG", "clicked on");

								final Dialog dialog = new Dialog(ViewSiteActivity.this, R.style.TopToBottomDialog);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dropdown_manufacture_year);

								ListView commonListsView = (ListView) dialog.findViewById(R.id.manufacture_year_list);
								commonList.clear();
								int year = Calendar.getInstance().get(Calendar.YEAR);

								for (int i = year; i > 1980; i--) {
									commonList.add("" + i);
								}

								AddCommonListAdapter addCommonListAdapter = new AddCommonListAdapter(
										ViewSiteActivity.this, commonList);
								commonListsView.setAdapter(addCommonListAdapter);
								addCommonListAdapter.notifyDataSetChanged();

								commonListsView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
										// TODO Auto-generated method stub
										String years = commonList.get(position);
										Log.e("TAG", "Value for Years:" + years);
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

						saveButton.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								MachineSiteMain result = new MachineSiteMain();
								result.setMachineMarksAvail(dropMarksAvails.getText().toString());
								result.setMachineVisualInspection(dropVisualInspect.getText().toString());
								result.setMachineWorkOrder(dropWorkingOrder.getText().toString());
								result.setMachineManuYear(dropManufacturerYear.getText().toString());
								if (voltage_yes.isChecked()) {
									result.setMachineVoltage("5");
								} else {
									result.setMachineVoltage("1");
								}
								if (suction_yes.isChecked()) {
									result.setMachineSuction("5");
								} else {
									result.setMachineSuction("1");
								}
								if (traction_yes.isChecked()) {
									result.setMachineTraction("5");
								} else {
									result.setMachineTraction("1");
								}
								if (water_yes.isChecked()) {
									result.setMachineWater("5");
								} else {
									result.setMachineWater("1	");
								}
								if (spare_yes.isChecked()) {
									result.setMachineSpareParts("5");
								} else {
									result.setMachineSpareParts("1");
								}

								int imachine = db.updateMachineBySI(result, machineSNo);
								int jmachine = db.updateMachineDetailsBySI(result, machineSNo);

								if (imachine > 0 || jmachine > 0) {
									Toast.makeText(getApplicationContext(), "Machine Updated Successfully",
											Toast.LENGTH_LONG).show();
								}

								dialog.dismiss();

								/*
								 * if (machineDetails.getVisibility() ==
								 * View.GONE) {
								 * machineDetails.setVisibility(View.VISIBLE); }
								 * else {
								 * machineDetails.setVisibility(View.GONE); }
								 */

								finish();
								startActivity(getIntent());

							}
						});

						dialog.show();
					}

				});

				Log.e("TAG", "serial Number >> " + manufacturerName);
				Log.e("TAG", "errorCode >> " + machineType);
				Log.e("TAG", "description >> " + machineModel);

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

		}

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		attach_machine_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String siteId = site_id;
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("site_id", siteId);

				intent.setClass(ViewSiteActivity.this, AddMachineActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
				// ViewSiteActivity.this.finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
		mArrayListData.clear();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}

	@Override
	public void onRestart() {
		super.onRestart();
		finish();
		startActivity(getIntent());
	}

}

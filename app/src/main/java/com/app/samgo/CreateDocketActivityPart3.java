package com.app.samgo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import com.app.adapter.CreateDocketSparePartsAdapter;
import com.app.adapter.SparePartsAutoCompletePart2Adapter;
import com.app.customview.CustomAutoCompleteView;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.CustomAutoCompletePart3TextChangedListeners;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.DocketList;
import com.app.model.MasterSpareParts;
import com.app.model.SparePartsModel;
import com.app.model.TrainingAddedToJob;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.R.color;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateDocketActivityPart3 extends Activity {

	private ImageView backButton, sign;
	private Button close_docket, authorised_sign, add_spare_parts, training_required, park_job;
	protected ArrayList<SparePartsModel> sparePartsArray;
	public SamgoSQLOpenHelper db;
	CreateDocketSparePartsAdapter createDocketAdapter;
	private ListView sparePartList;
	String root = Environment.getExternalStorageDirectory().toString();
	public static final int SIGNATURE_ACTIVITY = 1;
	public static final int SCAN_ACTIVITY = 2;
	private IntentIntegrator qrScan;

	// adapter for auto-complete
	public ArrayAdapter<MasterSpareParts> myAdapter;

	public AutoCompleteTextView spareParts;

	String spareId = "";
	String spareUnitSales = "0.00";

	public CustomAutoCompleteView myAutoComplete;

	private DatePickerDialog fromDatePickerDialog;

	private SimpleDateFormat dateFormatter;
	private SharedPreferences preferences;

	String signName = "";
	String signDecode = "";
	String comment = "";

	private EditText comment_section, description;

	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_docket_list);
		qrScan = new IntentIntegrator(this);

		db = new SamgoSQLOpenHelper(getApplicationContext());

		preferences = getSharedPreferences(Config.MYFREFS, MODE_PRIVATE);

		parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

		SharedPreferences settingsBack = getSharedPreferences(Config.MYFREFS, 0);

		String background_image = settingsBack.getString("background_image", "test");

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

		initialisation();
		listner();
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		signName = b.getString("yourName");
		signDecode = b.getString("encodedSign");
		comment = b.getString("comment");

		comment_section.setText(comment);

		dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		String job_id = Config.job_id;
		Log.e("TAG", "JOB ID BY FAULT:::" + job_id);
		sparePartsArray = db.getSpareParts(Config.job_id);

		String parkComment = db.getCountForParkJob(Config.job_id);

		Log.e("TAG", "Job parked Comment::" + parkComment);

		String[] park_comment = parkComment.split(",");
		if (park_comment.length > 0) {
			if (park_comment[0] != null && !park_comment[0].equalsIgnoreCase("null")) {
				// Log.e("TAG", "Job is parked" + Config.job_id);
				park_job.setVisibility(View.GONE);
			}
		}

		createDocketAdapter = new CreateDocketSparePartsAdapter(CreateDocketActivityPart3.this, sparePartsArray);
		sparePartList.setAdapter(createDocketAdapter);
		createDocketAdapter.notifyDataSetChanged();

		File imgFile = new File(
				Environment.getExternalStorageDirectory() + "/GetSignature/" + job_id + "_signature.png");
		if (imgFile.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			// ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

			sign.setImageBitmap(myBitmap);

		}

		/*
		 * Bitmap bMap = BitmapFactory
		 * .decodeFile(Environment.getExternalStorageDirectory() +
		 * "/GetSignature/" + job_id + "_signature.png"); if (bMap != null)
		 * sign.setImageBitmap(bMap);
		 */

	}

	private void listner() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		authorised_sign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent signPage = new Intent(CreateDocketActivityPart3.this, SignatureMainLayout.class);
				signPage.putExtra("job_id", Config.job_id);
				signPage.putExtra("comment", comment_section.getText().toString());
				startActivityForResult(signPage, SIGNATURE_ACTIVITY);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
			}
		});

		add_spare_parts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openPopUpForSpareParts();
			}
		});

		training_required.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openTraingAddPopUp();
			}
		});

		park_job.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openParkJobPopUp(Config.job_id);
			}
		});

		close_docket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("TAG", "JOB ID For Park Check::" + db.getCheckForParkJob(Config.job_id));
				// String parkComment = db.getCountForParkJob(Config.job_id);

				if (db.getCheckForParkJob(Config.job_id)) {

					String dateStr = preferences.getString("check_in_date_time", "null");
					db.updateJobTableAtCloseDocket(Config.job_id, getDateTime());
					Log.e("TAG", "chech_in time >> " + dateStr);
					if (!db.getCountWorkDoneForJob(Config.job_id)) {
						DocketList docketList = new DocketList();

						if (db.getCountForDocket(Config.job_id) > 0) {
							docketList.setJob_id(Config.job_id);
							docketList.setUsertype_id("1");
							docketList.setEngg_id(AppManager.getSinleton().getUser().getId());
							docketList.setDocket_date(getDateTime());
							docketList.setCheck_in(dateStr);
							docketList.setCheck_out(getDateTime());
							docketList.setPad_test("0");
							docketList.setComment(comment_section.getText().toString());
							docketList.setStatus("Not Approved");
							docketList.setDocket_complete("0");
							docketList.setSignName(signName);
							docketList.setSignDecode(signDecode);

							db.updateDocket(docketList);
							Log.e("TAG", "Saved Sucessfully::" + Config.job_id);
							db.updateJobTable(Config.job_id);
							Toast.makeText(getApplicationContext(), "Docket Saved Successfully", Toast.LENGTH_LONG)
									.show();
						} else {
							docketList.setJob_id(Config.job_id);
							docketList.setUsertype_id("1");
							docketList.setEngg_id(AppManager.getSinleton().getUser().getId());
							docketList.setDocket_date(getDateTime());
							docketList.setCheck_in(dateStr);
							docketList.setCheck_out(getDateTime());
							docketList.setPad_test("0");
							docketList.setComment(comment_section.getText().toString());
							docketList.setStatus("Not Approved");
							docketList.setDocket_complete("0");
							docketList.setSignName(signName);
							docketList.setSignDecode(signDecode);

							db.addDocketList(docketList);
							db.updateJobTable(Config.job_id);
							Log.e("TAG", "Saved Sucessfully::" + Config.job_id);
							Toast.makeText(getApplicationContext(), "Docket Saved Successfully", Toast.LENGTH_LONG)
									.show();
						}

						Intent goToFragmentJob = new Intent(CreateDocketActivityPart3.this, MainActivity.class);
						goToFragmentJob.putExtra("FROMSTARTACT", "close");
						startActivity(goToFragmentJob);

					} else {
						Log.e("TAG", "Not Saved");
						Toast.makeText(getApplicationContext(), "Please Enter the work done against the job",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Parked Job cannot be closed.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public void openPopUpForSpareParts() {

		final String machine_id = "0";
		final String job_id = Config.job_id;

		final Dialog dialog = new Dialog(CreateDocketActivityPart3.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.add_spare_parts);

		spareParts = (AutoCompleteTextView) dialog.findViewById(R.id.spare_parts_name);
		final EditText quantity = (EditText) dialog.findViewById(R.id.quantity);
		description = (EditText) dialog.findViewById(R.id.description);

		Button saveSpare = (Button) dialog.findViewById(R.id.save_spare_parts);
		Button cancelSpare = (Button) dialog.findViewById(R.id.cancel_spare_parts);
		Button scanSpareParts = (Button) dialog.findViewById(R.id.scan_barcode_spare_parts);

		try {

			spareParts.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// TODO Auto-generated method stub
					RelativeLayout rl = (RelativeLayout) arg1;
					TextView tv = (TextView) rl.getChildAt(0);
					TextView tv1 = (TextView) rl.getChildAt(1);
					TextView tv2 = (TextView) rl.getChildAt(2);

					spareParts.setText(tv.getText().toString());

					Log.e("TAG", "text >> " + tv1.getText().toString());

					spareId = tv1.getText().toString();

					spareUnitSales = tv2.getText().toString();

					String[] spareDesc = tv.getText().toString().split(Pattern.quote("||"));

					Log.e("TAG", "desc text >> " + spareDesc[2]);

					description.setText(spareDesc[2]);

				}
			});

			// add the listener so it will tries to suggest while the user types
			spareParts.addTextChangedListener(
					new CustomAutoCompletePart3TextChangedListeners(CreateDocketActivityPart3.this));

			// ObjectItemData has no value at first
			MasterSpareParts[] ObjectItemData = new MasterSpareParts[0];

			// set the custom ArrayAdapter
			myAdapter = new SparePartsAutoCompletePart2Adapter(CreateDocketActivityPart3.this,
					R.layout.spare_parts_one_row, ObjectItemData);
			spareParts.setAdapter(myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

		cancelSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		//edited by Sukesh Mahato


		scanSpareParts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//				//intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//				startActivityForResult(intent, 2);
				//
				// qrScan.addExtra("SCAN_MODE", 2);
				qrScan.setOrientationLocked(false);
				qrScan.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
			}
		});

		quantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final ArrayList<String> quantityArray = new ArrayList<String>();

				for (int i = 1; i <= 1000; i++) {
					quantityArray.add(String.valueOf(i));
				}

				final Dialog dialog1 = new Dialog(CreateDocketActivityPart3.this, R.style.TopToBottomDialog);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.select_quantity_pop_up);

				ListView quantityList = (ListView) dialog1.findViewById(R.id.quantity_number);

				ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CreateDocketActivityPart3.this,
						android.R.layout.simple_list_item_1, quantityArray);

				quantityList.setAdapter(itemsAdapter);
				itemsAdapter.notifyDataSetChanged();

				quantityList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
						// TODO Auto-generated method stub
						quantity.setText(quantityArray.get(position));

						dialog1.dismiss();
					}
				});

				dialog1.show();

			}
		});

		saveSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SparePartsModel sparePartsModel = new SparePartsModel();

				String sparePartsName = spareParts.getText().toString();

				Log.e("TAG", "sparePartsName >>> " + sparePartsName);

				String quantityName = quantity.getText().toString();
				String descriptionName = description.getText().toString();

				sparePartsModel.setJobId(job_id);
				sparePartsModel.setSpareId(spareId);
				sparePartsModel.setSparePartsId(sparePartsName);
				sparePartsModel.setDescription(descriptionName);
				sparePartsModel.setQuantity(quantityName);
				sparePartsModel.setUnitSales(spareUnitSales);
				sparePartsModel.setMachineId(machine_id);

				sparePartsArray.add(sparePartsModel);

				db.addSpareToMachine(sparePartsModel);

				sparePartsArray.clear();

				sparePartsArray = db.getSpareParts(Config.job_id);
				createDocketAdapter = new CreateDocketSparePartsAdapter(CreateDocketActivityPart3.this,
						sparePartsArray);
				sparePartList.setAdapter(createDocketAdapter);
				createDocketAdapter.notifyDataSetChanged();

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void deleteSpareParts(int position) {

		final Dialog dialog = new Dialog(CreateDocketActivityPart3.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_delete_spare_parts);

		Button cancelCompletion = (Button) dialog.findViewById(R.id.cancel_completion);
		Button ok_completion = (Button) dialog.findViewById(R.id.ok_delete_machine);

		final String spare_id = sparePartsArray.get(position).getSpareId();

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

				db.deleteSparePartsById(spare_id);

				sparePartsArray.clear();

				sparePartsArray = db.getSpareParts(Config.job_id);
				createDocketAdapter = new CreateDocketSparePartsAdapter(CreateDocketActivityPart3.this,
						sparePartsArray);
				sparePartList.setAdapter(createDocketAdapter);
				createDocketAdapter.notifyDataSetChanged();
			}
		});

		dialog.show();

	}

	public void editSpareParts(int position) {

		final String machine_id = "0";
		final String job_id = Config.job_id;

		String spare_id = sparePartsArray.get(position).getSpareId();

		final Dialog dialog = new Dialog(CreateDocketActivityPart3.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.add_spare_parts);

		SparePartsModel sparePartsModel = db.getSparePartsById(spare_id);

		spareParts = (AutoCompleteTextView) dialog.findViewById(R.id.spare_parts_name);
		final EditText quantity = (EditText) dialog.findViewById(R.id.quantity);
		final EditText description = (EditText) dialog.findViewById(R.id.description);

		Button saveSpare = (Button) dialog.findViewById(R.id.save_spare_parts);
		Button cancelSpare = (Button) dialog.findViewById(R.id.cancel_spare_parts);

		spareParts.setText(sparePartsModel.getSparePartsId());
		quantity.setText(sparePartsModel.getQuantity());
		description.setText(sparePartsModel.getDescription());

		try {

			spareParts.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// TODO Auto-generated method stub
					RelativeLayout rl = (RelativeLayout) arg1;
					TextView tv = (TextView) rl.getChildAt(0);
					TextView tv1 = (TextView) rl.getChildAt(1);
					TextView tv2 = (TextView) rl.getChildAt(2);

					spareParts.setText(tv.getText().toString());

					Log.e("TAG", "text >> " + tv1.getText().toString());

					spareId = tv1.getText().toString();

					spareUnitSales = tv2.getText().toString();

					String[] spareDesc = tv.getText().toString().split(Pattern.quote("||"));

					Log.e("TAG", "desc text >> " + spareDesc[2]);

					description.setText(spareDesc[2]);

				}
			});

			// add the listener so it will tries to suggest while the user types
			spareParts.addTextChangedListener(
					new CustomAutoCompletePart3TextChangedListeners(CreateDocketActivityPart3.this));

			// ObjectItemData has no value at first
			MasterSpareParts[] ObjectItemData = new MasterSpareParts[0];

			// set the custom ArrayAdapter
			myAdapter = new SparePartsAutoCompletePart2Adapter(CreateDocketActivityPart3.this,
					R.layout.spare_parts_one_row, ObjectItemData);
			spareParts.setAdapter(myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

		cancelSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		quantity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final ArrayList<String> quantityArray = new ArrayList<String>();

				for (int i = 1; i <= 1000; i++) {
					quantityArray.add(String.valueOf(i));
				}

				final Dialog dialog1 = new Dialog(CreateDocketActivityPart3.this, R.style.TopToBottomDialog);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.select_quantity_pop_up);

				ListView quantityList = (ListView) dialog1.findViewById(R.id.quantity_number);

				ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CreateDocketActivityPart3.this,
						android.R.layout.simple_list_item_1, quantityArray);

				quantityList.setAdapter(itemsAdapter);
				itemsAdapter.notifyDataSetChanged();

				quantityList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
						// TODO Auto-generated method stub
						quantity.setText(quantityArray.get(position));

						dialog1.dismiss();
					}
				});

				dialog1.show();

			}
		});

		saveSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SparePartsModel sparePartsModel = new SparePartsModel();

				String sparePartsName = spareParts.getText().toString();
				String quantityName = quantity.getText().toString();
				String descriptionName = description.getText().toString();

				sparePartsModel.setJobId(job_id);
				sparePartsModel.setSpareId(spareId);
				sparePartsModel.setSparePartsId(sparePartsName);
				sparePartsModel.setDescription(descriptionName);
				sparePartsModel.setQuantity(quantityName);
				sparePartsModel.setUnitSales(spareUnitSales);
				sparePartsModel.setMachineId(machine_id);

				sparePartsArray.add(sparePartsModel);

				db.addSpareToMachine(sparePartsModel);

				sparePartsArray.clear();

				sparePartsArray = db.getSpareParts(Config.job_id);
				createDocketAdapter = new CreateDocketSparePartsAdapter(CreateDocketActivityPart3.this,
						sparePartsArray);
				sparePartList.setAdapter(createDocketAdapter);
				createDocketAdapter.notifyDataSetChanged();

				dialog.dismiss();
			}
		});

		dialog.show();

	}

	protected void openTraingAddPopUp() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(CreateDocketActivityPart3.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_add_training);

		final TextView due_date = (TextView) dialog.findViewById(R.id.training_date_text);

		Button saveTraining = (Button) dialog.findViewById(R.id.save_training);

		due_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				due_date.setText("");
				Calendar newCalendar = Calendar.getInstance();
				fromDatePickerDialog = new DatePickerDialog(CreateDocketActivityPart3.this, R.style.DialogTheme,
						new OnDateSetListener() {

							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								Calendar newDate = Calendar.getInstance();
								newDate.set(year, monthOfYear, dayOfMonth);
								due_date.setText(dateFormatter.format(newDate.getTime()));
							}

						}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
						newCalendar.get(Calendar.DAY_OF_MONTH));

				fromDatePickerDialog.show();

			}
		});

		saveTraining.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TrainingAddedToJob trainingAddJob = new TrainingAddedToJob();

				String job_title = "Training Job For Job Id " + Config.docket_id;

				trainingAddJob.setClient_id(Config.client_id);
				trainingAddJob.setSite_id(Config.site_id);
				trainingAddJob.setJob_title(job_title);
				trainingAddJob.setAssign_date(due_date.getText().toString());
				trainingAddJob.setCreated_date(Config.getDateTime());
				trainingAddJob.setJob_id(Config.job_id);

				db.addTrainingToJob(trainingAddJob);

				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void initialisation() {
		// TODO Auto-generated method stub
		backButton = (ImageView) findViewById(R.id.back_button);
		sparePartList = (ListView) findViewById(R.id.docket_list);
		close_docket = (Button) findViewById(R.id.close_docket);
		authorised_sign = (Button) findViewById(R.id.authorised_sign);
		add_spare_parts = (Button) findViewById(R.id.add_spare_parts);
		training_required = (Button) findViewById(R.id.training_required);
		sign = (ImageView) findViewById(R.id.sign);
		comment_section = (EditText) findViewById(R.id.comment_section);
		park_job = (Button) findViewById(R.id.park_job);

	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//		if (result != null) {
//			//if qrcode has nothing in it
//			if (result.getContents() == null) {
//				Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//			} else {
//				//if qr contains data
//				try {
//					//converting the data to json
//					JSONObject obj = new JSONObject(result.getContents());
//					//setting values to textviews
////					textViewName.setText(obj.getString("name"));
////					textViewAddress.setText(obj.getString("address"));
//				} catch (JSONException e) {
//					e.printStackTrace();
//					//if control comes here
//					//that means the encoded format not matches
//					//in this case you can display whatever data is available on the qrcode
//					//to a toast
//					Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//				}
//			}
//		} else {
//			super.onActivityResult(requestCode, resultCode, data);
//		}
//	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SIGNATURE_ACTIVITY:
			if (resultCode == RESULT_OK) {

				Bundle bundle = data.getExtras();
				String status = bundle.getString("status");
				if (status.equalsIgnoreCase("done")) {
					Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 105, 50);
					toast.show();
				}
			}
			break;
		case 0x0000c0de:
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			//Toast.makeText(getApplicationContext(), "barcode >>  OK", Toast.LENGTH_LONG).show();
			if (resultCode == Activity.RESULT_OK) {


				String contents = result.getContents();
				try {
					//Toast.makeText(getApplicationContext(), "barcode >>" + contents, Toast.LENGTH_LONG).show();
					MasterSpareParts[] myObjs = db.getBarcodeSpareParts(contents);
					Log.e("TAG", "test >> " + myObjs[0].getDescription());
					String jointString = myObjs[0].getDescription() + "||" + myObjs[0].getQuantity() + "||"
							+ myObjs[0].getProduct_id();
					spareParts.setText(jointString.toString());
					spareId = myObjs[0].getProduct_id();
					spareUnitSales = myObjs[0].getUnit_sales();
					String[] spareDesc = jointString.toString().split(Pattern.quote("||"));
					description.setText(spareDesc[2]);

				}catch (Exception e){

                    Toast toast = Toast.makeText(getApplicationContext(),"Machine not present in database", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
				}

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
				Log.i("App", "Scan unsuccessful");
			}
			break;
		}

	}

	protected void openParkJobPopUp(String jobId) {
		// TODO Auto-generated method stub
		final String JobId = jobId;
		final Dialog dialog = new Dialog(CreateDocketActivityPart3.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.park_job);

		final Button park_required = (Button) dialog.findViewById(R.id.park_required);
		final EditText comment_section = (EditText) dialog.findViewById(R.id.park_comment_section);

		park_required.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (comment_section.getText().toString().equalsIgnoreCase("")
						|| comment_section.getText().toString() == null) {
					Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Comment", Toast.LENGTH_LONG);
					toast.show();
				} else {
					/*
					 * park_required.setEnabled(true);
					 * park_required.setClickable(true);
					 */
					int i = db.updateParkJobStatus(JobId, comment_section.getText().toString());
					if (i > 0) {
						Log.e("TAG", "JOB IS PARKED:" + JobId + " with " + comment_section.getText().toString());
					}
					Toast toast = Toast.makeText(getApplicationContext(),
							"JOB IS PARKED:::" + comment_section.getText().toString(), Toast.LENGTH_LONG);

					toast.show();
					dialog.dismiss();
					Intent goToFragmentJob = new Intent(CreateDocketActivityPart3.this, MainActivity.class);
					goToFragmentJob.putExtra("FROMSTARTACT", "close");
					startActivity(goToFragmentJob);
				}

			}
		});

		dialog.show();
	}
}

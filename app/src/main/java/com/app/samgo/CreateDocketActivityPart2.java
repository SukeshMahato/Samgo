package com.app.samgo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import com.app.adapter.DocketMachineDetailsAdapter;
import com.app.adapter.SparePartsAutoCompleteAdapter;
import com.app.customview.CustomAutoCompleteView;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.CustomAutoCompleteTextChangedListeners;
import com.app.model.Config;
import com.app.model.DocketDetail;
import com.app.model.DocketMachineDetails;
import com.app.model.JobDetails;
import com.app.model.MachineSiteMain;
import com.app.model.MachineView;
import com.app.model.MasterSpareParts;
import com.app.model.SparePartsModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateDocketActivityPart2 extends Activity {

	public SamgoSQLOpenHelper db;
	public String scanId = "";

	private ArrayList<DocketMachineDetails> docketMachineArray = new ArrayList<DocketMachineDetails>();

	private DocketMachineDetailsAdapter docketMachineDetailsAdapter;

	private ListView docket_machine_list;

	private ArrayList<SparePartsModel>  sparePartsArray = new ArrayList<SparePartsModel>();

	String spareId = "";
	String spareUnitSales = "0.00";

	public CustomAutoCompleteView myAutoComplete;

	// adapter for auto-complete
	public ArrayAdapter<MasterSpareParts> myAdapter;
	private IntentIntegrator qrScan;

	public AutoCompleteTextView spareParts;

	private static final int REQUEST_CAMERA = 2;

	private static final int SELECT_FILE = 3;

	private ImageView backButton;

	private Button addMachine, scanBarCode, nextPage;
	private String jobDetailId;

	private RelativeLayout parentLayout;

	private EditText description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.create_docket_page_machine_list);
		qrScan = new IntentIntegrator(this);

		docket_machine_list = (ListView) findViewById(R.id.docket_machine_list);
		backButton = (ImageView) findViewById(R.id.back_button);
		addMachine = (Button) findViewById(R.id.assign_machine_to_job);
		scanBarCode = (Button) findViewById(R.id.scan_machine_to_add);
		nextPage = (Button) findViewById(R.id.next_create_docket_page);
  		sparePartsArray.clear();

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

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		db = new SamgoSQLOpenHelper(getApplicationContext());

		Bundle extras = getIntent().getExtras();

		if (extras != null) {

			String jobId = extras.getString("JOBID");
			Log.e("TAG", "PART 2 Job id::" + jobId);

			Config.job_id = jobId;
			ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();
			jobDetailsArray.clear();
            sparePartsArray.clear();
			jobDetailsArray = db.getAllJobDetails(jobId);
			for (int i = 0; i < jobDetailsArray.size(); i++) {

				String machineId = jobDetailsArray.get(i).getMachineId();
				String job_id = jobDetailsArray.get(i).getJobId();
				String id = jobDetailsArray.get(i).getId();

				MachineView machineView = db.getMachineViewDataByMachineId(machineId);

				if (machineView.getMachine_master_name() != null) {

					DocketMachineDetails docketMachineDetails = new DocketMachineDetails();
					docketMachineDetails.setJobId(job_id);
					docketMachineDetails.setMachineName(machineView.getMachine_master_name());
					docketMachineDetails.setMachineModel(machineView.getMachine_model());
					docketMachineDetails.setMachineType(machineView.getMachine_type());
					docketMachineDetails.setMachineSlNo(machineView.getMachine_sl_no());
					docketMachineDetails.setMachineId(machineId);
					docketMachineDetails.setJobDetailId(id);
					docketMachineArray.add(docketMachineDetails);

					Log.e("machine",machineId+"");
					//Log.e("spare",sparePartsArray.get(i)+"");
				}

               // Log.e("Length",jobDetailsArray.get(i).getJobId());

			}
            SparePartsModel sparePartsModel = db.getSparePartsById(Config.job_id);
            sparePartsArray.add(sparePartsModel);
            for (int i=0;i<sparePartsArray.size();i++){
                Log.e("spare",sparePartsArray.get(i).getMachineId()+"");
            }


           // Log.e("Length",sparePartsArray.size()+"");

            //db.addSpareToMachine(sparePartsModel);
            docketMachineDetailsAdapter = new DocketMachineDetailsAdapter(this, docketMachineArray, sparePartsArray,
					db);
			docket_machine_list.setAdapter(docketMachineDetailsAdapter);
			docketMachineDetailsAdapter.notifyDataSetChanged();
            //sparePartsArray.clear();
		}

		addMachine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent assignMachine = new Intent(CreateDocketActivityPart2.this, AssignMachineToJob.class);
				startActivity(assignMachine);
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});

		scanBarCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				scanId = "SCAN_M";
				openCameraForScan();
			}
		});

		nextPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent gotoNextPage = new Intent(CreateDocketActivityPart2.this, CreateDocketActivityPart3.class);
				gotoNextPage.putExtra("job_id", Config.job_id);
				startActivity(gotoNextPage);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

			}
		});
	}

	public void openMachineDetails(int position) {

		Config.machineSlNo = docketMachineArray.get(position).getMachineSlNo();
		Intent machineDetails = new Intent(CreateDocketActivityPart2.this, MachineDetails.class);
		startActivity(machineDetails);
 	}

	public void openCameraForScan() {

//		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//		startActivityForResult(intent, 2);
		qrScan.setOrientationLocked(false);
		qrScan.initiateScan(IntentIntegrator.ALL_CODE_TYPES);
	}

	public void openGalleryForPickingImage(int position) {

		Config.machineSlNo = docketMachineArray.get(position).getMachineSlNo();
		jobDetailId = docketMachineArray.get(position).getJobDetailId();
		selectImage();
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(CreateDocketActivityPart2.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (items[item].equals("Take Photo")) {

					cameraIntent();

				} else if (items[item].equals("Choose from Library")) {

					galleryIntent();

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public void openPopUpForSpareParts(int position) {

		final String machine_id = docketMachineArray.get(position).getMachineId();
		final String job_id = docketMachineArray.get(position).getJobId();

		final Dialog dialog = new Dialog(CreateDocketActivityPart2.this, R.style.PauseDialog);
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
					//spareId = tv1.getText().toString();
					spareUnitSales = tv2.getText().toString();
					String[] spareDesc = tv.getText().toString().split(Pattern.quote("||"));
					description.setText(spareDesc[2]);

				}
			});

			// add the listener so it will tries to suggest while the user types
			spareParts
					.addTextChangedListener(new CustomAutoCompleteTextChangedListeners(CreateDocketActivityPart2.this));

			// ObjectItemData has no value at first
			MasterSpareParts[] ObjectItemData = new MasterSpareParts[0];

			// set the custom ArrayAdapter
			myAdapter = new SparePartsAutoCompleteAdapter(CreateDocketActivityPart2.this, R.layout.spare_parts_one_row,
					ObjectItemData);
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
                spareId="";
				dialog.dismiss();
			}
		});

		scanSpareParts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//				//intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//				startActivityForResult(intent, 2);
				scanId = "SCAN_S";
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

				final Dialog dialog1 = new Dialog(CreateDocketActivityPart2.this, R.style.TopToBottomDialog);
				dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog1.setContentView(R.layout.select_quantity_pop_up);

				ListView quantityList = (ListView) dialog1.findViewById(R.id.quantity_number);

				ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CreateDocketActivityPart2.this,
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
				//sparePartsArray.clear();
				SparePartsModel sparePartsModel = new SparePartsModel();

				String sparePartsName = spareParts.getText().toString();
				String quantityName = quantity.getText().toString();
				String descriptionName = description.getText().toString();

				sparePartsModel.setJobId(Config.job_id);
				sparePartsModel.setSpareId(spareId);
				sparePartsModel.setSparePartsId(sparePartsName);
				sparePartsModel.setDescription(descriptionName);
				sparePartsModel.setQuantity(quantityName);
				sparePartsModel.setUnitSales(spareUnitSales);
				sparePartsModel.setMachineId(machine_id);

				sparePartsArray.add(sparePartsModel);
                Log.e("jobid",job_id);
                Log.e("spareid",spareId);
                Log.e("spareUnitSales",spareUnitSales);
                Log.e("machine_id",machine_id);
				db.addSpareToMachine(sparePartsModel);

				docketMachineDetailsAdapter = new DocketMachineDetailsAdapter(CreateDocketActivityPart2.this,
						docketMachineArray, sparePartsArray, db);
				docket_machine_list.setAdapter(docketMachineDetailsAdapter);
				docketMachineDetailsAdapter.notifyDataSetChanged();

				dialog.dismiss();
			}
		});

		dialog.show();
	}
    String machine_id="";

	public void openPopUpForWorkDone(int position) {

		machine_id = docketMachineArray.get(position).getMachineId();
		final String job_id = docketMachineArray.get(position).getJobId();
		final String jobDetail_id = docketMachineArray.get(position).getJobDetailId();

		DocketDetail docketDetail = db.getDocketDetailByJobDetailId(jobDetail_id);

		final Dialog dialog = new Dialog(CreateDocketActivityPart2.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.work_done_to_machine);

		final EditText work_carried_out = (EditText) dialog.findViewById(R.id.work_carried_out);
		final EditText parts_unable_to_find = (EditText) dialog.findViewById(R.id.parts_unable_to_find);
		final RadioGroup eol = (RadioGroup) dialog.findViewById(R.id.radio_end_life);

		if (docketDetail != null) {
			work_carried_out.setText(docketDetail.getWork_carried_out());
			parts_unable_to_find.setText(docketDetail.getParts_unable_to_find());
		}

		Button saveSpare = (Button) dialog.findViewById(R.id.save_work_done);
		Button cancelSpare = (Button) dialog.findViewById(R.id.cancel_work_done);

		cancelSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		saveSpare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String work_carried = work_carried_out.getText().toString();
				String partsUnable = parts_unable_to_find.getText().toString();

				int selectedItem = eol.getCheckedRadioButtonId();

				RadioButton eolOptions = (RadioButton) dialog.findViewById(selectedItem);

				String eolStatus = eolOptions.getText().toString();

				DocketDetail docketDetail = new DocketDetail();

				docketDetail.setEol(eolStatus);
				docketDetail.setJob_id(job_id);
				docketDetail.setMachine_id(machine_id);
				docketDetail.setWork_carried_out(work_carried);
				docketDetail.setParts_unable_to_find(partsUnable);
				docketDetail.setJobdetail_id(jobDetail_id);

				int countDocket = db.getCountDocketDetail(jobDetail_id);

				if (countDocket > 0) {
					db.updateDocketDetailById(jobDetail_id, docketDetail);
				} else {
					db.addDocketDetail(docketDetail);
				}

				dialog.dismiss();

				docketMachineDetailsAdapter = new DocketMachineDetailsAdapter(CreateDocketActivityPart2.this,
						docketMachineArray, sparePartsArray, db);
				docket_machine_list.setAdapter(docketMachineDetailsAdapter);
				docketMachineDetailsAdapter.notifyDataSetChanged();
			}
		});

		dialog.show();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		//super.onActivityResult(requestCode, resultCode, data);
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

		if (requestCode == 0x0000c0de) {
			if (resultCode == Activity.RESULT_OK) {

				if (scanId == "SCAN_M") {

					String contents = result.getContents();
					String machineSlNo = db.getMachineSlNoData(contents);
					// Toast.makeText(getApplicationContext(), "machin >> " +
					// machineSlNo, Toast.LENGTH_LONG).show();

					if (machineSlNo.equalsIgnoreCase("")) {
						Toast.makeText(getApplicationContext(), "This machine is not available in your device.",
								Toast.LENGTH_LONG).show();
					} else {
						Config.machineSlNo = machineSlNo;
						MachineSiteMain machineSiteMain = db.getMachineDataListByMachineSlNo(Config.machineSlNo);
						if (machineSiteMain.getSite_id().equalsIgnoreCase(Config.site_id)) {
							Intent gotoVideoActivity = new Intent(getApplicationContext(), AssignMachineToJob.class);
							startActivity(gotoVideoActivity);
							overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
						} else {
							Toast.makeText(getApplicationContext(), "This machine does not belongs to this site.",
									Toast.LENGTH_LONG).show();
						}
					}
				}
				else if (scanId == "SCAN_S") {

					//String contents = intent.getStringExtra("SCAN_RESULT");
					//String format = data.getStringExtra("SCAN_RESULT_FORMAT");
					String contents = result.getContents();
					MasterSpareParts[] myObjs = db.getBarcodeSpareParts(contents);
					try {

						Log.e("TAG", "test >> " + myObjs[0]);
						String jointString = myObjs[0].getDescription() + "||" + myObjs[0].getQuantity() + "||"
								+ myObjs[0].getProduct_id();
						Log.e("TAG", "jointString >> " + jointString);
                        if (db.getSparePartsById(myObjs[0].getProduct_id(),machine_id,Config.job_id) == 0) {
                            spareParts.setText(jointString.toString());
                            spareId = myObjs[0].getProduct_id();
                            spareUnitSales = myObjs[0].getUnit_sales();
                            String[] spareDesc = jointString.toString().split(Pattern.quote("||"));
                            description.setText(spareDesc[2]);
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Spare parts already added", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

					} catch (Exception e) {
						// TODO: handle exception
						Toast toast = Toast.makeText(getApplicationContext(), "Machine not present in database", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
				}

//				String jointString = myObjs[0].getDescription() + "||" + myObjs[0].getQuantity() + "||"
//						+ myObjs[0].getProduct_id();
//				spareParts.setText(jointString.toString());
//				spareId = myObjs[0].getProduct_id();
//				spareUnitSales = myObjs[0].getUnit_sales();
//				String[] spareDesc = jointString.toString().split(Pattern.quote("||"));
//				description.setText(spareDesc[2]);

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
					Toast toast = Toast.makeText(getApplicationContext(),"Scan unsuccessful", Toast.LENGTH_LONG);
				Log.i("App", "Scan unsuccessful");
			}
		} else if (requestCode == SELECT_FILE) {
			if (resultCode == RESULT_OK) {

				onSelectFromGalleryResult(data);

			}
		} else if (requestCode == REQUEST_CAMERA) {
			if (resultCode == RESULT_OK) {

				onCaptureImageResult(data);

			}
		}

	}

	public void deleteMachineItem(final int position) {

		final Dialog dialog = new Dialog(CreateDocketActivityPart2.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_delete_machine);

		Button cancelCompletion = (Button) dialog.findViewById(R.id.cancel_completion);
		Button ok_completion = (Button) dialog.findViewById(R.id.ok_delete_machine);

		final String jobDetailId = docketMachineArray.get(position).getJobDetailId();

		final String machineId = docketMachineArray.get(position).getMachineId();

		final String jobId = docketMachineArray.get(position).getJobId();

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

				db.deleteJobDetailsById(jobDetailId);
				db.deleteSparePartsByMachineId(Config.docket_id,machineId);
				db.deleteDocketDetailByJobdetailId(jobDetailId);
				ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();
				jobDetailsArray.clear();
				docketMachineArray.clear();
				jobDetailsArray = db.getAllJobDetails(jobId);

				for (int i = 0; i < jobDetailsArray.size(); i++) {

					String machineId = jobDetailsArray.get(i).getMachineId();
					String job_id = jobDetailsArray.get(i).getJobId();
					String id = jobDetailsArray.get(i).getId();

					MachineView machineView = db.getMachineViewDataByMachineId(machineId);

					DocketMachineDetails docketMachineDetails = new DocketMachineDetails();
					docketMachineDetails.setJobId(job_id);
					docketMachineDetails.setMachineName(machineView.getMachine_master_name());
					docketMachineDetails.setMachineModel(machineView.getMachine_model());
					docketMachineDetails.setMachineType(machineView.getMachine_type());
					docketMachineDetails.setMachineSlNo(machineView.getMachine_sl_no());
					docketMachineDetails.setMachineId(machineId);
					docketMachineDetails.setJobDetailId(id);

					docketMachineArray.add(docketMachineDetails);
				}

				docketMachineDetailsAdapter = new DocketMachineDetailsAdapter(CreateDocketActivityPart2.this,
						docketMachineArray, sparePartsArray, db);
				docket_machine_list.setAdapter(docketMachineDetailsAdapter);
				docketMachineDetailsAdapter.notifyDataSetChanged();
			}
		});

		dialog.show();

	}

    public void deleteSpareParts(final int position) {

        final Dialog dialog = new Dialog(CreateDocketActivityPart2.this, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_pop_up_for_delete_machine);

        Button cancelCompletion = (Button) dialog.findViewById(R.id.cancel_completion);
        Button ok_completion = (Button) dialog.findViewById(R.id.ok_delete_machine);

        final String jobDetailId = docketMachineArray.get(position).getJobDetailId();

        final String machineId = docketMachineArray.get(position).getMachineId();

        final String jobId = docketMachineArray.get(position).getJobId();

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

               // db.deleteJobDetailsById(jobDetailId);
                db.deleteSparePartsByMachineId(Config.docket_id,machineId);
                //db.deleteDocketDetailByJobdetailId(jobDetailId);
                ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();
                jobDetailsArray.clear();
                docketMachineArray.clear();
				sparePartsArray.clear();
                jobDetailsArray = db.getAllJobDetails(jobId);

                for (int i = 0; i < jobDetailsArray.size(); i++) {

                    String machineId = jobDetailsArray.get(i).getMachineId();
                    String job_id = jobDetailsArray.get(i).getJobId();
                    String id = jobDetailsArray.get(i).getId();

                    MachineView machineView = db.getMachineViewDataByMachineId(machineId);
                    DocketMachineDetails docketMachineDetails = new DocketMachineDetails();
                    docketMachineDetails.setJobId(job_id);
                    docketMachineDetails.setMachineName(machineView.getMachine_master_name());
                    docketMachineDetails.setMachineModel(machineView.getMachine_model());
                    docketMachineDetails.setMachineType(machineView.getMachine_type());
                    docketMachineDetails.setMachineSlNo(machineView.getMachine_sl_no());
                    docketMachineDetails.setMachineId(machineId);
                    docketMachineDetails.setJobDetailId(id);

                    docketMachineArray.add(docketMachineDetails);
//                    SparePartsModel sparePartsModel = db.getSparePartsById(Config.docket_id,jobDetailsArray.get(i).getMachineId());
//                    sparePartsArray.add(sparePartsModel);
                }


                docketMachineDetailsAdapter = new DocketMachineDetailsAdapter(CreateDocketActivityPart2.this,
                        docketMachineArray, sparePartsArray, db);
                docket_machine_list.setAdapter(docketMachineDetailsAdapter);
                docketMachineDetailsAdapter.notifyDataSetChanged();
            }
        });

        dialog.show();

    }

	private void cameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	private void galleryIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);//
		startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
	}

	private void onSelectFromGalleryResult(Intent data) {
		Bitmap bm = null;
		if (data != null) {
			try {
				bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

				Log.e("TAG", "bit map >> " + bm);

			} catch (IOException e) {
				e.printStackTrace();
			}

			SaveIamge(bm);
		}
	}

	private void onCaptureImageResult(Intent data) {
		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
		FileOutputStream fo;
		try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SaveIamge(thumbnail);

	}

	private void SaveIamge(Bitmap finalBitmap) {

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/Samgo");
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + Config.job_id + ".jpg";
		String encoded = "";
		File file = new File(myDir, fname);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream out = new FileOutputStream(file);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
			out.flush();
			out.close();

			Toast.makeText(CreateDocketActivityPart2.this, "Picture added successfully", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

		db.addMachineImage(jobDetailId, Config.job_id, encoded);
	}
}

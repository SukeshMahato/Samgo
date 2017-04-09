package com.app.samgo;

import java.util.ArrayList;

import com.app.adapter.AddErrorCodeListAdapter;
import com.app.adapter.AddMachineSlNoListAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.ErrorCodeModel;
import com.app.model.JobDetails;
import com.app.model.MachineSiteMain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AssignMachineToJob extends Activity {

	private TextView machineSlNo, machineErrorCode;

	private EditText problem;

	private SamgoSQLOpenHelper db;

	private String machineId, jobId, errorCode;

	private Button assignMachineToJob;
	
	private ImageView backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assign_machine_to_job);

		initialisation();
		listner();

		db = new SamgoSQLOpenHelper(getApplicationContext());

		jobId = Config.job_id;
		Log.e("TAG", "Job id >>> " + jobId);

		Log.e("TAG", "Machine Sl No >>> " + Config.machineSlNo + "<<<>>>" + (Config.machineSlNo != ""));
		if (Config.machineSlNo != "") {
			MachineSiteMain machineSiteMain = db.getMachineDataListByMachineSlNo(Config.machineSlNo);
			Log.e("TAG", "Value for Id:" + machineSiteMain.getMachineName());
			String machine_name = machineSiteMain.getMachineName() + "[" + machineSiteMain.getMachineSINo() + "]";
			machineId = machineSiteMain.getMachineName_id();
			machineSlNo.setText(machine_name);
			Config.site_id = machineSiteMain.getSite_id();
		}
	}

	private void listner() {
		// TODO Auto-generated method stub
		machineSlNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated me thod stub
				Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AssignMachineToJob.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.select_machine_serial_no_popup);

				ListView machineSlNoList = (ListView) dialog.findViewById(R.id.machine_serial_no_list);

				final ArrayList<MachineSiteMain> machineSlNoArrayList = db.getMachineDataList(Config.site_id);

				// manufacturerList =
				Log.e("TAG", "Size isd >>>" + machineSlNoArrayList.size());

				AddMachineSlNoListAdapter addmachineSlNoListAdapter = new AddMachineSlNoListAdapter(
						AssignMachineToJob.this, machineSlNoArrayList);
				machineSlNoList.setAdapter(addmachineSlNoListAdapter);
				addmachineSlNoListAdapter.notifyDataSetChanged();

				machineSlNoList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						if(position >= 0){
							MachineSiteMain machineSiteMain = machineSlNoArrayList.get(position);

							Log.e("TAG", "Value for Id:" + machineSiteMain.getMachineName());

							String machine_name = machineSiteMain.getMachineName() + "[" + machineSiteMain.getMachineSINo()
									+ "]";

							machineId = machineSiteMain.getMachineName_id();

							machineSlNo.setText(machine_name);

							dialog.dismiss();
						}else{
							dialog.dismiss();
						}
						

					}
				});
				dialog.show();
			}
		});
		
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		machineErrorCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				Log.e("TAG", "clicked on");

				final Dialog dialog = new Dialog(AssignMachineToJob.this, R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.select_error_code_popup);

				ListView errorCodeList = (ListView) dialog.findViewById(R.id.error_code_list);

				final ArrayList<ErrorCodeModel> errorCodeArrayList = db.getAllErrorCodeDetails();

				// manufacturerList =
				Log.e("TAG", "Size isd >>>" + errorCodeArrayList.size());

				AddErrorCodeListAdapter addErrorCodeListAdapter = new AddErrorCodeListAdapter(AssignMachineToJob.this,
						errorCodeArrayList);
				errorCodeList.setAdapter(addErrorCodeListAdapter);
				addErrorCodeListAdapter.notifyDataSetChanged();

				errorCodeList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						if(position >= 0){
							ErrorCodeModel errorCodeModel = errorCodeArrayList.get(position);
							Log.e("TAG", "Value for Id:" + errorCodeModel.getProblem());

							machineErrorCode.setText(errorCodeModel.getProblem());

							errorCode = errorCodeModel.getErrorCode();

							problem.setText(machineErrorCode.getText().toString());
							dialog.dismiss();
						}else{
							dialog.dismiss();
						}
					}
				});
				dialog.show();

			}
		});

		assignMachineToJob.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				JobDetails jobDetails = new JobDetails();
				StringBuffer errorCodes = new StringBuffer();
				if (machineSlNo.getText().toString().equalsIgnoreCase("Select Machine Serial No.")) {
					machineSlNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					errorCodes.append("Select Machine Serial");
					errorCodes.append("\n");
				} else {
					jobDetails.setMachineId(machineId);
					jobDetails.setJobId(jobId);
				}

				if (machineErrorCode.getText().toString().equalsIgnoreCase("Select Machine Error Code")) {
					machineErrorCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					errorCodes.append("Select Machine Error Code");
					errorCodes.append("\n");
				} else {
					jobDetails.setErrorCode(errorCode);
				}

				if (errorCodes.toString().equalsIgnoreCase("")) {

					jobDetails.setProblem(problem.getText().toString());

					db.addJobDetails(jobDetails);

					Intent assignMachine = new Intent(AssignMachineToJob.this, CreateDocketActivityPart2.class);
					assignMachine.putExtra("JOBID", jobId);
					startActivity(assignMachine);
					finish();
					overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

				} else {
					Toast.makeText(AssignMachineToJob.this, errorCodes.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void initialisation() {
		// TODO Auto-generated method stub
		machineSlNo = (TextView) findViewById(R.id.machine_sr_no);

		machineErrorCode = (TextView) findViewById(R.id.machine_error_code);
		problem = (EditText) findViewById(R.id.error_problem);

		assignMachineToJob = (Button) findViewById(R.id.save_machine_to_job);
		backButton = (ImageView) findViewById(R.id.back_button);
	}

}

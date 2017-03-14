package com.app.samgo;

import java.util.ArrayList;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.Job;
import com.app.model.JobDetails;
import com.app.model.MachineView;
import com.app.model.TaskItem;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewJobActivity extends Activity {

	private ImageView backButton;
	private LinearLayout mLinearListView;
	ArrayList<TaskItem> mArrayListData = new ArrayList<TaskItem>();

	private TextView clientName_text;
	private TextView siteName_text;
	private TextView contractorName_Text;
	private TextView siteAdd_text;
	private TextView jobDate_text;
	private TextView dueDate_text;
	private TextView adminComment_text;

	private SamgoSQLOpenHelper db;
	
	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.view_job_activity);

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

		backButton = (ImageView) findViewById(R.id.back_button);
		mLinearListView = (LinearLayout) findViewById(R.id.task_details_wrapper);

		clientName_text = (TextView) findViewById(R.id.client_name_text);
		siteName_text = (TextView) findViewById(R.id.site_name_test);
		contractorName_Text = (TextView) findViewById(R.id.contractor_name_test);
		siteAdd_text = (TextView) findViewById(R.id.site_address_test);
		jobDate_text = (TextView) findViewById(R.id.jod_date_test);
		dueDate_text = (TextView) findViewById(R.id.due_date_test);
		adminComment_text = (TextView) findViewById(R.id.admin_comment_text);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			String docket_no = extras.getString("DocketNo");

			Job jobItem = db.getJobItemByDocketId(docket_no);

			siteName_text.setText(jobItem.getSiteName());
			siteAdd_text.setText(jobItem.getSiteAddress());
			clientName_text.setText(jobItem.getClient_name());
			contractorName_Text.setText(jobItem.getContractor_name());
			jobDate_text.setText(jobItem.getJob_date());
			dueDate_text.setText(jobItem.getDueDate());
			if (jobItem.getComment() == null || jobItem.getComment().equalsIgnoreCase(""))
				adminComment_text.setText("N/A");
			else
				adminComment_text.setText(jobItem.getComment());

			try {

				ArrayList<JobDetails> jobDetailsArray = new ArrayList<JobDetails>();

				jobDetailsArray.clear();

				jobDetailsArray = db.getAllJobDetails(jobItem.getId());

				for (int i = 0; i < jobDetailsArray.size(); i++) {

					String machineId = jobDetailsArray.get(i).getMachineId();

					String error_code = jobDetailsArray.get(i).getErrorCode();
					String problem = jobDetailsArray.get(i).getProblem();

					MachineView machineView = db.getMachineViewDataByMachineId(machineId);

					String machineSR = machineView.getMachine_sl_no();

					String mode_machine = db.getModelMachineFromMaster(machineId);
					Log.e("TAG", "mode_machine>>" + mode_machine);
					Log.e("TAG", "machine id>>" + machineId);
					String[] mode_machineArr = mode_machine.split(",");

					String model="";
					String machine="";
					if(mode_machineArr.length>0){
						model=mode_machineArr[0];
						machine=mode_machineArr[1];
					}
					mArrayListData
							.add(new TaskItem(machineSR, error_code, problem, model, machine));

					// mArrayListData.add(new TaskItem(machineSR, error_code,
					// problem));

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}

		if (mArrayListData.size() > 0) {
			/***
			 * adding item into listview
			 */
			for (int i = 0; i < mArrayListData.size(); i++) {
				/**
				 * inflate items/ add items in linear layout instead of listview
				 */

				Log.e("TAG", "size of i >> " + i);

				LayoutInflater inflater1 = null;
				inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View mLinearView = inflater1.inflate(R.layout.start_job_one_row_item, null);
				/**
				 * getting id of row.xml
				 */
				TextView mSerialNo = (TextView) mLinearView.findViewById(R.id.serial_no_text);
				TextView mErrorCode = (TextView) mLinearView.findViewById(R.id.error_code_text);
				TextView mDescription = (TextView) mLinearView.findViewById(R.id.description_text);

				TextView model_id = (TextView) mLinearView.findViewById(R.id.model_id);
				TextView machine_name = (TextView) mLinearView.findViewById(R.id.machine_name);

				/**
				 * set item into row
				 */
				String serialNo = mArrayListData.get(i).getSerialNo();
				String errorCode = mArrayListData.get(i).getErrorCode();
				String description = mArrayListData.get(i).getDescription();
				mSerialNo.setText(serialNo);
				mErrorCode.setText(errorCode);
				mDescription.setText(description);

				machine_name.setText(mArrayListData.get(i).getMachine_name());
				model_id.setText(mArrayListData.get(i).getModel_id());

				Log.e("TAG", "serial Number >> " + serialNo);
				Log.e("TAG", "errorCode >> " + errorCode);
				Log.e("TAG", "description >> " + description);

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
		} else {

		}

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});
	}

}

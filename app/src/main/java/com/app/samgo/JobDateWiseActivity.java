package com.app.samgo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.adapter.JobDateWiseAdapter;
import com.app.asyncs.JobListByDueDateService;
import com.app.listners.JobsListByDueDateListener;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.JobDateWisePojo;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JobDateWiseActivity extends Activity implements JobsListByDueDateListener {

	private ListView jobLists;

	private JobDateWiseAdapter jobDateWiseListsAdapter;

	private ArrayList<JobDateWisePojo> jobDataList = new ArrayList<JobDateWisePojo>();

	private TextView dateWiseHeading;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private ImageView back_button;
	
	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.job_date_wise_view);

		initialisation();

		listner();

		Bundle extras = getIntent().getExtras();

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		
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

		if (extras != null) {
			String due_date = extras.getString("SELECTEDDATE");

			String headDate = getFormattedString(due_date);

			dateWiseHeading.setText("Job list on " + headDate);

			/**
			 * Service call for job details by due date
			 */

			String eng_id = AppManager.getSinleton().getUser().getId();

			if (isInternetPresent) {

				new JobListByDueDateService(eng_id, due_date, this).execute();
			}

		}

	}

	private void listner() {
		jobLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initialisation() {
		jobLists = (ListView) findViewById(R.id.job_dashboard_list);
		dateWiseHeading = (TextView) findViewById(R.id.job_date_wise_heading);
		back_button = (ImageView) findViewById(R.id.back_button);
	}

	@Override
	public void getAllJobListByDueDateResponse(String response) {
		// TODO Auto-generated method stub
		if (response != null) {
			Log.e("TAG", "Response datewise >> " + response);
			try {

				JSONArray jobJsonArr = new JSONArray(response);

				for (int i = 0; i < jobJsonArr.length(); i++) {

					JobDateWisePojo jobItems = new JobDateWisePojo();

					String jobJsonString = jobJsonArr.getString(i);

					JSONObject jobJsonObj = new JSONObject(jobJsonString);

					/**
					 * JSON Parsing for Job Object
					 */

					String stringJob = jobJsonObj.getString("Job");

					JSONObject jobStringObj = new JSONObject(stringJob);

					String jobId = jobStringObj.getString("id");
					String jobTitle = jobStringObj.getString("title");

					/**
					 * JSON Parsing for Site Object
					 */

					String stringSite = jobJsonObj.getString("Site");

					JSONObject siteStringObj = new JSONObject(stringSite);

					String siteAddress = siteStringObj.getString("address");
					String siteName = siteStringObj.getString("name");

					/**
					 * JSON Parsing for JobStatus Object
					 */

					String stringJobStatus = jobJsonObj.getString("JobStatus");

					JSONObject jobStatusObj = new JSONObject(stringJobStatus);

					String jobStatus = jobStatusObj.getString("status");

					jobItems.setJobId(jobId);
					jobItems.setJobTitle(jobTitle);
					jobItems.setSiteAdd(siteAddress);
					jobItems.setSiteName(siteName);
					jobItems.setStatus(jobStatus);

					jobDataList.add(jobItems);

				}

				jobDateWiseListsAdapter = new JobDateWiseAdapter(this, jobDataList);
				jobLists.setAdapter(jobDateWiseListsAdapter);
				jobDateWiseListsAdapter.notifyDataSetChanged();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private String getFormattedString(String strCurrentDate) {
		String date = "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date newDate = format.parse(strCurrentDate);

			format = new SimpleDateFormat("MMM dd, yyyy");
			date = format.format(newDate);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return date;
	}

}

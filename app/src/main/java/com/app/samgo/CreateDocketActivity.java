package com.app.samgo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.Job;
import com.app.model.JobDetails;

import android.R.color;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateDocketActivity extends Activity implements OnClickListener {

	protected ImageView rightToLeftImage, leftToRightImage;

	protected TextView dateOut, timeOut, startPoint, endPoint;

	private DatePickerDialog fromDatePickerDialog;

	private SimpleDateFormat dateFormatter;

	private ImageView backButton;

	private SamgoSQLOpenHelper db;

	private Button nextButton, saveDateTime;

	private String jobId = "";

	private LinearLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_docket_page);

		rightToLeftImage = (ImageView) findViewById(R.id.right_to_left_image);
		leftToRightImage = (ImageView) findViewById(R.id.left_to_right_image);

		dateOut = (TextView) findViewById(R.id.date_out_picker);

		timeOut = (TextView) findViewById(R.id.time_out_picker);

		backButton = (ImageView) findViewById(R.id.back_button);

		startPoint = (TextView) findViewById(R.id.start_point_text);

		endPoint = (TextView) findViewById(R.id.end_point_text);

		nextButton = (Button) findViewById(R.id.save_end_time);

		saveDateTime = (Button) findViewById(R.id.save_date_time);

		db = new SamgoSQLOpenHelper(getApplicationContext());

		parentLayout = (LinearLayout) findViewById(R.id.parent_layout);

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

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String chech_in = sdf.format(date);
		SharedPreferences settings = getSharedPreferences(Config.MYFREFS, MODE_PRIVATE);

		boolean isSavedCheckIn = settings.getBoolean("check_in_time", false);

		Log.e("TAG", "Saved Check in >> " + isSavedCheckIn);

		if (isSavedCheckIn) {

		} else {

			SharedPreferences settings1 = getSharedPreferences(Config.MYFREFS, MODE_PRIVATE);
			SharedPreferences.Editor editor = settings1.edit();

			// Set "hasLoggedIn" to true
			editor.putBoolean("check_in_time", true);
			editor.putString("check_in_date_time", chech_in);

			// Commit the edits!
			editor.commit();
		}

		Bundle extras = getIntent().getExtras();

		if (extras != null) {

			String docket_no = extras.getString("DocketNo");

			Config.docket_id = docket_no;

			Log.e("TAG", "docket_no >>> " + docket_no);

			Job jobItem = db.getJobItemByDocketId(docket_no);

			Config.job_id = jobItem.getId();

			Log.e("TAG", "job id >>> " + Config.job_id);

			startPoint.setText(jobItem.getStartPlace());

			if (jobItem.getFinishPlace().equalsIgnoreCase("null")) {
				endPoint.setText("");
			} else {
				endPoint.setText(jobItem.getFinishPlace());
			}

			jobId = jobItem.getJobId();

			String taskDetails = jobItem.getTask_details();

			// Config.job_id=jobId;

			/**
			 * Insert task details into its local database
			 */

			try {

				JSONArray jArr = new JSONArray(taskDetails);

				Log.e("TAG", "TASK DETAILS Length>> " + jArr.length());

				for (int i = 0; i < jArr.length(); i++) {

					String jsonMachineDetails = jArr.getString(i);

					JSONObject jObjMachine = new JSONObject(jsonMachineDetails);

					String machineId = jObjMachine.getString("machine_id");
					String jobId = jObjMachine.getString("job_id");

					// Config.job_id = jobId;

					String errorCode = jObjMachine.getString("error_code");
					String problem = jObjMachine.getString("problem");

					JobDetails jobDetails = new JobDetails();

					jobDetails.setErrorCode(errorCode);
					jobDetails.setJobId(jobId);
					jobDetails.setMachineId(machineId);
					jobDetails.setProblem(problem);

					int countJobDetail = db.getCountForJobDetails(jobId, machineId, errorCode);

					if (countJobDetail > 0) {

					} else {
						db.addJobDetails(jobDetails);
					}

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		String curentDateTime = getDateTime();

		String[] currentString = curentDateTime.split(" ");

		dateOut.setText(currentString[0]);
		timeOut.setText(currentString[1]);

		dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

		Animation animation1 = new TranslateAnimation(0.0f, 300.0f, 0.0f, 0.0f);
		animation1.setDuration(5000);
		leftToRightImage.startAnimation(animation1);

		Animation animation4 = new TranslateAnimation(0.0f, -300.0f, 0.0f, 0.0f);
		animation4.setDuration(5000);
		rightToLeftImage.startAnimation(animation4);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});

		setDateTimeField();
		timeOut.setOnClickListener(this);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
				SharedPreferences settings = getSharedPreferences(Config.MYFREFS, MODE_PRIVATE);
				boolean isSavedCheckIn = settings.getBoolean("docket_check", false);
				String check_in = sdf.format(date);

				Log.e("TAG", "Saved Check in >> " + isSavedCheckIn);

				if (isSavedCheckIn) {

				} else {

					SharedPreferences settings1 = getSharedPreferences(Config.MYFREFS, MODE_PRIVATE);
					SharedPreferences.Editor editor = settings1.edit();

					// Set "hasLoggedIn" to true
					editor.putBoolean("check_in_time", true);
					editor.putString("check_in_date_time", check_in);

					// Commit the edits!
					editor.commit();
				}

				Intent nextPage = new Intent(CreateDocketActivity.this, CreateDocketActivityPart2.class);
				nextPage.putExtra("JOBID", Config.job_id);
				startActivity(nextPage);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

			}
		});

		saveDateTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (jobId.equalsIgnoreCase("")) {

				} else {

					String end_date = dateOut.getText().toString();
					String end_time = timeOut.getText().toString();

					String end_date_time = end_date + " " + end_time;

					db.updateJobStatus(jobId);

					db.updateStartJobTable(jobId, end_date_time);

					Intent nextPage = new Intent(CreateDocketActivity.this, CreateDocketActivityPart2.class);
					nextPage.putExtra("JOBID", Config.job_id);
					startActivity(nextPage);
					overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}

	private void setDateTimeField() {
		dateOut.setOnClickListener(this);

		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(year, monthOfYear, dayOfMonth);
				dateOut.setText(dateFormatter.format(newDate.getTime()));
			}

		}, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == dateOut) {
			fromDatePickerDialog.show();
		}
		if (view == timeOut) {
			CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(CreateDocketActivity.this,
					R.style.DialogTheme, timeSetListener, Calendar.getInstance().get(Calendar.HOUR),
					CustomTimePickerDialog.getRoundedMinute(
							Calendar.getInstance().get(Calendar.MINUTE) + CustomTimePickerDialog.TIME_PICKER_INTERVAL),
					true);
			// timePickerDialog.setTitle("Set hours and minutes");
			timePickerDialog.show();
		}
	}

	public static class CustomTimePickerDialog extends TimePickerDialog {

		public static final int TIME_PICKER_INTERVAL = 15;
		private boolean mIgnoreEvent = false;

		public CustomTimePickerDialog(Context context, int style, OnTimeSetListener callBack, int hourOfDay, int minute,
				boolean is24HourView) {
			super(context, style, callBack, hourOfDay, minute, is24HourView);
		}

		@Override
		public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
			super.onTimeChanged(timePicker, hourOfDay, minute);
			if (!mIgnoreEvent) {
				minute = getRoundedMinute(minute);
				mIgnoreEvent = true;
				timePicker.setCurrentMinute(minute);
				mIgnoreEvent = false;
			}
		}

		public static int getRoundedMinute(int minute) {
			if (minute % TIME_PICKER_INTERVAL != 0) {
				int minuteFloor = minute - (minute % TIME_PICKER_INTERVAL);
				minute = minuteFloor + (minute == minuteFloor + 1 ? TIME_PICKER_INTERVAL : 0);
				if (minute == 60)
					minute = 0;
			}

			return minute;
		}
	}

	private CustomTimePickerDialog.OnTimeSetListener timeSetListener = new CustomTimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			timeOut.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute) + ":" + "00");
		}
	};

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

}

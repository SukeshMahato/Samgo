package com.app.fragment;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.samgo.MainActivity;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentDeleteData extends Fragment {

	View v;

	private Button delete_data;

	private LinearLayout parentLayout;

	private ProgressDialog progressBar;

	private Handler handler;

	private SamgoSQLOpenHelper db;
	
	public static final String PREFS_NAME = "MyPrefsFile";

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	private ConnectionDetector cd;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.delete_activity, container, false);

		// get Internet status

		// creating connection detector class instance
		cd=new ConnectionDetector(getContext());
		
		isInternetPresent = cd.isConnectingToInternet();
		
		db = new SamgoSQLOpenHelper(getContext());

		initialisation();
		listner();

		handler = new Handler();

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

		return v;
	}

	private void initialisation() {
		// TODO Auto-generated method stub
		delete_data = (Button) v.findViewById(R.id.delete_data);
		parentLayout = (LinearLayout) v.findViewById(R.id.parent_layout);
	}

	private void listner() {
		delete_data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				

				if (isInternetPresent) {
					db.deletePreviousData();
					SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					
					// Set "TodayJob" to test
					editor.putString("TodayJobDate", null);

					// Commit the edits!
					editor.commit();
					
					// Set "TomorrowJob" to test
					editor.putString("TomorrowJobDate", null);

					// Commit the edits!
					editor.commit();
					Intent startDashboard = new Intent(getActivity(), MainActivity.class);
					startActivity(startDashboard);
				} else {
					Toast toast = Toast.makeText(getActivity(),
							"Please go online to delete data and fetch updated data...", Toast.LENGTH_LONG);
					toast.show();
				}
				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Deleting your previous data. please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
					}
				}, 3000); // 3000 milliseconds
			}
		});
	}

}

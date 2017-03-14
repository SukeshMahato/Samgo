package com.app.fragment;

import com.app.database.SamgoSQLOpenHelper;
import com.app.samgo.LoginActivity;
import com.app.samgo.MainActivity;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentLogout extends Fragment {

	View v;

	private ProgressDialog progressBar;

	private Handler handler;

	private SamgoSQLOpenHelper db;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	private ConnectionDetector cd;

	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.logout_activity, container, false);

		// get Internet status

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		isInternetPresent = cd.isConnectingToInternet();

		db = new SamgoSQLOpenHelper(getContext());

		initialisation();
		listner();

		handler = new Handler();

		if (isInternetPresent) {
			db.deleteAllDataLogout();
			SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();

			// Set "hasLoggedIn" to true
			editor.putBoolean("hasLoggedIn", false);

			// Commit the edits!
			editor.commit();

			// Set "TodayJob" to test
			editor.putString("TodayJobDate", null);

			// Commit the edits!
			editor.commit();

			// Set "TomorrowJob" to test
			editor.putString("TomorrowJobDate", null);

			// Commit the edits!
			editor.commit();
			Intent startDashboard = new Intent(getActivity(), LoginActivity.class);
			startActivity(startDashboard);
		} else {
			Toast toast = Toast.makeText(getActivity(), "Please go online to logout...", Toast.LENGTH_LONG);
			toast.show();
			Intent startDashboard = new Intent(getActivity(), MainActivity.class);
			startActivity(startDashboard);
		}
		progressBar = new ProgressDialog(getContext());
		progressBar.setCancelable(true);
		progressBar.setMessage("Loging out ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();

		handler.postDelayed(new Runnable() {
			public void run() {
				progressBar.dismiss();
			}
		}, 3000); // 3000 milliseconds

		return v;
	}

	private void initialisation() {
		// TODO Auto-generated method stub
	}

	private void listner() {
		// TODO Auto-generated method stub
	}

}

package com.app.fragment;

import com.app.model.Config;
import com.app.samgo.R;

import android.R.color;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentChangeTheme extends Fragment {

	View v;

	private LinearLayout firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, parentLayout;

	private ProgressDialog progressBar;

	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.change_layout_theme, container, false);

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
		firstLayout = (LinearLayout) v.findViewById(R.id.first_background);
		secondLayout = (LinearLayout) v.findViewById(R.id.second_background);
		thirdLayout = (LinearLayout) v.findViewById(R.id.third_background);
		fourthLayout = (LinearLayout) v.findViewById(R.id.fourth_background);
		fifthLayout = (LinearLayout) v.findViewById(R.id.fifth_background);
		parentLayout = (LinearLayout) v.findViewById(R.id.parent_layout);
	}

	private void listner() {
		firstLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences settings1 = getActivity().getSharedPreferences(Config.MYFREFS, 0);
				SharedPreferences.Editor editor = settings1.edit();

				// Set "hasLoggedIn" to true
				editor.putString("background_image", "background_1");

				// Commit the edits!
				editor.commit();

				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Applying new theme please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
						parentLayout.setBackgroundResource(R.drawable.background_1);
					}
				}, 3000); // 3000 milliseconds
			}
		});
		secondLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences settings1 = getActivity().getSharedPreferences(Config.MYFREFS, 0);
				SharedPreferences.Editor editor = settings1.edit();

				// Set "hasLoggedIn" to true
				editor.putString("background_image", "background_2");

				// Commit the edits!
				editor.commit();

				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Applying new theme please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
						parentLayout.setBackgroundResource(R.drawable.background_2);
					}
				}, 3000); // 3000 milliseconds
			}
		});
		thirdLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences settings1 = getActivity().getSharedPreferences(Config.MYFREFS, 0);
				SharedPreferences.Editor editor = settings1.edit();

				// Set "hasLoggedIn" to true
				editor.putString("background_image", "background_3");

				// Commit the edits!
				editor.commit();

				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Applying new theme please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
						parentLayout.setBackgroundResource(R.drawable.background_3);
					}
				}, 3000); // 3000 milliseconds
			}
		});
		fourthLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences settings1 = getActivity().getSharedPreferences(Config.MYFREFS, 0);
				SharedPreferences.Editor editor = settings1.edit();

				// Set "hasLoggedIn" to true
				editor.putString("background_image", "background_4");

				// Commit the edits!
				editor.commit();

				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Applying new theme please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
						parentLayout.setBackgroundResource(R.drawable.background_4);
					}
				}, 3000); // 3000 milliseconds
			}
		});
		fifthLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences settings1 = getActivity().getSharedPreferences(Config.MYFREFS, 0);
				SharedPreferences.Editor editor = settings1.edit();

				// Set "hasLoggedIn" to true
				editor.putString("background_image", "background_5");

				// Commit the edits!
				editor.commit();

				progressBar = new ProgressDialog(getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("Applying new theme please wait ...");
				progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressBar.show();

				handler.postDelayed(new Runnable() {
					public void run() {
						progressBar.dismiss();
						parentLayout.setBackgroundColor(color.white);
					}
				}, 3000); // 3000 milliseconds
			}
		});
	}

}

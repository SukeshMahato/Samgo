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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentIconsLegion extends Fragment {

	View v;

	protected TextView detail_header;

	protected LinearLayout parentLayout;

	protected ProgressDialog progressBar;

	protected Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.icons_layout, container, false);
		
		initialisation();
		listner();

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

	private void listner() {
		// TODO Auto-generated method stub

	}

	private void initialisation() {
		// TODO Auto-generated method stub
		detail_header = (TextView) v.findViewById(R.id.detail_header);
		parentLayout = (LinearLayout) v.findViewById(R.id.parent_layout);

	}

}

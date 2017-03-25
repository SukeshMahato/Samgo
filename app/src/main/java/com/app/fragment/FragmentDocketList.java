package com.app.fragment;

import java.util.ArrayList;

import com.app.adapter.DocketListsAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.DocketList;
import com.app.samgo.CreateDocketActivity;
import com.app.samgo.R;

import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FragmentDocketList extends Fragment {

	View v;

	private ListView docketList;

	private DocketListsAdapter docketListAdapter;

	private ArrayList<DocketList> docketDataList = new ArrayList<DocketList>();

	private SamgoSQLOpenHelper db;
	
	private RelativeLayout parentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_docket_lists, container, false);

		this.initialisation();

		this.listner();

		db = new SamgoSQLOpenHelper(getContext());
		
		parentLayout = (RelativeLayout) v.findViewById(R.id.parent_layout);

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

		docketDataList.clear();

		docketDataList = db.getDocketDetails();
        Log.e("docketno",docketDataList.get(0).getJob_id()+"");

		docketListAdapter = new DocketListsAdapter(getActivity(), docketDataList, FragmentDocketList.this, db);
		docketList.setAdapter(docketListAdapter);
		docketListAdapter.notifyDataSetChanged();

		return v;
	}

	private void listner() {
		// TODO Auto-generated method stub
		docketList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		docketList = (ListView) v.findViewById(R.id.docket_list);
	}

	public void openDocketListPage(int position) {
		String docket_no = docketDataList.get(position).getJob_id();
        Config.docket_id = docket_no;
        Log.e("docketno",docket_no);


//        Config.site_id = site_id;
//        Config.client_id = client_id;
//        Config.docket_id = docket_no;
		Intent goToDocketList = new Intent(getContext(), CreateDocketActivity.class);
		goToDocketList.putExtra("DocketNo", docket_no);
		startActivity(goToDocketList);

	}

}

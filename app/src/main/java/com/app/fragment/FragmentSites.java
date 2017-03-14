package com.app.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.SiteListsAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.SiteListListener;
import com.app.model.Config;
import com.app.model.MachineDetails;
import com.app.model.Site;
import com.app.samgo.AddMachineActivity;
import com.app.samgo.R;
import com.app.samgo.ViewMapActivity;
import com.app.samgo.ViewSiteActivity;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentSites extends Fragment implements SiteListListener {

	View v;

	private ListView siteLists;

	private SiteListsAdapter siteListsAdapter;

	private ArrayList<Site> siteDataList = new ArrayList<Site>();

	protected TextView searchBy;

	private EditText searchHere;
	private ArrayList<Site> siteDataList2 = new ArrayList<Site>();

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private SamgoSQLOpenHelper db;
	
	private RelativeLayout parentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.fragment_site_lists, container, false);

		this.initialisation();

		this.listner();

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

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

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		/*
		 * if (isInternetPresent) { new SiteDetailsServices(this,
		 * AppManager.getSinleton().getUser().getId(), getActivity()).execute();
		 * } else {
		 */

		siteDataList.clear();
		siteDataList2.clear();
		ArrayList<Site> getAllSiteList = db.getSiteLIstDetails();

		for (int i = 0; i < getAllSiteList.size(); i++) {
			Site site = getAllSiteList.get(i);

			siteDataList.add(site);
			siteDataList2.add(site);
		}
		siteListsAdapter = new SiteListsAdapter(FragmentSites.this, getActivity(), siteDataList);
		siteLists.setAdapter(siteListsAdapter);
		siteListsAdapter.notifyDataSetChanged();
		/* } */

		return v;
	}

	private void listner() {
		siteLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		siteLists.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * searchBy.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub
		 * 
		 * Log.e("TAG", "clicked on");
		 * 
		 * final Dialog dialog = new Dialog(getContext(),
		 * R.style.TopToBottomDialog);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.custom_search_dialog_for_sites);
		 * 
		 * TextView site_name = (TextView) dialog.findViewById(R.id.site_name);
		 * TextView site_city = (TextView) dialog.findViewById(R.id.site_city);
		 * TextView site_county = (TextView)
		 * dialog.findViewById(R.id.site_count);
		 * 
		 * site_name.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Site Name"); dialog.dismiss(); } });
		 * 
		 * site_city.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Site City"); dialog.dismiss(); } });
		 * 
		 * site_county.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Site County"); dialog.dismiss(); } });
		 * 
		 * dialog.show(); } });
		 */

		searchHere.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String searchString = searchHere.getText().toString().toLowerCase();
				int textLength = searchString.length();

				// if (searchBy.getText().toString().contains("Search By")) {

				// } else {
				siteDataList.clear();

				for (int i = 0; i < siteDataList2.size(); i++) {

					Site SiteItem = new Site();

					String siteCity = siteDataList2.get(i).getCity();
					String siteName = siteDataList2.get(i).getName();
					String siteCounty = siteDataList2.get(i).getCounty();

					// if (searchBy.getText().toString().contains("Site City"))
					// {

					if (textLength <= siteCity.length()) {

						if (siteCity.toLowerCase().contains(searchString)
								|| siteName.toLowerCase().contains(searchString)
								|| siteCounty.toLowerCase().contains(searchString)) {

							SiteItem.setCity(siteCity);
							SiteItem.setCounty(siteCounty);
							SiteItem.setName(siteName);

							siteDataList.add(SiteItem);
							// }

							// }

						} /*
							 * else if (searchBy.getText().toString().contains(
							 * "Site Name")) {
							 * 
							 * if (textLength <= siteName.length()) {
							 * 
							 * if
							 * (siteName.toLowerCase().contains(searchString)) {
							 * 
							 * SiteItem.setCity(siteCity);
							 * SiteItem.setCounty(siteCounty);
							 * SiteItem.setName(siteName);
							 * 
							 * siteDataList.add(SiteItem); }
							 * 
							 * }
							 * 
							 * } else if
							 * (searchBy.getText().toString().contains(
							 * "Site Count")) {
							 * 
							 * if (textLength <= siteCounty.length()) {
							 * 
							 * if
							 * (siteCounty.toLowerCase().contains(searchString))
							 * {
							 * 
							 * SiteItem.setCity(siteCity);
							 * SiteItem.setCounty(siteCounty);
							 * SiteItem.setName(siteName);
							 * 
							 * siteDataList.add(SiteItem); }
							 * 
							 * }
							 * 
							 * }
							 */

					}

				}

				siteListsAdapter = new SiteListsAdapter(FragmentSites.this, getActivity(), siteDataList);
				siteLists.setAdapter(siteListsAdapter);
				siteListsAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		siteLists = (ListView) v.findViewById(R.id.site_list);
		//searchBy = (TextView) v.findViewById(R.id.search_by);
		searchHere = (EditText) v.findViewById(R.id.search_here);
	}

	@Override
	public void getAllSiteListResponse(String response) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Log.e("TAG", "Result Sites ??? >> " + response);

		if (response != null) {
			try {
				JSONArray jArr = new JSONArray(response);
				siteDataList.clear();
				siteDataList2.clear();
				db.deletesiteListAllJobRecords();

				db.deleteMachineDetailsRecords();

				for (int i = 0; i < jArr.length(); i++) {
					Site site = new Site();
					String completeString = jArr.getString(i);
					JSONObject jObj = new JSONObject(completeString);
					String siteString = jObj.getString("Site");
					JSONObject siteObj = new JSONObject(siteString);

					String id = siteObj.getString("id");
					site.setId(id);

					String name = siteObj.getString("name");
					site.setName(name);
					String county = siteObj.getString("county");
					site.setCounty(county);
					String city = siteObj.getString("city");
					site.setCity(city);
					String latitude = siteObj.getString("latitude");
					String longitude = siteObj.getString("longitude");
					String photo_path = siteObj.getString("photo_path");
					String address = siteObj.getString("address");
					String clientString = siteObj.getString("Client");
					JSONObject clientObj = new JSONObject(clientString);
					String clientName = clientObj.getString("first_name");
					String contractorString = siteObj.getString("Contractor");

					String contractor_name = "";
					Log.e("TAG", "contractor >>> " + contractorString);

					Object json = new JSONTokener(contractorString).nextValue();
					if (json instanceof JSONObject) {
						Log.e("TAG", "contractor >>> " + contractorString);
						JSONObject contractorObj = new JSONObject(contractorString);

						contractor_name = contractorObj.getString("name");
					} else if (json instanceof JSONArray) {
						Log.e("TAG", "contractor >>> " + contractorString);

						contractor_name = "N/A";
					}

					JSONArray jArrMachine = new JSONArray(siteObj.getString("Machine"));

					for (int j = 0; j < jArrMachine.length(); j++) {

						JSONObject jObjMachine = new JSONObject(jArrMachine.getString(j));

						String site_id = jObjMachine.getString("site_id");

						String machine_si_no = jObjMachine.getString("machine_serial_no");
						String machine_name = jObjMachine.getString("machine_name");
						String machine_type = jObjMachine.getString("machine_type_id");
						String manufacturer = jObjMachine.getString("machine_manufacturer_id");
						String machine_model = jObjMachine.getString("model_id");
						String machine_added_by = jObjMachine.getString("added_by");
						String voltage = jObjMachine.getString("voltage");
						String suction = jObjMachine.getString("suction");
						String traction = jObjMachine.getString("traction");
						String water = jObjMachine.getString("water");
						String mfg_year = jObjMachine.getString("mfg_year");

						MachineDetails machineDetails = new MachineDetails(manufacturer, machine_type, machine_model,
								machine_name, machine_si_no, machine_added_by, voltage, suction, traction, water,
								mfg_year);

						int count = db.getMachineCountBySerialNo(machine_si_no);
						if (count > 0) {

						} else {
							db.addMachineDetails(machineDetails, site_id);
						}
					}

					site.setClientName(clientName);
					site.setLatitude(latitude);
					site.setLongitude(longitude);
					site.setPhotoPath(photo_path);
					site.setSiteAddress(address);
					site.setContractorName(contractor_name);

					db.addSite(site);

					siteDataList.add(site);
					siteDataList2.add(site);

				}
				siteListsAdapter = new SiteListsAdapter(FragmentSites.this, getActivity(), siteDataList);
				siteLists.setAdapter(siteListsAdapter);
				siteListsAdapter.notifyDataSetChanged();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void openSiteLocationActivity(int position) {
		// TODO Auto-generated method stub
		String longitude = siteDataList.get(position).getLongitude();
		String latitude = siteDataList.get(position).getLatitude();
		String address = siteDataList.get(position).getSiteAddress();

		Intent startSite = new Intent(getContext(), ViewMapActivity.class);

		startSite.putExtra("longitude", longitude);
		startSite.putExtra("latitude", latitude);
		startSite.putExtra("address", address);

		Log.e("TAG", "Longitude>>" + longitude);
		Log.e("TAG", "latitude>>" + latitude);
		startActivity(startSite);

	}

	public void openAddMachineSiteActivity(int position) {
		String site_id = siteDataList.get(position).getId();

		Intent gotoViewSite = new Intent(getContext(), AddMachineActivity.class);

		gotoViewSite.putExtra("site_id", site_id);

		startActivity(gotoViewSite);

		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
	}

	public void openViewSiteActivity(int position) {
		// TODO Auto-generated method stub
		String clientName = siteDataList.get(position).getClientName();
		String contractorName = siteDataList.get(position).getContractorName();
		String siteName = siteDataList.get(position).getName();
		String siteAddress = siteDataList.get(position).getSiteAddress();
		String site_id = siteDataList.get(position).getId();

		Intent startSite = new Intent(getContext(), ViewSiteActivity.class);

		startSite.putExtra("clientName", clientName);
		startSite.putExtra("contractorName", contractorName);
		startSite.putExtra("siteName", siteName);
		startSite.putExtra("siteAddress", siteAddress);
		startSite.putExtra("site_id", site_id);

		Log.e("TAG", "clientName>>" + clientName);
		Log.e("TAG", "contractorName>>" + contractorName);
		Log.e("TAG", "siteName>>" + siteName);
		Log.e("TAG", "siteAddress>>" + siteAddress);
		Log.e("TAG", "siteId >>> " + site_id);

		startActivity(startSite);
	}

}

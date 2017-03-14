package com.app.samgo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewMapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;

	private ImageView backButton;
	private String longitude;
	private String latitude;
	private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_site_location);

		backButton = (ImageView) findViewById(R.id.back_button);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			longitude = extras.getString("longitude");
			latitude = extras.getString("latitude");
			address = extras.getString("address");

			try {
				// Loading map
				initilizeMap(longitude, latitude, address);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 */
	private void initilizeMap(String longitude, String latitude, String address) {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			Log.e("TAG", "Latitude Azizul:" + longitude);
			Log.e("TAG", "Longitude Azizul:" + latitude);

			// latitude and longitude
			double Latitude = Double.parseDouble(latitude);
			double Longitude = Double.parseDouble(longitude);

			CameraPosition INIT = new CameraPosition.Builder().target(new LatLng(Latitude, Longitude)).zoom(15.5F)
					.bearing(300F) // orientation
					.tilt(50F) // viewing angle
					.build();

			// use map to move camera into position
			googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

			// create marker
			Marker marker = googleMap
					.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Google Maps"));

			// ROSE color icon
			marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

			marker.setSnippet(address);

			marker.showInfoWindow();

			// adding marker
			// googleMap.addMarker(marker);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap(longitude, latitude, address);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}

}

package com.app.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.Toast;

public class GPSLocationService extends Service {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 40000; // in
	// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 100000; // in
	// Milliseconds
	protected LocationManager locationManager;
	protected Button retrieveLocationButton;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "GPS Service created ...", Toast.LENGTH_LONG).show();
		try {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
					MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			float latitude = (float) location.getLatitude();
			float longitude = (float) location.getLongitude();

			if (location != null) {
				String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", longitude,
						latitude);
				Toast.makeText(GPSLocationService.this, message, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "GPS Service destroyed ...", Toast.LENGTH_LONG).show();
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(),
					location.getLatitude());
			Toast.makeText(GPSLocationService.this, message, Toast.LENGTH_LONG).show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(GPSLocationService.this, "Provider status changed", Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(GPSLocationService.this, "Provider disabled by the user. GPS turned off", Toast.LENGTH_LONG)
					.show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(GPSLocationService.this, "Provider enabled by the user. GPS turned on", Toast.LENGTH_LONG)
					.show();
		}
	}

}

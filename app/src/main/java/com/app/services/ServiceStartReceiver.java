package com.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		Intent dailyUpdater = new Intent(context, GetSparePartsService.class);
//		context.startService(dailyUpdater);
		Log.e("AlarmReceiver", "Called context.GetSparePartsService from AlarmReceiver.onReceive");
	}

}

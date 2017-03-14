package com.app.fragment;

import com.app.model.Config;
import com.app.samgo.R;

import android.app.Activity;
//import com.google.zxing.client.android.captureActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentScanQrCode extends Fragment {

	View v;

	Button b1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.scan_qr_code, container, false);

		b1 = (Button) v.findViewById(R.id.button1);

		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, Config.REQUEST_SCAN);
			}
		});
		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		Toast.makeText(getContext(), "requestCode in frag >> " + requestCode, Toast.LENGTH_LONG).show();

		super.onActivityResult(requestCode, resultCode, intent);

		Toast.makeText(getContext(), "requestCode in frag 2>> " + requestCode, Toast.LENGTH_LONG).show();

		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {

				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				Toast.makeText(getContext(), "content >> " + contents, Toast.LENGTH_LONG).show();
				Toast.makeText(getContext(), "format >> " + format, Toast.LENGTH_LONG).show();
				// Handle successful scan

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
				Log.i("App", "Scan unsuccessful");
			}
		}
	}

}
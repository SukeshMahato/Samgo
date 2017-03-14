package com.app.asyncs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.app.listners.TomorrowJobsListener;
import com.app.model.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

public class TomorrowJobServices extends AsyncTask<Void, Void, String> {

	private Activity mContext;

	private TomorrowJobsListener callback;
	private String engineerId;
	private ProgressDialog progressBar;

	public TomorrowJobServices(Fragment frag, String engineerId, Activity mContext) {
		// TODO Auto-generated constructor stub

		this.mContext = mContext;

		this.callback = (TomorrowJobsListener) frag;
		this.engineerId = engineerId;

		Log.e("TAG", "Tommorow >>> " + engineerId);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar = new ProgressDialog(mContext);
		progressBar.setCancelable(true);
		progressBar.setMessage("Please wait ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();

	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			URL url = new URL(Config.base_url + "engservices/eng_tomorrows_job");

			List<NameValuePair> loginData = new ArrayList<NameValuePair>();
			loginData.add(new BasicNameValuePair("engineer_id", engineerId));
			Log.e("engineer_id", engineerId);

			HttpURLConnection conection = (HttpURLConnection) url.openConnection();
			conection.setReadTimeout(10000);
			conection.setConnectTimeout(15000);
			conection.setRequestMethod("POST");
			conection.setDoInput(true);
			conection.setDoOutput(true);

			OutputStream os = conection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(loginData));
			writer.flush();
			writer.close();
			os.close();

			conection.connect();

			int responseCode = conection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				String line = "";
				StringBuilder responseOutput = new StringBuilder();
				System.out.println("output===============" + br);
				while ((line = br.readLine()) != null) {
					responseOutput.append(line);
				}
				br.close();
				response = responseOutput.toString();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;
	}

	@Override
	protected void onPostExecute(String RequestMessage) {
		// TODO Auto-generated method stub
		super.onPostExecute(RequestMessage);
		progressBar.dismiss();
		try {
			callback.getTomorrowJobResponse(RequestMessage);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            Log.e("name",pair.getName());
            Log.e("value",pair.getValue());

		}

		return result.toString();
	}

}

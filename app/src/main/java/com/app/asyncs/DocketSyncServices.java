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

import com.app.listners.DocketSyncListner;
import com.app.model.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class DocketSyncServices extends AsyncTask<Void, Void, String> {

	private ProgressDialog progressBar;

	private DocketSyncListner callback;

	private Activity activity;

	String data, jobDetailData, docketDetailData, sparePartsData, trainingData, jobStatusData;

	// private ProfileSaveListener callback;

	public DocketSyncServices(Activity activity, String data, String jobDetailData, String docketDetailData,
			String sparePartsData, String trainingData, String jobStatusData, Fragment frag) {
		// TODO Auto-generated constructor stub

		this.activity = activity;
		this.data = data;
		this.jobDetailData = jobDetailData;
		this.docketDetailData = docketDetailData;
		this.sparePartsData = sparePartsData;
		this.trainingData = trainingData;
		this.jobStatusData = jobStatusData;
		this.callback = (DocketSyncListner) frag;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar = new ProgressDialog(activity);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please wait while we are syncing your data ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			URL url = new URL(Config.base_url + "engservices/eng_sync_docket");

			List<NameValuePair> profileData = new ArrayList<NameValuePair>();
			profileData.add(new BasicNameValuePair("sync_docket", data));
			profileData.add(new BasicNameValuePair("sync_job_details", jobDetailData));
			profileData.add(new BasicNameValuePair("sync_docket_details", docketDetailData));
			profileData.add(new BasicNameValuePair("sync_spare_details", sparePartsData));
			profileData.add(new BasicNameValuePair("sync_training", trainingData));
			profileData.add(new BasicNameValuePair("sync_job_status", jobStatusData));

			HttpURLConnection conection = (HttpURLConnection) url.openConnection();
			conection.setReadTimeout(10000);
			conection.setConnectTimeout(30000);
			conection.setRequestMethod("POST");
			conection.setDoInput(true);
			conection.setDoOutput(true);

			OutputStream os = conection.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(profileData));
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
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressBar.dismiss();
		try {
			callback.getSyncSuccess(result);
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
		}

		return result.toString();
	}

}

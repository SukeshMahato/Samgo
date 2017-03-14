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

import com.app.listners.ProfileSaveListener;
import com.app.model.Config;
import com.app.model.SiteEnggModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

public class ProfileSaveServices extends AsyncTask<Void, Void, String> {

	private ProgressDialog progressBar;
	private String decodedPic, enggId;

	private Activity activity;
	SiteEnggModel siteEnggModel;

	private ProfileSaveListener callback;

	public ProfileSaveServices(SiteEnggModel siteEnggModel, String enggId, String image_data, Fragment mContext,
			Activity activity) {
		// TODO Auto-generated constructor stub
		
		Log.e("call", "calling service >> ");

		this.decodedPic = image_data;
		this.activity = activity;
		this.siteEnggModel = siteEnggModel;
		this.enggId = enggId;

		this.callback = (ProfileSaveListener) mContext;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar = new ProgressDialog(activity);
		progressBar.setCancelable(true);
		progressBar.setMessage("Saving Data ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			URL url = new URL(Config.base_url + "engservices/eng_profile_update");

			List<NameValuePair> profileData = new ArrayList<NameValuePair>();
			
			//Log.e("call", "request string  >> " + enggId + "," + siteEnggModel.getFirstName() + "," +siteEnggModel.getFirstName() + "," );

			profileData.add(new BasicNameValuePair("engineer_id", enggId));
			profileData.add(new BasicNameValuePair("first_name", siteEnggModel.getFirstName()));
			profileData.add(new BasicNameValuePair("last_name", siteEnggModel.getLastName()));
			profileData.add(new BasicNameValuePair("mob_no", siteEnggModel.getMobNo()));
			profileData.add(new BasicNameValuePair("shift_start", siteEnggModel.getShiftStart()));
			profileData.add(new BasicNameValuePair("shift_end", siteEnggModel.getShiftEnd()));
			profileData.add(new BasicNameValuePair("country_id", siteEnggModel.getCountryId()));
			profileData.add(new BasicNameValuePair("county_name", siteEnggModel.getCountyName()));
			profileData.add(new BasicNameValuePair("city_name", siteEnggModel.getCityName()));
			profileData.add(new BasicNameValuePair("address", siteEnggModel.getAddress()));
			profileData.add(new BasicNameValuePair("latitude", siteEnggModel.getLatitude()));
			profileData.add(new BasicNameValuePair("longitude", siteEnggModel.getLogitude()));
			profileData.add(new BasicNameValuePair("photo", decodedPic));

			HttpURLConnection conection = (HttpURLConnection) url.openConnection();
			conection.setReadTimeout(10000);
			conection.setConnectTimeout(15000);
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
			
			Log.e("call", "responseCode >> " + responseCode);

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
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressBar.dismiss();
		try {
			callback.getUpdateProfileSuccess(result);
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
		Log.e("TAG", "Request Data ..." + result.toString());
		return result.toString();
	}

}

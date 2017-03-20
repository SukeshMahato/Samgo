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

import com.app.listners.AddMachineMasterListener;
import com.app.model.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public class MachineMasterServices extends AsyncTask<Void, Void, String> {

	private Activity mContext;

	private AddMachineMasterListener callback;
	private String limit;
	private String start_id;
	private ProgressDialog progressBar;

	public MachineMasterServices(String start_id, String limit, Fragment fragment, Activity mContext) {
		// TODO Auto-generated constructor stub
		this.start_id=start_id;
		this.mContext = mContext;
		this.callback = (AddMachineMasterListener) fragment;
		this.limit = limit;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar = new ProgressDialog(mContext);
		progressBar.setCancelable(false);
		progressBar.setMessage("Please Wait it will take few minutes ...");
		progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressBar.show();
		
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response = "";
		try {
			URL url = new URL(Config.base_url + "engservices/eng_machine_master_list");

			List<NameValuePair> loginData = new ArrayList<NameValuePair>();
			loginData.add(new BasicNameValuePair("limit", limit));
			loginData.add(new BasicNameValuePair("start_id", start_id));

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

	/* @Override
	   protected void onProgressUpdate(Integer... values) {
	      super.onProgressUpdate(values);
	      progressBar.setProgress(values[0]);
	   }*/
	
	@Override
	protected void onPostExecute(String requestMessage) {
		// TODO Auto-generated method stub
		super.onPostExecute(requestMessage);
//		try{
//			Thread.sleep(5000);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		progressBar.dismiss();
		try {
			callback.getAllMachineMasterListResponse(requestMessage);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

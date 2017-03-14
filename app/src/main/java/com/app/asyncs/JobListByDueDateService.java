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

import com.app.listners.JobsListByDueDateListener;
import com.app.model.Config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class JobListByDueDateService extends AsyncTask<Void, Void, String> {

	private Activity mContext;

	private JobsListByDueDateListener callback;
	private String engineerId, due_date;
	private ProgressDialog progressBar;

	public JobListByDueDateService(String engineerId, String due_date, Activity mContext) {
		// TODO Auto-generated constructor stub

		this.mContext = mContext;

		this.callback = (JobsListByDueDateListener) mContext;
		this.engineerId = engineerId;
		this.due_date = due_date;
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
			URL url = new URL(Config.base_url + "engservices/eng_job_by_due_date");

			List<NameValuePair> loginData = new ArrayList<NameValuePair>();
			loginData.add(new BasicNameValuePair("engineer_id", engineerId));
			loginData.add(new BasicNameValuePair("due_date", due_date));

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
	protected void onPostExecute(String requestMessage) {
		// TODO Auto-generated method stub
		super.onPostExecute(requestMessage);
		progressBar.dismiss();
		try {
			callback.getAllJobListByDueDateResponse(requestMessage);
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

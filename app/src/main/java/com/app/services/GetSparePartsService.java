package com.app.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.MasterSpareParts;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

public class GetSparePartsService extends IntentService {

	private SamgoSQLOpenHelper db;

	public GetSparePartsService() {
		// TODO Auto-generated constructor stub
		super("GetSpareParts");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		db = new SamgoSQLOpenHelper(getApplicationContext());
		Log.e("Calling >> ", "Calling Master data spare parts >> ");
		new MasterSparePartsServices().execute();
	}

	private class MasterSparePartsServices extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String response = "";
			try {
				URL url = new URL(Config.base_url + "engservices/eng_spare_master_list");

				List<NameValuePair> loginData = new ArrayList<NameValuePair>();
				loginData.add(new BasicNameValuePair("limit", "-1"));

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
					//.out.println("output===============" + br);
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

			if (RequestMessage != null) {
				db.truncateSpareParts();
				InputStream stream = new ByteArrayInputStream(RequestMessage.getBytes(StandardCharsets.UTF_8));
				try {
					readJsonStream(stream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

		public void readJsonStream(InputStream in) throws IOException {
			JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			try {
				readJSONArray(reader);
			} finally {
				reader.close();
			}
		}

		public void readJSONArray(JsonReader reader) throws IOException {
			reader.beginArray();

			while (reader.hasNext()) {

				readParentJSONObject(reader);

			}
			reader.endArray();
		}

		public void readParentJSONObject(JsonReader reader) throws IOException {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				//Log.e("TAG", "NAMe in parent >> " + name);
				if (name.equals("SpareMaster") && reader.peek() != JsonToken.NULL) {
					readJSONObject(reader);
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		}

		public void readJSONObject(JsonReader reader) throws IOException {
			reader.beginObject();

			MasterSpareParts masterSpareParts = new MasterSpareParts();

			String id = "";
			String product_id = "";
			String desc = "";
			String sales_price = "";
			String qty = "";
			String barcode = "";

			while (reader.hasNext()) {

				String name = reader.nextName();
				
				//Log.e("Name >> ", "name >> " + name);

				if (name.equals("id") && reader.peek() != JsonToken.NULL) {
					id = reader.nextString();

					masterSpareParts.setId(id);
				} else if (name.equals("product_id") && reader.peek() != JsonToken.NULL) {
					product_id = reader.nextString();

					masterSpareParts.setProduct_id(product_id);
				} else if (name.equals("description") && reader.peek() != JsonToken.NULL) {
					desc = reader.nextString();
					masterSpareParts.setDescription(desc);
				} else if (name.equals("sales_price") && reader.peek() != JsonToken.NULL) {
					sales_price = reader.nextString();
					masterSpareParts.setUnit_sales(sales_price);
				} else if (name.equals("qty") && reader.peek() != JsonToken.NULL) {
					qty = reader.nextString();

					masterSpareParts.setQuantity(qty);
				} else if (name.equals("barcode") && reader.peek() != JsonToken.NULL) {
					barcode = reader.nextString();
					Log.e("TAG", "Barcode >> " + barcode);
					masterSpareParts.setBarcode(barcode);
				} else {
					reader.skipValue();
				}

			}

			int count = db.getCountSparePartsById(id);

			//Log.e("TAG", "spare ids >> " + id);
			//.e("TAG", "product ids >> " + product_id);
			//Log.e("TAG", "Quantity >> " + qty);

			//Log.e("TAG", "Spare parts count >> " + count);
			if (count > 0) {

			} else {
				Log.e("TAG", "DONE >>");
				db.addSpareParts(masterSpareParts);
			}

			reader.endObject();
		}

	}

}

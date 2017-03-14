package com.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.CountryModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CountryService extends IntentService {

    SamgoSQLOpenHelper db;


    public CountryService() {
        super("CountryService");
    }

    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String LAUNCH_ID="LAUNCHED";

    public void save(Context context, String id) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(LAUNCH_ID, id); //3
        editor.commit();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        db = new SamgoSQLOpenHelper(getApplicationContext());
        if (intent != null) {
            String response = "";
            try {
                URL url = new URL(Config.base_url + "engservices/eng_country_list");

                List<NameValuePair> profileData = new ArrayList<NameValuePair>();
                profileData.add(new BasicNameValuePair("limit", "-1"));

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
            try {
                //Log.e("TAG", "CountryMessage >> " + countryMessage);
                JSONArray jArr = new JSONArray(response);

                for (int i = 0; i < jArr.length(); i++) {
                    JSONObject countyObj = new JSONObject(jArr.getString(i));
                    String country = countyObj.getString("Country");
                    JSONObject jObjCountry = new JSONObject(country);
                    String countryId = jObjCountry.getString("id");
                    //Log.e("TAG", "calling country service:id" + countryId);
                    String countryName = jObjCountry.getString("countries_name");
                    CountryModel countryModel = new CountryModel();
                    countryModel.setCountryId(countryId);
                    countryModel.setCountryName(countryName);
                    db.addCountry(countryModel);
                }
                //startService(new Intent(this, GetSparePartsService.class));
            } catch (Exception e) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAGC", "Ending country service");
        save(getApplicationContext(),"1");
    }
}

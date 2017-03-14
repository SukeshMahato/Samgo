package com.app.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.app.database.SamgoSQLOpenHelper;
import com.app.samgo.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddSparePartsService extends IntentService {

    SamgoSQLOpenHelper db;
    JSONArray jsonArray = null;
    int k=0;
    String sqlStatement="";


    public AddSparePartsService() {
        super("service-started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_SHORT).show();
        db = new SamgoSQLOpenHelper(getApplicationContext());

        try{

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                jsonArray = new JSONArray(intent.getStringArrayExtra("response"));
            }

            try {

                int j = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    k = i;
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            // some code #3 (Write your code here to run in UI thread)
//                            progressDialog.setMessage("Synchronizing data...." + k + "/" + jsonArray.length());
//                        }
//                    });
                    // progressDialog.setMessage("Synchronizing data...."+i+"/1000");
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("SpareMaster");
                    String pid = jsonObject1.getString("product_id");
                    String id = jsonObject1.getString("id");

                    String desc = jsonObject1.getString("description");
                    desc = desc.replace("'","");
                    String sales_price = jsonObject1.getString("sales_price");
                    String qty = jsonObject1.getString("qty");
                    String barcode = jsonObject1.getString("barcode");

                    j++;
                    try {
                        if (i == (jsonArray.length() - 1)) {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "')";
                            db.insertStatement(sqlStatement);
                        } else if (j == 500) {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "')";
                            db.insertStatement(sqlStatement);
                            sqlStatement = "";
                            j = 0;
                        } else {
                            sqlStatement += "('" + id + "' , '" + pid + "' ,' " + desc + "' ,'" + sales_price + "','" + barcode + "' ,'" + qty + "'),";
                        }
                    }catch (SQLException sqlexception){
                        Log.e("sqlexcep: ", sqlexception.toString());
                        Log.e("Id: ",id);
                    }

                }
                Log.e("values", sqlStatement);


            }catch (JSONException e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e){
            Log.e("Exception: ",""+e);
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Spare Parts Added Successfully",Toast.LENGTH_SHORT).show();
    }
}

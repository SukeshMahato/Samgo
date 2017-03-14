package com.app.samgo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.asyncs.CountrySaveServices;
import com.app.asyncs.ForgotPassServices;
import com.app.asyncs.LoginServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.CountryListner;
import com.app.listners.ForgotPassListners;
import com.app.listners.LoginListners;
import com.app.model.AppManager;
import com.app.model.CountryModel;
import com.app.model.User;
import com.app.services.GetSparePartsService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements LoginListners, ForgotPassListners {

	private EditText emailId;
	private EditText password;
	private Button logIn;
	private Button forgotPass;

	String email, pass;

	private Boolean exit = false;

	private SamgoSQLOpenHelper db;

	public static final String PREFS_NAME = "MyPrefsFile";

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		db = new SamgoSQLOpenHelper(getApplicationContext());

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

		Log.e("TAG", "haslogin >> " + hasLoggedIn);

		if (hasLoggedIn) {
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		} else {

			initialisation();
			listner();
		}

	}

	private void listner() {
		// TODO Auto-generated method stub

		logIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				email = emailId.getText().toString();
				pass = password.getText().toString();
				Log.e("TAG", "Email id >> " + email);
				Log.e("TAG", "Password >>> " + pass);
				if (!(email.equals("")) && !(pass.equals(""))) {
					new LoginServices(email, pass, LoginActivity.this).execute();
					//new CountrySaveServices(LoginActivity.this).execute();
					Log.e("TAG", "calling country service");
				} else {
					emailId.setError("Please Enter email id");
					password.setError("Please enter password");
				}
			}
		});

		forgotPass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openForgotPassPopUp();
			}
		});
	}

	private void initialisation() {
		// TODO Auto-generated method stub
		emailId = (EditText) findViewById(R.id.email_id);
		password = (EditText) findViewById(R.id.password);
		logIn = (Button) findViewById(R.id.login);
		forgotPass = (Button) findViewById(R.id.forgot_pass);
	}

	@Override
	public void getLoginSuccess(String loginMessage) {
		// TODO Auto-generated method stub
		try {
			Log.e("TAG", "LoginMessage >> " + loginMessage);
			JSONObject jObj = new JSONObject(loginMessage);
			String error_code = jObj.getString("error_status");
			Log.e("TAG", "error code >> " + error_code);
			Log.e("TAG", "error code >>> " + error_code.equalsIgnoreCase("0") + "<<<>>>" + error_code.equals("0"));

			if (error_code.equalsIgnoreCase("0")) {
				String id = jObj.getString("id");
				String role = jObj.getString("role");
				String first_name = jObj.getString("first_name");
				String last_name = jObj.getString("last_name");
				String staff_no = jObj.getString("staff_no");
				String mob_no = jObj.getString("phone");
				String sage_no = jObj.getString("sage_no");
				String photo_path = jObj.getString("photo");
				String email_id = jObj.getString("email_id");
				String address = jObj.getString("address");
				String county = jObj.getString("county");
				String country_id = jObj.getString("country_id");
				String city = jObj.getString("city");
				String latitude = jObj.getString("latitude");
				String longitude = jObj.getString("longitude");
				String shiftStart = jObj.getString("shift_start");
				String shiftEnd = jObj.getString("shift_end");
				String countryNameString = jObj.getString("Country");
				JSONObject countryNameObj = new JSONObject(countryNameString);
				String countryName = countryNameObj.getString("countries_name");

				User user = new User();
				user.setId(id);
				user.setRole(role);
				user.setFirstName(first_name);
				user.setLastName(last_name);
				user.setStaffNo(staff_no);
				user.setSageNo(sage_no);
				user.setEmailId(email_id);
				user.setPhone(mob_no);
				user.setAddress(address);
				user.setPhoto(photo_path);
				Log.e("TAG", "Address>>" + address);
				user.setLatitude(latitude);
				user.setLongitude(longitude);
				user.setCounty(county);
				user.setCity(city);
				user.setCountryId(country_id);
				user.setCountryName(countryName);
				user.setShiftStart(shiftStart);
				user.setShiftEnd(shiftEnd);
				AppManager.getSinleton().setUser(user);
				db.addUser(user);
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				// Set "hasLoggedIn" to true
				editor.putBoolean("hasLoggedIn", true);
				// Commit the edits!
				editor.commit();

				Intent startDashboard = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(startDashboard);
				overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

			} else if (error_code.equalsIgnoreCase("1")) {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog);
				// dialog.setTitle("Login Failed");

				Button closeDialog = (Button) dialog.findViewById(R.id.close_dialog);
				closeDialog.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();
			} else {
				final Dialog dialog = new Dialog(this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog);
				// dialog.setTitle("Login Failed");

				Button closeDialog = (Button) dialog.findViewById(R.id.close_dialog);

				closeDialog.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

//	@Override
//	public void getCountrySuccess(String countryMessage) {
//		// TODO Auto-generated method stub
//		try {
//			Log.e("TAG", "CountryMessage >> " + countryMessage);
//			JSONArray jArr = new JSONArray(countryMessage);
//
//			for (int i = 0; i < jArr.length(); i++) {
//				JSONObject countyObj = new JSONObject(jArr.getString(i));
//				String country = countyObj.getString("Country");
//				JSONObject jObjCountry = new JSONObject(country);
//				String countryId = jObjCountry.getString("id");
//				Log.e("TAG", "calling country service:id" + countryId);
//				String countryName = jObjCountry.getString("countries_name");
//				CountryModel countryModel = new CountryModel();
//				countryModel.setCountryId(countryId);
//				countryModel.setCountryName(countryName);
//				db.addCountry(countryModel);
//			}
//			startService(new Intent(this, GetSparePartsService.class));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	protected void openForgotPassPopUp() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(LoginActivity.this, R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.forgot_password);

		final Button forgot_required = (Button) dialog.findViewById(R.id.forgot_required);
		final EditText forgot_password_section = (EditText) dialog.findViewById(R.id.forgot_password_section);

		forgot_required.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (forgot_password_section.getText().toString().equalsIgnoreCase("")
						|| forgot_password_section.getText().toString() == null) {
					Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address",
							Toast.LENGTH_LONG);
					toast.show();
				} else if (!validate(forgot_password_section.getText().toString())) {
					Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Valid Email Address",
							Toast.LENGTH_LONG);
					toast.show();
				} else {

					new ForgotPassServices(forgot_password_section.getText().toString(), LoginActivity.this).execute();

					Toast toast = Toast.makeText(getApplicationContext(),
							"Password recovery mail sent to email at:" + forgot_password_section.getText().toString(),
							Toast.LENGTH_LONG);

					toast.show();
					dialog.dismiss();
					/*
					 * Intent goToLogin = new Intent(LoginActivity.this,
					 * LoginActivity.class); //
					 * goToFragmentJob.putExtra("FROMSTARTACT", "close");
					 * startActivity(goToLogin);
					 */
				}

			}
		});

		dialog.show();
	}

	@Override
	public void getForgotPassSuccess(String forgotMessage) {
		// TODO Auto-generated method stub
		Log.e("TAG", "Mail Sent..." + forgotMessage);
	}

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	@Override
	public void onBackPressed() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
		if (!hasLoggedIn) {
			if (exit) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
		          intent.addCategory(Intent.CATEGORY_HOME);
		          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
		          startActivity(intent);
		          finish();
		          System.exit(0);
			} else {
				Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
				exit = true;

			}
		}

	}

}

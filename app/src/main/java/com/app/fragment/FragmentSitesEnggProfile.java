package com.app.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.app.adapter.AddCountryListAdapter;
import com.app.asyncs.ProfileSaveServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.ProfileSaveListener;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.CountryModel;
import com.app.model.SiteEnggModel;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;
import com.app.utility.ImageLoader;

import android.R.color;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSitesEnggProfile extends Fragment implements ProfileSaveListener {

	View v;

	String picturePath;

	private EditText firstName, lastName, mobNo, cityName, address;
	private TextView startHour, endHour, countryName, countyName, latitude, longitude;
	private Button browseButton, saveButton;
	private static int RESULT_LOAD_IMAGE = 1;
	ImageView imageview;
	private String countryId;

	SiteEnggModel siteEnggModel;

	ArrayList<String> timing = new ArrayList<String>();
	ArrayList<CountryModel> countryList = new ArrayList<CountryModel>();
	String enggId;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	int loader = R.drawable.image_view;

	private SamgoSQLOpenHelper db;
	
	private LinearLayout parentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.site_engineer_profile, container, false);

		this.initialisation();

		this.listner();
		enggId = AppManager.getSinleton().getUser().getId();

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		db = new SamgoSQLOpenHelper(getContext());
		
		parentLayout = (LinearLayout) v.findViewById(R.id.parent_layout);

		SharedPreferences settings = getActivity().getSharedPreferences(Config.MYFREFS, 0);

		String background_image = settings.getString("background_image", "test");

		Log.e("TAG", "background_image >> " + background_image);

		if (background_image != null) {

			if (background_image.equalsIgnoreCase("background_1")) {
				parentLayout.setBackgroundResource(R.drawable.background_1);
			} else if (background_image.equalsIgnoreCase("background_2")) {
				parentLayout.setBackgroundResource(R.drawable.background_2);
			} else if (background_image.equalsIgnoreCase("background_3")) {
				parentLayout.setBackgroundResource(R.drawable.background_3);
			} else if (background_image.equalsIgnoreCase("background_4")) {
				parentLayout.setBackgroundResource(R.drawable.background_4);
			} else if (background_image.equalsIgnoreCase("background_5")) {
				parentLayout.setBackgroundColor(color.white);
			}

		}

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			siteEnggModel = db.SiteEnggDetails(AppManager.getSinleton().getUser().getId(), getActivity());
			firstName.setText(siteEnggModel.getFirstName());
			lastName.setText(siteEnggModel.getLastName());
			mobNo.setText(siteEnggModel.getMobNo());
			cityName.setText(siteEnggModel.getCityName());
			address.setText(siteEnggModel.getAddress());
			countryName.setText(siteEnggModel.getCountryName());
			countyName.setText(siteEnggModel.getCountyName());
			latitude.setText(siteEnggModel.getLatitude());
			longitude.setText(siteEnggModel.getLogitude());
			startHour.setText(siteEnggModel.getShiftStart());
			endHour.setText(siteEnggModel.getShiftEnd());
			String path = siteEnggModel.getPhotoPath();
			/*
			 * File imgFile = new File(path); if(imgFile.exists()) { Bitmap
			 * myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			 * imageview.setImageBitmap(myBitmap); Log.e("TAG", "Image>>"+path);
			 * }
			 */
			ImageLoader imgLoader = new ImageLoader(getActivity());

			// whenever you want to load an image from url
			// call DisplayImage function
			// url - image url to load
			// loader - loader image, will be displayed before getting image
			// image - ImageView
			imgLoader.DisplayImage(path, loader, imageview);
		} else {
			siteEnggModel = db.SiteEnggDetails(AppManager.getSinleton().getUser().getId(), getActivity());
			firstName.setText(siteEnggModel.getFirstName());
			lastName.setText(siteEnggModel.getLastName());
			mobNo.setText(siteEnggModel.getMobNo());
			cityName.setText(siteEnggModel.getCityName());
			address.setText(siteEnggModel.getAddress());
			countryName.setText(siteEnggModel.getCountryName());
			countyName.setText(siteEnggModel.getCountyName());
			latitude.setText(siteEnggModel.getLatitude());
			longitude.setText(siteEnggModel.getLogitude());
			startHour.setText(siteEnggModel.getShiftStart());
			endHour.setText(siteEnggModel.getShiftEnd());
		}
		return v;
	}

	private void listner() {
		startHour.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(getActivity(), R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.common_one_row_shift_item);

				ListView timingListsView = (ListView) dialog.findViewById(R.id.list_timing);
				String[] items = getResources().getStringArray(R.array.shift_timing);
				timing.clear();
				for (int i = 0; i < items.length; i++) {
					String s = items[i];
					timing.add(s);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, timing);
				timingListsView.setAdapter(adapter);

				timingListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String hours = timing.get(position);
						Log.e("TAG", "Value for Years:" + hours);
						startHour.setText(hours);
						dialog.dismiss();

					}

				});

				dialog.show();
			}
		});

		endHour.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(getActivity(), R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.common_one_row_shift_item);

				ListView timingListsView = (ListView) dialog.findViewById(R.id.list_timing);
				String[] items = getResources().getStringArray(R.array.shift_timing);
				timing.clear();
				for (int i = 0; i < items.length; i++) {
					String s = items[i];
					timing.add(s);
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_1, timing);
				timingListsView.setAdapter(adapter);

				timingListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						String hours = timing.get(position);
						Log.e("TAG", "Value for Years:" + hours);
						endHour.setText(hours);
						dialog.dismiss();

					}

				});

				dialog.show();
			}
		});

		browseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		saveButton.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {

				StringBuffer errorText = new StringBuffer();
				siteEnggModel = new SiteEnggModel();

				final int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					if (firstName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter First Name ");
						errorText.append("\n");
						firstName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setFirstName(firstName.getText().toString());
						firstName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}

					if (lastName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Last Name ");
						errorText.append("\n");
						lastName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setLastName(lastName.getText().toString());
						lastName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (mobNo.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Mobile No. ");
						errorText.append("\n");
						mobNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setMobNo(mobNo.getText().toString());
						mobNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (startHour.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Select Start Shift. ");
						errorText.append("\n");
						startHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setShiftStart(startHour.getText().toString());
						startHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (endHour.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Select End Shift. ");
						errorText.append("\n");
						endHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setShiftEnd(endHour.getText().toString());
						endHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (countyName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter County Name ");
						errorText.append("\n");
						countyName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setCountyName(countyName.getText().toString());
						countyName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (cityName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter City Name ");
						errorText.append("\n");
						cityName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setCityName(cityName.getText().toString());
						cityName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (address.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Address ");
						errorText.append("\n");
						address.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setAddress(address.getText().toString());
						address.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}

				} else {
					if (firstName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter First Name ");
						errorText.append("\n");
						firstName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setFirstName(firstName.getText().toString());
						firstName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}

					if (lastName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Last Name ");
						errorText.append("\n");
						lastName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setLastName(lastName.getText().toString());
						lastName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (mobNo.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Mobile No. ");
						errorText.append("\n");
						mobNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setMobNo(mobNo.getText().toString());
						mobNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (startHour.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Select Start Shift. ");
						errorText.append("\n");
						startHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setShiftStart(startHour.getText().toString());
						startHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (endHour.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Select End Shift. ");
						errorText.append("\n");
						endHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));
					} else {
						siteEnggModel.setShiftEnd(endHour.getText().toString());
						endHour.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (countyName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter County Name ");
						errorText.append("\n");
						countyName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setCountyName(countyName.getText().toString());
						countyName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (cityName.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter City Name ");
						errorText.append("\n");
						cityName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setCityName(cityName.getText().toString());
						cityName.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}
					if (address.getText().toString().equalsIgnoreCase("")) {
						errorText.append("Enter Address ");
						errorText.append("\n");
						address.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_red_border));

					} else {
						siteEnggModel.setAddress(address.getText().toString());
						address.setBackgroundDrawable(getResources().getDrawable(R.drawable.radio_border));
					}

				}

				siteEnggModel.setCountryId(countryId);
				siteEnggModel.setLatitude(latitude.getText().toString());
				siteEnggModel.setLogitude(longitude.getText().toString());
				siteEnggModel.setCountryName(countryName.getText().toString());
				// siteEnggModel.setPhotoData(photoData);

				// Log.e("call", "in if above >> " + picturePath);

				if (errorText.toString().equalsIgnoreCase("")) {
					if (picturePath == null) {
						Log.e("call", "in if >> ");

						new ProfileSaveServices(siteEnggModel, enggId, "", FragmentSitesEnggProfile.this, getActivity())
								.execute();
					} else {

						Log.e("call", "in else >> ");

						new ProfileSaveServices(siteEnggModel, enggId,
								getStringImage(BitmapFactory.decodeFile(picturePath)), FragmentSitesEnggProfile.this,
								getActivity()).execute();
					}
				} else {
					Toast.makeText(getActivity(), errorText.toString(), Toast.LENGTH_SHORT).show();
				}

				// Toast.makeText(getActivity(), "Profile Updated Successfully",
				// Toast.LENGTH_LONG).show();

			}
		});

		countryName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				countryList = db.getCountry();

				Log.e("TAG", "Country Size" + countryList.size());

				final Dialog dialog = new Dialog(getActivity(), R.style.TopToBottomDialog);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dropdown_country_model);

				ListView countryNameListsView = (ListView) dialog.findViewById(R.id.country_model_list);

				AddCountryListAdapter addCountryListAdapter = new AddCountryListAdapter(getActivity(), countryList);
				countryNameListsView.setAdapter(addCountryListAdapter);
				addCountryListAdapter.notifyDataSetChanged();

				countryNameListsView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						CountryModel countryModel = countryList.get(position);
						Log.e("TAG", "Value for Country Id:" + countryModel.getCountryId());
						// machine_id = machineMaster.getMachine_id();
						Log.e("TAG", "Value for Country Name:" + countryModel.getCountryName());
						countryName.setText(countryModel.getCountryName());
						countryId = countryModel.getCountryId();

						dialog.dismiss();

					}
				});
				try {
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialisation() {
		firstName = (EditText) v.findViewById(R.id.first_name_edit);
		lastName = (EditText) v.findViewById(R.id.last_name_edit);
		mobNo = (EditText) v.findViewById(R.id.mobile_edit);
		cityName = (EditText) v.findViewById(R.id.city_name_edit);
		address = (EditText) v.findViewById(R.id.adress_edit);
		startHour = (TextView) v.findViewById(R.id.shift_start_edit);
		endHour = (TextView) v.findViewById(R.id.shift_end_edit);
		countryName = (TextView) v.findViewById(R.id.country_name_edit);
		countyName = (TextView) v.findViewById(R.id.county_name_edit);
		latitude = (TextView) v.findViewById(R.id.latitude_readonly);
		longitude = (TextView) v.findViewById(R.id.longitude_readonly);
		browseButton = (Button) v.findViewById(R.id.browse);
		imageview = (ImageView) v.findViewById(R.id.image_view);
		saveButton = (Button) v.findViewById(R.id.save_profile);

	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		super.getActivity();

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			/*
			 * Cursor cursor = getContentResolver().query(selectedImage,
			 * filePathColumn, null, null, null);
			 */
			Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			imageview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			Log.e("TAG", "VAlue PAth" + picturePath);
			Log.e("TAG", "VAlue image" + getStringImage(BitmapFactory.decodeFile(picturePath)));

		}

	}

	public String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
		return encodedImage;
	}

	@Override
	public void getUpdateProfileSuccess(String profileData) {
		// TODO Auto-generated method stub
		if (profileData != null) {
			Log.e("TAG", "Result <<>> " + profileData);
		}
	}

}

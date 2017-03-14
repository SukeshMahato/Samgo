package com.app.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.TrainingListsAdapter;
import com.app.asyncs.TrainingDetailsServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.TrainingDetailsListener;
import com.app.listners.TrainingUpdateListener;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.TrainingPojo;
import com.app.model.TrainingVideoModel;
import com.app.samgo.R;
import com.app.samgo.TrainingDetails;
import com.app.services.ConnectionDetector;

import android.R.color;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentTraining extends Fragment implements TrainingDetailsListener, TrainingUpdateListener {

	View v;

	private ListView trainingLists;

	private TrainingListsAdapter trainingListsAdapter;

	private ArrayList<TrainingPojo> trainingDataList = new ArrayList<TrainingPojo>();
	private ArrayList<TrainingPojo> trainingDataList2 = new ArrayList<TrainingPojo>();

	protected TextView searchBy;

	private EditText searchHere;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private SamgoSQLOpenHelper db;

	private RelativeLayout parentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.fragment_training_lists, container, false);

		this.initialisation();

		this.listner();

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		db = new SamgoSQLOpenHelper(getContext());

		parentLayout = (RelativeLayout) v.findViewById(R.id.parent_layout);

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
			new TrainingDetailsServices(getActivity(), this, AppManager.getSinleton().getUser().getId(), "-1")
					.execute();
		} else {
			trainingDataList.clear();
			trainingDataList2.clear();

			trainingDataList = db.getAllTrainingData();
			trainingDataList2 = db.getAllTrainingData();

			trainingListsAdapter = new TrainingListsAdapter(FragmentTraining.this, getActivity(), trainingDataList);
			trainingLists.setAdapter(trainingListsAdapter);
			trainingListsAdapter.notifyDataSetChanged();

		}

		return v;
	}

	private void listner() {
		trainingLists.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * searchBy.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub
		 * 
		 * Log.e("TAG", "clicked on");
		 * 
		 * Log.e("TAG", "size >>> " + trainingDataList2.size());
		 * 
		 * final Dialog dialog = new Dialog(getContext(),
		 * R.style.TopToBottomDialog);
		 * dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * dialog.setContentView(R.layout.custom_search_dialog_for_training);
		 * 
		 * TextView site_name = (TextView) dialog.findViewById(R.id.site_name);
		 * TextView job_id = (TextView) dialog.findViewById(R.id.job_id);
		 * 
		 * site_name.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Site Name"); dialog.dismiss(); } });
		 * 
		 * job_id.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub searchBy.setText("Job Id"); dialog.dismiss(); } });
		 * 
		 * dialog.show(); } });
		 */

		searchHere.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				String searchString = searchHere.getText().toString().toLowerCase();
				int textLength = searchString.length();

				trainingDataList.clear();

				for (int i = 0; i < trainingDataList2.size(); i++) {

					TrainingPojo trainingItems = new TrainingPojo();

					String jobId = trainingDataList2.get(i).getDocket_no();
					String siteName = trainingDataList2.get(i).getSite();
					String title = trainingDataList2.get(i).getTitle();
					String sectorName = trainingDataList2.get(i).getSectorName();
					String dueDate = trainingDataList2.get(i).getDue_date();
					String status = trainingDataList2.get(i).getTrainingStatus();

					// if (searchBy.getText().toString().contains("Job Id")) {

					if (textLength <= jobId.length()) {

						if (jobId.toLowerCase().contains(searchString) || siteName.toLowerCase().contains(searchString)
								|| title.toLowerCase().contains(searchString)
								|| sectorName.toLowerCase().contains(searchString)
								|| dueDate.toLowerCase().contains(searchString)
								|| status.toLowerCase().contains(searchString)) {

							trainingItems.setSectorName(sectorName);
							trainingItems.setDue_date(dueDate);
							trainingItems.setDocket_no(jobId);
							trainingItems.setTitle(title);
							trainingItems.setSite(siteName);
							trainingItems.setTrainingStatus(status);

							trainingDataList.add(trainingItems);
						}

						// }

					} /*
						 * else if (searchBy.getText().toString().contains(
						 * "Site Name")) {
						 * 
						 * if (textLength <= siteName.length()) {
						 * 
						 * if (siteName.toLowerCase().contains(searchString)) {
						 * 
						 * trainingItems.setSectorName(sectorName);
						 * trainingItems.setDue_date(dueDate);
						 * trainingItems.setDocket_no(jobId);
						 * trainingItems.setTitle(title);
						 * trainingItems.setSite(siteName);
						 * trainingItems.setTrainingStatus(status);
						 * 
						 * trainingDataList.add(trainingItems); }
						 * 
						 * }
						 * 
						 * }
						 */

				}

				trainingListsAdapter = new TrainingListsAdapter(FragmentTraining.this, getActivity(), trainingDataList);
				trainingLists.setAdapter(trainingListsAdapter);
				trainingListsAdapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		trainingLists = (ListView) v.findViewById(R.id.training_list);
		// searchBy = (TextView) v.findViewById(R.id.search_by);
		searchHere = (EditText) v.findViewById(R.id.search_here);
	}

	@Override
	public void getRequestSuccess(String requestMessage) {
		// TODO Auto-generated method stub
		Log.e("TAG", "Testing");
		try {
			// Log.e("TAG", "requestMessage >> " + RequestMessage);
			JSONArray jArr = new JSONArray(requestMessage);

			trainingDataList.clear();
			trainingDataList2.clear();

			db.deleteTrainingData();
			db.deleteTrainingVideoData();
			db.deleteTrainingTraineeTable();

			for (int i = 0; i < jArr.length(); i++) {
				String jsonObject = jArr.getString(i);

				JSONObject jObj = new JSONObject(jsonObject);

				String jsonJobObject = jObj.getString("Training");

				JSONObject jobObj = new JSONObject(jsonJobObject);

				String id = jobObj.getString("id");
				String docket_no = jobObj.getString("docket_no");
				String client_id = jobObj.getString("client_id");
				String site_id = jobObj.getString("site_id");
				String trans_title = jobObj.getString("title");
				String due_date = jobObj.getString("due_date");
				String created_by = jobObj.getString("created_by");
				String creator_type = jobObj.getString("creator_type");
				String register_date = jobObj.getString("register_date");
				String is_deleted = jobObj.getString("is_deleted");

				String jsonSiteObject = jObj.getString("Site");
				JSONObject siteObj = new JSONObject(jsonSiteObject);

				String sitename = siteObj.getString("name");
				String sectorObj = siteObj.getString("Sector");

				String clientObj = jObj.getString("Client");

				Log.e("TAG", "CLient >> " + clientObj);

				JSONObject clientjObj = new JSONObject(clientObj);

				String clientName = clientjObj.getString("first_name");

				String sectorName = "";

				Object json = new JSONTokener(sectorObj).nextValue();
				if (json instanceof JSONObject) {
					Log.e("TAG", "Sector >>> " + sectorObj);
					JSONObject sectorjObj = new JSONObject(sectorObj);

					sectorName = sectorjObj.getString("name");
				} else if (json instanceof JSONArray) {
					Log.e("TAG", "Sector >>> " + sectorObj);

					sectorName = "Awaiting Sector from Admin";
				}

				String jsonTrainingStatusObj = jObj.getString("TrainingStatus");
				JSONObject trainingObj = new JSONObject(jsonTrainingStatusObj);
				String training_status = trainingObj.getString("status");

				String jsonTrainingVideoAssign = jObj.getString("TrainingVideoAssign");

				JSONArray jArray = new JSONArray(jsonTrainingVideoAssign);

				if (jArray.length() > 0) {

					for (int j = 0; j < jArray.length(); j++) {
						String videoJson = jArray.getString(j);

						JSONObject videoJsonObj = new JSONObject(videoJson);

						String videoId = videoJsonObj.getString("video_id");
						String trainingId = videoJsonObj.getString("training_id");
						String videoTitle = videoJsonObj.getString("video_title");
						String videoURL = videoJsonObj.getString("video_url");

						TrainingVideoModel trainingVideo = new TrainingVideoModel();

						trainingVideo.setVideoId(videoId);
						trainingVideo.setVideoTitle(videoTitle);
						trainingVideo.setTrainingId(trainingId);
						trainingVideo.setVideoUrl(videoURL);

						db.addTrainingVideoData(trainingVideo);
					}

				}

				TrainingPojo trainingPojo = new TrainingPojo();

				trainingPojo.setTrainId(id);
				trainingPojo.setSite_id(site_id);
				trainingPojo.setSite(sitename);
				trainingPojo.setDocket_no(docket_no);
				trainingPojo.setClient_id(client_id);
				trainingPojo.setTitle(trans_title);
				trainingPojo.setDue_date(due_date);
				trainingPojo.setCreated_by(created_by);
				trainingPojo.setCreator_type(creator_type);
				trainingPojo.setRegister_date(register_date);
				trainingPojo.setIs_deleted(is_deleted);
				trainingPojo.setSectorName(sectorName);
				trainingPojo.setClientName(clientName);
				trainingPojo.setTrainingStatus(training_status);

				db.addTrainingData(trainingPojo);

				trainingDataList.add(trainingPojo);
				trainingDataList2.add(trainingPojo);

			}

			trainingListsAdapter = new TrainingListsAdapter(FragmentTraining.this, getActivity(), trainingDataList);
			trainingLists.setAdapter(trainingListsAdapter);
			trainingListsAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void openTrainingDetails(int position) {

		Intent gotoViewSite = new Intent(getActivity(), TrainingDetails.class);
		gotoViewSite.putExtra("TRAININGID", trainingDataList.get(position).getTrainId());
		startActivity(gotoViewSite);
		getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

	}

	public void openTraineePopUp(int position) {
		// TODO Auto-generated method stub
		final String training_id = trainingDataList.get(position).getTrainId();

		final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_pop_up_for_trainee_name);

		final EditText trainee_names = (EditText) dialog.findViewById(R.id.trainee_name);

		Button saveTrainee = (Button) dialog.findViewById(R.id.save_trainee_names);

		saveTrainee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (TextUtils.isEmpty(trainee_names.getText().toString())) {
					trainee_names.setError("Enter Trainee Names");
				} else {

					if (isInternetPresent) {

						// new TrainingUpdateServices(training_id,
						// trainee_names.getText().toString(), getActivity(),
						// FragmentTraining.this).execute();

					}

					db.updateTrainingStatus(training_id);

					db.addTrainingTraineeName(training_id, trainee_names.getText().toString());

					dialog.dismiss();

					trainingDataList.clear();
					trainingDataList2.clear();

					trainingDataList = db.getAllTrainingData();
					trainingDataList2 = db.getAllTrainingData();

					trainingListsAdapter = new TrainingListsAdapter(FragmentTraining.this, getActivity(),
							trainingDataList);
					trainingLists.setAdapter(trainingListsAdapter);
					trainingListsAdapter.notifyDataSetChanged();
				}
			}
		});

		dialog.show();
	}

	@Override
	public void getTrainingUpdateSuccess(String response) {
		// TODO Auto-generated method stub

	}

}

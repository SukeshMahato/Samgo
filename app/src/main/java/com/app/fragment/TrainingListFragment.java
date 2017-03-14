package com.app.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.app.adapter.DashboardTrainingAdapter;
import com.app.asyncs.TrainingDetailsServices;
import com.app.asyncs.TrainingUpdateServices;
import com.app.database.SamgoSQLOpenHelper;
import com.app.listners.TrainingDetailsListener;
import com.app.listners.TrainingUpdateListener;
import com.app.model.AppManager;
import com.app.model.TrainingPojo;
import com.app.model.TrainingVideoModel;
import com.app.samgo.R;
import com.app.samgo.TrainingDetails;
import com.app.services.ConnectionDetector;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

public class TrainingListFragment extends Fragment implements TrainingDetailsListener, TrainingUpdateListener {

	View v;
	private ListView jobList;

	private DashboardTrainingAdapter dashboardTrainingAdapter;

	private ArrayList<TrainingPojo> trainingDataList = new ArrayList<TrainingPojo>();

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	private SamgoSQLOpenHelper db;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_training_list, container, false);

		this.initialisation();

		this.listner();

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		db = new SamgoSQLOpenHelper(getContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			new TrainingDetailsServices(getActivity(), this, AppManager.getSinleton().getUser().getId(), "5").execute();
		} else {
			trainingDataList.clear();

			trainingDataList = db.getAllTrainingData();

			dashboardTrainingAdapter = new DashboardTrainingAdapter(TrainingListFragment.this, getActivity(),
					trainingDataList);
			jobList.setAdapter(dashboardTrainingAdapter);
			dashboardTrainingAdapter.notifyDataSetChanged();

		}

		return v;
	}

	private void listner() {
		jobList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initialisation() {
		jobList = (ListView) v.findViewById(R.id.training_dashboard_list);

	}

	@Override
	public void getRequestSuccess(String RequestMessage) {
		// TODO Auto-generated method stub
		Log.e("TAG", "Testing");
		try {
			// Log.e("TAG", "requestMessage >> " + RequestMessage);
			JSONArray jArr = new JSONArray(RequestMessage);

			trainingDataList.clear();

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

			}

			dashboardTrainingAdapter = new DashboardTrainingAdapter(TrainingListFragment.this, getActivity(),
					trainingDataList);
			jobList.setAdapter(dashboardTrainingAdapter);
			dashboardTrainingAdapter.notifyDataSetChanged();

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

						new TrainingUpdateServices(training_id, trainee_names.getText().toString(), getActivity(),
								TrainingListFragment.this).execute();

					}

					dialog.dismiss();

					db.updateTrainingStatus(training_id);

					db.addTrainingTraineeName(training_id, trainee_names.getText().toString());

					trainingDataList.clear();

					trainingDataList = db.getAllTrainingData();

					dashboardTrainingAdapter = new DashboardTrainingAdapter(TrainingListFragment.this, getActivity(),
							trainingDataList);
					jobList.setAdapter(dashboardTrainingAdapter);
					dashboardTrainingAdapter.notifyDataSetChanged();
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

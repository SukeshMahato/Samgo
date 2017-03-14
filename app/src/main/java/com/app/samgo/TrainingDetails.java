package com.app.samgo;

import java.util.ArrayList;

import com.app.adapter.TrainingVideoListAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.TrainingPojo;
import com.app.model.TrainingVideoModel;
import com.app.model.YouTubeContent;
import com.app.model.YouTubeContent.YouTubeVideo;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrainingDetails extends FragmentActivity {

	private ImageView back_button;

	private TrainingVideoListAdapter trainingVideoListAdapter;
	private GridView videoList;

	public ArrayList<TrainingVideoModel> trainingYoutubeItems = new ArrayList<TrainingVideoModel>();

	/**
	 * An array of YouTube videos
	 */
	public ArrayList<YouTubeVideo> youtubeItems = new ArrayList<YouTubeVideo>();
	private String training_id = "";

	private SamgoSQLOpenHelper db;
	private TextView clientName, jobDate, siteName, dueDate;
	
	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_details);

		back_button = (ImageView) findViewById(R.id.back_button);

		videoList = (GridView) findViewById(R.id.training_grid);

		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		clientName = (TextView) findViewById(R.id.client_name_text);
		jobDate = (TextView) findViewById(R.id.job_date_text);
		siteName = (TextView) findViewById(R.id.site_name_text);
		dueDate = (TextView) findViewById(R.id.due_date_text);

		db = new SamgoSQLOpenHelper(getApplicationContext());
		
		parentLayout = (RelativeLayout) findViewById(R.id.parent_layout);

		SharedPreferences settings = getSharedPreferences(Config.MYFREFS, 0);

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

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			training_id = extras.getString("TRAININGID");
		}

		if (Integer.parseInt(training_id) > 0) {
			TrainingPojo trainingItems = db.getTrainingRowById(training_id);

			clientName.setText(trainingItems.getClientName());
			jobDate.setText(trainingItems.getRegister_date());
			siteName.setText(trainingItems.getSite());
			dueDate.setText(trainingItems.getDue_date());

			trainingYoutubeItems = db.getVideoListOfTraining(training_id);

			if (trainingYoutubeItems.size() > 0) {
				for (int i = 0; i < trainingYoutubeItems.size(); i++) {
					TrainingVideoModel mTrainingVideo = new TrainingVideoModel();

					mTrainingVideo = trainingYoutubeItems.get(i);

					String videoTitle = mTrainingVideo.getVideoTitle();
					String videoURL = mTrainingVideo.getVideoUrl();

					String[] videoURLIds = videoURL.split("=");

					youtubeItems.add(new YouTubeVideo(videoURLIds[1], videoTitle));
				}

				trainingVideoListAdapter = new TrainingVideoListAdapter(this, youtubeItems);
				videoList.setAdapter(trainingVideoListAdapter);
				trainingVideoListAdapter.notifyDataSetChanged();
			}

		}

		videoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long id) {
				// TODO Auto-generated method stub
				final Context context = TrainingDetails.this;
				final String DEVELOPER_KEY = getString(R.string.DEVELOPER_KEY);
				final YouTubeContent.YouTubeVideo video = youtubeItems.get(position);

				if (YouTubeIntents.canResolvePlayVideoIntent(context)) {
					// Opens in the StandAlonePlayer, defaults to fullscreen
					startActivity(
							YouTubeStandalonePlayer.createVideoIntent(TrainingDetails.this, DEVELOPER_KEY, video.id));
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}
}

package com.app.fragment;

import java.util.ArrayList;

import com.app.adapter.MachineVideoAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.MachineView;
import com.app.model.YouTubeContent;
import com.app.model.YouTubeContent.YouTubeVideo;
import com.app.samgo.R;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MachineViewVideos extends Fragment {

	View v;

	private MachineVideoAdapter machineVideoListAdapter;

	private GridView videoList;

	/**
	 * An array of YouTube videos
	 */
	public ArrayList<YouTubeVideo> youtubeItems = new ArrayList<YouTubeVideo>();

	private SamgoSQLOpenHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.machine_videos, container, false);

		videoList = (GridView) v.findViewById(R.id.grid);

		db = new SamgoSQLOpenHelper(getContext());

		Log.e("TAG", "Machine view >> " + Config.machineSlNo);

		MachineView machineView = db.getMachineViewData(Config.machineSlNo);

		Log.e("TAG", "Video 1 >> " + machineView.getYoutube_link1());
		Log.e("TAG", "Video 1 >> " + machineView.getYoutube_link1().equalsIgnoreCase(""));

		if (machineView.getYoutube_link1().equalsIgnoreCase("")) {

		} else {
			String[] youtubeId = machineView.getYoutube_link1().split("=");

			youtubeItems.add(new YouTubeVideo(youtubeId[1], "Over View Video 1"));
		}

		Log.e("TAG", "Video 2 >> " + machineView.getYoutube_link2());
		Log.e("TAG", "Video 2 >> " + machineView.getYoutube_link2().equalsIgnoreCase(""));

		if (machineView.getYoutube_link2().equalsIgnoreCase("")) {

		} else {
			String[] youtubeId = machineView.getYoutube_link2().split("=");

			youtubeItems.add(new YouTubeVideo(youtubeId[1], "Over View Video 2"));
		}

		machineVideoListAdapter = new MachineVideoAdapter(getActivity(), youtubeItems);
		videoList.setAdapter(machineVideoListAdapter);
		machineVideoListAdapter.notifyDataSetChanged();

		videoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long id) {
				// TODO Auto-generated method stub
				final Context context = getActivity();
				final String DEVELOPER_KEY = getString(R.string.DEVELOPER_KEY);
				final YouTubeContent.YouTubeVideo video = youtubeItems.get(position);

				if (YouTubeIntents.canResolvePlayVideoIntent(context)) {
					// Opens in the StandAlonePlayer, defaults to fullscreen
					startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(), DEVELOPER_KEY, video.id));
				}
			}
		});

		return v;
	}

}

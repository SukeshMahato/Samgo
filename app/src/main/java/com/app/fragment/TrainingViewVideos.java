package com.app.fragment;

import java.util.ArrayList;

import com.app.adapter.TrainingVideoListAdapter;
import com.app.model.YouTubeContent;
import com.app.model.YouTubeContent.YouTubeVideo;
import com.app.samgo.R;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class TrainingViewVideos extends Fragment {

	View v;

	private TrainingVideoListAdapter trainingVideoListAdapter;

	private GridView videoList;

	/**
	 * An array of YouTube videos
	 */
	public ArrayList<YouTubeVideo> youtubeItems = new ArrayList<YouTubeVideo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.training_videos, container, false);

		videoList = (GridView) v.findViewById(R.id.training_grid);

		youtubeItems.add(new YouTubeVideo("tttG6SdnCd4", "Open in the YouTube App"));
		youtubeItems.add(new YouTubeVideo("x-hH_Txxzls", "Open in the YouTube App in fullscreen"));
		youtubeItems.add(new YouTubeVideo("TTh_qYMzSZk", "Open in the Standalone player in fullscreen"));
		youtubeItems.add(new YouTubeVideo("tttG6SdnCd4", "Open in the Standalone player in \"Light Box\" mode"));
		youtubeItems.add(new YouTubeVideo("x-hH_Txxzls", "Open in the YouTubeFragment"));
		youtubeItems.add(new YouTubeVideo("TTh_qYMzSZk", "Hosting the YouTubeFragment in an Activity"));

		trainingVideoListAdapter = new TrainingVideoListAdapter(getActivity(), youtubeItems);
		videoList.setAdapter(trainingVideoListAdapter);
		trainingVideoListAdapter.notifyDataSetChanged();

		videoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long id) {
				// TODO Auto-generated method stub
				final Context context = getActivity();
				final String DEVELOPER_KEY = getString(R.string.DEVELOPER_KEY);
				final YouTubeContent.YouTubeVideo video = YouTubeContent.ITEMS.get(position);

				if (YouTubeIntents.canResolvePlayVideoIntent(context)) {
					// Opens in the StandAlonePlayer, defaults to fullscreen
					startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(), DEVELOPER_KEY, video.id));
				}
			}
		});

		return v;
	}

}

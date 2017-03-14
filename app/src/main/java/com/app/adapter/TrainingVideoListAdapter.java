package com.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.app.model.YouTubeContent.YouTubeVideo;
import com.app.samgo.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TrainingVideoListAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener {

	private Context mContext;
	private Map<View, YouTubeThumbnailLoader> mLoaders;

	private ArrayList<YouTubeVideo> videoList = new ArrayList<YouTubeVideo>();

	private YouTubeVideo tmpValues;

	public TrainingVideoListAdapter(final Context context, ArrayList<YouTubeVideo> videoList) {
		mContext = context;
		mLoaders = new HashMap<View, YouTubeThumbnailLoader>();

		this.videoList = videoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return videoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return videoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class VideoHolder {
		YouTubeThumbnailView thumb;
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		// TODO Auto-generated method stub\
		VideoHolder holder;

		if (convertView == null) {
			// Create the row
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.training_video_list_one_row_item, parentView, false);

			// Create the video holder
			holder = new VideoHolder();

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = videoList.get(position);

			// Set the title
			holder.title = (TextView) convertView.findViewById(R.id.training_title);
			holder.title.setText(tmpValues.getTitle());

			// Initialise the thumbnail

			Log.e("TAG", "id >> " + tmpValues.getId());

			holder.thumb = (YouTubeThumbnailView) convertView.findViewById(R.id.training_video_thumbnail);
			holder.thumb.setTag(tmpValues.getId());
			holder.thumb.initialize(mContext.getString(R.string.DEVELOPER_KEY), this);

			convertView.setTag(holder);
		} else {
			// Create it again
			holder = (VideoHolder) convertView.getTag();
			final YouTubeThumbnailLoader loader = mLoaders.get(holder.thumb);

			if (tmpValues != null) {
				// Set the title
				holder.title.setText(tmpValues.getTitle());

				// Setting the video id can take a while to actually change the
				// image
				// in the meantime the old image is shown.
				// Removing the image will cause the background color to show
				// instead, not ideal
				// but preferable to flickering images.
				holder.thumb.setImageBitmap(null);

				if (loader == null) {
					// Loader is currently initialising
					holder.thumb.setTag(tmpValues.getId());
				} else {
					// The loader is already initialised
					// Note that it's possible to get a DeadObjectException here
					try {
						loader.setVideo(tmpValues.getId());
					} catch (IllegalStateException exception) {
						// If the Loader has been released then remove it from
						// the map and re-init
						mLoaders.remove(holder.thumb);
						holder.thumb.initialize(mContext.getString(R.string.DEVELOPER_KEY), this);

					}
				}

			}
		}

		return convertView;
	}

	@Override
	public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
		final String errorMessage = errorReason.toString();
		Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
		mLoaders.put(view, loader);
		loader.setVideo((String) view.getTag());
	}

}

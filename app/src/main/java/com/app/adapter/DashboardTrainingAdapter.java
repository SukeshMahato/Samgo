package com.app.adapter;

import java.util.ArrayList;

import com.app.fragment.TrainingListFragment;
import com.app.model.TrainingPojo;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardTrainingAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<TrainingPojo> jobData = new ArrayList<TrainingPojo>();

	protected TrainingPojo tmpValues;

	protected Fragment frag;

	protected static LayoutInflater inflater = null;

	public DashboardTrainingAdapter(Fragment frag, Activity a, ArrayList<TrainingPojo> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.jobData = data;
		this.frag = frag;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (jobData.size() <= 0)
			return 1;
		return jobData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public static class ViewHolder {
		protected TextView trainingId;
		protected TextView trainingTitle;
		protected TextView siteName;
		protected ImageView actions, trainingComplete;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		ViewHolder viewHolder;

		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater.inflate(R.layout.fragment_training_job_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.trainingId = (TextView) vi.findViewById(R.id.training_id);
			viewHolder.trainingTitle = (TextView) vi.findViewById(R.id.training_title);
			viewHolder.actions = (ImageView) vi.findViewById(R.id.action_photo);
			viewHolder.trainingComplete = (ImageView) vi.findViewById(R.id.training_complete);
			viewHolder.siteName = (TextView) vi.findViewById(R.id.site_name);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

			viewHolder.trainingId.setText("No data to display.");
			viewHolder.trainingTitle.setVisibility(View.GONE);
			viewHolder.actions.setVisibility(View.GONE);
			viewHolder.trainingComplete.setVisibility(View.GONE);
			viewHolder.siteName.setVisibility(View.GONE);

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			viewHolder.trainingTitle.setVisibility(View.VISIBLE);
			viewHolder.actions.setVisibility(View.VISIBLE);
			viewHolder.trainingComplete.setVisibility(View.VISIBLE);
			viewHolder.siteName.setVisibility(View.VISIBLE);

			/************ Set Model values in Holder elements ***********/

			viewHolder.trainingId.setText(tmpValues.getTrainId());
			viewHolder.trainingTitle.setText(tmpValues.getTitle());
			// viewHolder.actions.setText("Job Date: " + tmpValues.getAction());
			viewHolder.siteName.setText(tmpValues.getSite());

			if (tmpValues.getTrainingStatus().equalsIgnoreCase("Pending")) {
				viewHolder.trainingComplete.setVisibility(View.VISIBLE);
			} else {
				viewHolder.trainingComplete.setVisibility(View.GONE);
			}

			viewHolder.actions.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((TrainingListFragment) frag).openTrainingDetails(position);
				}
			});

			viewHolder.trainingComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((TrainingListFragment) frag).openTraineePopUp(position);
				}
			});

			if (position % 2 == 1) {
				vi.setBackgroundColor(Color.parseColor("#f6f6f6"));
			} else {
				vi.setBackgroundColor(Color.parseColor("#ffffff"));
			}

		}

		return vi;
	}

}

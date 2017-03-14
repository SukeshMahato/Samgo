package com.app.adapter;

import java.util.ArrayList;

import com.app.fragment.FragmentTraining;
import com.app.model.TrainingPojo;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TrainingListsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<TrainingPojo> jobData = new ArrayList<TrainingPojo>();

	protected Fragment frag;

	protected TrainingPojo tmpValues;
	protected LayoutInflater inflater = null;

	public TrainingListsAdapter(Fragment frag, Activity a, ArrayList<TrainingPojo> data) {
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
		protected TextView job_id;
		protected TextView job_title;
		protected TextView sector_name;
		protected TextView site_name;
		protected TextView due_date;
		protected TextView traning_status;
		protected ImageView action, trainingComplete;

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
			vi = inflater.inflate(R.layout.fragment_training_list_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.job_id = (TextView) vi.findViewById(R.id.job_id);
			viewHolder.job_title = (TextView) vi.findViewById(R.id.job_title);
			viewHolder.sector_name = (TextView) vi.findViewById(R.id.sector_name);
			viewHolder.site_name = (TextView) vi.findViewById(R.id.site_name);
			viewHolder.due_date = (TextView) vi.findViewById(R.id.due_date);
			viewHolder.traning_status = (TextView) vi.findViewById(R.id.training_status);
			viewHolder.action = (ImageView) vi.findViewById(R.id.training_view);
			viewHolder.trainingComplete = (ImageView) vi.findViewById(R.id.training_complete);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

			viewHolder.job_id.setText("No data to display.");
			viewHolder.job_title.setVisibility(View.GONE);
			viewHolder.sector_name.setVisibility(View.GONE);
			viewHolder.site_name.setVisibility(View.GONE);
			viewHolder.due_date.setVisibility(View.GONE);
			viewHolder.traning_status.setVisibility(View.GONE);
			viewHolder.action.setVisibility(View.GONE);
			viewHolder.trainingComplete.setVisibility(View.GONE);

		} else {

			viewHolder.job_title.setVisibility(View.VISIBLE);
			viewHolder.sector_name.setVisibility(View.VISIBLE);
			viewHolder.site_name.setVisibility(View.VISIBLE);
			viewHolder.due_date.setVisibility(View.VISIBLE);
			viewHolder.traning_status.setVisibility(View.VISIBLE);
			viewHolder.action.setVisibility(View.VISIBLE);
			viewHolder.trainingComplete.setVisibility(View.VISIBLE);

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.job_id.setText(tmpValues.getDocket_no());
			viewHolder.job_title.setText(tmpValues.getTitle());
			viewHolder.sector_name.setText(tmpValues.getSectorName());
			viewHolder.site_name.setText(tmpValues.getSite());
			viewHolder.due_date.setText(tmpValues.getDue_date());
			viewHolder.traning_status.setText(tmpValues.getTrainingStatus());
			// viewHolder.action.setText(tmpValues.getAction());
			if (tmpValues.getTrainingStatus().equalsIgnoreCase("Pending")) {
				viewHolder.trainingComplete.setVisibility(View.VISIBLE);
			} else {
				viewHolder.trainingComplete.setVisibility(View.GONE);
			}

			viewHolder.action.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.e("TAG", "Position >>> " + position);

					((FragmentTraining) frag).openTrainingDetails(position);
				}
			});

			viewHolder.trainingComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((FragmentTraining) frag).openTraineePopUp(position);
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

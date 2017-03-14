package com.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.app.fragment.FragmentJobs;
import com.app.model.Job;
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

public class JobListsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Job> jobData = new ArrayList<Job>();

	protected Job tmpValues;

	protected static LayoutInflater inflater = null;
	protected Fragment fr;

	public JobListsAdapter(Activity a, ArrayList<Job> data, FragmentJobs frag) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.jobData = data;
		this.fr = frag;

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
		protected TextView jobid;
		protected TextView jobTitle;
		protected TextView sectorName;
		protected TextView siteName;
		protected TextView dueDate;
		protected TextView jobStatus;
		protected TextView action;
		protected ImageView view, complete, start, docket;

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
			vi = inflater.inflate(R.layout.fragment_job_list_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.jobid = (TextView) vi.findViewById(R.id.job_id);
			viewHolder.jobTitle = (TextView) vi.findViewById(R.id.job_title);
			viewHolder.sectorName = (TextView) vi.findViewById(R.id.sector_name);
			viewHolder.siteName = (TextView) vi.findViewById(R.id.site_name);
			viewHolder.dueDate = (TextView) vi.findViewById(R.id.due_date);
			viewHolder.jobStatus = (TextView) vi.findViewById(R.id.job_status);
			viewHolder.view = (ImageView) vi.findViewById(R.id.view);
			viewHolder.start = (ImageView) vi.findViewById(R.id.start);
			viewHolder.complete = (ImageView) vi.findViewById(R.id.complete);
			viewHolder.docket = (ImageView) vi.findViewById(R.id.docket);

			/************ Set holder with LayoutInflater ************/

			viewHolder.view.setTag(position);
			viewHolder.start.setTag(position);
			viewHolder.complete.setTag(position);
			viewHolder.docket.setTag(position);

			vi.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

			viewHolder.jobid.setText("No data to display.");
			viewHolder.jobTitle.setVisibility(View.GONE);
			viewHolder.sectorName.setVisibility(View.GONE);
			viewHolder.siteName.setVisibility(View.GONE);
			viewHolder.dueDate.setVisibility(View.GONE);
			viewHolder.jobStatus.setVisibility(View.GONE);
			viewHolder.view.setVisibility(View.GONE);
			viewHolder.start.setVisibility(View.GONE);
			viewHolder.complete.setVisibility(View.GONE);
			viewHolder.docket.setVisibility(View.GONE);

		} else {

			viewHolder.jobTitle.setVisibility(View.VISIBLE);
			viewHolder.sectorName.setVisibility(View.VISIBLE);
			viewHolder.siteName.setVisibility(View.VISIBLE);
			viewHolder.dueDate.setVisibility(View.VISIBLE);
			viewHolder.jobStatus.setVisibility(View.VISIBLE);
			viewHolder.view.setVisibility(View.VISIBLE);
			viewHolder.start.setVisibility(View.VISIBLE);
			viewHolder.complete.setVisibility(View.VISIBLE);
			viewHolder.docket.setVisibility(View.VISIBLE);

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.jobid.setText(tmpValues.getJobId());
			viewHolder.jobTitle.setText(tmpValues.getJobTitle());
			viewHolder.sectorName.setText(tmpValues.getSectorName());
			viewHolder.dueDate.setText(tmpValues.getDueDate());
			viewHolder.siteName.setText(tmpValues.getSiteName());
			viewHolder.jobStatus.setText(tmpValues.getJobStatus());

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
				Date strDate = sdf.parse(tmpValues.getDueDate());

				if (new Date().after(strDate)) {
					if (tmpValues.getJobStatus().equalsIgnoreCase("pending")) {

						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.start.setVisibility(View.VISIBLE);
						viewHolder.complete.setVisibility(View.GONE);
						viewHolder.docket.setVisibility(View.GONE);

					} else if (tmpValues.getJobStatus().equalsIgnoreCase("inprogress")) {

						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.complete.setVisibility(View.VISIBLE);
						viewHolder.docket.setVisibility(View.VISIBLE);
						viewHolder.start.setVisibility(View.GONE);

					} else if (tmpValues.getJobStatus().equalsIgnoreCase("completed by eng")) {

						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.docket.setVisibility(View.VISIBLE);
						viewHolder.start.setVisibility(View.GONE);
						viewHolder.complete.setVisibility(View.GONE);

					} else if (tmpValues.getJobStatus().equalsIgnoreCase("Closed")) {

						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.docket.setVisibility(View.GONE);
						viewHolder.start.setVisibility(View.GONE);
						viewHolder.complete.setVisibility(View.GONE);

					} else if (tmpValues.getJobStatus().equalsIgnoreCase("Call Back")) {

						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.docket.setVisibility(View.VISIBLE);
						viewHolder.start.setVisibility(View.GONE);
						viewHolder.complete.setVisibility(View.VISIBLE);

					} else {
						viewHolder.view.setVisibility(View.VISIBLE);
						viewHolder.docket.setVisibility(View.GONE);
						viewHolder.start.setVisibility(View.GONE);
						viewHolder.complete.setVisibility(View.GONE);
					}
				} else {
					viewHolder.view.setVisibility(View.VISIBLE);
					viewHolder.docket.setVisibility(View.GONE);
					viewHolder.start.setVisibility(View.GONE);
					viewHolder.complete.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			viewHolder.view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.e("TAG", "Position >>> " + position);
					((FragmentJobs) fr).openViewJobActivity(position);

				}
			});
			viewHolder.start.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.e("TAG", "Position >>> " + position);
					((FragmentJobs) fr).openStartJobActivity(position);

				}
			});

			viewHolder.complete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.e("TAG", "Position >>> " + position);
					((FragmentJobs) fr).completeJobStatusPopUp(position);

				}
			});

			viewHolder.docket.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					// Toast.makeText(v.getContext(), "Clicked Laugh
					// Vote::"+position, Toast.LENGTH_SHORT).show();

					((FragmentJobs) fr).openCreateDocketActivity(position);

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

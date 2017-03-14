package com.app.adapter;

import java.util.ArrayList;

import com.app.model.JobDateWisePojo;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JobDateWiseAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<JobDateWisePojo> jobData = new ArrayList<JobDateWisePojo>();

	protected JobDateWisePojo tmpValues;

	protected static LayoutInflater inflater = null;

	public JobDateWiseAdapter(Activity a, ArrayList<JobDateWisePojo> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.jobData = data;

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
		protected TextView jobId;
		protected TextView jobTitle;
		protected TextView siteName;
		protected TextView siteAddress;
		protected TextView status;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		ViewHolder viewHolder;

		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater.inflate(R.layout.job_date_wise_one_row_view, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.jobId = (TextView) vi.findViewById(R.id.job_id);
			viewHolder.jobTitle = (TextView) vi.findViewById(R.id.job_title);
			viewHolder.siteName = (TextView) vi.findViewById(R.id.site_name);
			viewHolder.siteAddress = (TextView) vi.findViewById(R.id.site_address);
			viewHolder.status = (TextView) vi.findViewById(R.id.status);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.jobId.setText(tmpValues.getJobId());
			viewHolder.jobTitle.setText(tmpValues.getJobTitle());

			viewHolder.siteName.setText(tmpValues.getSiteName());
			viewHolder.siteAddress.setText(tmpValues.getSiteAdd());
			viewHolder.status.setText(tmpValues.getStatus());

			if (position % 2 == 1) {
				vi.setBackgroundColor(Color.parseColor("#f6f6f6"));
			} else {
				vi.setBackgroundColor(Color.parseColor("#ffffff"));
			}

		}

		return vi;
	}

}

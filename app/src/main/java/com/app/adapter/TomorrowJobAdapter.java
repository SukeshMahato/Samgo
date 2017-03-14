package com.app.adapter;

import java.util.ArrayList;

import com.app.fragment.TomorrowJobFragment;
import com.app.model.Job;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TomorrowJobAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Job> jobData = new ArrayList<Job>();

	protected Job tmpValues;

	protected Fragment frag;

	protected static LayoutInflater inflater = null;
	int count = 0;

	public TomorrowJobAdapter(Fragment f, Activity a, ArrayList<Job> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.jobData = data;
		this.frag = f;

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
		protected TextView jobNo;
		protected TextView jobTitle;
		protected TextView jobDate;
		protected TextView clientName;
		protected TextView siteName;
		protected TextView siteAddress;
		protected Button viewTaskButton;

		protected RelativeLayout first_layout;
		protected LinearLayout second_layout;
		protected LinearLayout third_layout;
		protected LinearLayout fourth_layout;

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
			vi = inflater.inflate(R.layout.tomorrows_job_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.jobNo = (TextView) vi.findViewById(R.id.job_no_text);
			viewHolder.jobDate = (TextView) vi.findViewById(R.id.job_date_text);
			viewHolder.jobTitle = (TextView) vi.findViewById(R.id.job_title_text);
			viewHolder.clientName = (TextView) vi.findViewById(R.id.client_name_text);
			viewHolder.siteName = (TextView) vi.findViewById(R.id.site_name_text);
			viewHolder.siteAddress = (TextView) vi.findViewById(R.id.site_address_text);
			viewHolder.viewTaskButton = (Button) vi.findViewById(R.id.view_task_button);

			viewHolder.first_layout = (RelativeLayout) vi.findViewById(R.id.second_layout);
			viewHolder.second_layout = (LinearLayout) vi.findViewById(R.id.first_layout);
			viewHolder.third_layout = (LinearLayout) vi.findViewById(R.id.second_layout2);
			viewHolder.fourth_layout = (LinearLayout) vi.findViewById(R.id.third_layout);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

			viewHolder.first_layout.setVisibility(View.GONE);
			viewHolder.second_layout.setVisibility(View.GONE);
			viewHolder.third_layout.setVisibility(View.GONE);
			viewHolder.fourth_layout.setVisibility(View.VISIBLE);

		} else {

			viewHolder.first_layout.setVisibility(View.VISIBLE);
			viewHolder.second_layout.setVisibility(View.VISIBLE);
			viewHolder.third_layout.setVisibility(View.VISIBLE);
			viewHolder.fourth_layout.setVisibility(View.GONE);

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.jobNo.setText(tmpValues.getJobId());
			viewHolder.jobTitle.setText(tmpValues.getJobTitle());
			viewHolder.jobDate.setText(tmpValues.getJob_date());
			viewHolder.clientName.setText(tmpValues.getClient_name());
			viewHolder.siteName.setText(tmpValues.getSiteName());
			viewHolder.siteAddress.setText(tmpValues.getSiteAddress());

			viewHolder.viewTaskButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((TomorrowJobFragment) frag).openPopupForTaskDetails(position);
				}
			});

		}

		return vi;
	}

}
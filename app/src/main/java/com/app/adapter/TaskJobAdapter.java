package com.app.adapter;

import java.util.ArrayList;

import com.app.model.TaskItem;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskJobAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<TaskItem> jobData = new ArrayList<TaskItem>();

	protected TaskItem tmpValues;

	protected static LayoutInflater inflater = null;

	public TaskJobAdapter(Activity a, ArrayList<TaskItem> data) {
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
		protected TextView mSerialNo;
		protected TextView mErrorCode;
		protected TextView mDescription;

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
			vi = inflater.inflate(R.layout.start_job_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			/**
			 * getting id of row.xml
			 */

			viewHolder = new ViewHolder();
			viewHolder.mSerialNo = (TextView) vi.findViewById(R.id.serial_no_text);
			viewHolder.mErrorCode = (TextView) vi.findViewById(R.id.error_code_text);
			viewHolder.mDescription = (TextView) vi.findViewById(R.id.description_text);

			vi.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}

		if (jobData.size() <= 0) {

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.mSerialNo.setText(tmpValues.getSerialNo());
			viewHolder.mErrorCode.setText(tmpValues.getSerialNo());
			viewHolder.mDescription.setText(tmpValues.getSerialNo());

		}

		return null;
	}

}

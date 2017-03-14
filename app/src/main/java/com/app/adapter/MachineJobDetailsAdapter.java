package com.app.adapter;

import java.util.ArrayList;

import com.app.model.MachineDesc;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MachineJobDetailsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<MachineDesc> jobData = new ArrayList<MachineDesc>();

	protected MachineDesc tmpValues;
	int count = 0;

	protected static LayoutInflater inflater = null;

	public MachineJobDetailsAdapter(Activity a, ArrayList<MachineDesc> data) {
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
		protected TextView machineSR;
		protected TextView desc;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		ViewHolder viewHolder;

		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater.inflate(R.layout.todays_job_machine_description_one_row, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.machineSR = (TextView) vi.findViewById(R.id.machine_sr_text);
			viewHolder.desc = (TextView) vi.findViewById(R.id.description_text);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {
			viewHolder.machineSR.setVisibility(View.GONE);
			viewHolder.desc.setText("No Data to display");
		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			viewHolder.machineSR.setVisibility(View.VISIBLE);

			/************ Set Model values in Holder elements ***********/

			viewHolder.machineSR.setText(tmpValues.getMachineSr());
			viewHolder.desc.setText(tmpValues.getMachineDesc());

		}

		return vi;
	}

}

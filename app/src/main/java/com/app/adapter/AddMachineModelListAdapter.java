package com.app.adapter;

import java.util.ArrayList;

import com.app.model.MachineModel;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddMachineModelListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<MachineModel> machineModelList = new ArrayList<MachineModel>();
	protected static LayoutInflater inflater = null;
	protected MachineModel tmpValues;

	public AddMachineModelListAdapter(Activity a, ArrayList<MachineModel> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.machineModelList = data;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		protected TextView machine_model;

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
			vi = inflater.inflate(R.layout.machine_one_row_model, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/
			viewHolder = new ViewHolder();
			viewHolder.machine_model = (TextView) vi.findViewById(R.id.machine_model);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (machineModelList.size() <= 0) {
			viewHolder.machine_model.setText("No Records Found");

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = machineModelList.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.machine_model.setText(tmpValues.getModel_name());

			if (position % 2 == 1) {
				vi.setBackgroundColor(Color.parseColor("#f6f6f6"));
			} else {
				vi.setBackgroundColor(Color.parseColor("#ffffff"));
			}

		}

		return vi;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (machineModelList.size() <= 0)
			return 1;
		return machineModelList.size();
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

}

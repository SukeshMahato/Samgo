package com.app.adapter;

import java.util.ArrayList;

import com.app.model.MachineManufacturer;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddMachineListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<MachineManufacturer> machineManufacturer = new ArrayList<MachineManufacturer>();
	protected static LayoutInflater inflater = null;
	protected MachineManufacturer tmpValues;

	public AddMachineListAdapter(Activity a, ArrayList<MachineManufacturer> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.machineManufacturer = data;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		protected TextView manufacturer_id;
		protected TextView manufacturer_name;

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
			vi = inflater.inflate(R.layout.manufacturer_one_row_name, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/
			viewHolder = new ViewHolder();
			viewHolder.manufacturer_name = (TextView) vi.findViewById(R.id.manufacturer_name_text);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (machineManufacturer.size() <= 0) {
			viewHolder.manufacturer_name.setText("No Records Found");

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = machineManufacturer.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.manufacturer_name.setText(tmpValues.getManufacturer_name());

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
		if (machineManufacturer.size() <= 0)
			return 1;
		return machineManufacturer.size();
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

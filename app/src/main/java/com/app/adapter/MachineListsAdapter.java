package com.app.adapter;

import java.util.ArrayList;

import com.app.fragment.FragmentMachines;
import com.app.model.Machine;
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

public class MachineListsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Machine> jobData = new ArrayList<Machine>();

	protected Machine tmpValues;

	protected static LayoutInflater inflater = null;
	protected Fragment frag;

	public MachineListsAdapter(Fragment f, Activity a, ArrayList<Machine> data) {
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
		protected TextView machine_si_no;
		protected TextView machine_name;
		protected TextView machine_type;
		protected TextView manufacturer;
		protected TextView site_name;
		protected TextView marks;
		protected ImageView action;

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
			vi = inflater.inflate(R.layout.fragment_machine_list_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.machine_si_no = (TextView) vi.findViewById(R.id.machine_si_no);
			viewHolder.machine_name = (TextView) vi.findViewById(R.id.machine_name);
			viewHolder.machine_type = (TextView) vi.findViewById(R.id.machine_type);
			viewHolder.manufacturer = (TextView) vi.findViewById(R.id.manufacturer);
			viewHolder.site_name = (TextView) vi.findViewById(R.id.site_name);
			viewHolder.marks = (TextView) vi.findViewById(R.id.marks);
			viewHolder.action = (ImageView) vi.findViewById(R.id.machine_view);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {

			viewHolder.machine_si_no.setText("No Item is available..");
			viewHolder.machine_name.setVisibility(View.GONE);
			viewHolder.machine_type.setVisibility(View.GONE);
			viewHolder.manufacturer.setVisibility(View.GONE);
			viewHolder.site_name.setVisibility(View.GONE);
			viewHolder.marks.setVisibility(View.GONE);
			viewHolder.action.setVisibility(View.GONE);

		} else {

			viewHolder.machine_name.setVisibility(View.VISIBLE);
			viewHolder.machine_type.setVisibility(View.VISIBLE);
			viewHolder.manufacturer.setVisibility(View.VISIBLE);
			viewHolder.site_name.setVisibility(View.VISIBLE);
			viewHolder.marks.setVisibility(View.VISIBLE);
			viewHolder.action.setVisibility(View.VISIBLE);

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.machine_si_no.setText(tmpValues.getMachine_si_no());
			viewHolder.machine_name.setText(tmpValues.getMachine_name());
			viewHolder.machine_type.setText(tmpValues.getMachine_type());
			viewHolder.manufacturer.setText(tmpValues.getManufacturer());
			viewHolder.site_name.setText(tmpValues.getSite_name());
			viewHolder.marks.setText(tmpValues.getMarks());
			// viewHolder.action.setText(tmpValues.getAction());

			viewHolder.action.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((FragmentMachines) frag).openMachineDetailsActivity(position);
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

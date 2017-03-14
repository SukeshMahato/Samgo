package com.app.adapter;

import java.util.ArrayList;

import com.app.model.ErrorCodeModel;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddErrorCodeListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ErrorCodeModel> errorCodeArrayList = new ArrayList<ErrorCodeModel>();
	protected static LayoutInflater inflater = null;
	protected ErrorCodeModel tmpValues;

	public AddErrorCodeListAdapter(Activity a, ArrayList<ErrorCodeModel> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.errorCodeArrayList = data;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {

		protected TextView common_name;

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
			vi = inflater.inflate(R.layout.common_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/
			viewHolder = new ViewHolder();
			viewHolder.common_name = (TextView) vi.findViewById(R.id.common_name);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (errorCodeArrayList.size() <= 0) {
			viewHolder.common_name.setText("No Records Found");

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = errorCodeArrayList.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.common_name.setText(tmpValues.getProblem());

		}

		return vi;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (errorCodeArrayList.size() <= 0)
			return 1;
		return errorCodeArrayList.size();
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

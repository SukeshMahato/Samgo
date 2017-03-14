package com.app.adapter;

import java.util.ArrayList;

import com.app.model.CountryModel;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddCountryListAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<CountryModel> countryList = new ArrayList<CountryModel>();
	protected static LayoutInflater inflater = null;
	protected CountryModel tmpValues;

	public AddCountryListAdapter(Activity a, ArrayList<CountryModel> data) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.countryList = data;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		protected TextView country_List;

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
			vi = inflater.inflate(R.layout.country_one_row_model, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/
			viewHolder = new ViewHolder();
			viewHolder.country_List = (TextView) vi.findViewById(R.id.country_list);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (countryList.size() <= 0) {
			viewHolder.country_List.setText("No Records Found");

		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = countryList.get(position);

			/************ Set Model values in Holder elements ***********/
			
			Log.e("TAG", "Country Name111>>"+tmpValues.getCountryName());

			viewHolder.country_List.setText(tmpValues.getCountryName());

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
		if (countryList.size() <= 0)
			return 1;
		return countryList.size();
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

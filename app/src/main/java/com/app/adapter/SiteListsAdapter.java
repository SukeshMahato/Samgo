package com.app.adapter;

import java.util.ArrayList;

import com.app.fragment.FragmentSites;
import com.app.model.Site;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class SiteListsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Site> jobData = new ArrayList<Site>();

	protected Site tmpValues;

	protected static LayoutInflater inflater = null;

	private Fragment frag;

	public SiteListsAdapter(Fragment f, Activity a, ArrayList<Site> data) {
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
		protected TextView name;
		protected TextView city;
		protected TextView county;
		protected ImageView photo;
		protected ImageView sites_view, sites_location, sites_add_machine;
		protected LinearLayout actionImages;

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
			vi = inflater.inflate(R.layout.fragment_site_list_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) vi.findViewById(R.id.name);
			viewHolder.city = (TextView) vi.findViewById(R.id.city);
			viewHolder.county = (TextView) vi.findViewById(R.id.county);
			viewHolder.photo = (ImageView) vi.findViewById(R.id.photo);
			viewHolder.sites_view = (ImageView) vi.findViewById(R.id.sites_view);
			viewHolder.sites_location = (ImageView) vi.findViewById(R.id.sites_location);
			viewHolder.sites_add_machine = (ImageView) vi.findViewById(R.id.sites_add_machine);
			viewHolder.actionImages = (LinearLayout) vi.findViewById(R.id.action_image);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (jobData.size() <= 0) {
			viewHolder.name.setText("No data to display.");
			viewHolder.photo.setVisibility(View.GONE);
			viewHolder.actionImages.setVisibility(View.GONE);
			viewHolder.county.setVisibility(View.GONE);
			viewHolder.city.setVisibility(View.GONE);
		} else {
			/***** Get each Model object from Arraylist ********/
			tmpValues = null;
			tmpValues = jobData.get(position);

			/************ Set Model values in Holder elements ***********/

			viewHolder.name.setText(tmpValues.getName());
			viewHolder.city.setText(tmpValues.getCity());
			viewHolder.county.setText(tmpValues.getCounty());
			viewHolder.sites_view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.e("TAG", "Position >>> " + position);
					((FragmentSites) frag).openViewSiteActivity(position);
				}
			});
			viewHolder.sites_add_machine.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.e("TAG", "Position >>> " + position);
					((FragmentSites) frag).openAddMachineSiteActivity(position);
				}
			});
			viewHolder.sites_location.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.e("TAG", "Position >>> " + position);
					((FragmentSites) frag).openSiteLocationActivity(position);
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

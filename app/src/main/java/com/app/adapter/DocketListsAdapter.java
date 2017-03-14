package com.app.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.app.database.SamgoSQLOpenHelper;
import com.app.fragment.FragmentDocketList;
import com.app.model.DocketList;
import com.app.model.Job;
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
import android.widget.TextView;

public class DocketListsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<DocketList> docketData = new ArrayList<DocketList>();

	protected DocketList tmpValues;

	protected static LayoutInflater inflater = null;
	protected Fragment fr;
	protected SamgoSQLOpenHelper db;

	public DocketListsAdapter(Activity a, ArrayList<DocketList> data, FragmentDocketList frag, SamgoSQLOpenHelper db) {
		// TODO Auto-generated constructor stub
		this.activity = a;
		this.docketData = data;
		this.fr = frag;
		this.db = db;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (docketData.size() <= 0)
			return 1;
		return docketData.size();
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
		protected TextView jobid;
		protected TextView jobTitle;
		protected TextView clientName;
		protected TextView siteName;
		protected TextView docketDate;
		protected TextView docketStatus;
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
			vi = inflater.inflate(R.layout.fragment_docket_list_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.jobid = (TextView) vi.findViewById(R.id.job_id);
			viewHolder.jobTitle = (TextView) vi.findViewById(R.id.job_title);
			viewHolder.clientName = (TextView) vi.findViewById(R.id.client_name);
			viewHolder.siteName = (TextView) vi.findViewById(R.id.site_name);
			viewHolder.docketDate = (TextView) vi.findViewById(R.id.docket_date);
			viewHolder.docketStatus = (TextView) vi.findViewById(R.id.docket_status);
			viewHolder.action = (ImageView) vi.findViewById(R.id.edit_docket);

			/************ Set holder with LayoutInflater ************/

			viewHolder.action.setTag(position);

			vi.setTag(viewHolder);

		} else
			viewHolder = (ViewHolder) vi.getTag();

		if (docketData.size() <= 0) {

			viewHolder.jobid.setText("No data to display.");
			viewHolder.jobTitle.setVisibility(View.GONE);
			viewHolder.clientName.setVisibility(View.GONE);
			viewHolder.siteName.setVisibility(View.GONE);
			viewHolder.docketDate.setVisibility(View.GONE);
			viewHolder.docketStatus.setVisibility(View.GONE);
			viewHolder.action.setVisibility(View.GONE);

		} else {

			viewHolder.jobTitle.setVisibility(View.VISIBLE);
			viewHolder.clientName.setVisibility(View.VISIBLE);
			viewHolder.siteName.setVisibility(View.VISIBLE);
			viewHolder.docketDate.setVisibility(View.VISIBLE);
			viewHolder.docketStatus.setVisibility(View.VISIBLE);
			viewHolder.action.setVisibility(View.VISIBLE);

			/***** Get each Model object from Arraylist ********/
			tmpValues = null;

			tmpValues = docketData.get(position);

			/************ Set Model values in Holder elements ***********/

			Job jobValues = db.getJobItemByJobId(tmpValues.getJob_id());

			viewHolder.jobid.setText(jobValues.getJobId());
			viewHolder.jobTitle.setText(jobValues.getJobTitle());
			viewHolder.clientName.setText(jobValues.getClient_name());
			viewHolder.docketDate.setText(getFormattedDateString(tmpValues.getDocket_date()));
			viewHolder.siteName.setText(jobValues.getSiteName());
			viewHolder.docketStatus.setText("Awaiting Approval");

			viewHolder.action.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Log.e("TAG", "Position >>> " + position);

					((FragmentDocketList) fr).openDocketListPage(position);

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

	private String getFormattedDateString(String dateString) {
		String date = "";
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			Date newDate = sdf.parse(dateString);

			sdf = new SimpleDateFormat("dd/MM/yy");
			date = sdf.format(newDate);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return date;

	}

}

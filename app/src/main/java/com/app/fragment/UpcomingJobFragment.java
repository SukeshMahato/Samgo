package com.app.fragment;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.adapter.CalendarAdapter;
import com.app.asyncs.UpcomingJobServices;
import com.app.listners.UpcomingJobsListener;
import com.app.model.AppManager;
import com.app.samgo.JobDateWiseActivity;
import com.app.samgo.R;
import com.app.services.ConnectionDetector;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpcomingJobFragment extends Fragment implements UpcomingJobsListener {

	View v;

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;
	public ArrayList<String> items = new ArrayList<String>();
	public ArrayList<String> itemCount = new ArrayList<String>();
	// needs showing the event marker
	ArrayList<String> event;
	LinearLayout rLayout;
	ArrayList<String> date;
	ArrayList<String> desc;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_upcoming_job, container, false);

		Locale.setDefault(Locale.US);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();

		adapter = new CalendarAdapter(getContext(), month);

		GridView gridview = (GridView) v.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		// creating connection detector class instance
		cd = new ConnectionDetector(getContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		/**
		 * Service call for calendar
		 */

		if (isInternetPresent) {
			new UpcomingJobServices(this, AppManager.getSinleton().getUser().getId(), getActivity()).execute();
		}

		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) v.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("TAG", "Size >> " + items.size());
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout) v.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("TAG", "Size >> " + items.size());
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, View v, int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);

				// showToast(selectedGridDate);

				if (items.contains(selectedGridDate)) {
					// showToast("containing events");
					Intent gotoJobListDateWise = new Intent(getContext(), JobDateWiseActivity.class);
					gotoJobListDateWise.putExtra("SELECTEDDATE", selectedGridDate);
					getActivity().startActivity(gotoJobListDateWise);
				} else {
					// showToast("not containing events");
				}

			}
		});

		return v;
	}

	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) v.findViewById(R.id.title);
		adapter.refreshDays();
		Log.e("TAG", "Size >> " + items.size());

		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {

			Log.e("TAG", "Size >> " + items.size());

			adapter.setItems(items, itemCount);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public void getUpcomingJobResponse(String RequestMessage) {
		// TODO Auto-generated method stub
		if (RequestMessage != null) {

			Log.e("TAG", "Response upcoming >> " + RequestMessage);

			try {
				JSONArray jArr = new JSONArray(RequestMessage);
				items.clear();
				for (int i = 0; i < jArr.length(); i++) {

					String jsonOneRow = jArr.getString(i);

					JSONObject jsonObj = new JSONObject(jsonOneRow);

					String date_string = jsonObj.getString("due_date");
					String eventCount = jsonObj.getString("total_job");

					items.add(date_string);

					itemCount.add(eventCount);

				}

				handler = new Handler();
				handler.post(calendarUpdater);

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}

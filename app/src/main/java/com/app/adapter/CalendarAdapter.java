package com.app.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.app.samgo.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {

	private Context mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;

	private ArrayList<String> items;
	private ArrayList<String> itemCount;
	public static List<String> dayString;
	private View previousView;

	public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;

		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);

		this.items = new ArrayList<String>();
		this.itemCount = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());

		refreshDays();
	}

	public void setItems(ArrayList<String> items, ArrayList<String> count) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				count.get(i);
				items.set(i, "0" + items.get(i));
				count.set(i, count.get(i));
			}
		}
		this.items = items;
		this.itemCount = count;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayString.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dayString.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.date);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");

		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			dayView.setTextColor(Color.parseColor("#CECECE"));
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.parseColor("#CECECE"));
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {
			// setting current month's days in blue color.
			if (dayString.get(position).equals(curentDateString)) {
				dayView.setTextColor(Color.parseColor("#ffffff"));
			} else {
				dayView.setTextColor(Color.parseColor("#344364"));
			}
		}

		if (dayString.get(position).equals(curentDateString)) {
			setSelected(v);
			previousView = v;
		} else {
			v.setBackgroundResource(R.drawable.list_item_background);
		}
		dayView.setText(gridvalue);

		// create date string for comparison
		String date = dayString.get(position);

		if (date.length() == 1) {
			date = "0" + date;
		}

		String monthStr = "" + (month.get(GregorianCalendar.MONTH));
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}

		// show icon if date is not empty and it exists in the items array
		LinearLayout iw = (LinearLayout) v.findViewById(R.id.date_icon_layout);
		TextView tv = (TextView) v.findViewById(R.id.day_count);
		if (date.length() > 0 && items != null && items.contains(date)) {
			int count = items.indexOf(date);

			String countNoOfJob = itemCount.get(count);

			Log.e("TAG", "Calendar count >> " + countNoOfJob);

			iw.setVisibility(View.VISIBLE);
			tv.setText(countNoOfJob);

		} else {
			iw.setVisibility(View.INVISIBLE);
		}
		return v;
	}

	public View setSelected(View view) {
		if (previousView != null) {
			previousView.setBackgroundResource(R.drawable.list_item_background);

			int childcount = ((LinearLayout) previousView).getChildCount();

			Log.e("TAG", "count >>> " + childcount);

			View v = ((LinearLayout) previousView).getChildAt(0);

			((TextView) v).setTextColor(Color.parseColor("#344364"));
		}
		previousView = view;

		int childcount = ((LinearLayout) view).getChildCount();

		Log.e("TAG", "count >>> " + childcount);

		View v = ((LinearLayout) view).getChildAt(0);

		((TextView) v).setTextColor(Color.parseColor("#ffffff"));

		view.setBackgroundResource(R.drawable.machine_tab_background_selector);
		return view;
	}

	public void refreshDays() {
		// clear items
		// items.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1), month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}

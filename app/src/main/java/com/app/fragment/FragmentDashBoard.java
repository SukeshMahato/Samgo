package com.app.fragment;

import java.util.List;
import java.util.Vector;

import com.app.adapter.MyFragmentPageAdapter;
import com.app.model.Config;
import com.app.samgo.R;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class FragmentDashBoard extends Fragment implements OnPageChangeListener, OnTabChangeListener {

	View rootView;

	private ViewPager viewPager;
	private TabHost tabHost;
	private MyFragmentPageAdapter myFragmentPageAdapter;
	private FrameLayout parentLayout;
	int i = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.tabs_viewpager_layout, container, false);
		i++;
		this.initializeViewPager();
		this.initializeTabHost();
		parentLayout = (FrameLayout) rootView.findViewById(android.R.id.tabcontent);
		SharedPreferences settings = getActivity().getSharedPreferences(Config.MYFREFS, 0);
		String background_image = settings.getString("background_image", "test");
		Log.e("TAG", "background_image >> " + background_image);
		if (background_image != null) {
			if (background_image.equalsIgnoreCase("background_1")) {
				parentLayout.setBackgroundResource(R.drawable.background_1);
			} else if (background_image.equalsIgnoreCase("background_2")) {
				parentLayout.setBackgroundResource(R.drawable.background_2);
			} else if (background_image.equalsIgnoreCase("background_3")) {
				parentLayout.setBackgroundResource(R.drawable.background_3);
			} else if (background_image.equalsIgnoreCase("background_4")) {
				parentLayout.setBackgroundResource(R.drawable.background_4);
			} else if (background_image.equalsIgnoreCase("background_5")) {
				parentLayout.setBackgroundColor(color.white);
			}
		}
		return rootView;
	}

	private void initializeTabHost() {
		// TODO Auto-generated method stub
		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();

		String[] tabNames = { "Today's Job", "Tomorrow's Job", "Training List", "Upcoming Jobs", "Scan Qr Code" };

		for (int i = 0; i < tabNames.length; i++) {
			TabHost.TabSpec tabSpec;
			tabSpec = tabHost.newTabSpec(tabNames[i]);
			tabSpec.setIndicator(tabNames[i]);
			tabSpec.setContent(new FakeContent(getActivity()));
			tabHost.addTab(tabSpec);

			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); // Unselected
																											// Tabs
			tv.setTextColor(Color.parseColor("#ffffff"));
		}

		tabHost.setOnTabChangedListener(this);
	}

	@SuppressWarnings("deprecation")
	private void initializeViewPager() {
		// TODO Auto-generated method stub
		this.viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

		List<Fragment> listFragments = new Vector<Fragment>();
		listFragments.add(new TodayJobFragment());
		listFragments.add(new TomorrowJobFragment());
		listFragments.add(new TrainingListFragment());
		listFragments.add(new UpcomingJobFragment());
		listFragments.add(new FragmentScanQrCode());
		this.myFragmentPageAdapter = new MyFragmentPageAdapter(getChildFragmentManager(), listFragments);
		viewPager.setAdapter(myFragmentPageAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	public class FakeContent implements TabContentFactory {

		Context context;

		public FakeContent(Context mContext) {
			// TODO Auto-generated constructor stub
			this.context = mContext;
		}

		@Override
		public View createTabContent(String arg0) {
			// TODO Auto-generated method stub
			View fakeView = new View(context);
			fakeView.setMinimumHeight(0);
			fakeView.setMinimumWidth(0);

			return fakeView;
		}

	}

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		int selectedItem = tabHost.getCurrentTab();
		viewPager.setCurrentItem(selectedItem);

		HorizontalScrollView hScrollView = (HorizontalScrollView) rootView.findViewById(R.id.h_scroll_view);
		View tabView = tabHost.getCurrentTabView();
		int scrollPos = tabHost.getBottom() - (hScrollView.getHeight() - tabView.getHeight()) / 2;
		hScrollView.smoothScrollTo(scrollPos, 0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int selectedItem) {
		// TODO Auto-generated method stub
		tabHost.setCurrentTab(selectedItem);
	}

}

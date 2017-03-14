package com.app.samgo;

import java.util.List;
import java.util.Vector;

import com.app.adapter.MyFragmentPageAdapter;
import com.app.fragment.MachineViewDetails;
import com.app.fragment.MachineViewDocuments;
import com.app.fragment.MachineViewVideos;
import com.app.model.Config;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MachineDetails extends FragmentActivity implements OnPageChangeListener, OnTabChangeListener {

	private ViewPager viewPager;
	private TabHost tabHost;
	private MyFragmentPageAdapter myFragmentPageAdapter;

	private ImageView back_button;

	private FrameLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.machine_details);

		back_button = (ImageView) findViewById(R.id.back_button);

		parentLayout = (FrameLayout) findViewById(android.R.id.tabcontent);

		SharedPreferences settings = getSharedPreferences(Config.MYFREFS, 0);

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

		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		this.initializeViewPager();

		this.initializeTabHost();
	}

	private void initializeTabHost() {
		// TODO Auto-generated method stub
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		String[] tabNames = { "Machine Details", "Machine Videos", "Machine Documents" };

		for (int i = 0; i < tabNames.length; i++) {
			TabHost.TabSpec tabSpec;
			tabSpec = tabHost.newTabSpec(tabNames[i]);
			tabSpec.setIndicator(tabNames[i]);
			tabSpec.setContent(new FakeContent(MachineDetails.this));
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
		this.viewPager = (ViewPager) findViewById(R.id.machine_viewpager);

		List<Fragment> listFragments = new Vector<Fragment>();
		listFragments.add(new MachineViewDetails());
		listFragments.add(new MachineViewVideos());
		listFragments.add(new MachineViewDocuments());

		this.myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), listFragments);
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
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		int selectedItem = tabHost.getCurrentTab();
		viewPager.setCurrentItem(selectedItem);

		HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.h_scroll_view_machine);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
	}
}

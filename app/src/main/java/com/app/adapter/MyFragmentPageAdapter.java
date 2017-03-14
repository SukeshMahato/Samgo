package com.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
	
	List<Fragment> lFragments;

	public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> lFragments) {
		super(fm);
		this.lFragments = lFragments;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return lFragments.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lFragments.size();
	}

}

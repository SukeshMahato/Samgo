package com.app.adapter;

import java.util.List;

import com.app.model.NavItem;
import com.app.samgo.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavListAdapter extends ArrayAdapter<NavItem> {

	Context mContext;

	int resLayout;
	List<NavItem> listNavItems;

	public NavListAdapter(Context context, int resLayout, List<NavItem> listNavItems) {
		super(context, resLayout, listNavItems);

		this.mContext = context;
		this.resLayout = resLayout;
		this.listNavItems = listNavItems;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = View.inflate(mContext, resLayout, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.title);
		ImageView navIcon = (ImageView) view.findViewById(R.id.nav_icon);
		NavItem navItem = listNavItems.get(position);
		tvTitle.setText(navItem.getTitle());
		navIcon.setImageResource(navItem.getResIcon());
				

		return view;
	}

}

package com.app.samgo;

import java.util.ArrayList;
import java.util.List;

import com.app.adapter.NavListAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.fragment.FragmentChangeTheme;
import com.app.fragment.FragmentDashBoard;
import com.app.fragment.FragmentDeleteData;
import com.app.fragment.FragmentDocketList;
import com.app.fragment.FragmentIconsLegion;
import com.app.fragment.FragmentJobs;
import com.app.fragment.FragmentLogout;
import com.app.fragment.FragmentMachines;
import com.app.fragment.FragmentSites;
import com.app.fragment.FragmentSitesEnggProfile;
import com.app.fragment.FragmentTraining;
import com.app.fragment.SyncClass;
import com.app.model.AppManager;
import com.app.model.Config;
import com.app.model.NavItem;
import com.app.model.User;
import com.app.services.GPSLocationService;
import com.app.services.GetSparePartsService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	DrawerLayout mDrawerLayout;
	RelativeLayout drawerPane;

	ListView lavNav;

	List<NavItem> listNavItems;
	List<Fragment> listFragments;

	ActionBarDrawerToggle mDrawerToggle;

	private SamgoSQLOpenHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		startService(new Intent(this, GPSLocationService.class));
		//startService(new Intent(this, GetSparePartsService.class));
		db = new SamgoSQLOpenHelper(getApplicationContext());
		User user = db.getUserDetails();
		Log.e("TAG", "User details >> " + user.getId());
		AppManager.getSinleton().setUser(user);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
		lavNav = (ListView) findViewById(R.id.nav_list);
		listNavItems = new ArrayList<NavItem>();
		listNavItems.add(new NavItem("DASHBOARD", R.drawable.dashboard));
		listNavItems.add(new NavItem("JOBS", R.drawable.jobs));
		listNavItems.add(new NavItem("SITES", R.drawable.sites));
		listNavItems.add(new NavItem("MACHINES", R.drawable.machine));
		listNavItems.add(new NavItem("TRAINING", R.drawable.training));
		listNavItems.add(new NavItem("PROFILE", R.drawable.profile));
		listNavItems.add(new NavItem("SYNC DATA", R.drawable.syndata));
		listNavItems.add(new NavItem("DOCKET LIST", R.drawable.dockt));
		listNavItems.add(new NavItem("CHANGE THEME", R.drawable.dockt));
		listNavItems.add(new NavItem("ICONS LEGEND", R.drawable.dockt));
		listNavItems.add(new NavItem("DELETE DATA", R.drawable.dockt));
		listNavItems.add(new NavItem("LOGOUT", R.drawable.profile));

		NavListAdapter navListAdapter = new NavListAdapter(getApplicationContext(), R.layout.item_nav_list,
				listNavItems);

		lavNav.setAdapter(navListAdapter);
		listFragments = new ArrayList<Fragment>();
		listFragments.add(new FragmentDashBoard());
		listFragments.add(new FragmentJobs());
		listFragments.add(new FragmentSites());
		listFragments.add(new FragmentMachines());
		listFragments.add(new FragmentTraining());
		listFragments.add(new FragmentSitesEnggProfile());
		listFragments.add(new SyncClass());
		listFragments.add(new FragmentDocketList());
		listFragments.add(new FragmentChangeTheme());
		listFragments.add(new FragmentIconsLegion());
		listFragments.add(new FragmentDeleteData());
		listFragments.add(new FragmentLogout());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String extra = extras.getString("FROMSTARTACT");
			if (extra.equalsIgnoreCase("startActivity")) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(1)).commit();
				setTitle(listNavItems.get(1).getTitle());
				lavNav.setItemChecked(1, true);
				mDrawerLayout.closeDrawer(drawerPane);
			}
			if (extra.equalsIgnoreCase("close")) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(1)).commit();
				setTitle(listNavItems.get(1).getTitle());
				lavNav.setItemChecked(1, true);
				mDrawerLayout.closeDrawer(drawerPane);
			}
		} else {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(0)).commit();
			setTitle(listNavItems.get(0).getTitle());
			lavNav.setItemChecked(0, true);
			mDrawerLayout.closeDrawer(drawerPane);
		}

		lavNav.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.main_content, listFragments.get(position)).commit();
				setTitle(listNavItems.get(position).getTitle());
				lavNav.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(drawerPane);
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// turn on the Navigation Drawer image;
		// this is called in the LowerLevelFragments
		mDrawerToggle.setDrawerIndicatorEnabled(true);

		// overridePendingTransition(R.anim.pull_in_right,
		// R.anim.push_out_left);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(getApplicationContext(), "response code >> " + requestCode, Toast.LENGTH_LONG).show();
		if (requestCode == 196720) {
			if (resultCode == Activity.RESULT_OK) {

				String contents = data.getStringExtra("SCAN_RESULT");
				String machineSlNo = db.getMachineSlNoData(contents);
				Toast.makeText(getApplicationContext(), "machine >> " + machineSlNo, Toast.LENGTH_LONG).show();
				if (machineSlNo.equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(),
							"This machine is not available in your device." + requestCode, Toast.LENGTH_LONG).show();
				} else {
					Config.machineSlNo = machineSlNo;
					Intent gotoVideoActivity = new Intent(getApplicationContext(), MachineDetails.class);
					Log.e("TAG", "machineSlNo>>" + machineSlNo);
					startActivity(gotoVideoActivity);
					overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
				}

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
				Log.i("App", "Scan unsuccessful");
			}
		} 

	}

}

package com.app.fragment;

import java.util.ArrayList;

import com.app.adapter.MachineDocumentsAdapter;
import com.app.database.SamgoSQLOpenHelper;
import com.app.model.Config;
import com.app.model.MachineDocumentDetails;
import com.app.model.MachineView;
import com.app.samgo.R;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MachineViewDocuments extends Fragment {

	View v;

	private MachineDocumentsAdapter machineDocumentListAdapter;

	private GridView documentList;

	/**
	 * An array of YouTube videos
	 */
	public ArrayList<MachineDocumentDetails> documentsItem = new ArrayList<MachineDocumentDetails>();

	private SamgoSQLOpenHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.machine_documents, container, false);

		documentList = (GridView) v.findViewById(R.id.grid_documents);

		db = new SamgoSQLOpenHelper(getContext());

		Log.e("TAG", "Machine view >> " + Config.machineSlNo);

		documentsItem.clear();

		MachineView machineView = db.getMachineViewData(Config.machineSlNo);

		Log.e("TAG", "Parts Drawling >> " + machineView.getParts_drawling());

		if (machineView.getParts_drawling().equalsIgnoreCase("")) {

		} else {
			MachineDocumentDetails machineDocDetails = new MachineDocumentDetails();

			machineDocDetails.setFileLink(machineView.getParts_drawling());
			machineDocDetails.setFileTitle("Parts Drawling");
			documentsItem.add(machineDocDetails);
		}

		Log.e("TAG", "User Manual >> " + machineView.getUser_manual());

		if (machineView.getUser_manual().equalsIgnoreCase("")) {

		} else {
			MachineDocumentDetails machineDocDetails = new MachineDocumentDetails();

			machineDocDetails.setFileLink(machineView.getUser_manual());
			machineDocDetails.setFileTitle("User Manual");
			documentsItem.add(machineDocDetails);
		}

		Log.e("TAG", "Data Sheet >> " + machineView.getData_sheet());

		if (machineView.getData_sheet().equalsIgnoreCase("")) {

		} else {
			MachineDocumentDetails machineDocDetails = new MachineDocumentDetails();

			machineDocDetails.setFileLink(machineView.getData_sheet());
			machineDocDetails.setFileTitle("Data Sheet");
			documentsItem.add(machineDocDetails);
		}

		machineDocumentListAdapter = new MachineDocumentsAdapter(getActivity(), documentsItem);
		documentList.setAdapter(machineDocumentListAdapter);
		machineDocumentListAdapter.notifyDataSetChanged();

		documentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long id) {
				// TODO Auto-generated method stub
				try {
					Log.e("TAG", "Link >>> " + documentsItem.get(position).getFileLink());
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(documentsItem.get(position).getFileLink()));
					startActivity(browserIntent);
				} catch (ActivityNotFoundException e) {
					// TODO: handle exception
					Toast.makeText(getContext(), "No App found to open this document.", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});

		return v;
	}

}

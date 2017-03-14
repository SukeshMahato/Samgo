package com.app.listners;

import com.app.adapter.SparePartsAutoCompletePart2Adapter;
import com.app.model.MasterSpareParts;
import com.app.samgo.CreateDocketActivityPart3;
import com.app.samgo.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class CustomAutoCompletePart3TextChangedListeners implements TextWatcher {

	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
	Context context;

	public CustomAutoCompletePart3TextChangedListeners(Context context) {
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence userInput, int start, int before, int count) {
		// TODO Auto-generated method stub

		try {
			// if you want to see in the logcat what the user types
			Log.e(TAG, "User input: CreateDocketActivityPart3 " + userInput);

			if (userInput.length() > 2) {
				CreateDocketActivityPart3 mainActivity = ((CreateDocketActivityPart3) context);

				// update the adapater
				mainActivity.myAdapter.notifyDataSetChanged();

				// get suggestions from the database
				MasterSpareParts[] myObjs = mainActivity.db.getMatchedSpareParts(userInput.toString());

				// update the adapter
				mainActivity.myAdapter = new SparePartsAutoCompletePart2Adapter(mainActivity,
						R.layout.spare_parts_one_row, myObjs);

				mainActivity.spareParts.setAdapter(mainActivity.myAdapter);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.app.listners;

import com.app.adapter.SparePartsAutoCompleteAdapter;
import com.app.model.MasterSpareParts;
import com.app.samgo.CreateDocketActivityPart2;
import com.app.samgo.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class CustomAutoCompleteTextChangedListeners implements TextWatcher {

	public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
	Context context;

	public CustomAutoCompleteTextChangedListeners(Context context) {
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
			Log.e(TAG, "User input: " + userInput);

			if (userInput.length() > 2) {
				CreateDocketActivityPart2 mainActivity = ((CreateDocketActivityPart2) context);

				// update the adapater
				mainActivity.myAdapter.notifyDataSetChanged();

				// get suggestions from the database
				MasterSpareParts[] myObjs = mainActivity.db.getMatchedSpareParts(userInput.toString());
				//MasterSpareParts[] myObjs = mainActivity.db.getBarcodeSpareParts(userInput.toString());
				// update the adapter
				mainActivity.myAdapter = new SparePartsAutoCompleteAdapter(mainActivity, R.layout.spare_parts_one_row,
						myObjs);

				mainActivity.spareParts.setAdapter(mainActivity.myAdapter);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

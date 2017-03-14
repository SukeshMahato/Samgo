package com.app.adapter;

import com.app.model.MasterSpareParts;
import com.app.samgo.CreateDocketActivityPart2;
import com.app.samgo.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SparePartsAutoCompleteAdapter extends ArrayAdapter<MasterSpareParts> {

	final String TAG = "SparePartsAutoCompleteAdapter.java";

	Context mContext;
	int layoutResourceId;
	MasterSpareParts data[] = null;

	public SparePartsAutoCompleteAdapter(Context mContext, int layoutResourceId, MasterSpareParts[] data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			if (convertView == null) {
				// inflate the layout
				LayoutInflater inflater = ((CreateDocketActivityPart2) mContext).getLayoutInflater();
				convertView = inflater.inflate(layoutResourceId, parent, false);
			}

			// object item based on the position
			MasterSpareParts objectItem = data[position];

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView textViewItem = (TextView) convertView.findViewById(R.id.spare_parts_with_desc);
			TextView textViewItem2 = (TextView) convertView.findViewById(R.id.spare_parts_id);
			TextView textViewItem3 = (TextView) convertView.findViewById(R.id.spare_parts_unit_sales);

			Log.e("jointString", "desc >> " + objectItem.getDescription());

			String jointString = objectItem.getDescription() + "||" + objectItem.getQuantity() + "||"
					+ objectItem.getProduct_id();
			textViewItem.setText(jointString);
			Log.e("jointString", "id  >> " + objectItem.getId());
			Log.e("custom", "barcode  >> " + objectItem.getBarcode());
			textViewItem2.setText(objectItem.getId());
			textViewItem3.setText(objectItem.getUnit_sales());

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;

	}
}

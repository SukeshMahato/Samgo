package com.app.adapter;

import java.util.ArrayList;

import com.app.model.SparePartsModel;
import com.app.samgo.CreateDocketActivityPart3;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateDocketSparePartsAdapter extends BaseAdapter {
	protected Activity activity;

	protected static LayoutInflater inflater = null;
	protected SparePartsModel tmpValues;

	protected ArrayList<SparePartsModel> sparePartsArray = new ArrayList<SparePartsModel>();

	public CreateDocketSparePartsAdapter(Activity a, ArrayList<SparePartsModel> sparePartsArray) {
		// TODO Auto-generated constructor stub
		this.activity = a;

		this.sparePartsArray = sparePartsArray;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		protected TextView spare_parts_id, descriptions, quantity, unit_sale_price;
		protected ImageView action_photo_view, action_photo_delete;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		ViewHolder viewHolder;

		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater.inflate(R.layout.create_docket_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/

			viewHolder = new ViewHolder();
			viewHolder.spare_parts_id = (TextView) vi.findViewById(R.id.spare_parts_id);
			viewHolder.descriptions = (TextView) vi.findViewById(R.id.descriptions);
			viewHolder.quantity = (TextView) vi.findViewById(R.id.quantity);
			viewHolder.unit_sale_price = (TextView) vi.findViewById(R.id.unit_sale_price);
			viewHolder.action_photo_view = (ImageView) vi.findViewById(R.id.action_photo_view);
			viewHolder.action_photo_delete = (ImageView) vi.findViewById(R.id.action_photo_delete);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		/***** Get each Model object from Arraylist ********/
		

		if (sparePartsArray.size() <= 0) {
			viewHolder.spare_parts_id.setVisibility(View.GONE);
			viewHolder.descriptions.setVisibility(View.VISIBLE);
			viewHolder.descriptions.setText("No spare parts added yet");
			viewHolder.quantity.setVisibility(View.GONE);
			viewHolder.unit_sale_price.setVisibility(View.GONE);
			viewHolder.action_photo_view.setVisibility(View.GONE);
			viewHolder.action_photo_delete.setVisibility(View.GONE);

		} else {
			tmpValues = null;
			tmpValues = sparePartsArray.get(position);
			viewHolder.spare_parts_id.setVisibility(View.VISIBLE);
			viewHolder.descriptions.setVisibility(View.VISIBLE);
			viewHolder.quantity.setVisibility(View.VISIBLE);
			viewHolder.unit_sale_price.setVisibility(View.VISIBLE);
			viewHolder.action_photo_view.setVisibility(View.VISIBLE);
			viewHolder.action_photo_delete.setVisibility(View.VISIBLE);

			/************ Set Model values in Holder elements ***********/

			viewHolder.spare_parts_id.setText(tmpValues.getSparePartsId());
			viewHolder.descriptions.setText(tmpValues.getDescription());
			viewHolder.quantity.setText(tmpValues.getQuantity());
			viewHolder.unit_sale_price.setText(tmpValues.getUnitSales());

			viewHolder.action_photo_view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((CreateDocketActivityPart3) activity).editSpareParts(position);
				}
			});

			viewHolder.action_photo_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((CreateDocketActivityPart3) activity).deleteSpareParts(position);
				}
			});
		}
		return vi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (sparePartsArray.size() <= 0)
			return 1;
		return sparePartsArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}

package com.app.adapter;

import java.util.ArrayList;

import com.app.database.SamgoSQLOpenHelper;
import com.app.model.DocketMachineDetails;
import com.app.model.SparePartsModel;
import com.app.samgo.CreateDocketActivityPart2;
import com.app.samgo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DocketMachineDetailsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<DocketMachineDetails> docketMachineArray = new ArrayList<DocketMachineDetails>();
	protected static LayoutInflater inflater = null;
	protected DocketMachineDetails tmpValues;
	int pos=0;
    static int k = 0;

	protected ArrayList<SparePartsModel> sparePartsArray = new ArrayList<SparePartsModel>();

	private SamgoSQLOpenHelper db;

	public  DocketMachineDetailsAdapter(Activity a, ArrayList<DocketMachineDetails> data,
			ArrayList<SparePartsModel> sparePartsArray, SamgoSQLOpenHelper db) {
		// TODO Auto-generated constructor stub

		this.activity = a;
		this.docketMachineArray = data;
		this.sparePartsArray = sparePartsArray;
		this.db = db;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public static class ViewHolder {
		protected TextView machine_type, machine_name, machine_model, machine_sl_no;

		protected LinearLayout sparePartsLayout, mLinearSpareDetails, create_docket_heading;

		protected ImageView addSpareParts, viewDocuments, deleteMachine, pickingImage, workDone;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View vi = convertView;

		ViewHolder viewHolder;

		if (convertView == null) {
			/******
			 * Inflate tabitem.xml file for each row ( Defined below )
			 *******/
			vi = inflater.inflate(R.layout.create_docket_page_machine_one_row_item, parent, false);

			/******
			 * View Holder Object to contain tabitem.xml file elements
			 ******/
			viewHolder = new ViewHolder();
			viewHolder.machine_type = (TextView) vi.findViewById(R.id.machine_type);
			viewHolder.machine_name = (TextView) vi.findViewById(R.id.machine_name);
			viewHolder.machine_model = (TextView) vi.findViewById(R.id.machine_model);
			viewHolder.machine_sl_no = (TextView) vi.findViewById(R.id.machine_sl_no);
			viewHolder.sparePartsLayout = (LinearLayout) vi.findViewById(R.id.spare_parts_wrapper);
			viewHolder.mLinearSpareDetails = (LinearLayout) vi.findViewById(R.id.spare_parts_detail);
			viewHolder.addSpareParts = (ImageView) vi.findViewById(R.id.add_spare_parts);
			viewHolder.viewDocuments = (ImageView) vi.findViewById(R.id.view_documents);

			viewHolder.deleteMachine = (ImageView) vi.findViewById(R.id.delete_machine);
			viewHolder.pickingImage = (ImageView) vi.findViewById(R.id.add_pictures);
			viewHolder.workDone = (ImageView) vi.findViewById(R.id.work_done);
			viewHolder.create_docket_heading = (LinearLayout) vi.findViewById(R.id.create_docket_heading);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(viewHolder);
		} else {
            viewHolder = (ViewHolder) vi.getTag();
        }
	Log.e("pos",pos+"");
		if (docketMachineArray.size() <= 0) {
			viewHolder.machine_type.setText("No Records Found");
			viewHolder.machine_name.setVisibility(View.GONE);
			viewHolder.machine_model.setVisibility(View.GONE);
			viewHolder.machine_sl_no.setVisibility(View.GONE);

			viewHolder.addSpareParts.setVisibility(View.GONE);
			viewHolder.viewDocuments.setVisibility(View.GONE);

			viewHolder.deleteMachine.setVisibility(View.GONE);
			viewHolder.pickingImage.setVisibility(View.GONE);
			viewHolder.workDone.setVisibility(View.GONE);

		} else {
            /***** Get each Model object from Arraylist ********/
            tmpValues = null;

            tmpValues = docketMachineArray.get(position);

            /************ Set Model values in Holder elements ***********/

            viewHolder.machine_name.setVisibility(View.VISIBLE);
            viewHolder.machine_model.setVisibility(View.VISIBLE);
            viewHolder.machine_sl_no.setVisibility(View.VISIBLE);

            viewHolder.addSpareParts.setVisibility(View.VISIBLE);
            viewHolder.viewDocuments.setVisibility(View.VISIBLE);

            viewHolder.deleteMachine.setVisibility(View.VISIBLE);
            viewHolder.pickingImage.setVisibility(View.VISIBLE);
            viewHolder.workDone.setVisibility(View.VISIBLE);

            viewHolder.machine_type.setText(tmpValues.getMachineType());
            viewHolder.machine_name.setText(tmpValues.getMachineName());
            viewHolder.machine_model.setText(tmpValues.getMachineModel());
            viewHolder.machine_sl_no.setText(tmpValues.getMachineSlNo());

            Log.e("TAG", "job job detail id >> " + tmpValues.getJobDetailId());

            if (db.getWorkDoneForJobBoolean(tmpValues.getJobDetailId())) {
                viewHolder.create_docket_heading.setBackgroundResource(R.color.colorNoWork);
                Log.e("TAG", "In Red");
            } else {
                viewHolder.create_docket_heading.setBackgroundResource(R.color.colorWork);
                Log.e("TAG", "In Green");
            }

            Log.e("TAG", "Size >> " + sparePartsArray.size());

            Log.e("TAG", "Machine Id >> " + tmpValues.getMachineId());

            viewHolder.addSpareParts.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    ((CreateDocketActivityPart2) activity).openPopUpForSpareParts(position);
                }
            });

            viewHolder.viewDocuments.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    ((CreateDocketActivityPart2) activity).openMachineDetails(position);
                }
            });

            viewHolder.deleteMachine.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    ((CreateDocketActivityPart2) activity).deleteMachineItem(position);
                }
            });

            viewHolder.pickingImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    ((CreateDocketActivityPart2) activity).openGalleryForPickingImage(position);
                }
            });

            viewHolder.workDone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ((CreateDocketActivityPart2) activity).openPopUpForWorkDone(position);
                }
            });

            if (k == position){
                if (sparePartsArray.size() > 0
                        ) {
                    Log.e("TAG", "values >> " + sparePartsArray.get(0).getDescription());
                    viewHolder.sparePartsLayout.setVisibility(View.VISIBLE);

                    for (int j = 0; j < sparePartsArray.size(); j++) {
                        /**
                         * inflate items/ add items in linear layout instead of
                         * listview
                         */
                        try {
                            //Log.e("TAG", "s >> " + sparePartsArray.get(j).getMachineId());
                            Log.e("TAG", "m >> " + sparePartsArray.get(j).getMachineId());

                            if (sparePartsArray.get(j).getMachineId().equals(docketMachineArray.get(position).getMachineId())) {

                                //Log.e("TAG", "size of i >> " + j);
                                LayoutInflater inflater1 = null;
                                inflater1 = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View mLinearView = inflater1.inflate(R.layout.spare_parts_details_one_row_item, null);

                                /**
                                 * getting id of row.xml
                                 */
                                TextView spareId = (TextView) mLinearView.findViewById(R.id.spare_parts_id);
                                TextView description = (TextView) mLinearView.findViewById(R.id.spare_description);
                                TextView quantity = (TextView) mLinearView.findViewById(R.id.spare_quantity);
                                TextView unitsales = (TextView) mLinearView.findViewById(R.id.spare_unit_sales);
                                ImageView deleteSpareParts = (ImageView) mLinearView.findViewById(R.id.delete_spare_parts);
                                /**
                                 * set item into row
                                 */
                                String spareIdText = sparePartsArray.get(position).getSparePartsId();
                                String descriptionText = sparePartsArray.get(position).getDescription();
                                String quantityText = sparePartsArray.get(position).getQuantity();
                                String unitsalesText = sparePartsArray.get(position).getUnitSales();

                                spareId.setText(spareIdText);
                                description.setText(descriptionText);
                                quantity.setText(quantityText);
                                unitsales.setText(unitsalesText);
                                pos = j;

                                deleteSpareParts.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        ((CreateDocketActivityPart2) activity).deleteSpareParts(pos);
                                    }
                                });

                                /**
                                 * add view in top linear
                                 */

                                viewHolder.mLinearSpareDetails.addView(mLinearView);
                            }
                        } catch (Exception e) {
                            Log.e("exceptionCase", e.toString());
                        }

                    }

                } else {
                    viewHolder.sparePartsLayout.setVisibility(View.GONE);
                    // sparePartsArray.remove((sparePartsArray.size() - 1));
                }
                k++;
        }
			//pos++;

			if (position % 2 == 1) {
				vi.setBackgroundColor(Color.parseColor("#f6f6f6"));
			} else {
				vi.setBackgroundColor(Color.parseColor("#ffffff"));
			}
		}
		return vi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (docketMachineArray.size() <= 0)
			return 1;
		return docketMachineArray.size();
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

//    @Override
//    public int getItemViewType(int position) {
//        return (sparePartsArray.get(position).getMachineId().equals( docketMachineArray.get(position).getMachineId())) ? 0 : 1;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

}

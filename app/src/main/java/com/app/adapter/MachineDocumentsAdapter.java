package com.app.adapter;

import java.util.ArrayList;

import com.app.model.MachineDocumentDetails;
import com.app.samgo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MachineDocumentsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<MachineDocumentDetails> documentList = new ArrayList<MachineDocumentDetails>();

	private MachineDocumentDetails tmpValues;

	public MachineDocumentsAdapter(final Context context, ArrayList<MachineDocumentDetails> documentList) {
		mContext = context;

		this.documentList = documentList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return documentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return documentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	static class VideoHolder {
		ImageView fileImage;
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		// TODO Auto-generated method stub\
		VideoHolder holder;

		if (convertView == null) {
			// Create the row
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.machine_documents_one_row_item, parentView, false);

			// Create the video holder
			holder = new VideoHolder();

			// Set the title
			holder.title = (TextView) convertView.findViewById(R.id.file_heading);
			holder.fileImage = (ImageView) convertView.findViewById(R.id.download_file);

			convertView.setTag(holder);
		} else
			holder = (VideoHolder) convertView.getTag();

		if (documentList.size() <= 0) {
			holder.title.setText("No File Added");
			holder.fileImage.setVisibility(View.GONE);
		} else {
			/***** Get each Model object from Arraylist ********/

			tmpValues = null;

			tmpValues = documentList.get(position);

			holder.fileImage.setVisibility(View.VISIBLE);

			holder.title.setText(tmpValues.getFileTitle());

		}

		return convertView;
	}

}

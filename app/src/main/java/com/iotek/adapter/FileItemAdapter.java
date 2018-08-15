package com.iotek.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.entity.FileEntity;
import com.iotek.ycbrowser.R;

public class FileItemAdapter extends BaseAdapter {
	LayoutInflater mLayoutInflater;
	LinkedList<FileEntity> mData = new LinkedList<FileEntity>();
	
	public FileItemAdapter(Context context){
		mLayoutInflater = LayoutInflater.from(context);;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.location_item_layout,null);
			holder.img_flag = (ImageView) convertView.findViewById(R.id.file_pic);
			holder.tv_name = (TextView) convertView.findViewById(R.id.file_title);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FileEntity file = mData.get(position);
		holder.img_flag.setBackgroundResource(file.getPicSrcID());
		holder.tv_name.setText(file.getName());
		return convertView;
	}
	
	class ViewHolder{
		ImageView img_flag;
		TextView tv_name;
	}
	
	public void resetData(LinkedList<FileEntity> fileInfo)
	{
		mData.clear();
		mData.addAll(fileInfo);
		notifyDataSetChanged();
	}

}

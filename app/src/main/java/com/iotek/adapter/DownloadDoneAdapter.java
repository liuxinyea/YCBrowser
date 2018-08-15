package com.iotek.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.entity.DownloadDoneItem;
import com.iotek.util.IOUtils;
import com.iotek.ycbrowser.R;

public class DownloadDoneAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<DownloadDoneItem> mDownloadList;
	private ViewHolder viewHolder;
	
	public DownloadDoneAdapter(LayoutInflater inflater){
		this.inflater=inflater;
		mDownloadList=new ArrayList<DownloadDoneItem>();
	}
	
	@Override
	public int getCount() {
		return mDownloadList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDownloadList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.item_listview_downloaddone, null);
			viewHolder.iv_downloaddone=(ImageView) convertView.findViewById(R.id.iv_downloaddone);
			viewHolder.tv_downloaddone_name=(TextView) convertView.findViewById(R.id.tv_downloaddone_name);
			viewHolder.tv_downloaddone_size=(TextView) convertView.findViewById(R.id.tv_downloaddone_size);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		DownloadDoneItem downloadDoneItem=mDownloadList.get(position);
		viewHolder.tv_downloaddone_name.setText(downloadDoneItem.getFileName());
		String sizeString=IOUtils.FormetFileSize(downloadDoneItem.getSize());
		viewHolder.tv_downloaddone_size.setText(sizeString);
		return convertView;
	}
	
	private class ViewHolder {
		ImageView iv_downloaddone;
		TextView tv_downloaddone_name;
		TextView tv_downloaddone_size;
	}
	
	public void reSetData(List<DownloadDoneItem> list) {
		mDownloadList.clear();
		mDownloadList.addAll(list);
		notifyDataSetChanged();
	}

}

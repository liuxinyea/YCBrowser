package com.iotek.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.entity.WebBasic;
import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class WebBasicAdapter extends BaseAdapter implements Filterable {
	private ArrayFilter mFilter;
	private List<WebBasic> mList;
	private Context context;
	private ArrayList<WebBasic> mUnfilteredData;

	private int maxMatch = 10;// 最多显示多少个选项,负数表示全部

	public WebBasicAdapter(List<WebBasic> mList, Context context) {
		this.mList = mList;
		this.context = context;
	}

	@Override
	public int getCount() {

		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.webbasic_item_layout, null);
			holder = new ViewHolder();

			holder.img_icon = (ImageView) view
					.findViewById(R.id.web_basic_icon);
			holder.tv_title = (TextView) view
					.findViewById(R.id.web_basic_title);
			holder.tv_url = (TextView) view.findViewById(R.id.web_basic_url);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		WebBasic pc = mList.get(position);

		holder.img_icon.setBackgroundResource(pc.getImgId());
		holder.tv_title.setText(pc.getTitle());
		holder.tv_url.setText(pc.getUrl());

		return view;
	}

	static class ViewHolder {
		ImageView img_icon;
		TextView tv_title;
		TextView tv_url;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<WebBasic>(mList);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<WebBasic> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<WebBasic> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<WebBasic> newValues = new ArrayList<WebBasic>(count);

				for (int i = 0; i < count; i++) {
					WebBasic webBasic = unfilteredValues.get(i);
					if (webBasic != null) {
						/**
						 * 匹配关键字 算法优化
						 */
						/*
						 * if (webBasic.getTitle() != null &&
						 * webBasic.getTitle().contains(prefixString)) {
						 * 
						 * newValues.add(webBasic); } else if (webBasic.getUrl()
						 * != null && webBasic.getUrl().contains(prefixString))
						 * {
						 * 
						 * newValues.add(webBasic); }
						 */
						// 优化后
						if (webBasic.getTitle() != null
								&& Util.StringMatch(webBasic.getTitle(),
										prefixString)) {
							if (!newValues.contains(webBasic)) {
								newValues.add(webBasic);
							}
							
						} else if (webBasic.getUrl() != null
								&& Util.StringMatch(webBasic.getUrl(),
										prefixString)) {
							if (!newValues.contains(webBasic)) {
								newValues.add(webBasic);
							}
						}

						if (newValues.size() > maxMatch - 1) {// 不要太多
							break;
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			mList = (List<WebBasic>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}
}
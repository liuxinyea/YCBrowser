package com.iotek.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotek.entity.WebsiteInfo;
import com.iotek.ycbrowser.BookmarkEditActivity;
import com.iotek.ycbrowser.R;

public class BookmarkListviewAdapter extends BaseAdapter {

	private List<WebsiteInfo> BookmarkList = new ArrayList<WebsiteInfo>();
	Context context;
	private LayoutInflater inflater;

	public BookmarkListviewAdapter(Context context,
			List<WebsiteInfo> bookmarkList) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		BookmarkList = bookmarkList;
	}

	@Override
	public int getCount() {
		return BookmarkList.size();
	}

	@Override
	public Object getItem(int position) {
		return BookmarkList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		CheckBox bookmark_item_cb;
		TextView bookmark_item_tv_name;
		TextView bookmark_item_tv_url;
		ImageView bookmark_item_iv;
		ImageView bookmark_item_iv_edit;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final WebsiteInfo websiteInfo = BookmarkList.get(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_item_bookmark, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.bookmark_item_cb = (CheckBox) view
					.findViewById(R.id.bookmark_item_cb);
			viewHolder.bookmark_item_tv_name = (TextView) view
					.findViewById(R.id.bookmark_item_tv_name);
			viewHolder.bookmark_item_tv_url = (TextView) view
					.findViewById(R.id.bookmark_item_tv_url);
			viewHolder.bookmark_item_iv = (ImageView) view
					.findViewById(R.id.bookmark_item_iv);
			viewHolder.bookmark_item_iv_edit = (ImageView) view
					.findViewById(R.id.bookmark_item_iv_edit);

			view.setTag(viewHolder);

		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.bookmark_item_cb.setChecked(websiteInfo.isCheck());
		viewHolder.bookmark_item_cb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						BookmarkList.get(position).setCheck(isChecked);

					}
				});
		viewHolder.bookmark_item_tv_name.setText(websiteInfo.getName());
		viewHolder.bookmark_item_tv_url.setText(websiteInfo.getURL());
		viewHolder.bookmark_item_iv.setImageBitmap(websiteInfo.getImage());
		viewHolder.bookmark_item_iv_edit
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								BookmarkEditActivity.class);
						intent.putExtra("id", websiteInfo.getId());
						intent.putExtra("name", websiteInfo.getName());
						intent.putExtra("url", websiteInfo.getURL());
						context.startActivity(intent);
					}
				});
		return view;
	}
}

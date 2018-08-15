package com.iotek.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotek.entity.HistoryInfo;
import com.iotek.ycbrowser.R;

public class HistoryExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private String[] armTpye;
	private Map<Integer, List<HistoryInfo>> childMap = new HashMap<Integer, List<HistoryInfo>>();

	public HistoryExpandableListAdapter(Context context,
			Map<Integer, List<HistoryInfo>> childMap) {
		this.context = context;
		this.childMap = childMap;
		armTpye = new String[] {
				context.getResources().getString(
						R.string.history_group_title_today),
				context.getResources().getString(
						R.string.history_group_title_yesterday),
				context.getResources().getString(
						R.string.history_group_title_last) };
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getGroupCount() {
		return armTpye.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (childMap.isEmpty()) {
			return 0;
		}
		return childMap.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return armTpye[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// 返回历史记录名称用于Toast显示
		return childMap.get(groupPosition).get(childPosition).getName();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			convertView = LinearLayout.inflate(context,
					R.layout.list_item_history_group, null);
			groupHolder = new GroupHolder();
			groupHolder.list_item_history_group_tv_groupname = (TextView) convertView
					.findViewById(R.id.list_item_history_group_tv_groupname);
			convertView.setTag(groupHolder);

		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		if (isExpanded) {
			groupHolder.list_item_history_group_tv_groupname
					.setBackgroundColor(color.holo_green_light);// 选中打开分类时改背景
		} else {

			groupHolder.list_item_history_group_tv_groupname
					.setBackgroundColor(color.holo_blue_bright);

		}

		groupHolder.list_item_history_group_tv_groupname
				.setText(armTpye[groupPosition]);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (childMap.isEmpty()) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_history_empty, null);
			return convertView;
		} else {

			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_history, null);
				childHolder = new ChildHolder();
				childHolder.history_item_iv = (ImageView) convertView
						.findViewById(R.id.history_item_iv);
				childHolder.history_item_tv_name = (TextView) convertView
						.findViewById(R.id.history_item_tv_name);
				childHolder.history_item_tv_time = (TextView) convertView
						.findViewById(R.id.history_item_tv_time);
				childHolder.history_item_tv_url = (TextView) convertView
						.findViewById(R.id.history_item_tv_url);

				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}
			childHolder.history_item_iv.setImageBitmap(childMap
					.get(groupPosition).get(childPosition).getImage());
			childHolder.history_item_tv_name.setText(childMap
					.get(groupPosition).get(childPosition).getName());
			childHolder.history_item_tv_time.setText(childMap
					.get(groupPosition).get(childPosition).getTime());
			childHolder.history_item_tv_url.setText(childMap.get(groupPosition)
					.get(childPosition).getURL());

			return convertView;
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;// 使子项可以被点击
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	class GroupHolder {
		TextView list_item_history_group_tv_groupname;
	}

	class ChildHolder {
		ImageView history_item_iv;
		TextView history_item_tv_name;
		TextView history_item_tv_time;
		TextView history_item_tv_url;

	}

	public void resetData() {

	}

}

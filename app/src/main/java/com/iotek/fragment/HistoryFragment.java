package com.iotek.fragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.iotek.adapter.HistoryExpandableListAdapter;
import com.iotek.dao.HistoryDao;
import com.iotek.entity.HistoryInfo;
import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.ycbrowser.MainActivity;
import com.iotek.ycbrowser.R;

public class HistoryFragment extends Fragment {
	private ExpandableListView historyListView;
	private HistoryExpandableListAdapter hAdapter;
	private Map<Integer, List<HistoryInfo>> historyChildMap;
	private List<HistoryInfo> todayHistoryList;
	private List<HistoryInfo> yesterdayHistoryList;
	private List<HistoryInfo> pastHistoryList;
	private List<HistoryInfo> allHistoryList;
	private TextView history_empty_tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, container,
				false);

		try {
			initData();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		historyListView = (ExpandableListView) view
				.findViewById(R.id.histroy_elv);
		historyListView.setAdapter(hAdapter);
		for (int i = 0; i < 3; i++) {
			historyListView.expandGroup(i);
		}
		;
		history_empty_tv = (TextView) view.findViewById(R.id.history_empty_tv);
		history_empty_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder emptyDialog = new AlertDialog.Builder(
						getActivity());
				emptyDialog.setTitle(getResources().getString(
						R.string.empty_dialog_title));
				emptyDialog.setMessage(getResources().getString(
						R.string.empty_dialog_message));
				emptyDialog.setCancelable(false);

				emptyDialog.setNegativeButton(
						getResources()
								.getString(R.string.empty_dialog_negative),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				emptyDialog
						.setNeutralButton(
								getResources().getString(
										R.string.empty_dialog_neutral),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										HistoryDao
												.deleteAllHistory(getActivity());
										try {
											initData();
										} catch (ParseException e) {
											e.printStackTrace();
										}

										// historyChildMap.clear();
										hAdapter = new HistoryExpandableListAdapter(
												getActivity(), historyChildMap);
										historyListView.setAdapter(hAdapter);
										// hAdapter.notifyDataSetChanged();
									}
								});

				emptyDialog.show();

			}
		});
		// initView();
		initEvents();
		return view;
	}

	private void initData() throws ParseException {
		allHistoryList = new ArrayList<HistoryInfo>();

		allHistoryList = Util.queryAllHistory(getActivity());

		todayHistoryList = new ArrayList<HistoryInfo>();
		yesterdayHistoryList = new ArrayList<HistoryInfo>();
		pastHistoryList = new ArrayList<HistoryInfo>();

		// 判断时间分类
		for (int i = 0; i < allHistoryList.size(); i++) {
			String stringDateTemp;
			Date dateDateTemp;
			Date newDate = Util.getNow();
			stringDateTemp = allHistoryList.get(i).getTime();
			dateDateTemp = Util.stringToDate(stringDateTemp);
			long nowDays = newDate.getDay();
			// long diff = newDate.getTime() - dateDateTemp.getTime();
			long lastDays = dateDateTemp.getDay();
			int days = (int) (nowDays - lastDays);
			if (days < 1) {
				todayHistoryList.add(0, allHistoryList.get(i));
			} else if (days < 2) {
				yesterdayHistoryList.add(0, allHistoryList.get(i));
			} else {
				pastHistoryList.add(0, allHistoryList.get(i));
			}
		}

		historyChildMap = new HashMap<Integer, List<HistoryInfo>>();
		historyChildMap.put(0, todayHistoryList);
		historyChildMap.put(1, yesterdayHistoryList);
		historyChildMap.put(2, pastHistoryList);

		hAdapter = new HistoryExpandableListAdapter(getActivity(),
				historyChildMap);

	}

	// 教学内容--未使用
	private void initView() {

		// 在drawable文件夹下新建了indicator.xml，下面这个语句也可以实现group伸展收缩时的indicator变化
		// historyListView.setGroupIndicator(this.getResources().getDrawable(R.drawable.indicator));
		historyListView.setGroupIndicator(null);// 这里不显示系统默认的group indicator
		historyListView.setAdapter(hAdapter);
		// registerForContextMenu(historyListView);//
		// 给historyListViewView添加上下文菜单
	}

	private void initEvents() {
		// child子项的单击事件
		historyListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				// TODO 单项删除可以在这里实现

				Intent mainIntent = new Intent(getActivity(),
						MainActivity.class);
				mainIntent.putExtra("historyURL",
						historyChildMap.get(groupPosition).get(childPosition)
								.getURL());
				mainIntent.putExtra("ActionNumber", 4);
				Log.d(Constants.LOG_TAG_BOOKMARK,
						historyChildMap.get(groupPosition).get(childPosition)
								.getURL());
				startActivity(mainIntent);
				getActivity().finish();
				return true;
			}
		});
		// TODO 子项长按事件--删除

		historyListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						/*
						 * if (ExpandableListView.getPackedPositionType(id) ==
						 * ExpandableListView.PACKED_POSITION_TYPE_CHILD) { long
						 * packedPos = ((ExpandableListView) parent)
						 * .getExpandableListPosition(position); int
						 * groupPosition = ExpandableListView
						 * .getPackedPositionGroup(packedPos); int childPosition
						 * = ExpandableListView
						 * .getPackedPositionChild(packedPos); Log.d("chuan",
						 * groupPosition + ""); Log.d("chuan", childPosition +
						 * ""); HistoryInfo deleteHistoryInfo = null;
						 * deleteHistoryInfo = historyChildMap.get(
						 * groupPosition).get(childPosition); Log.d("chuan",
						 * historyChildMap.get(groupPosition)
						 * .get(childPosition).getId() + ""); //
						 * deleteOneHistory
						 * (String.valueOf(deleteHistoryInfo.getId()));
						 * 
						 * return true; }
						 */
						return false;
					}
				});

	}

}

package com.iotek.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.iotek.adapter.BookmarkListviewAdapter;
import com.iotek.dao.BookMarkDao;
import com.iotek.entity.WebsiteInfo;
import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.ycbrowser.MainActivity;
import com.iotek.ycbrowser.R;

/**
 * 书签/历史记录activity中的书签fragment
 * 
 * @author
 */
public class BookmarkFragment extends Fragment implements OnClickListener {

	private ListView bookmarkListView;
	private List<WebsiteInfo> websiteInfoInfoList = new ArrayList<WebsiteInfo>();
	private BookmarkListviewAdapter adapter;
	private TextView bookmark_tv_select;
	private TextView bookmark_tv_delete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bookmark, container,
				false);

		initData();
		bookmarkListView = (ListView) view.findViewById(R.id.bookmark_lv);

		adapter = new BookmarkListviewAdapter(getActivity(),
				websiteInfoInfoList);
		bookmarkListView.setAdapter(adapter);
		bookmarkListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent mainIntent = new Intent(getActivity(),
						MainActivity.class);
				mainIntent.putExtra("bookmarkURL",
						websiteInfoInfoList.get(position).getURL());
				mainIntent.putExtra("ActionNumber", 3);
				Log.d(Constants.LOG_TAG_BOOKMARK,
						websiteInfoInfoList.get(position).getURL());
				startActivity(mainIntent);
				getActivity().finish();
			}
		});

		bookmark_tv_select = (TextView) view
				.findViewById(R.id.bookmark_tv_select);
		bookmark_tv_delete = (TextView) view
				.findViewById(R.id.bookmark_tv_delete);

		bookmark_tv_select.setOnClickListener(this);
		bookmark_tv_delete.setOnClickListener(this);

		return view;
	}

	@Override
	public void onStart() {
		websiteInfoInfoList = BookMarkDao.queryAllBookmark(getActivity());
		adapter=new BookmarkListviewAdapter(getActivity(), websiteInfoInfoList);
		bookmarkListView.setAdapter(adapter);
		//adapter.notifyDataSetChanged();
		super.onStart();
	}

	/**
	 * 初始化书签数据
	 */
	private void initData() {
		websiteInfoInfoList = BookMarkDao.queryAllBookmark(getActivity());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bookmark_tv_select:

			if (getCheckedNum() > websiteInfoInfoList.size() / 2) {
				for (int i = 0; i < websiteInfoInfoList.size(); i++) {
					websiteInfoInfoList.get(i).setCheck(false);
				}
			} else {
				for (int i = 0; i < websiteInfoInfoList.size(); i++) {
					websiteInfoInfoList.get(i).setCheck(true);
				}
			}

			adapter.notifyDataSetChanged();
			break;
		case R.id.bookmark_tv_delete:
			if (getCheckedNum() != 0) {
				AlertDialog.Builder deleteDialog = new AlertDialog.Builder(
						getActivity());
				deleteDialog.setTitle(getResources().getString(
						R.string.delete_dialog_title));
				deleteDialog.setMessage(getResources().getString(
						R.string.delete_dialog_message1)
						+ getCheckedNum()
						+ getResources().getString(
								R.string.delete_dialog_message2));
				deleteDialog.setCancelable(false);

				deleteDialog.setNegativeButton(
						getResources().getString(
								R.string.delete_dialog_negative),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				deleteDialog.setNeutralButton(
						getResources()
								.getString(R.string.delete_dialog_neutral),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								for (int i = 0; i < websiteInfoInfoList.size(); i++) {
									if (websiteInfoInfoList.get(i).isCheck()) {
										BookMarkDao.deleteBookmark(
												getActivity(),
												websiteInfoInfoList.get(i)
														.getId());
										websiteInfoInfoList.remove(i);
									}
								}
								adapter.notifyDataSetChanged();
							}
						});

				deleteDialog.show();

			} else {
				Util.displayToast(
						getActivity(),
						getResources().getString(
								R.string.delete_nochecked_toast));
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 查看被勾选的书签数量
	 */
	private int getCheckedNum() {
		int checkedNum = 0;
		for (int i = 0; i < websiteInfoInfoList.size(); i++) {
			if (websiteInfoInfoList.get(i).isCheck()) {
				checkedNum++;
			}

		}
		return checkedNum;
	}

}

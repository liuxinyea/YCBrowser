package com.iotek.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.iotek.adapter.DownloadAdapter;
import com.iotek.dao.DownloadNDoneDao;
import com.iotek.entity.DownloadItem;
import com.iotek.manager.DownloadManager;
import com.iotek.ycbrowser.R;

public class DownloadFragment extends Fragment {

	private ListView lv_download;
	private DownloadAdapter adapter;
	private List<DownloadItem> mListDownloadItem;
	FrameLayout mDownLoadFrameLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_download, container,
				false);
		mDownLoadFrameLayout = (FrameLayout) view.findViewById(R.id.download_framelayout);

		lv_download = (ListView) view.findViewById(R.id.lv_download);
		mListDownloadItem = DownloadManager.getInstance().getDownloadList();
		
		if(mListDownloadItem.size() == 0){
			View temp = inflater.inflate(R.layout.none_item_layout, mDownLoadFrameLayout,
					false);
			mDownLoadFrameLayout.addView(temp);
		}
		adapter = new DownloadAdapter(inflater);
		if(DownloadManager.getInstance().getDownloadList().size()>0){
			adapter.reSetData(DownloadManager.getInstance().getDownloadList());
		}/*else{
			adapter.reSetData(DownloadNDoneDao.queryDownloadNDone(getActivity()));
		}*/
		
		
		adapter.reSetData(mListDownloadItem);
		lv_download.setAdapter(adapter);
		lv_download.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DownloadItem downloadItem = DownloadManager.getInstance()
						.getDownloadList().get(position);
				boolean currentPaused = downloadItem.getRunnable().getPaused();
				downloadItem.getRunnable().setPaused(!currentPaused);
				adapter.reSetState(position);
			}
		});

		lv_download.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				return false;
			}

		});
		return view;
	}

	/**
	 * callback update download list
	 */
	/*
	 * @Override public void onCompleteDownload() {
	 * adapter.reSetData(DownloadManager.getInstance().getDownloadList()); }
	 */

}

package com.iotek.fragment;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.iotek.adapter.DownloadDoneAdapter;
import com.iotek.dao.DownloadDoneDao;
import com.iotek.entity.DownloadDoneItem;
import com.iotek.util.FileUtils;
import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class DownloadDoneFragment extends Fragment {

	private ListView lv_download;
	private DownloadDoneAdapter adapter;
	private List<DownloadDoneItem> mDownloadList;
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
		mDownloadList = DownloadDoneDao.queryDownloadDone(getActivity());
		
		if(mDownloadList.size() == 0){
			View temp = inflater.inflate(R.layout.none_item_layout, mDownLoadFrameLayout,
					false);
			mDownLoadFrameLayout.addView(temp);
		}
		adapter = new DownloadDoneAdapter(inflater);
		adapter.reSetData(mDownloadList);
		lv_download.setAdapter(adapter);
		lv_download.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DownloadDoneItem downloadDoneItem = mDownloadList.get(position);
				String path = downloadDoneItem.getPath();
				File file = new File(path);
				if(file.exists()){
					openFile(file);
				}else{
					Util.displayToast(getActivity(), "文件已被删除");
					DownloadDoneDao.deleteDownloadFile(getActivity(), path);
					mDownloadList = DownloadDoneDao.queryDownloadDone(getActivity());
					adapter.reSetData(mDownloadList);
				}
				
			}
		});
		return view;
	}

	/**
	 * 使用默认的软件打开
	 * 
	 * @param file
	 */
	private void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		String type = FileUtils.getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);
	}

}

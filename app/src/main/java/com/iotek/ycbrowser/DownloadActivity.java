package com.iotek.ycbrowser;

import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.iotek.dao.DownloadDoneDao;
import com.iotek.dao.DownloadNDoneDao;
import com.iotek.entity.DownloadItem;
import com.iotek.fragment.DownloadDoneFragment;
import com.iotek.fragment.DownloadFragment;
import com.iotek.manager.DownloadManager;
import com.iotek.runnables.DownloadThread;
import com.iotek.service.DownloadService;
import com.iotek.service.DownloadService.DownloadBinder;
import com.iotek.util.IOUtils;
import com.iotek.util.Util;

/**
 * DownloadActivity is to interaction with service
 * 
 * @author zy
 * 
 */

public class DownloadActivity extends FragmentActivity implements
		OnClickListener {
	private Button btn_manage_download_back;
	private Button btn_download;
	private Button btn_done;
	private DownloadBinder mBinder;
	private ServiceConnection connection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download);
		doBindService();
		initView();

	}

	/**
	 * startService
	 */
	private void dostartService() {
		Intent intentService = new Intent(this, DownloadService.class);
		startService(intentService);
	}

	/**
	 * bindService Get DownloadService return Binder initDownloadList
	 */
	private void doBindService() {
		Intent intent = new Intent(this, DownloadService.class);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mBinder = (DownloadBinder) service;
				initDownloadList();
			}
		};
		bindService(intent, connection, BIND_AUTO_CREATE);

	}

	/**
	 * Get MainActicity intent url Add DownloadItem to List Start download
	 * setDefaultFragment
	 */

	private void initDownloadList() {
		String url = getIntent().getStringExtra("url");
		if (url != null) {
			String fileName = getIntent().getStringExtra("fileName");
			if (fileName == null) {
				fileName = IOUtils.getFileNameFromUrl(url);
			}
			int type = IOUtils.getFileTypeFromUrl(url);
			DownloadItem downloadItem = new DownloadItem(url, fileName, type,
					new DownloadThread(url, fileName, this), 0);
			if (!DownloadManager.getInstance().checkDownloadItem(downloadItem)) {
				if (!DownloadDoneDao
						.checkDownloadDoneItem(this, IOUtils
								.getDownloadFolder().getAbsolutePath()
								+ "/"
								+ fileName)) {
					DownloadManager.getInstance().addToDownloadList(
							downloadItem);
					mBinder.addThreadToDownload(downloadItem.getRunnable());
				} else {
					Util.displayToast(getApplicationContext(), getResources().getString(R.string.loaded_tip));
				}
			} else {
				Util.displayToast(getApplicationContext(),getResources().getString(R.string.loading_tip));
			}
		} else {
			List<DownloadItem> downloadList = DownloadNDoneDao
					.queryDownloadNDone(getApplicationContext());
			Log.d("zy", "进入未完成的再次下载"+downloadList.size());
			for (DownloadItem downloadItem : downloadList) {
				downloadItem.setRunnable(new DownloadThread(downloadItem.getUrl(), this, downloadItem.getDownloadpos()));
				DownloadManager.getInstance().addToDownloadList(
						downloadItem);
				Log.d("zy", downloadItem.toString());
				mBinder.addThreadToDownload(downloadItem.getRunnable());
				DownloadNDoneDao.deleteDownloadNC(this, downloadItem.getUrl());
			}
		}
		setDefaultFragment();
	}

	private void initView() {
		btn_download = (Button) findViewById(R.id.btn_downloading);
		btn_done = (Button) findViewById(R.id.btn_done);
		btn_manage_download_back = (Button) findViewById(R.id.manage_download_back);
		btn_download.setOnClickListener(this);
		btn_done.setOnClickListener(this);
		btn_manage_download_back.setOnClickListener(this);

	}

	/**
	 * Set DownloadFragment
	 */
	private void setDefaultFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mDownloadFragment = new DownloadFragment();
		transaction.replace(R.id.framelayout_download, mDownloadFragment);
		transaction.commit();
	}

	/**
	 * set DownloadDoneFragent
	 */
	private void setDownloadDoneFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment downloadDoneFragment = new DownloadDoneFragment();
		transaction.replace(R.id.framelayout_download, downloadDoneFragment);
		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_downloading:
			btn_download.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
			btn_done.setTextColor(getResources().getColor(android.R.color.background_dark));
			setDefaultFragment();
			
			break;
		case R.id.btn_done:
			btn_done.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
			btn_download.setTextColor(getResources().getColor(android.R.color.background_dark));
			setDownloadDoneFragment();
			break;
		case R.id.manage_download_back:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * DownloadActivity destory unbindService
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
	}

}

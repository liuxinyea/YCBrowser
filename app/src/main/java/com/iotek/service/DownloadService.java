package com.iotek.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.iotek.entity.DownloadItem;
import com.iotek.manager.DownloadManager;
import com.iotek.runnables.DownloadThread;

public class DownloadService extends Service {

	private DownloadBinder mDownloadBinder;
	List<DownloadItem> mListDownloadItem;

	@Override
	public void onCreate() {
		mDownloadBinder = new DownloadBinder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mListDownloadItem = DownloadManager.getInstance().getDownloadList();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mDownloadBinder;
	}

	@Override
	public void onDestroy() {
		Log.d("zy", "onDestroy---->DownloadService has stop");
	}

	public class DownloadBinder extends Binder {

		public void addThreadToDownload(DownloadThread downloadThread) {
			new Thread(downloadThread).start();
		}

	}

}

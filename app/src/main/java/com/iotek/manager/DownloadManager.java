package com.iotek.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.iotek.dao.DownloadDoneDao;
import com.iotek.dao.DownloadNDoneDao;
import com.iotek.entity.DownloadItem;

/**
 * manage download list(add,cancel)
 * 
 * @author zy
 * 
 */

public class DownloadManager {

	private List<DownloadItem> mListDownloadItem;
	private static DownloadManager downloadManager = new DownloadManager();

	private DownloadManager() {
		mListDownloadItem = new ArrayList<DownloadItem>();
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static DownloadManager getInstance() {
		return downloadManager;
	}

	/**
	 * get list
	 * 
	 * @return
	 */
	public List<DownloadItem> getDownloadList() {
		return mListDownloadItem;
	}

	/**
	 * add item to list
	 * 
	 * @param downloadItem
	 */
	public void addToDownloadList(DownloadItem downloadItem) {
		mListDownloadItem.add(downloadItem);
	}

	/**
	 * remove completed download in list delete completed in table
	 * download_ndone insert in table download_done
	 */
	public void removeDoneDownloads(Context context) {
		for (int len = mListDownloadItem.size() - 1; len >= 0; len--) {
			if (mListDownloadItem.get(len).getRunnable().getFinished()) {

				DownloadNDoneDao.deleteDownloadNC(context, mListDownloadItem
						.get(len).getUrl());
				DownloadDoneDao.InsertDownloadDone(context, mListDownloadItem
						.get(len).getRunnable().getDownloadFilePath(),
						mListDownloadItem.get(len).getFileName(),
						(int) mListDownloadItem.get(len).getRunnable()
								.getCount(), mListDownloadItem.get(len)
								.getType());
				mListDownloadItem.remove(len);
			}
		}
	}

	/**
	 * cancel download
	 * 
	 * @param downloadItem
	 */
	public void cancelDownload(DownloadItem downloadItem) {
		for (int len = mListDownloadItem.size() - 1; len >= 0; len--) {
			if (mListDownloadItem.get(len).getUrl()
					.equals(downloadItem.getUrl())) {
				mListDownloadItem.remove(len);
			}
		}
	}

	public boolean checkDownloadItem(DownloadItem downloadItem) {
		boolean isExist = false;
		for (int len = mListDownloadItem.size() - 1; len >= 0; len--) {
			if (mListDownloadItem.get(len).getUrl()
					.equals(downloadItem.getUrl())) {
				isExist = true;
			}
		}
		return isExist;
	}
	
	//clear all downloadItem
	public void chearAllDownloadItem(){
		mListDownloadItem.clear();
	}

}

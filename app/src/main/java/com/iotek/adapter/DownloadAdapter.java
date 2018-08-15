package com.iotek.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iotek.entity.DownloadItem;
import com.iotek.manager.DownloadManager;
import com.iotek.util.Constants;
import com.iotek.util.Util;
import com.iotek.ycbrowser.R;

public class DownloadAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<ViewHolder> holders = new ArrayList<DownloadAdapter.ViewHolder>();
	private List<DownloadItem> mListDownloadItems;
	private ViewHolder viewHolder;

	public DownloadAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
		mListDownloadItems = new ArrayList<DownloadItem>();

	}

	@Override
	public int getCount() {
		return mListDownloadItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mListDownloadItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_listview_downloading,
					null);
			viewHolder.iv_document = (ImageView) convertView
					.findViewById(R.id.iv_document);
			viewHolder.tv_download_name = (TextView) convertView
					.findViewById(R.id.tv_download_name);
			viewHolder.pb_current_download = (ProgressBar) convertView
					.findViewById(R.id.pb_current_download);
			viewHolder.tv_download_speed = (TextView) convertView
					.findViewById(R.id.tv_download_speed);
			viewHolder.tv_download_state = (TextView) convertView
					.findViewById(R.id.tv_download_state);
			holders.add(viewHolder);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (mListDownloadItems.size() > 0) {
			DownloadItem downloadItem = mListDownloadItems.get(position);
			viewHolder.tv_download_name.setText(downloadItem.getFileName());
			viewHolder.tv_download_state.setText(Constants.DOWNLOADING);
			// 需要自动更新一下DownloadManager中的List
			/*
			 * List<DownloadItem> list = DownloadManager.getInstance()
			 * .getDownloadList(); if(list.size()>0){ new
			 * DownloadTask(position).
			 * executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); }
			 */
			new DownloadTask(position)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		}
		return convertView;
	}

	public void reSetState(int position) {
		if (holders.get(position).tv_download_state.getText().equals(
				Constants.DOWNLOADING)) {
			holders.get(position).tv_download_state
					.setText(Constants.DOWNLOADSTOP);
		} else {
			holders.get(position).tv_download_state
					.setText(Constants.DOWNLOADING);
		}

	}

	private class ViewHolder {
		ImageView iv_document;
		TextView tv_download_name;
		ProgressBar pb_current_download;
		TextView tv_download_speed;// 目前显示百分比
		TextView tv_download_state;
	}

	public void reSetData(List<DownloadItem> list) {
		mListDownloadItems.clear();
		mListDownloadItems.addAll(list);
		notifyDataSetChanged();
	}

	private class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

		private List<DownloadItem> mList = DownloadManager.getInstance()
				.getDownloadList();
		private int position;
		private DownloadItem downloadItem;

		public DownloadTask(int position) {
			this.position = position;
			if (mList.size() == 0) {

			}
			downloadItem = mList.get(position);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			while (!downloadItem.getRunnable().getFinished()
					&& !downloadItem.getRunnable().getStop()) {
				try {
					int currentProgress = (int) downloadItem.getRunnable()
							.getCurrenrProgress();
					publishProgress(currentProgress);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.d("zy", "InterruptedException" + e.toString());
					return false;
				}

			}
			return true;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			holders.get(position).pb_current_download.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			/**
			 * finish or stop
			 */
			if (result && downloadItem.getRunnable().getFinished()) {
				Util.displayToast(inflater.getContext(),
						downloadItem.getFileName() + "下载完成");
				reSetData(DownloadManager.getInstance().getDownloadList());
			}
		}

	}

}

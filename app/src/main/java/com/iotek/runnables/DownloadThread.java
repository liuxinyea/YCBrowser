package com.iotek.runnables;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iotek.manager.DownloadManager;
import com.iotek.util.IOUtils;

public class DownloadThread implements Runnable {

	private String mUrl;
	private String fileName;
	private boolean finished = false;
	private boolean paused = false;
	private boolean stop = false;
	private double progress;// 进度条的显示
	private Context mContext;
	private long nPos;// 再次下载开始的位置
	private int currentDownloadpos;// 当前下载到的位置
	private double count;// 总大小
	private File downloadFile;// 下载的文件

	public DownloadThread() {
		super();
	}

	public DownloadThread(String mUrl, String fileName, Context mContext) {
		super();
		this.mUrl = mUrl;
		this.fileName = fileName;
		this.mContext = mContext;
	}

	public DownloadThread(String mUrl, Context mContext, long nPos) {
		this.mUrl = mUrl;
		this.mContext = mContext;
		this.nPos = nPos;
	}

	@Override
	public void run() {
		/* requestDataVolley(); */
		downloadFile = getFile();
		if (downloadFile != null) {
			try {
				URL httpUrl = new URL(mUrl);
				HttpURLConnection conn = (HttpURLConnection) httpUrl
						.openConnection();
				// 判断是否是断点续传
				if (nPos > 0) {
					HttpURLConnection conn2 = (HttpURLConnection) httpUrl
							.openConnection();
					count = conn2.getContentLength();
					conn.setRequestProperty("Range", "bytes=" + nPos + "-");
				} else {
					count = conn.getContentLength();
				}

				InputStream in = conn.getInputStream();
				/* FileOutputStream out = new FileOutputStream(downloadFile); */
				byte[] b = new byte[6 * 1024];
				int len;
				double current = 0.0;// int类型会溢出
				currentDownloadpos = 0;
				while (!finished && !stop) {
					if (in != null) {
						while (!paused && (len = in.read(b)) != -1 && !finished
								&& !stop) {
							// 随机访问文件流
							RandomAccessFile out = new RandomAccessFile(
									downloadFile, "rwd");
							double pro;
							if (nPos > 0) {
								out.seek(nPos);
								nPos = nPos + len;
								current = nPos;

							} else {
								// Gets the current position within this file
								// always 0
								out.seek(currentDownloadpos);
								currentDownloadpos = currentDownloadpos + len;
								current = currentDownloadpos ;
							}
							pro = ((current / count) * 100);
							BigDecimal bigDecimal = new BigDecimal(pro);
							progress = bigDecimal.setScale(1,
									BigDecimal.ROUND_HALF_UP).doubleValue();
							/*
							 * Log.d("iotek", "nPos:" + nPos + "current:" +
							 * (int) current + "count:" + (int) count + "len:" +
							 * len + "progress:" + progress + "当前文件指针：" +
							 * out.getFilePointer());
							 */
							if (out != null) {
								out.write(b, 0, len);
							}
							if (nPos >= count || currentDownloadpos >= count) {
								finished = true;
							}
							if (out != null) {
								out.close();
							}
						}
					}

				}

				if (in != null) {
					in.close();
				}
				if(finished){
					Log.d("zy", "下载完成");
				}
				DownloadManager.getInstance().removeDoneDownloads(mContext);
			} catch (IOException e) {
				Log.d("zy", e.toString());
				e.printStackTrace();
			}
		}

	}

	/*
	 * onDownloadListener downloadListener;
	 * 
	 * public interface onDownloadListener { public void onCompleteDownload(); }
	 * 
	 * 
	 * public void setOnDownloadListener(onDownloadListener l){
	 * downloadListener=l; }
	 */

	private void requestDataVolley() {
		RequestQueue mQueue = Volley.newRequestQueue(mContext);
		StringRequest stringRequest = new StringRequest(mUrl,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							Log.d("zy", "正常响应");
							Log.d("zy", "正常响应：" + jsonObject.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("zy", "错误响应：" + error.toString());
					}
				});

		mQueue.add(stringRequest);
	}

	/**
	 * Create new file into default path
	 * 
	 * @return
	 */

	private File getFile() {
		boolean isExist = false;
		File existFile = null;
		File downloadFolder = IOUtils.getDownloadFolder();
		if (downloadFolder != null) {
			File[] files = downloadFolder.listFiles();
			for (File file : files) {
				if (fileName != null) {
					if (file.getName().equals(fileName)) {
						isExist = true;
						existFile = file;
					}
				} else {
					if (file.getName().equals(IOUtils.getFileNameFromUrl(mUrl))) {
						isExist = true;
						existFile = file;
					}
				}
			}
			if (!isExist) {
				if (fileName != null) {
					return new File(downloadFolder, fileName);
				} else {
					return new File(downloadFolder,
							IOUtils.getFileNameFromUrl(mUrl));
				}

			} else {
				Log.d("zy", IOUtils.getFileNameFromUrl(mUrl) + "is exist");
				return existFile;

			}

		} else {
			return null;
		}

	}

	/**
	 * external set paused
	 * 
	 * @param paused
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * external get paused
	 * 
	 * @return
	 */
	public boolean getPaused() {
		return paused;
	}

	/**
	 * external get progress
	 * 
	 * @return
	 */
	public double getCurrenrProgress() {
		return progress;
	}

	/**
	 * external compulsory end
	 * 
	 * @param finished
	 */
	public void setFinish(boolean finished) {
		this.finished = finished;
	}

	/**
	 * judge is or no finish
	 * 
	 * @return
	 */
	public boolean getFinished() {
		return finished;
	}

	/**
	 * if exist currentDownloadpos need preservation
	 * 
	 * @return
	 */
	public int getCurrentDownloadPos() {
		
		if(currentDownloadpos==0){
			return (int)nPos;
		}else{
			return currentDownloadpos;
		}
	}

	/**
	 * get download file size
	 * 
	 * @return
	 */
	public double getCount() {
		return count;
	}

	/**
	 * get download file path
	 * 
	 * @return
	 */
	public String getDownloadFilePath() {
		return downloadFile.getAbsolutePath();
	}

	/**
	 * stop download
	 * 
	 * @param stop
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	/**
	 * get stop
	 * 
	 * @return
	 */
	public boolean getStop() {
		return stop;
	}

}

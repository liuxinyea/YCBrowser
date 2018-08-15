package com.iotek.entity;

import java.io.Serializable;

import com.iotek.runnables.DownloadThread;

public class DownloadItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String url;
	private String fileName;
	private int type;
	private int size;
	private DownloadThread runnable;
	private int downloadpos;

	public DownloadItem() {
		super();
	}

	public DownloadItem(String url) {
		super();
		this.url = url;
	}

	public DownloadItem(String url, String fileName, int type,
			DownloadThread runnable, int downloadpos) {
		super();
		this.url = url;
		this.fileName = fileName;
		this.type = type;
		this.runnable = runnable;
		this.downloadpos = downloadpos;
	}

	public DownloadItem(String url, String fileName, int type, int downloadpos) {
		super();
		this.url = url;
		this.fileName = fileName;
		this.type = type;
		this.downloadpos = downloadpos;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public DownloadThread getRunnable() {
		return runnable;
	}

	public void setRunnable(DownloadThread runnable) {
		this.runnable = runnable;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDownloadpos() {
		return downloadpos;
	}

	public void setDownloadpos(int downloadpos) {
		this.downloadpos = downloadpos;
	}

	@Override
	public String toString() {
		return "DownloadItem [url=" + url + ", fileName=" + fileName
				+ ", type=" + type + ", size=" + size + ", runnable="
				+ runnable + ", downloadpos=" + downloadpos + "]";
	}

}

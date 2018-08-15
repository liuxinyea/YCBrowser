package com.iotek.entity;

public class DownloadDoneItem {

	private String path;
	private String fileName;
	private int type;
	private int size;

	public DownloadDoneItem() {
		super();
	}

	public DownloadDoneItem(String path, String fileName, int type, int size) {
		super();
		this.path = path;
		this.fileName = fileName;
		this.type = type;
		this.size = size;
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

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	

}

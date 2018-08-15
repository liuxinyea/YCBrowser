package com.iotek.entity;

public class FileEntity{
	private boolean isDirectory;
	private String name;
	private String path;
	private int picSrcID;
	private String size;
	
	public FileEntity() {
		super();
	}

	public FileEntity(int picSrcID, String name,String path,String size,boolean isDirectory) {
		super();
		this.picSrcID = picSrcID;
		this.name = name;
		this.path = path;
		this.size = size;
		this.isDirectory = isDirectory;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public int getPicSrcID() {
		return picSrcID;
	}

	public String getSize() {
		return size;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPicSrcID(int picSrcID) {
		this.picSrcID = picSrcID;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
}

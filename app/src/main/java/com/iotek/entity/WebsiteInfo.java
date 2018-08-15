package com.iotek.entity;

import android.graphics.Bitmap;

public class WebsiteInfo {
	int id;
	String name;
	String URL;
	Bitmap image;
	boolean isCheck;

	public WebsiteInfo(String name, String uRL, Bitmap image, boolean isCheck) {
		super();
		this.name = name;
		URL = uRL;
		this.image = image;
		this.isCheck = isCheck;
	}

	public WebsiteInfo(int id, String name, String uRL, Bitmap image,
			boolean isCheck) {
		super();
		this.id = id;
		this.name = name;
		URL = uRL;
		this.image = image;
		this.isCheck = isCheck;
	}

	public String getName() {
		return name;
	}

	public WebsiteInfo() {
		super();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "WebsiteInfo [id=" + id + ", name=" + name + ", URL=" + URL
				+ ", image=" + image + ", isCheck=" + isCheck + "]";
	}

}

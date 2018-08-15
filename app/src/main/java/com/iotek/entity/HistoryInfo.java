package com.iotek.entity;

import android.graphics.Bitmap;

public class HistoryInfo {
	int id;
	String name;
	String URL;
	Bitmap image;
	String time;

	public HistoryInfo(String name, String uRL, Bitmap image, String time) {
		super();
		this.name = name;
		URL = uRL;
		this.image = image;
		this.time = time;
	}

	public HistoryInfo(int id, String name, String uRL, Bitmap image,
			String time) {
		super();
		this.id = id;
		this.name = name;
		URL = uRL;
		this.image = image;
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "WebsiteInfo [id=" + id + ", name=" + name + ", URL=" + URL
				+ ", image=" + image + ", isCheck=" + "]";
	}

}

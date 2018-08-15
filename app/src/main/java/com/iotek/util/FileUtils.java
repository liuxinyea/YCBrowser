package com.iotek.util;

import java.io.File;

public class FileUtils {

	public static String getMIMEType(File file) {
		String type = "";
		String name = file.getName();
		// 文件扩展名
		String end = name.substring(name.lastIndexOf(".") + 1, name.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("mp4") || end.equals("3gp")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg")
				|| end.equals("bmp") || end.equals("gif")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		if (end.equals("apk")) {
			type = "";
			type = "application/vnd.android.package-archive";
		}
		return type;
	}

}

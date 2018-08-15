/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.iotek.util;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Environment;

/**
 * Utilities for I/O reading and writing.
 */
public class IOUtils {
	
	private static final String APPLICATION_FOLDER = "test";
	private static final String DOWNLOAD_FOLDER = "downloads";
	private static final String BOOKMARKS_EXPORT_FOLDER = "bookmarks-exports";
	
	/**
	 * Get the application folder on the SD Card. Create it if not present.
	 * @return The application folder.
	 */
	public static File getApplicationFolder() {
		//path:/mnt/sdcard
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			
			File folder = new File(root, APPLICATION_FOLDER);
			
			if (!folder.exists()) {
				folder.mkdir();
			}
			
			return folder;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Get the application download folder on the SD Card. Create it if not present.
	 * @return The application download folder.
	 */
	public static File getDownloadFolder() {
		File root = getApplicationFolder();
		
		if (root != null) {
			
			File folder = new File(root, DOWNLOAD_FOLDER);
			
			if (!folder.exists()) {
				folder.mkdir();
			}
			
			return folder;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Get the application folder for bookmarks export. Create it if not present.
	 * @return The application folder for bookmarks export.
	 */
	public static File getBookmarksExportFolder() {
		File root = getApplicationFolder();
		
		if (root != null) {
			
			File folder = new File(root, BOOKMARKS_EXPORT_FOLDER);
			
			if (!folder.exists()) {
				folder.mkdir();
			}
			
			return folder;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Get the list of xml files in the bookmark export folder.
	 * @return The list of xml files in the bookmark export folder.
	 */
	public static List<String> getExportedBookmarksFileList() {
		List<String> result = new ArrayList<String>();
		
		File folder = getBookmarksExportFolder();		
		
		if (folder != null) {
			
			FileFilter filter = new FileFilter() {
				
				@Override
				public boolean accept(File pathname) {
					if ((pathname.isFile()) &&
							(pathname.getPath().endsWith(".xml"))) {
						return true;
					}
					return false;
				}
			};
			
			File[] files = folder.listFiles(filter);
			
			for (File file : files) {
				result.add(file.getName());
			}			
		}
		
		Collections.sort(result, new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {				
				return arg1.compareTo(arg0);
			}    		
    	});
		
		return result;
	}
	
	/**
	 * Get download file name by url
	 * 
	 * @param url
	 * @return
	 */

	public static String getFileNameFromUrl(String url) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		String name = null;
		if (fileName.contains("?")) {
			name = fileName.substring(0, fileName.indexOf("?"));
		} else {
			name = fileName;
		}
		return name;
	}
	
	public static String getFileNameByContent(String contentdisposition){
		String fileNameString=contentdisposition.substring(contentdisposition.indexOf("\"")+1,contentdisposition.lastIndexOf("\""));
		return fileNameString;
	}
	
	public static int getFileTypeFromUrl(String url){
		String name=getFileNameFromUrl(url);
		String suffix=name.substring(name.indexOf(".") + 1);
		if(suffix.equals("apk")){
			return Constants.DOWNLOAD_ICON_APK;
		}else if(suffix.equals("txt")){
			return Constants.DOWNLOAD_ICON_TXT;
		}else{
			return Constants.DOWNLOAD_ICON_OTHER;
		}
	}
	
	
	public static String FormetFileSize(long fileS) { 
        DecimalFormat df = new DecimalFormat("#.00"); 
        String fileSizeString = ""; 
        if (fileS < 1024) { 
            fileSizeString = df.format((double) fileS) + "B"; 
        } else if (fileS < 1048576) { 
            fileSizeString = df.format((double) fileS / 1024) + "K"; 
        } else if (fileS < 1073741824) { 
            fileSizeString = df.format((double) fileS / 1048576) + "M"; 
        } else { 
            fileSizeString = df.format((double) fileS / 1073741824) + "G"; 
        } 
        return fileSizeString; 
    } 

}

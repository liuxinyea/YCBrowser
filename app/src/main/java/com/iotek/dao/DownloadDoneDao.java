package com.iotek.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iotek.database.BrowserDatabaseHelper;
import com.iotek.entity.DownloadDoneItem;
import com.iotek.util.Constants;

public class DownloadDoneDao {

	private static BrowserDatabaseHelper databaseHelper;
	private static SQLiteDatabase db;

	private static void initDatabaseHelper(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new BrowserDatabaseHelper(context,
					Constants.DB_NAME, null, 6);
		}
		if (db == null) {
			db = databaseHelper.getWritableDatabase();
		}

	}

	/*
	 * private DownloadDoneDao(Context context){ databaseHelper = new
	 * BrowserDatabaseHelper(context, Constants.DB_NAME, null, 6); db =
	 * databaseHelper.getWritableDatabase(); }
	 * 
	 * //懒汉式单例类.在第一次调用的时候实例化自己 private static DownloadDoneDao downloadDoneDao;
	 * public static DownloadDoneDao getInstance(Context context){
	 * if(downloadDoneDao==null){ synchronized (DownloadDoneDao.class) { if
	 * (downloadDoneDao==null) { downloadDoneDao=new DownloadDoneDao(context); }
	 * } } return downloadDoneDao; }
	 */

	// insert into table download_done
	public static boolean InsertDownloadDone(Context context, String path,
			String name, int size, int type) {
		initDatabaseHelper(context);
		ContentValues values = new ContentValues();
		values.clear();
		values.put("path", path);
		values.put("name", name);
		values.put("size", size);
		values.put("type", type);
		if (db.insert("download_done", null, values) == -1) {
			return false;
		} else {
			return true;
		}
	}

	// select all in table download_done
	public static List<DownloadDoneItem> queryDownloadDone(Context context) {
		initDatabaseHelper(context);
		List<DownloadDoneItem> downloadList = new ArrayList<DownloadDoneItem>();
		Cursor cursor = db.query("download_done", null, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				String path = cursor.getString(cursor.getColumnIndex("path"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int size = cursor.getInt(cursor.getColumnIndex("size"));
				int type = cursor.getInt(cursor.getColumnIndex("type"));
				DownloadDoneItem downloadDoneItem = new DownloadDoneItem(path,
						name, type, size);
				downloadList.add(downloadDoneItem);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return downloadList;
	}

	//delete downloadDoneItem by path
	public static void deleteDownloadFile(Context context,String path){
		initDatabaseHelper(context);
		db.delete("download_done", "path=?", new String []{path});
	} 
	
	//check download url is has downloaded 
	public static boolean checkDownloadDoneItem(Context context, String path) {
		boolean isExist=false;
		initDatabaseHelper(context);
		Cursor cursor = db.query("download_done", null, "path=?",
				new String[] { path }, null, null, null);
		if (cursor.moveToFirst()) {
			isExist=true;
		}
		return isExist;
	}
	
}

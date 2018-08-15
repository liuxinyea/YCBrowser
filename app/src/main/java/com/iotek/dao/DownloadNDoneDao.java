package com.iotek.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iotek.database.BrowserDatabaseHelper;
import com.iotek.entity.DownloadItem;
import com.iotek.util.Constants;

//download not completed
public class DownloadNDoneDao {

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

	// insert into download_ndone
	public static boolean InsertDownloadNC(Context context, String url, int npos,String name,int type) {
		initDatabaseHelper(context);
		ContentValues values = new ContentValues();
		values.clear();
		values.put("url", url);
		values.put("downloadpos", npos);
		values.put("name", name);
		values.put("type", type);
		Log.d("zy", "InsertDownloadNC+++++>"+npos);
		if (db.insert("download_ndone", null, values) == -1) {
			return false;
		} else {
			return true;
		}
	}

	// delete download_ndone by url
	public static void deleteDownloadNC(Context context, String url) {
		initDatabaseHelper(context);
		db.delete("download_ndone", "url=?", new String[] { url });
	}

	// check downloadItem is Exist
	public static boolean checkDownloadItem(Context context, String url) {
		boolean isExist = false;
		initDatabaseHelper(context);
		Cursor cursor = db.query("download_ndone", null, "url=?",
				new String[] { url }, null, null, null);
		if (cursor.moveToFirst()) {
			isExist = true;
		}
		return isExist;
	}

	//
	public static List<DownloadItem> queryDownloadNDone(Context context) {
		initDatabaseHelper(context);
		List<DownloadItem> downloadList = new ArrayList<DownloadItem>();
		Cursor cursor = db.query("download_ndone", null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			do {
				String url = cursor.getString(cursor.getColumnIndex("url"));
				int downloadpos = cursor.getInt(cursor
						.getColumnIndex("downloadpos"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				int type = cursor.getInt(cursor.getColumnIndex("type"));
				DownloadItem downloadItem = new DownloadItem(url, name, type,
						downloadpos);
				Log.d("zy", "queryDownloadNDone----->"+downloadpos);
				downloadList.add(downloadItem);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return downloadList;
	}

}

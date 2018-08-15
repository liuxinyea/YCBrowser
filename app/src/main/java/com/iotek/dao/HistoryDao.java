package com.iotek.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.iotek.database.BrowserDatabaseHelper;
import com.iotek.entity.HistoryInfo;
import com.iotek.util.Constants;
import com.iotek.util.Util;

/**
 * 数据库历史记录表Dao层及实现
 * 
 * @author Administrator
 * 
 */
public class HistoryDao {
	private static BrowserDatabaseHelper databaseHelper;
	private static SQLiteDatabase db;

	/**
	 * 初始化数据库连接（缓存）
	 * 
	 * @param context
	 */
	private static void initDatabaseHelper(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new BrowserDatabaseHelper(context,
					Constants.DB_NAME, null, 6);
		}
		if (db == null) {
			db = databaseHelper.getWritableDatabase();
		}

	}

	/**
	 * 数据库添加,添加历史记录
	 * 
	 * @param websiteInfo
	 */
	public static void insertHistory(Context context, HistoryInfo historyInfo) {
		initDatabaseHelper(context);
		byte[] in = Util.img(historyInfo.getImage());
		ContentValues values = new ContentValues();
		values.put(Constants.DB_HISTORY_NAME, historyInfo.getName());
		values.put(Constants.DB_HISTORY_URL, historyInfo.getURL());
		values.put(Constants.DB_HISTORY_IMAGE, in);
		values.put(Constants.DB_HISTORY_TIME, historyInfo.getTime());
		db.insert(Constants.DB_HISTORY_TABLENAME, null, values);
	}

	/**
	 * 通过内容提供器删除，通过ID删除历史记录
	 * 
	 * @param context
	 * @param id
	 */
	public static void deleteOneHistory(Context context, String id) {
		Uri uri = Uri.parse("content://com.iotek.database.provider/history/"
				+ id);
		context.getContentResolver().delete(uri, null, null);
	}

	/**
	 * 通过内容提供器删除，删除所有历史记录
	 * 
	 * @param context
	 */
	public static void deleteAllHistory(Context context) {
		Uri uri = Uri.parse("content://com.iotek.database.provider/history");
		context.getContentResolver().delete(uri, null, null);
	}

	/**
	 * 数据库更新，通过URL更新历史记录图标
	 * 
	 * @param websiteInfo
	 */
	public static void updateHistoryImage(Context context,
			HistoryInfo historyInfo) {
		initDatabaseHelper(context);
		ContentValues values = new ContentValues();
		values.put(Constants.DB_HISTORY_IMAGE, Util.img(historyInfo.getImage()));
		db.update(Constants.DB_HISTORY_TABLENAME, values, "url=?",
				new String[] { historyInfo.getURL() });
	}

}

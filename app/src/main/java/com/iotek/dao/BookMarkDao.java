package com.iotek.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.iotek.database.BrowserDatabaseHelper;
import com.iotek.entity.WebsiteInfo;
import com.iotek.util.Constants;
import com.iotek.util.Util;

/**
 * 数据库书签表Dao层及实现
 * 
 * @author Administrator
 * 
 */
public class BookMarkDao {
	private static final String UPDATE_BOOKMARK_BYURL = "update bookmark set name=? where url=?  ";
	private static final String UPDATE_BOOKMARK = "update bookmark set name=?,url=? where id=? ";
	private static final String FIND_BOOKMARK_BYURL = "select * from bookmark where url=?";
	public static final String QUERY_ALLBOOKMARK = "select* from bookmark";
	public static final String DELETE_BOOKMARK = "delete from bookmark where id=?";

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
	 * 数据库添加，添加书签
	 * 
	 * @param websiteInfo
	 */
	public static void insertBookmark(Context context, WebsiteInfo websiteInfo) {
		initDatabaseHelper(context);
		byte[] in = Util.img(websiteInfo.getImage());
		ContentValues values = new ContentValues();
		values.put(Constants.DB_BOOKMARK_NAME, websiteInfo.getName());
		values.put(Constants.DB_BOOKMARK_URL, websiteInfo.getURL());
		values.put(Constants.DB_BOOKMARK_IMAGE, in);
		db.insert(Constants.DB_BOOKMARK_TABLENAME, null, values);
	}

	/**
	 * 数据库删除，通过ID删除书签
	 * 
	 * @param context
	 * @param id
	 */
	public static void deleteBookmark(Context context, int id) {
		initDatabaseHelper(context);
		db.execSQL(DELETE_BOOKMARK, new String[] { String.valueOf(id) });
	}

	/**
	 * 数据库更新,更新书签名称URL，通过URL
	 * 
	 * @param newWebsiteInfo
	 */
	public static void updateBookmarkByURL(Context context,
			WebsiteInfo newWebsiteInfo) {
		initDatabaseHelper(context);
		db.execSQL(
				UPDATE_BOOKMARK_BYURL,
				new String[] { newWebsiteInfo.getName(),
						newWebsiteInfo.getURL() });
	}

	/**
	 * 数据库更新,更新书签名称URL，通过ID
	 * 
	 * @param newWebsiteInfo
	 */
	public static void updateBookmark(Context context,
			WebsiteInfo newWebsiteInfo) {
		initDatabaseHelper(context);
		db.execSQL(
				UPDATE_BOOKMARK,
				new String[] { newWebsiteInfo.getName(),
						newWebsiteInfo.getURL(),
						String.valueOf(newWebsiteInfo.getId()) });
	}

	/**
	 * 数据库查询，通过URL查找是否存过此网址的书签
	 * 
	 * @param url
	 */
	public static boolean findBookmarkByURL(Context context, String findURL) {
		initDatabaseHelper(context);
		String[] args = { findURL };
		Cursor cursor = db.rawQuery(FIND_BOOKMARK_BYURL, args);
		List<WebsiteInfo> findList = new ArrayList<WebsiteInfo>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_NAME));
				String url = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_URL));
				byte[] image = cursor.getBlob(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_IMAGE));

				Bitmap bmpout = BitmapFactory.decodeByteArray(image, 0,
						image.length);

				findList.add(new WebsiteInfo(id, name, url, bmpout, false));

			} while (cursor.moveToNext());
		}
		cursor.close();

		if (findList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 数据库查询，查询返回所有的书签
	 */
	public static List<WebsiteInfo> queryAllBookmark(Context context) {
		initDatabaseHelper(context);
		Cursor cursor = db.rawQuery(QUERY_ALLBOOKMARK, null);
		List<WebsiteInfo> websiteInfoInfoList = new ArrayList<WebsiteInfo>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_NAME));
				String url = cursor.getString(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_URL));
				byte[] image = cursor.getBlob(cursor
						.getColumnIndex(Constants.DB_BOOKMARK_IMAGE));

				Bitmap bmpout = BitmapFactory.decodeByteArray(image, 0,
						image.length);

				websiteInfoInfoList.add(new WebsiteInfo(id, name, url, bmpout,
						false));

			} while (cursor.moveToNext());
		}
		cursor.close();

		return websiteInfoInfoList;
	}

}

package com.iotek.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BrowserProvider extends ContentProvider {
	public static final int HISTORY_ITEM = 1;
	public static final int HISTORY_DIR = 0;
	public static final String AUTHORITY = "com.iotek.database.provider";

	private static UriMatcher uriMatcher;
	private BrowserDatabaseHelper dbHelper;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "history", HISTORY_DIR);
		uriMatcher.addURI(AUTHORITY, "history/#", HISTORY_ITEM);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new BrowserDatabaseHelper(getContext(), "BookMark.db", null,
				6);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// 查询数据
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case HISTORY_DIR:
			cursor = db.query("history", projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		default:
			break;
		}

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case HISTORY_DIR:
			return "vnd.android.cursor.dir/vnd.com.iotek.database.provider.history";
		case HISTORY_ITEM:
			return "vnd.android.cursor.item/vnd.com.iotek.database.provider.history";
		default:
			break;
		}
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i("wx", "insert");
		// 添加数据
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Uri uriReturn = null;
		switch (uriMatcher.match(uri)) {
		case HISTORY_DIR:
		case HISTORY_ITEM:
			long newHistoryId = db.insert("history", null, values);
			uriReturn = Uri.parse("content://" + AUTHORITY + "/history/"
					+ newHistoryId);
			break;

		default:
			break;
		}
		return uriReturn;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// 删除数据
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deleteRows = 0;
		switch (uriMatcher.match(uri)) {
		case HISTORY_DIR:
			deleteRows = db.delete("history", selection, selectionArgs);

			break;
		case HISTORY_ITEM:
			String historyId = uri.getPathSegments().get(1);

			deleteRows = db.delete("history", "id=?",
					new String[] { historyId });

			break;

		default:
			break;
		}

		return deleteRows;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		//更新数据
		/*SQLiteDatabase db=dbHelper.getWritableDatabase();
		int updataRows=0;
		switch (uriMatcher.match(uri)) {
		case HISTORY_ITEM:
			
			
			break;

		default:
			break;
		}
		*/
		return 0;
	}

}

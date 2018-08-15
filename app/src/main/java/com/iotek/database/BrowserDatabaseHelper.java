package com.iotek.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BrowserDatabaseHelper extends SQLiteOpenHelper {
	public static final String CREATE_BOOKMARK = "create table bookmark(id integer primary key autoincrement,name text,url text,image BLOB)";
	public static final String CREATE_HiSTORY = "create table history(id integer primary key autoincrement,name text,url text,image BLOB,time text)";
	public static final String CREATE_DOWNLOADNC = "create table download_ndone(url text primary key,downloadpos integer,name text,type integer)";
	public static final String CREATE_DOWNLOADDONE = "create table download_done(path text primary key,name text,size integer,type integer)";
	public static final String DROP_BOOKMARK = "drop table if exists bookmark";
	public static final String DROP_HISTORY = "drop table if exists history";
	public static final String DROP_DOWNLOADNC = "drop table if exists download_ndone";
	public static final String DROP_DOWNLOADDONE = "drop table if exists download_done";

	private Context context;

	public BrowserDatabaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		this.context = context;
	}

	public BrowserDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOKMARK);
		db.execSQL(CREATE_HiSTORY);
		db.execSQL(CREATE_DOWNLOADNC);
		db.execSQL(CREATE_DOWNLOADDONE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_BOOKMARK);
		db.execSQL(DROP_HISTORY);
		db.execSQL(DROP_DOWNLOADNC);
		db.execSQL(DROP_DOWNLOADDONE);
		onCreate(db);
	}

}
